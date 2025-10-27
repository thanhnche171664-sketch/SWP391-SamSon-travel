package controller;

import dao.MealServiceDAO;
import dao.WellnessServiceDAO;
import entity.MealService;
import entity.WellnessService;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet xử lý danh sách dịch vụ (Meal & Wellness)
 * Hỗ trợ cả offline booking (lễ tân tại quầy) và online booking
 * 
 * @author SamSon Travel Team
 */
@WebServlet(name = "ServiceListServlet", urlPatterns = {"/service-list"})
public class ServiceListServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ServiceListServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("=== ServiceListServlet.doGet() START ===");
        
        try {
            // Khởi tạo DAO
            MealServiceDAO mealServiceDAO = new MealServiceDAO();
            WellnessServiceDAO wellnessServiceDAO = new WellnessServiceDAO();
            
            // Lấy parameters để filter (nếu có)
            String filterType = request.getParameter("filter");
            String searchKeyword = request.getParameter("search");
            String hotelIdParam = request.getParameter("hotelId");
            
            LOGGER.log(Level.INFO, "Filter type: {0}, Search: {1}, Hotel ID: {2}", 
                new Object[]{filterType, searchKeyword, hotelIdParam});
            
            List<MealService> mealServices = null;
            List<WellnessService> wellnessServices = null;
            
            // Xử lý filter theo hotel (cho offline booking tại quầy của từng hotel)
            if (hotelIdParam != null && !hotelIdParam.trim().isEmpty()) {
                int hotelId = Integer.parseInt(hotelIdParam);
                
                if ("MEAL".equals(filterType)) {
                    mealServices = mealServiceDAO.getMealsByHotel(hotelId);
                } else if ("WELLNESS".equals(filterType)) {
                    wellnessServices = wellnessServiceDAO.getServicesByHotel(hotelId);
                } else {
                    mealServices = mealServiceDAO.getMealsByHotel(hotelId);
                    wellnessServices = wellnessServiceDAO.getServicesByHotel(hotelId);
                }
                
            // Xử lý search keyword
            } else if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                mealServices = mealServiceDAO.searchMeals(searchKeyword);
                wellnessServices = wellnessServiceDAO.searchServicesByName(searchKeyword);
                request.setAttribute("searchKeyword", searchKeyword);
                
            // Xử lý filter theo type
            } else if ("MEAL".equals(filterType)) {
                mealServices = mealServiceDAO.getAllActiveMeals();
                
            } else if ("WELLNESS".equals(filterType)) {
                wellnessServices = wellnessServiceDAO.getAllActiveServices();
                
            // Lấy tất cả (default)
            } else {
                mealServices = mealServiceDAO.getAllActiveMeals();
                wellnessServices = wellnessServiceDAO.getAllActiveServices();
            }
            
            // Log kết quả
            int mealCount = (mealServices != null) ? mealServices.size() : 0;
            int wellnessCount = (wellnessServices != null) ? wellnessServices.size() : 0;
            LOGGER.log(Level.INFO, "Loaded {0} meals and {1} wellness services", 
                new Object[]{mealCount, wellnessCount});
            
            // Set attributes cho JSP
            request.setAttribute("mealServices", mealServices);
            request.setAttribute("wellnessServices", wellnessServices);
            request.setAttribute("filterType", filterType);
            
            // Forward to JSP
            request.getRequestDispatcher("/Service-list.jsp").forward(request, response);
            
            LOGGER.info("=== ServiceListServlet.doGet() END ===");
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid hotel ID format", e);
            request.setAttribute("errorMessage", "ID hotel không hợp lệ");
            request.getRequestDispatcher("/Service-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in ServiceListServlet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Lỗi khi tải danh sách dịch vụ: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Service List Servlet - Hiển thị danh sách dịch vụ Meal và Wellness cho offline/online booking";
    }
}
