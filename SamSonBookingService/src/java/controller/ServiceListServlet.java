package controller;

import dao.MealServiceDAO;
import dao.WellnessServiceDAO;
import entity.MealService;
import entity.WellnessService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ServiceListServlet - Hiển thị danh sách dịch vụ (meal + wellness) từ database
 * Xử lý URL: /service-list
 * 
 * @author SamSon Travel Team
 */
@WebServlet(name = "ServiceListServlet", urlPatterns = {"/service-list"})
public class ServiceListServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ServiceListServlet.class.getName());
    
    private final MealServiceDAO mealServiceDAO = new MealServiceDAO();
    private final WellnessServiceDAO wellnessServiceDAO = new WellnessServiceDAO();
    
    /**
     * Xử lý GET request - Hiển thị danh sách dịch vụ
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        try {
            LOGGER.info("ServiceListServlet: Bắt đầu lấy danh sách dịch vụ từ database...");
            
            // Lấy danh sách meal services từ database
            List<MealService> mealServices = mealServiceDAO.getAllActiveMealServices();
            
            // Kiểm tra null
            if (mealServices == null) {
                LOGGER.warning("ServiceListServlet: mealServiceDAO.getAllActiveMealServices() trả về null");
                mealServices = new ArrayList<>();
            }
            
            LOGGER.info("ServiceListServlet: Số lượng meal services: " + mealServices.size());
            for (MealService meal : mealServices) {
                LOGGER.info("ServiceListServlet: Meal ID: " + meal.getMealId() + 
                           ", Loại: " + meal.getMealType() + 
                           ", Giá: " + meal.getPrice());
            }
            
            // Lấy danh sách wellness services từ database
            List<WellnessService> wellnessServices = wellnessServiceDAO.getAllActiveWellnessServices();
            
            // Kiểm tra null
            if (wellnessServices == null) {
                LOGGER.warning("ServiceListServlet: wellnessServiceDAO.getAllActiveWellnessServices() trả về null");
                wellnessServices = new ArrayList<>();
            }
            
            LOGGER.info("ServiceListServlet: Số lượng wellness services: " + wellnessServices.size());
            for (WellnessService wellness : wellnessServices) {
                LOGGER.info("ServiceListServlet: Wellness ID: " + wellness.getWellnessId() + 
                           ", Tên: " + wellness.getServiceName() + 
                           ", Giá: " + wellness.getBasePrice());
            }
            
            // Đặt danh sách dịch vụ vào request attribute
            request.setAttribute("mealServices", mealServices);
            request.setAttribute("wellnessServices", wellnessServices);
            
            // Forward đến JSP
            LOGGER.info("ServiceListServlet: Forward đến Service-list.jsp");
            request.getRequestDispatcher("Service-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "ServiceListServlet: Lỗi khi lấy danh sách dịch vụ", e);
            e.printStackTrace();
            
            // Nếu có lỗi, vẫn forward nhưng với danh sách rỗng
            request.setAttribute("mealServices", new ArrayList<MealService>());
            request.setAttribute("wellnessServices", new ArrayList<WellnessService>());
            request.setAttribute("error", "Không thể tải danh sách dịch vụ: " + e.getMessage());
            
            try {
                request.getRequestDispatcher("Service-list.jsp").forward(request, response);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "ServiceListServlet: Lỗi khi forward đến JSP", ex);
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Lỗi hệ thống</h1>");
                response.getWriter().println("<p>Không thể tải trang Service-list.jsp</p>");
                response.getWriter().println("<p>Lỗi: " + ex.getMessage() + "</p>");
                response.getWriter().println("<pre>");
                ex.printStackTrace(new java.io.PrintWriter(response.getWriter()));
                response.getWriter().println("</pre>");
                response.getWriter().println("</body></html>");
            }
        }
    }
}





