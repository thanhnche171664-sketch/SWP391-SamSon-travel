package controller;

import com.google.gson.Gson;
import dao.TourScheduleDAO;
import entity.TourSchedule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "BookingSchedulesApiServlet", urlPatterns = {"/booking/api/schedules"})
public class BookingSchedulesApiServlet extends HttpServlet {
    private final TourScheduleDAO dao = new TourScheduleDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int tourId = Integer.parseInt(req.getParameter("tourId"));
        List<TourSchedule> list = dao.getSchedulesByTourId(tourId);
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.write(gson.toJson(list));
        }
    }
}


