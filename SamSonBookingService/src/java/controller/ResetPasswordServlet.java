/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ResetTokenDAO;
import dao.UserDAO;
import entity.ResetToken;
import entity.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.PasswordUtil;
import util.TokenGenerator;

/**
 * ResetPasswordServlet
 * GET: render reset form if token valid
 * POST: validate and update password
 */
public class ResetPasswordServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ResetPasswordServlet.class.getName());

    private final ResetTokenDAO resetTokenDAO = new ResetTokenDAO();
    private final UserDAO userDAO = new UserDAO();

    private static final String CSRF_ATTR = "csrfToken";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String token = request.getParameter("token");
            if (!TokenGenerator.isValidTokenFormat(token)) {
                renderInvalid(request, response);
                return;
            }
            ResetToken found = resetTokenDAO.getTokenByValue(token);
            if (found == null || found.isUsed() || found.getExpiresAt() == null || found.getExpiresAt().getTime() <= System.currentTimeMillis()) {
                renderInvalid(request, response);
                return;
            }
            ensureCsrfToken(request.getSession(true));
            request.setAttribute("token", token);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/reset_password.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error rendering reset password page", e);
            renderInvalid(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (!validateCsrf(request)) {
                renderInvalid(request, response);
                return;
            }
            String token = request.getParameter("token");
            String password = request.getParameter("password");
            String confirm = request.getParameter("confirmPassword");

            if (!TokenGenerator.isValidTokenFormat(token)) {
                renderInvalid(request, response);
                return;
            }
            ResetToken found = resetTokenDAO.getTokenByValue(token);
            if (found == null || found.isUsed() || found.getExpiresAt() == null || found.getExpiresAt().getTime() <= System.currentTimeMillis()) {
                renderInvalid(request, response);
                return;
            }
            if (password == null || confirm == null || !password.equals(confirm)) {
                request.setAttribute("errorMessage", "Mật khẩu nhập lại không khớp.");
                request.setAttribute("token", token);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/reset_password.jsp");
                dispatcher.forward(request, response);
                return;
            }
            if (!PasswordUtil.isValidPassword(password)) {
                request.setAttribute("errorMessage", "Mật khẩu phải tối thiểu 8 ký tự và gồm chữ hoa, chữ thường, số, ký tự đặc biệt.");
                request.setAttribute("token", token);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/reset_password.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Update password
            User user = userDAO.getUserById(found.getUserId());
            if (user == null) {
                renderInvalid(request, response);
                return;
            }
            String hashed = PasswordUtil.hashPassword(password);
            boolean updated = userDAO.updatePassword(user.getId(), hashed);
            if (!updated) {
                LOGGER.warning("Failed to update password for user " + user.getId());
                request.setAttribute("errorMessage", "Lỗi hệ thống. Vui lòng thử lại sau.");
                request.setAttribute("token", token);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/reset_password.jsp");
                dispatcher.forward(request, response);
                return;
            }
            // Mark token used and cleanup others
            resetTokenDAO.markTokenAsUsed(token);
            resetTokenDAO.deleteTokensByUserId(user.getId());

            // Success page
            RequestDispatcher dispatcher = request.getRequestDispatcher("/reset_password_success.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error resetting password", e);
            renderInvalid(request, response);
        }
    }

    private void renderInvalid(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/reset_password_invalid.jsp");
        dispatcher.forward(request, response);
    }

    private void ensureCsrfToken(HttpSession session) {
        Object token = session.getAttribute(CSRF_ATTR);
        if (token == null) {
            session.setAttribute(CSRF_ATTR, TokenGenerator.generateCustomToken(32));
        }
    }

    private boolean validateCsrf(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        String expected = (String) session.getAttribute(CSRF_ATTR);
        String got = request.getParameter("csrfToken");
        return expected != null && expected.equals(got);
    }
}



