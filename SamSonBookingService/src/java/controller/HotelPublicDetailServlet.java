/*
 * HotelPublicDetailServlet - Public hotel detail page
 * Displays hotel details in a user-friendly format matching homepage style
 * 
 * @author SamSon Travel Team
 */
package controller;

import dao.HotelDAO;
import dao.ImageDAO;
import dao.RoomDAO;
import entity.Hotel;
import entity.Image;
import entity.Room;
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
 * Public hotel detail servlet
 * Displays hotel details for public viewing with homepage-style design
 */
@WebServlet(name = "HotelPublicDetailServlet", urlPatterns = {"/hotel-details"})
public class HotelPublicDetailServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(HotelPublicDetailServlet.class.getName());
    
    private final HotelDAO hotelDAO = new HotelDAO();
    private final ImageDAO imageDAO = new ImageDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    
    /**
     * Handles GET requests to display hotel detail
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
            
            // Get hotel ID parameter
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Không tìm thấy thông tin khách sạn.");
                request.getRequestDispatcher("/error/404.jsp").forward(request, response);
                return;
            }
            
            try {
                int hotelId = Integer.parseInt(idParam);
                Hotel hotel = hotelDAO.getHotelById(hotelId);
                
                if (hotel == null) {
                    request.setAttribute("errorMessage", "Khách sạn không tồn tại.");
                    request.getRequestDispatcher("/error/404.jsp").forward(request, response);
                    return;
                }
                
                // Load all images for hotel
                List<Image> hotelImages = imageDAO.getImagesByEntity("hotel", hotelId);
                Image primaryImage = imageDAO.getPrimaryImage("hotel", hotelId);
                if (primaryImage == null && !hotelImages.isEmpty()) {
                    primaryImage = hotelImages.get(0);
                }
                
                // Load rooms for hotel
                List<Room> rooms = roomDAO.getRoomsByHotelId(hotelId);
                
                // Load images for each room
                Map<Integer, List<Image>> roomImagesMap = new HashMap<>();
                for (Room room : rooms) {
                    List<Image> roomImages = imageDAO.getImagesByEntity("room", room.getId());
                    roomImagesMap.put(room.getId(), roomImages);
                }
                
                // Set request attributes
                request.setAttribute("currentUser", currentUser);
                request.setAttribute("userRole", userRole);
                request.setAttribute("hotel", hotel);
                request.setAttribute("hotelImages", hotelImages);
                request.setAttribute("primaryImage", primaryImage);
                request.setAttribute("rooms", rooms);
                request.setAttribute("roomImagesMap", roomImagesMap);
                
                // Set page title and meta information
                request.setAttribute("pageTitle", hotel.getName() + " - SamSon Travel");
                request.setAttribute("pageDescription", hotel.getDescription() != null && !hotel.getDescription().isEmpty() 
                    ? hotel.getDescription() : "Khám phá " + hotel.getName() + " tại Sầm Sơn");
                request.setAttribute("pageKeywords", hotel.getName() + ", khách sạn sầm sơn, resort sầm sơn, đặt phòng");
                
                // Forward to hotel_detail_public.jsp
                request.getRequestDispatcher("/hotel_detail_public.jsp").forward(request, response);
                
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid hotel ID parameter: " + idParam, e);
                request.setAttribute("errorMessage", "ID khách sạn không hợp lệ.");
                request.getRequestDispatcher("/error/404.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in HotelPublicDetailServlet", e);
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải thông tin khách sạn. Vui lòng thử lại sau.");
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

