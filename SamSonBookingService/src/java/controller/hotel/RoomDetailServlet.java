/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.hotel;

import dao.ImageDAO;
import dao.RoomDAO;
import entity.Image;
import entity.Room;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RoomDetailServlet", urlPatterns = {"/hotel/room/detail"})
public class RoomDetailServlet extends HttpServlet {
    private RoomDAO roomDAO = new RoomDAO();
    private ImageDAO imageDAO = new ImageDAO();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int roomId = Integer.parseInt(request.getParameter("id"));
            Room room = roomDAO.getRoomById(roomId);
            if (room == null) {
                response.sendRedirect(request.getContextPath() + "/hotel/list");
                return;
            }
            List<Image> roomImages = imageDAO.getImagesByEntity("room", roomId);
            request.setAttribute("room", room);
            request.setAttribute("roomImages", roomImages);
            request.getRequestDispatcher("/hotel/room_detail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        }
    }
}
