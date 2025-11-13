package controller;

import dao.BannerManagerDAO;
import entity.Banner;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

@WebServlet("/banner_add")
@MultipartConfig(
        fileSizeThreshold = 2 * 1024 * 1024, // 2MB
        maxFileSize = 10 * 1024 * 1024, // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class BannerAddServlet extends HttpServlet {

    private BannerManagerDAO bannerDAO;

    @Override
    public void init() throws ServletException {
        bannerDAO = new BannerManagerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/bannerAdd.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String targetUrl = request.getParameter("targetUrl");
        String status = request.getParameter("status");

        String startAtStr = request.getParameter("startAt");
        String endAtStr = request.getParameter("endAt");

        Date startAt = null;
        Date endAt = null;

        try {
            if (startAtStr != null && !startAtStr.isEmpty()) {
                String tmp = startAtStr.replace('T', ' ') + ":00";
                Timestamp ts = Timestamp.valueOf(tmp);
                startAt = new Date(ts.getTime());
            }

            if (endAtStr != null && !endAtStr.isEmpty()) {
                String tmp = endAtStr.replace('T', ' ') + ":00";
                Timestamp ts = Timestamp.valueOf(tmp);
                endAt = new Date(ts.getTime());
            }

            // ✅ KIỂM TRA: endAt < startAt → báo lỗi, không insert
            if (startAt != null && endAt != null && endAt.before(startAt)) {
                request.setAttribute("error", "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.");

                // Forward lại về form, dữ liệu vẫn còn trong ${param.*}
                request.getRequestDispatcher("/bannerAdd.jsp").forward(request, response);
                return;
            }

            // Xử lý ảnh upload
            Part imagePart = request.getPart("imageFile");
            String imageUrl = null;

            if (imagePart != null && imagePart.getSize() > 0) {
                String fileName = getFileName(imagePart);
                if (fileName != null && !fileName.isBlank()) {
                    String uploadDir = getServletContext().getRealPath("/uploads/banners");
                    File uploadFolder = new File(uploadDir);
                    if (!uploadFolder.exists()) {
                        uploadFolder.mkdirs();
                    }

                    File file = new File(uploadFolder, fileName);
                    imagePart.write(file.getAbsolutePath());

                    imageUrl = "uploads/banners/" + fileName;
                }
            }

            if (imageUrl == null) {
                imageUrl = "uploads/banners/default-banner.jpg";
            }

            Banner b = new Banner();
            b.setTitle(title);
            b.setDescription(description);
            b.setTargetUrl(targetUrl);
            b.setStatus(status);
            b.setStartAt(startAt);
            b.setEndAt(endAt);
            b.setImageUrl(imageUrl);

            boolean ok = bannerDAO.addBanner(b);

            if (ok) {
                request.getSession().setAttribute("banner_message", "Thêm banner mới thành công!");
            } else {
                request.getSession().setAttribute("banner_error", "Thêm banner thất bại!");
            }

            response.sendRedirect(request.getContextPath() + "/banner_list");

        } catch (IllegalArgumentException | SQLException e) {
            throw new ServletException("Lỗi khi thêm banner", e);
        }
    }

    private String getFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) {
            return null;
        }

        for (String cd : header.split(";")) {
            cd = cd.trim();
            if (cd.startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
                return fileName;
            }
        }
        return null;
    }
}
