/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ResetTokenDAO;
import dao.UserDAO;
import entity.ResetToken;
import entity.User;
import entity.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EmailUtil;
import util.TokenGenerator;

/**
 * ForgotPasswordRequestServlet
 * Handles rendering forgot password form and processing reset requests.
 */
public class ForgotPasswordRequestServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ForgotPasswordRequestServlet.class.getName());

    private final UserDAO userDAO = new UserDAO();
    private final ResetTokenDAO resetTokenDAO = new ResetTokenDAO();

    // CSRF
    private static final String CSRF_ATTR = "csrfToken";

    // Rate limit in memory (simple)
    private static final String RATE_LIMIT_MAP_CTX = "fp_rate_limit_map";
    private static final int RATE_LIMIT_MAX_PER_HOUR = 5;
    private static final long RATE_LIMIT_WINDOW_MS = TimeUnit.HOURS.toMillis(1);
    private static final long RATE_LIMIT_LOCKOUT_MS = TimeUnit.MINUTES.toMillis(15);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureCsrfToken(request.getSession(true));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/forgot_password.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // CSRF check
            if (!validateCsrf(request)) {
                LOGGER.warning("CSRF validation failed on forgot-password/request");
                setNeutralMessage(request);
                forwardForgot(request, response);
                return;
            }

            String email = getParam(request, "email");

            // Rate limit check
            String clientIp = getClientIp(request);
            if (isRateLimited(request.getServletContext(), clientIp, email)) {
                LOGGER.warning("Rate limit triggered for IP/email: " + maskEmail(email) + "@" + clientIp);
                setNeutralMessage(request);
                forwardForgot(request, response);
                return;
            }

            // Always send neutral response
            setNeutralMessage(request);

            if (email != null && !email.isEmpty()) {
                User user = userDAO.getUserByEmail(email.toLowerCase());
                if (user != null && !"inactive".equalsIgnoreCase(user.getStatus()) && !"suspended".equalsIgnoreCase(user.getStatus())) {
                    // Invalidate old tokens
                    resetTokenDAO.deleteTokensByUserId(user.getId());
                    // Create new token expires in 1 hour
                    String token = TokenGenerator.generateToken();
                    ResetToken resetToken = new ResetToken();
                    resetToken.setUserId(user.getId());
                    resetToken.setToken(token);
                    resetToken.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                    resetToken.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    resetToken.setExpiresAt(new Timestamp(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)));
                    resetToken.setUsed(false);

                    boolean created = resetTokenDAO.createToken(resetToken);
                    if (created) {
                        // Build base URL dynamically
                        String baseUrl = request.getRequestURL().toString()
                                .replace(request.getRequestURI(), request.getContextPath());
                        String resetLink = baseUrl + "/reset-password?token=" + token;
                        // Send email with full link
                        EmailUtil.sendPasswordResetEmailWithLink(email, resetLink);
                        LOGGER.info("Password reset email dispatched to: " + maskEmail(email));
                    } else {
                        LOGGER.warning("Failed to create reset token for user: " + user.getId());
                    }
                } else {
                    LOGGER.info("Forgot-password request for non-existent or inactive user: " + maskEmail(email));
                }
            }

            forwardForgot(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing forgot-password request", e);
            setNeutralMessage(request);
            forwardForgot(request, response);
        }
    }

    private void forwardForgot(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/forgot_password.jsp");
        dispatcher.forward(request, response);
    }

    private String getParam(HttpServletRequest request, String name) {
        String v = request.getParameter(name);
        return v == null ? null : v.trim();
        }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    // Simple in-memory rate limiter using ServletContext
    @SuppressWarnings("unchecked")
    private boolean isRateLimited(ServletContext ctx, String ip, String email) {
        String key = ip + "|" + (email != null ? email.toLowerCase() : "");
        Map<String, RateRecord> map = (Map<String, RateRecord>) ctx.getAttribute(RATE_LIMIT_MAP_CTX);
        if (map == null) {
            map = new HashMap<>();
            ctx.setAttribute(RATE_LIMIT_MAP_CTX, map);
        }
        long now = System.currentTimeMillis();
        RateRecord rec = map.get(key);
        if (rec == null) {
            rec = new RateRecord(1, now, 0);
            map.put(key, rec);
            return false;
        }
        // Lockout handling
        if (rec.lockedUntil > now) {
            return true;
        }
        // Window rollover
        if (now - rec.windowStart > RATE_LIMIT_WINDOW_MS) {
            rec.windowStart = now;
            rec.count = 1;
            rec.lockedUntil = 0;
            return false;
        }
        rec.count++;
        if (rec.count > RATE_LIMIT_MAX_PER_HOUR) {
            rec.lockedUntil = now + RATE_LIMIT_LOCKOUT_MS;
            return true;
        }
        return false;
    }

    private static class RateRecord {
        int count;
        long windowStart;
        long lockedUntil;
        RateRecord(int c, long w, long l) {
            this.count = c;
            this.windowStart = w;
            this.lockedUntil = l;
        }
    }

    private void ensureCsrfToken(HttpSession session) {
        Object token = session.getAttribute(CSRF_ATTR);
        if (token == null) {
            session.setAttribute(CSRF_ATTR, generateCsrf());
        }
    }

    private boolean validateCsrf(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        String expected = (String) session.getAttribute(CSRF_ATTR);
        String got = request.getParameter("csrfToken");
        return expected != null && expected.equals(got);
    }

    private String generateCsrf() {
        return TokenGenerator.generateCustomToken(32) + Long.toHexString(Instant.now().toEpochMilli());
    }

    private void setNeutralMessage(HttpServletRequest request) {
        request.setAttribute("successMessage", "Nếu email tồn tại, chúng tôi đã gửi hướng dẫn đặt lại mật khẩu.");
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "n/a";
        String[] parts = email.split("@", 2);
        String local = parts[0];
        String domain = parts[1];
        String maskedLocal = local.length() <= 2 ? local.charAt(0) + "*" : local.substring(0, 2) + "***";
        return maskedLocal + "@" + domain;
    }
}


