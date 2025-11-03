package controller;

import dao.TransportServiceDAO;
import entity.TransportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name="TransportDeleteServlet", urlPatterns={"/transport-delete"})
public class TransportDeleteServlet extends HttpServlet {
    private final TransportServiceDAO dao = new TransportServiceDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String force = req.getParameter("force");
        int id;

        try { id = Integer.parseInt(idStr); }
        catch (Exception e) {
            resp.sendRedirect(req.getContextPath()+"/transport-service?action=list&error=invalid_id");
            return;
        }

        if ("true".equalsIgnoreCase(force)) {
            try {
                boolean ok = dao.delete(id);
                if (ok) resp.sendRedirect(req.getContextPath()+"/transport-service?action=list&message=delete_success");
                else   resp.sendRedirect(req.getContextPath()+"/transport-service?action=list&error=delete_not_found");
            } catch (RuntimeException ex) {
                String msg = ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage();
                resp.sendRedirect(req.getContextPath()+"/transport-service?action=list&error="+java.net.URLEncoder.encode(msg,"UTF-8"));
            }
            return;
        }

        TransportService item = dao.getById(id);
        if (item == null) {
            resp.sendRedirect(req.getContextPath()+"/transport-service?action=list&error=not_found");
            return;
        }
        req.setAttribute("item", item);
        req.getRequestDispatcher("/transport_delete.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String idStr = req.getParameter("id");
        int id;

        try { id = Integer.parseInt(idStr); }
        catch (Exception e) {
            resp.sendRedirect(req.getContextPath()+"/transport-service?action=list&error=invalid_id");
            return;
        }

        try {
            boolean ok = dao.delete(id);
            if (ok) {
                resp.sendRedirect(req.getContextPath()+"/transport-service?action=list&message=delete_success");
            } else {
                resp.sendRedirect(req.getContextPath()+"/transport-service?action=list&error=delete_not_found");
            }
        } catch (RuntimeException ex) {
            String msg = ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage();
            TransportService item = dao.getById(id);
            req.setAttribute("item", item);
            req.setAttribute("error", "Không thể xoá: " + msg);
            req.getRequestDispatcher("/transport_delete.jsp").forward(req, resp);
        }
    }
}