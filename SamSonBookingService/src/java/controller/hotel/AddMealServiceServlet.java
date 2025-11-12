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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "AddMealServiceServlet", urlPatterns = {"/hotel/meal/add"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class AddMealServiceServlet extends HttpServlet {
    private MealServiceDAO mealServiceDAO = new MealServiceDAO();
    private ImageDAO imageDAO = new ImageDAO();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hotelIdParam = request.getParameter("hotelId");
        if (hotelIdParam == null || hotelIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/hotel/list");
            return;
        }
        request.getRequestDispatcher("/hotel/meal_service_add.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String mealType = request.getParameter("mealType");
            String mealDateStr = request.getParameter("mealDate");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            String status = request.getParameter("status");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date mealDate = sdf.parse(mealDateStr);
            
            MealService service = new MealService();
            service.setHotelId(hotelId);
            service.setCategoryId(categoryId);
            service.setMealType(mealType);
            service.setMealDate(mealDate);
            service.setDescription(description);
            service.setPrice(price);
            service.setStatus(status);
            
            int mealId = mealServiceDAO.insertMealServiceAndReturnId(service);
            
            if (mealId > 0) {
                handleMultipleFileUpload(request, mealId);
                response.sendRedirect(request.getContextPath() + "/hotel/detail?id=" + hotelId);
            } else {
                request.setAttribute("error", "Không thể thêm dịch vụ!");
                request.getRequestDispatcher("/hotel/meal_service_add.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("/hotel/meal_service_add.jsp").forward(request, response);
        }
    }
    
    private void handleMultipleFileUpload(HttpServletRequest request, int mealId) throws IOException, ServletException {
        Collection<Part> fileParts = request.getParts();
        
        // Lưu vào thư mục web/uploads (source directory, không bị mất khi clean build)
        String realPath = getServletContext().getRealPath("/");
        // Chuyển từ build/web sang web (source directory)
        String webPath = realPath.replace("build" + java.io.File.separator + "web", "web");
        String uploadPath = webPath + "uploads" + java.io.File.separator + "meals";
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        
        System.out.println("DEBUG Meal: Saving to " + uploadPath);
        
        boolean isFirstImage = true;
        int displayOrder = 1;
        
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
