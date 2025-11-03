package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

public class CsrfTokenUtil {
    private static final String CSRF_ATTR = "csrfToken";
    private static final SecureRandom RNG = new SecureRandom();

    public static String ensureToken(HttpSession session) {
        Object t = session.getAttribute(CSRF_ATTR);
        if (t instanceof String) return (String) t;
        byte[] bytes = new byte[32];
        RNG.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        session.setAttribute(CSRF_ATTR, token);
        return token;
    }

    public static boolean isValid(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        Object t = session.getAttribute(CSRF_ATTR);
        String sent = req.getParameter("_csrf");
        return t != null && t.equals(sent);
    }
}


