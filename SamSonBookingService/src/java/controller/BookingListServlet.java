package controller;

import dao.BookingManagerDAO;
import entity.BookingView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/booking_list")
public class BookingListServlet extends HttpServlet {

    private BookingManagerDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingManagerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        try {
            // 👉 Xem chi tiết booking
            if ("view".equals(action) && idParam != null) {
                int id = Integer.parseInt(idParam);
                BookingView booking = bookingDAO.getBookingViewById(id);

                if (booking == null) {
                    response.sendRedirect(request.getContextPath() + "/booking_list");
                    return;
                }

                request.setAttribute("booking", booking);
                request.getRequestDispatcher("/bookingDetail.jsp")
                        .forward(request, response);
                return;
            }

            // 👉 Danh sách + lọc
            String keyword = request.getParameter("keyword");
            String status = request.getParameter("status");

            if (status == null || status.trim().isEmpty()) {
                status = "all";
            }

            List<BookingView> list = bookingDAO.searchBookingViews(keyword, status);
            request.setAttribute("bookings", list);

            request.getRequestDispatcher("/bookingList.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error processing booking list", e);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid booking id", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String redirect = request.getParameter("redirect"); // "detail" hoặc null
        String idParam = request.getParameter("id");

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/booking_list");
            return;
        }

        try {
            if ("update".equals(action)) {
                int id = Integer.parseInt(idParam);
                String newStatus = request.getParameter("newStatus");

                if (newStatus != null && !newStatus.isEmpty()) {
                    bookingDAO.updateBookingStatus(id, newStatus);
                }

                // Nếu update từ trang detail → quay lại detail
                if ("detail".equals(redirect)) {
                    response.sendRedirect(request.getContextPath()
                            + "/booking_list?action=view&id=" + id);
                } else {
                    response.sendRedirect(request.getContextPath() + "/booking_list");
                }

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(idParam);
                bookingDAO.deleteBooking(id);
                response.sendRedirect(request.getContextPath() + "/booking_list");
            } else {
                response.sendRedirect(request.getContextPath() + "/booking_list");
            }

        } catch (SQLException e) {
            throw new ServletException("Error updating or deleting booking", e);
        }
    }
}
