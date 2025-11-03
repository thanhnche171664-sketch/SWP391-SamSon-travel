package controller.hotel;

import dao.HotelDAO;
import dao.ImageDAO;
import entity.Hotel;
import entity.Image;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet hiển thị danh sách khách sạn với phân trang và tìm kiếm
 * Dành cho Hotel Manager (role_id = 3)
 */
@WebServlet("/hotel/list")
public class HotelListServlet extends HttpServlet {

    private static final HotelDAO hotelDAO = new HotelDAO();
    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Kiểm tra phân quyền
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user.getRoleId() != 3) { // Chỉ Hotel Manager mới truy cập được
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        // Lấy parameters
        String pageParam = request.getParameter("page");
        String searchKeyword = request.getParameter("search");
        
        int currentPage = 1;
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // Hotel Manager chỉ thấy khách sạn của mình quản lý
        Integer managerId = user.getId();

        // Lấy danh sách khách sạn với phân trang
        List<Hotel> hotelList = hotelDAO.getHotelsPaginated(currentPage, PAGE_SIZE, searchKeyword, managerId);
        int totalRecords = hotelDAO.countHotelsFiltered(searchKeyword, managerId);
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        // Load ảnh cho mỗi hotel
        ImageDAO imageDAO = new ImageDAO();
        Map<Integer, String> hotelImages = new HashMap<>();
        for (Hotel hotel : hotelList) {
            Image primaryImage = imageDAO.getPrimaryImage("hotel", hotel.getId());
            if (primaryImage != null) {
                hotelImages.put(hotel.getId(), primaryImage.getImageUrl());
            } else {
                // Nếu không có primary, lấy ảnh đầu tiên
                Image firstImage = imageDAO.getFirstImage("hotel", hotel.getId());
                if (firstImage != null) {
                    hotelImages.put(hotel.getId(), firstImage.getImageUrl());
                } else {
                    hotelImages.put(hotel.getId(), "uploads/hotels/default.jpg");
                }
            }
        }

        // Set attributes
        request.setAttribute("hotelList", hotelList);
        request.setAttribute("hotelImages", hotelImages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("searchKeyword", searchKeyword != null ? searchKeyword : "");

        // Forward to JSP
        request.getRequestDispatcher("/hotel/hotel_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
