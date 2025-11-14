package controller.hotel;

import dao.HotelDAO;
import entity.Hotel;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Test Servlet để kiểm tra dữ liệu hotels trong database
 * Chỉ dùng để debug, xóa sau khi fix xong
 */
@WebServlet("/hotel/test")
public class HotelTestServlet extends HttpServlet {

    private static final HotelDAO hotelDAO = new HotelDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Hotel Test</title>");
        out.println("<style>body{font-family: monospace; padding: 20px;} table{border-collapse: collapse; width: 100%;} th,td{border: 1px solid #ddd; padding: 8px;} th{background: #667eea; color: white;}</style>");
        out.println("</head><body>");
        out.println("<h1>🔍 Hotel Database Test</h1>");

        // Kiểm tra session
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            out.println("<h2>Current User:</h2>");
            out.println("<p>ID: " + user.getId() + "<br/>");
            out.println("Name: " + user.getName() + "<br/>");
            out.println("Role ID: " + user.getRoleId() + "</p>");
        } else {
            out.println("<p style='color: red;'>No user in session!</p>");
        }

        // Test 1: Tổng số hotels
        out.println("<h2>Test 1: Total Hotels Count</h2>");
        int totalCount = hotelDAO.getHotelCount();
        out.println("<p>Total hotels in database: <strong>" + totalCount + "</strong></p>");

        // Test 2: Lấy tất cả hotels
        out.println("<h2>Test 2: All Hotels (No Filter)</h2>");
        List<Hotel> allHotels = hotelDAO.getAllHotels();
        out.println("<p>Number of hotels returned: <strong>" + (allHotels != null ? allHotels.size() : "NULL") + "</strong></p>");

        if (allHotels != null && !allHotels.isEmpty()) {
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Name</th><th>Address</th><th>Manager ID</th><th>Created At</th></tr>");
            for (Hotel hotel : allHotels) {
                out.println("<tr>");
                out.println("<td>" + hotel.getId() + "</td>");
                out.println("<td>" + (hotel.getName() != null ? hotel.getName() : "NULL") + "</td>");
                out.println("<td>" + (hotel.getAddress() != null ? hotel.getAddress() : "NULL") + "</td>");
                out.println("<td>" + hotel.getManagerId() + "</td>");
                out.println("<td>" + (hotel.getCreatedAt() != null ? hotel.getCreatedAt().toString() : "NULL") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        } else {
            out.println("<p style='color: red;'>⚠️ No hotels found in database!</p>");
        }

        // Test 3: Lấy hotels theo manager (nếu có user)
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            out.println("<h2>Test 3: Hotels for Manager ID = " + user.getId() + "</h2>");
            List<Hotel> managerHotels = hotelDAO.getHotelsPaginated(1, 10, null, user.getId());
            int managerCount = hotelDAO.countHotelsFiltered(null, user.getId());
            out.println("<p>Hotels for this manager: <strong>" + managerCount + "</strong></p>");
            
            if (managerHotels != null && !managerHotels.isEmpty()) {
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Name</th><th>Address</th></tr>");
                for (Hotel hotel : managerHotels) {
                    out.println("<tr>");
                    out.println("<td>" + hotel.getId() + "</td>");
                    out.println("<td>" + hotel.getName() + "</td>");
                    out.println("<td>" + hotel.getAddress() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<p style='color: orange;'>⚠️ No hotels found for manager ID: " + user.getId() + "</p>");
                out.println("<p>Possible reasons:</p>");
                out.println("<ul>");
                out.println("<li>No hotels exist with manager_id = " + user.getId() + "</li>");
                out.println("<li>Check if hotels in database have correct manager_id</li>");
                out.println("</ul>");
            }
        }

        out.println("<hr/>");
        out.println("<p><a href='" + request.getContextPath() + "/hotel/list'>← Back to Hotel List</a></p>");
        out.println("</body></html>");
    }
}


