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
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "MealServiceDetailServlet", urlPatterns = {"/hotel/meal/detail"})
public class MealServiceDetailServlet extends HttpServlet {
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
            List<Image> mealImages = imageDAO.getImagesByEntity("meal", mealId);
            request.setAttribute("meal", meal);
            request.setAttribute("mealImages", mealImages);
            request.getRequestDispatcher("/hotel/meal_service_detail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        }
    }
}
