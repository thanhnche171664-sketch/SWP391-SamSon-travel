package controller.hotel;

import dao.ImageDAO;
import entity.Image;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet xử lý các thao tác ảnh dành cho dịch vụ ăn uống (AJAX)
 * URL: /meal/images
 */
@WebServlet("/meal/images")
public class MealImageServlet extends HttpServlet {

    private final ImageDAO imageDAO = new ImageDAO();
    private static final String UPLOAD_DIR = "uploads/meals";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleAction(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleAction(request, response);
    }

    private void handleAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                handleDelete(request, response, out);
                return;
            }
            if ("setPrimary".equals(action)) {
                handleSetPrimary(request, response, out);
                return;
            }
            out.print("{\"success\":false, \"message\":\"Unknown action\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false, \"message\":\"Server error: " + e.getMessage() + "\"}");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        try {
            String idParam = request.getParameter("imageId");
            if (idParam == null || idParam.trim().isEmpty()) {
                out.print("{\"success\":false, \"message\":\"Missing imageId\"}");
                return;
            }

            int imageId = Integer.parseInt(idParam);
            Image image = imageDAO.getImageById(imageId);
            if (image == null) {
                out.print("{\"success\":false, \"message\":\"Image not found\"}");
                return;
            }

            // Delete from database first
            boolean dbDeleted = imageDAO.deleteImage(imageId);
            if (!dbDeleted) {
                out.print("{\"success\":false, \"message\":\"Failed to delete image record\"}");
                return;
            }

            // Delete physical file if exists (from web directory)
            try {
                String imageUrl = image.getImageUrl(); // e.g., uploads/meals/xxx.jpg
                String realPath = getServletContext().getRealPath("/");
                // Chuyển từ build/web sang web (source directory)
                String webPath = realPath.replace("build" + File.separator + "web", "web");
                String filePath = webPath + imageUrl.replace("/", File.separator);
                File f = new File(filePath);
                if (f.exists()) {
                    f.delete();
                    System.out.println("Deleted image file: " + filePath);
                } else {
                    System.out.println("Image file not found: " + filePath);
                }
            } catch (Exception ex) {
                // Log and continue; DB record already removed
                ex.printStackTrace();
            }

            // If there is no primary image, set first image as primary
            Image primary = imageDAO.getPrimaryImage("meal", image.getEntityId());
            if (primary == null) {
                Image first = imageDAO.getFirstImage("meal", image.getEntityId());
                if (first != null) {
                    imageDAO.setPrimaryImage(first.getId(), "meal", image.getEntityId());
                }
            }

            out.print("{\"success\":true}");
        } catch (NumberFormatException nfe) {
            out.print("{\"success\":false, \"message\":\"Invalid imageId\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false, \"message\":\"Server error: " + e.getMessage() + "\"}");
        }
    }

    private void handleSetPrimary(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        try {
            String idParam = request.getParameter("imageId");
            String mealIdParam = request.getParameter("mealId");
            if (idParam == null || mealIdParam == null) {
                out.print("{\"success\":false, \"message\":\"Missing parameters\"}");
                return;
            }
            int imageId = Integer.parseInt(idParam);
            int mealId = Integer.parseInt(mealIdParam);
            boolean ok = imageDAO.setPrimaryImage(imageId, "meal", mealId);
            out.print("{\"success\":" + ok + "}");
        } catch (NumberFormatException nfe) {
            out.print("{\"success\":false, \"message\":\"Invalid parameters\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false, \"message\":\"Server error: " + e.getMessage() + "\"}");
        }
    }
}
