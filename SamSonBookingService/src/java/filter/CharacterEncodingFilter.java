package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

/**
 * Ensures all requests and responses use UTF-8 encoding to prevent mojibake.
 */
public class CharacterEncodingFilter implements Filter {

    private String encoding = "UTF-8";
    private boolean force = true;

    @Override
    public void init(FilterConfig filterConfig) {
        String configuredEncoding = filterConfig.getInitParameter("encoding");
        if (configuredEncoding != null && !configuredEncoding.isEmpty()) {
            this.encoding = configuredEncoding;
        }
        String forceParam = filterConfig.getInitParameter("force");
        if (forceParam != null && !forceParam.isEmpty()) {
            this.force = Boolean.parseBoolean(forceParam);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (this.force || request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(this.encoding);
        }
        response.setCharacterEncoding(this.encoding);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to clean up
    }
}


