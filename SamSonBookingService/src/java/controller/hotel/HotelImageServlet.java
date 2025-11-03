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
 * Servlet xử lý các thao tác ảnh dành cho khách sạn (AJAX)
 * URL: /hotel/images
 */
@WebServlet("/hotel/images")
public class HotelImageServlet extends HttpServlet {

    private final ImageDAO imageDAO = new ImageDAO();
    private static final String UPLOAD_DIR = "uploads/hotels";

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
            out.print("{\"success\":false, \"message\":\"Server error\"}");
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

            boolean dbDeleted = imageDAO.deleteImage(imageId);
            if (!dbDeleted) {
                out.print("{\"success\":false, \"message\":\"Failed to delete image record\"}");
                return;
            }

            // Delete physical file if exists
            try {
                String imageUrl = image.getImageUrl(); // e.g., uploads/hotels/xxx.jpg
                String realPath = getServletContext().getRealPath("") + File.separator + imageUrl.replace("/", File.separator);
                File f = new File(realPath);
                if (f.exists()) {
                    f.delete();
                }
            } catch (Exception ex) {
                // Log and continue; DB record already removed
                ex.printStackTrace();
            }

            // If there is no primary image, set first image as primary
            Image primary = imageDAO.getPrimaryImage("hotel", image.getEntityId());
            if (primary == null) {
                Image first = imageDAO.getFirstImage("hotel", image.getEntityId());
                if (first != null) {
                    imageDAO.setPrimaryImage(first.getId(), "hotel", image.getEntityId());
                }
            }

            out.print("{\"success\":true}");
        } catch (NumberFormatException nfe) {
            out.print("{\"success\":false, \"message\":\"Invalid imageId\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false, \"message\":\"Server error\"}");
        }
    }

    private void handleSetPrimary(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        try {
            String idParam = request.getParameter("imageId");
            String hotelIdParam = request.getParameter("hotelId");
            if (idParam == null || hotelIdParam == null) {
                out.print("{\"success\":false, \"message\":\"Missing parameters\"}");
                return;
            }
            int imageId = Integer.parseInt(idParam);
            int hotelId = Integer.parseInt(hotelIdParam);
            boolean ok = imageDAO.setPrimaryImage(imageId, "hotel", hotelId);
            out.print("{\"success\":" + ok + "}");
        } catch (NumberFormatException nfe) {
            out.print("{\"success\":false, \"message\":\"Invalid parameters\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false, \"message\":\"Server error\"}");
        }
    }
}
