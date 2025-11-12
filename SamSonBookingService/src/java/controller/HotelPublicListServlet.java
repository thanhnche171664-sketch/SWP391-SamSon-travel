/*
 * HotelPublicListServlet - Public hotel listing page
 * Displays all hotels in a user-friendly format matching homepage style
 * 
 * @author SamSon Travel Team
 */
package controller;

import dao.HotelDAO;
import dao.ImageDAO;
import entity.Hotel;
import entity.Image;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Public hotel listing servlet
 * Displays all hotels for public viewing with homepage-style design
 */
@WebServlet(name = "HotelPublicListServlet", urlPatterns = {"/hotels"})
public class HotelPublicListServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(HotelPublicListServlet.class.getName());
    
    private final HotelDAO hotelDAO = new HotelDAO();
    private final ImageDAO imageDAO = new ImageDAO();
    
    /**
     * Handles GET requests to display hotel list
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get user session and role (optional - for displaying user-specific content)
            HttpSession session = request.getSession(false);
            User currentUser = null;
            String userRole = "GUEST";
            
            if (session != null && session.getAttribute("user") != null) {
                currentUser = (User) session.getAttribute("user");
                userRole = getUserRoleName(currentUser.getRoleId());
            }
            
            // Get search parameter
            String searchKeyword = request.getParameter("search");
            
            // Fetch all hotels or search results
            List<Hotel> hotels;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                hotels = hotelDAO.searchHotels(searchKeyword);
            } else {
                hotels = hotelDAO.getAllHotels();
            }
            
            // Load images for each hotel from Images table
            Map<Integer, String> hotelImages = new HashMap<>();
            for (Hotel hotel : hotels) {
                Image primaryImage = imageDAO.getPrimaryImage("hotel", hotel.getId());
                if (primaryImage != null) {
                    String imageUrl = primaryImage.getImageUrl();
                    LOGGER.info("Hotel ID " + hotel.getId() + " (" + hotel.getName() + ") - Image URL: " + imageUrl);
                    hotelImages.put(hotel.getId(), imageUrl);
                } else {
                    // If no primary image, get first image
                    Image firstImage = imageDAO.getFirstImage("hotel", hotel.getId());
                    if (firstImage != null) {
                        String imageUrl = firstImage.getImageUrl();
                        LOGGER.info("Hotel ID " + hotel.getId() + " (" + hotel.getName() + ") - First Image URL: " + imageUrl);
                        hotelImages.put(hotel.getId(), imageUrl);
                    } else {
                        // Default image if no image found
                        LOGGER.warning("Hotel ID " + hotel.getId() + " (" + hotel.getName() + ") - No image found, using default");
                        hotelImages.put(hotel.getId(), "assets/images/hotels/default-hotel.jpg");
                    }
                }
            }
            
            // Debug: Log all hotel images
            LOGGER.info("Total hotels with images: " + hotelImages.size());
            for (Map.Entry<Integer, String> entry : hotelImages.entrySet()) {
                LOGGER.info("  Hotel " + entry.getKey() + ": " + entry.getValue());
            }
            
            // Set request attributes
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("userRole", userRole);
            request.setAttribute("hotels", hotels);
            request.setAttribute("hotelImages", hotelImages);
            request.setAttribute("searchKeyword", searchKeyword != null ? searchKeyword : "");
            request.setAttribute("totalHotels", hotels.size());
            
            // Set page title and meta information
            request.setAttribute("pageTitle", "Danh Sách Khách Sạn - SamSon Travel");
            request.setAttribute("pageDescription", "Khám phá các khách sạn và resort cao cấp tại Sầm Sơn");
            request.setAttribute("pageKeywords", "khách sạn sầm sơn, resort sầm sơn, đặt phòng khách sạn");
            
            // Forward to hotels_list.jsp
            request.getRequestDispatcher("/hotels_list.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in HotelPublicListServlet", e);
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải danh sách khách sạn. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/error/500.jsp").forward(request, response);
        }
    }
    
    /**
     * Get user role name by role ID
     * @param roleId Role ID
     * @return Role name
     */
    private String getUserRoleName(int roleId) {
        switch (roleId) {
            case 1: return "ADMINISTRATOR";
            case 2: return "SERVICE_MANAGER";
            case 3: return "HOTEL_MANAGER";
            case 4: return "CUSTOMER";
            case 5: return "FRONT_OFFICE";
            default: return "GUEST";
        }
    }
}

