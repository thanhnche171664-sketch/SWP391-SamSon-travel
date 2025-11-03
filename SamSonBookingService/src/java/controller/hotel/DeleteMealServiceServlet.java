/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.hotel;
import dao.ImageDAO;
import dao.MealServiceDAO;
import entity.Image;
import entity.MealService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DeleteMealServiceServlet", urlPatterns = {"/hotel/meal/delete"})
public class DeleteMealServiceServlet extends HttpServlet {
    private MealServiceDAO mealServiceDAO = new MealServiceDAO();
    private ImageDAO imageDAO = new ImageDAO();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int mealId = Integer.parseInt(request.getParameter("id"));
            MealService meal = mealServiceDAO.getMealServiceById(mealId);
            if (meal == null) {
                response.sendRedirect(request.getContextPath() + "/hotel/list");
                return;
            }
            Image primaryImage = imageDAO.getPrimaryImage("meal", mealId);
            request.setAttribute("meal", meal);
            request.setAttribute("primaryImage", primaryImage);
            request.getRequestDispatcher("/hotel/meal_service_delete.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            
            imageDAO.deleteAllImagesByEntity("meal", mealId);
            boolean success = mealServiceDAO.deleteMealService(mealId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/hotel/detail?id=" + hotelId);
            } else {
                request.setAttribute("error", "Không thể xóa dịch vụ!");
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            doGet(request, response);
        }
    }
}
