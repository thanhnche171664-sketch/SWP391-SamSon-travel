/*
 * ImageProxyServlet - Proxy servlet to load external images
 * This helps bypass CORS and hotlinking restrictions
 * 
 * @author SamSon Travel Team
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proxy servlet for loading external images
 * URL: /image-proxy?url=<encoded-image-url>
 */
@WebServlet(name = "ImageProxyServlet", urlPatterns = {"/image-proxy"})
public class ImageProxyServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ImageProxyServlet.class.getName());
    private static final int TIMEOUT = 10000; // 10 seconds
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String imageUrl = request.getParameter("url");
        
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing image URL parameter");
            return;
        }
        
        try {
            // Decode URL if needed
            imageUrl = java.net.URLDecoder.decode(imageUrl, "UTF-8");
            
            // Validate URL
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL format");
                return;
            }
            
            LOGGER.info("Proxying image: " + imageUrl);
            
            // Create connection to external image
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            connection.setRequestProperty("Referer", request.getRequestURL().toString());
            
            // Get content type
            String contentType = connection.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                // Try to detect from URL
                if (imageUrl.toLowerCase().endsWith(".jpg") || imageUrl.toLowerCase().endsWith(".jpeg")) {
                    contentType = "image/jpeg";
                } else if (imageUrl.toLowerCase().endsWith(".png")) {
                    contentType = "image/png";
                } else if (imageUrl.toLowerCase().endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (imageUrl.toLowerCase().endsWith(".webp")) {
                    contentType = "image/webp";
                } else {
                    contentType = "image/jpeg"; // Default
                }
            }
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Set response headers
                response.setContentType(contentType);
                response.setHeader("Cache-Control", "public, max-age=86400"); // Cache for 1 day
                response.setHeader("Access-Control-Allow-Origin", "*");
                
                // Copy image data
                try (InputStream inputStream = connection.getInputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        response.getOutputStream().write(buffer, 0, bytesRead);
                    }
                }
            } else {
                LOGGER.warning("Failed to load image: " + imageUrl + " - Response code: " + responseCode);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error proxying image: " + imageUrl, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading image");
        }
    }
}

