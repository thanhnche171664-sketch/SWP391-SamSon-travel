package controller;

import dao.BannerManagerDAO;
import entity.Banner;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/banner_list")
public class BannerListServlet extends HttpServlet {

    private BannerManagerDAO bannerDAO;

    @Override
    public void init() throws ServletException {
        bannerDAO = new BannerManagerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String message = null;
        String error = null;

        try {
            // XÓA BANNER
            if ("delete".equalsIgnoreCase(action)) {
                String idRaw = request.getParameter("id");
                try {
                    int id = Integer.parseInt(idRaw);
                    boolean deleted = bannerDAO.deleteBanner(id);
                    if (deleted) {
                        message = "Đã xóa banner ID " + id;
                    } else {
                        error = "Không tìm thấy hoặc không xóa được banner ID " + id;
                    }
                } catch (NumberFormatException e) {
                    error = "ID banner không hợp lệ.";
                }

                if (message != null) {
                    request.getSession().setAttribute("banner_message", message);
                }
                if (error != null) {
                    request.getSession().setAttribute("banner_error", error);
                }

                response.sendRedirect(request.getContextPath() + "/banner_list");
                return;
            }

            // HIỂN THỊ DANH SÁCH (CÓ TÌM KIẾM + LỌC TRẠNG THÁI)
            String keyword = request.getParameter("q");
            String status = request.getParameter("status");

            if (status == null || status.isEmpty()) {
                status = "ALL"; // mặc định
            }

            List<Banner> list = bannerDAO.searchBanners(keyword, status);

            // Lấy message / error từ session (nếu vừa redirect sau khi xóa)
            Object msgObj = request.getSession().getAttribute("banner_message");
            Object errObj = request.getSession().getAttribute("banner_error");
            if (msgObj != null) {
                message = msgObj.toString();
                request.getSession().removeAttribute("banner_message");
            }
            if (errObj != null) {
                error = errObj.toString();
                request.getSession().removeAttribute("banner_error");
            }

            request.setAttribute("bannerList", list);
            request.setAttribute("keyword", keyword);
            request.setAttribute("status", status);
            request.setAttribute("message", message);
            request.setAttribute("error", error);

            request.getRequestDispatcher("/bannerList.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error loading banner list", e);
        }
    }
}
