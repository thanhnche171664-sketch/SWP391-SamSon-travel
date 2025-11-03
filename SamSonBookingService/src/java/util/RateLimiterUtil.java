package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Simple session-based rate limiter for short polling endpoints.
 */
public class RateLimiterUtil {
    private static final String ATTR_LAST = "rl_last_ts";
    private static final String ATTR_TOKENS = "rl_tokens";

    // Token bucket: refill 1 token per second up to burst, cost 1 per call
    public static boolean allow(HttpServletRequest req, int burst) {
        HttpSession s = req.getSession(true);
        long now = System.currentTimeMillis();
        Long last = (Long) s.getAttribute(ATTR_LAST);
        Integer tokens = (Integer) s.getAttribute(ATTR_TOKENS);
        if (last == null) { last = now; }
        if (tokens == null) { tokens = burst; }
        long elapsedSec = Math.max(0, (now - last) / 1000);
        if (elapsedSec > 0) {
            tokens = Math.min(burst, tokens + (int) elapsedSec);
            last = now;
        }
        if (tokens <= 0) {
            s.setAttribute(ATTR_LAST, last);
            s.setAttribute(ATTR_TOKENS, tokens);
            return false;
        }
        tokens -= 1;
        s.setAttribute(ATTR_LAST, last);
        s.setAttribute(ATTR_TOKENS, tokens);
        return true;
    }
}


