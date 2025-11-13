/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
@WebServlet(name = "WellnessServiceController", urlPatterns = {"/wellness-service"})
public class WellnessServiceController extends HttpServlet {

    private WellnessServiceDAO wellnessServiceDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        wellnessServiceDAO = new WellnessServiceDAO();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listWellnessServices(request, response);
                    break;
                case "view":
                    viewWellnessService(request, response);
                    break;
                case "add":
                    addWellnessService(request, response);
                    break;
                case "edit":
                    editWellnessService(request, response);
                    break;
                case "update":
                    updateWellnessService(request, response);
                    break;
                case "delete":
                    deleteWellnessService(request, response);
                    break;
                case "search":
                    searchWellnessServices(request, response);
                    break;
                case "filter":
                    filterWellnessServices(request, response);
                    break;
                default:
                    listWellnessServices(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void listWellnessServices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<WellnessService> services = wellnessServiceDAO.getAll(1, 5, "all");
        request.setAttribute("wellnessServices", services);
        request.getRequestDispatcher("/wellness_list.jsp").forward(request, response);
    }

    private void viewWellnessService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int wellnessId = Integer.parseInt(request.getParameter("id"));
        WellnessService service = wellnessServiceDAO.getById(wellnessId);

        if (service != null) {
            request.setAttribute("wellnessService", service);
            request.getRequestDispatcher("/wellness_detail.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không tìm thấy dịch vụ wellness");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void addWellnessService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getMethod().equals("GET")) {
            request.getRequestDispatcher("/wellness_form.jsp").forward(request, response);
        } else if (request.getMethod().equals("POST")) {
            try {
                int hotelId = Integer.parseInt(request.getParameter("hotelId"));
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                String serviceName = request.getParameter("serviceName");
                String description = request.getParameter("description");
                double basePrice = Double.parseDouble(request.getParameter("basePrice"));
                int durationMinutes = Integer.parseInt(request.getParameter("durationMinutes"));
                String operatingHours = request.getParameter("operatingHours");
                int capacity = Integer.parseInt(request.getParameter("capacity"));
                String status = request.getParameter("status");

                WellnessService service = new WellnessService();
                service.setHotelId(hotelId);
                service.setCategoryId(categoryId);
                service.setServiceName(serviceName);
                service.setDescription(description);
                service.setBasePrice(basePrice);
                service.setDurationMinutes(durationMinutes);
                service.setOperatingHours(operatingHours);
                service.setCapacity(capacity);
                service.setStatus(status);
                service.setCreatedAt(new Date());
                service.setUpdatedAt(new Date());

                boolean success = wellnessServiceDAO.addWellnessService(service);

                if (success) {
                    response.sendRedirect("wellness-service?action=list&message=add_success");
                } else {
                    request.setAttribute("error", "Thêm dịch vụ wellness thất bại");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Dữ liệu đầu vào không hợp lệ");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        }
    }

    private void editWellnessService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int wellnessId = Integer.parseInt(request.getParameter("id"));
        WellnessService service = wellnessServiceDAO.getById(wellnessId);

        if (service != null) {
            request.setAttribute("wellnessService", service);
            request.setAttribute("ws", service);
            request.getRequestDispatcher("/wellness_edit.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không tìm thấy dịch vụ wellness");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void updateWellnessService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int wellnessId = Integer.parseInt(request.getParameter("wellnessId"));
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String serviceName = request.getParameter("serviceName");
            String description = request.getParameter("description");
            double basePrice = Double.parseDouble(request.getParameter("basePrice"));
            int durationMinutes = Integer.parseInt(request.getParameter("durationMinutes"));
            String operatingHours = request.getParameter("operatingHours");
            int capacity = Integer.parseInt(request.getParameter("capacity"));
            String status = request.getParameter("status");

            WellnessService service = new WellnessService();
            service.setWellnessId(wellnessId);
            service.setHotelId(hotelId);
            service.setCategoryId(categoryId);
            service.setServiceName(serviceName);
            service.setDescription(description);
            service.setBasePrice(basePrice);
            service.setDurationMinutes(durationMinutes);
            service.setOperatingHours(operatingHours);
            service.setCapacity(capacity);
            service.setStatus(status);
            service.setUpdatedAt(new Date());

            boolean success = wellnessServiceDAO.updateWellnessService(service);

            if (success) {
                response.sendRedirect("wellness-service?action=list&message=update_success");
            } else {
                request.setAttribute("error", "Cập nhật dịch vụ wellness thất bại");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu đầu vào không hợp lệ");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void deleteWellnessService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int wellnessId = Integer.parseInt(request.getParameter("id"));

        boolean success = wellnessServiceDAO.deleteWellnessService(wellnessId);

        if (success) {
            response.sendRedirect("wellness-service?action=list&message=delete_success");
        } else {
            request.setAttribute("error", "Xóa dịch vụ wellness thất bại");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void searchWellnessServices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("searchTerm");
        String status = request.getParameter("status");
        if (status == null || status.isEmpty()) {
            status = "all";
        }

        int page = 1;
        int pageSize = 5;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException ignored) {
        }

        List<WellnessService> services;
        int totalRecords;

        if (keyword != null && !keyword.trim().isEmpty()) {
            services = wellnessServiceDAO.searchByName(keyword.trim(), page, pageSize, status);
            totalRecords = wellnessServiceDAO.countSearch(keyword.trim(), status);
        } else {
            services = wellnessServiceDAO.getAll(page, pageSize, status);
            totalRecords = wellnessServiceDAO.countAll(status);
        }

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("wellnessServices", services);
        request.setAttribute("searchTerm", keyword);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("statusFilter", status);

        request.getRequestDispatcher("/wellness_list.jsp").forward(request, response);
    }

    // Lọc wellness services theo tiêu chí
    private void filterWellnessServices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String filterType = request.getParameter("filterType");
        List<WellnessService> services = null;

        switch (filterType) {
            case "hotel":
                int hotelId = Integer.parseInt(request.getParameter("hotelId"));
                services = wellnessServiceDAO.getWellnessServicesByHotelId(hotelId);
                break;
            case "category":
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                services = wellnessServiceDAO.getWellnessServicesByCategoryId(categoryId);
                break;
            case "price":
                double minPrice = Double.parseDouble(request.getParameter("minPrice"));
                double maxPrice = Double.parseDouble(request.getParameter("maxPrice"));
                services = wellnessServiceDAO.getWellnessServicesByPriceRange(minPrice, maxPrice);
                break;
            default:
                services = wellnessServiceDAO.getAll(1, 5, "all");
                break;
        }

        request.setAttribute("wellnessServices", services);
        request.setAttribute("filterType", filterType);
        request.getRequestDispatcher("/wellness-service-list.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
