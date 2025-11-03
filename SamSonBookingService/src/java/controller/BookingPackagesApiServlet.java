package controller;

import com.google.gson.Gson;
import dao.TourPackageDAO;
import entity.TourPackage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "BookingPackagesApiServlet", urlPatterns = {"/booking/api/packages"})
public class BookingPackagesApiServlet extends HttpServlet {
    private final TourPackageDAO dao = new TourPackageDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int tourId = Integer.parseInt(req.getParameter("tourId"));
        List<TourPackage> list = dao.getPackagesByTourId(tourId);
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.write(gson.toJson(list));
        }
    }
}


