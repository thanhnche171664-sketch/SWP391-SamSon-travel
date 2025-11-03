package controller;

import dao.TourDAO;
import entity.Tour;
import jakarta.servlet.http.HttpSession;
import util.CsrfTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BookingSelectTourServlet", urlPatterns = {"/booking/select-tour"})
public class BookingSelectTourServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TourDAO tourDAO = new TourDAO();
        List<Tour> tours = tourDAO.getAllTours();
        req.setAttribute("tours", tours);
        HttpSession session = req.getSession(true);
        String csrf = CsrfTokenUtil.ensureToken(session);
        req.setAttribute("csrfToken", csrf);
        // Schedules and packages will be fetched dynamically via subsequent page requests
        req.getRequestDispatcher("/booking/select_tour.jsp").forward(req, resp);
    }
}


