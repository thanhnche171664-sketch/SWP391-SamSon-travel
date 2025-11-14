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

@WebServlet("/banner_edit")
@MultipartConfig(
        fileSizeThreshold = 2 * 1024 * 1024,  // 2MB
        maxFileSize = 10 * 1024 * 1024,       // 10MB
        maxRequestSize = 50 * 1024 * 1024     // 50MB
)
public class BannerEditServlet extends HttpServlet {

    private BannerManagerDAO bannerDAO;

    @Override
    public void init() throws ServletException {
        bannerDAO = new BannerManagerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");

        if (idRaw == null) {
            response.sendRedirect(request.getContextPath() + "/banner_list");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);
            Banner b = bannerDAO.getBannerById(id);

            if (b == null) {
                request.getSession().setAttribute("banner_error", "Không tìm thấy banner ID " + id);
                response.sendRedirect(request.getContextPath() + "/banner_list");
                return;
            }

            request.setAttribute("banner", b);
            request.getRequestDispatcher("/bannerEdit.jsp").forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            throw new ServletException("Không load được banner để sửa", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idRaw = request.getParameter("bannerId");
        if (idRaw == null) {
            response.sendRedirect(request.getContextPath() + "/banner_list");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);

            Banner oldBanner = bannerDAO.getBannerById(id);
            if (oldBanner == null) {
                request.getSession().setAttribute("banner_error", "Không tìm thấy banner ID " + id);
                response.sendRedirect(request.getContextPath() + "/banner_list");
                return;
            }

            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String targetUrl = request.getParameter("targetUrl");
            String status = request.getParameter("status");

            String startAtStr = request.getParameter("startAt");
            String endAtStr = request.getParameter("endAt");

            Date startAt = null;
            Date endAt = null;

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

            // ✅ KIỂM TRA: endAt < startAt → báo lỗi, không update
            if (startAt != null && endAt != null && endAt.before(startAt)) {
                String error = "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.";

                // Tạo banner tạm với dữ liệu người dùng vừa nhập để hiển thị lại
                Banner tmpBanner = new Banner();
                tmpBanner.setBannerId(id);
                tmpBanner.setTitle(title);
                tmpBanner.setDescription(description);
                tmpBanner.setTargetUrl(targetUrl);
                tmpBanner.setStatus(status);
                tmpBanner.setStartAt(startAt);
                tmpBanner.setEndAt(endAt);
                tmpBanner.setImageUrl(oldBanner.getImageUrl());

                request.setAttribute("error", error);
                request.setAttribute("banner", tmpBanner);

                request.getRequestDispatcher("/bannerEdit.jsp").forward(request, response);
                return;
            }

            // Xử lý file ảnh upload
            Part imagePart = request.getPart("imageFile");
            String imageUrl = oldBanner.getImageUrl(); // mặc định giữ ảnh cũ

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

            Banner updated = new Banner();
            updated.setBannerId(id);
            updated.setTitle(title);
            updated.setDescription(description);
            updated.setTargetUrl(targetUrl);
            updated.setStatus(status);
            updated.setStartAt(startAt);
            updated.setEndAt(endAt);
            updated.setImageUrl(imageUrl);

            boolean ok = bannerDAO.updateBanner(updated);

            if (ok) {
                request.getSession().setAttribute("banner_message", "Cập nhật banner thành công!");
            } else {
                request.getSession().setAttribute("banner_error", "Cập nhật banner thất bại!");
            }

            response.sendRedirect(request.getContextPath() + "/banner_list");

        } catch (NumberFormatException | SQLException e) {
            throw new ServletException("Lỗi khi cập nhật banner", e);
        }
    }

    private String getFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) return null;

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
