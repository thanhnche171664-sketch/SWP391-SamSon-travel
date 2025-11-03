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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "EditMealServiceServlet", urlPatterns = {"/hotel/meal/edit"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class EditMealServiceServlet extends HttpServlet {
    private MealServiceDAO mealServiceDAO = new MealServiceDAO();
    private ImageDAO imageDAO = new ImageDAO();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            String hotelIdParam = request.getParameter("hotelId");
            
            if (idParam == null || idParam.trim().isEmpty()) {
                System.out.println("EditMealServiceServlet: Missing id parameter");
                response.sendRedirect(request.getContextPath() + "/hotel/list");
                return;
            }
            
            int mealId = Integer.parseInt(idParam);
            System.out.println("EditMealServiceServlet: Loading meal with id=" + mealId);
            
            MealService meal = mealServiceDAO.getMealServiceById(mealId);
            if (meal == null) {
                System.out.println("EditMealServiceServlet: Meal not found with id=" + mealId);
                response.sendRedirect(request.getContextPath() + "/hotel/list");
                return;
            }
            
            // Get hotelId from parameter or from meal object
            int hotelId = meal.getHotelId();
            if (hotelIdParam != null && !hotelIdParam.trim().isEmpty()) {
                hotelId = Integer.parseInt(hotelIdParam);
            }
            
            List<Image> mealImages = imageDAO.getImagesByEntity("meal", mealId);
            request.setAttribute("meal", meal);
            request.setAttribute("mealImages", mealImages);
            request.setAttribute("hotelId", hotelId);
            
            System.out.println("EditMealServiceServlet: Forwarding to meal_service_edit.jsp with hotelId=" + hotelId);
            request.getRequestDispatcher("/hotel/meal_service_edit.jsp").forward(request, response);
        } catch (NumberFormatException nfe) {
            System.err.println("EditMealServiceServlet: Invalid id format - " + nfe.getMessage());
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        } catch (Exception e) {
            System.err.println("EditMealServiceServlet: Error in doGet - " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Note: Image delete and setPrimary operations are now handled by MealImageServlet (/meal/images)
        
        try {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            String uploadOnly = request.getParameter("uploadOnly");
            
            if ("true".equals(uploadOnly)) {
                handleMultipleFileUpload(request, mealId);
                response.sendRedirect(request.getContextPath() + "/hotel/meal/edit?id=" + mealId + "&hotelId=" + hotelId);
                return;
            }
            
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String mealType = request.getParameter("mealType");
            String mealDateStr = request.getParameter("mealDate");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            String status = request.getParameter("status");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date mealDate = sdf.parse(mealDateStr);
            
            MealService meal = new MealService();
            meal.setMealId(mealId);
            meal.setHotelId(hotelId);
            meal.setCategoryId(categoryId);
            meal.setMealType(mealType);
            meal.setMealDate(mealDate);
            meal.setDescription(description);
            meal.setPrice(price);
            meal.setStatus(status);
            
            boolean success = mealServiceDAO.updateMealService(meal);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/hotel/detail?id=" + hotelId);
            } else {
                request.setAttribute("error", "Không thể cập nhật dịch vụ!");
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            doGet(request, response);
        }
    }
    
    private void handleMultipleFileUpload(HttpServletRequest request, int mealId) throws IOException, ServletException {
        Collection<Part> fileParts = request.getParts();
        String uploadPath = getServletContext().getRealPath("") + "uploads" + java.io.File.separator + "meals";
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        
        Image primaryImage = imageDAO.getPrimaryImage("meal", mealId);
        boolean isFirstImage = (primaryImage == null);
        List<Image> existingImages = imageDAO.getImagesByEntity("meal", mealId);
        int displayOrder = existingImages.size() + 1;
        
        for (Part part : fileParts) {
            if (part.getName().equals("images") && part.getSize() > 0) {
                String fileName = part.getSubmittedFileName();
                String contentType = part.getContentType();
                if (!contentType.startsWith("image/")) continue;
                
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                String uniqueFileName = "meal_" + mealId + "_" + System.currentTimeMillis() + fileExtension;
                Path filePath = uploadDir.resolve(uniqueFileName);
                Files.copy(part.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                Image image = new Image();
                image.setEntityType("meal");
                image.setEntityId(mealId);
                image.setImageUrl("uploads/meals/" + uniqueFileName);
                image.setPrimary(isFirstImage);
                image.setDisplayOrder(displayOrder);
                image.setAltText("Meal service image " + displayOrder);
                imageDAO.insertImage(image);
                
                isFirstImage = false;
                displayOrder++;
            }
        }
    }
}
