package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "WellnessEditServlet", urlPatterns = {"/wellness-edit"})
public class WellnessEditServlet extends HttpServlet {

    private WellnessServiceDAO dao;
    private static final int DEFAULT_PAGE_SIZE = 10;


    @Override
    public void init() throws ServletException {
        dao = new WellnessServiceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cancel = request.getParameter("cancel");
            if ("true".equalsIgnoreCase(cancel)) {
                int page = 1;
                if (pageParam != null && !pageParam.isEmpty()) {
                    try {
                        page = Integer.parseInt(pageParam);
                    } catch (NumberFormatException ignored) {}
                }

                String statusFilter = request.getParameter("status");
                if (statusFilter == null || statusFilter.trim().isEmpty()) {
                    statusFilter = "all";
                }

                int totalRecords = dao.countAll(statusFilter);
                int totalPages = (int) Math.ceil((double) totalRecords / DEFAULT_PAGE_SIZE);
                if (page > totalPages && totalPages > 0) page = totalPages;
                if (page <= 0) page = 1;

                List<WellnessService> list = dao.getAll(page, DEFAULT_PAGE_SIZE, statusFilter);

                request.setAttribute("list", list);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("statusFilter", statusFilter);

                request.getRequestDispatcher("wellness_list.jsp").forward(request, response);
                return;
            }


            int id = Integer.parseInt(request.getParameter("id"));
            WellnessService ws = dao.getById(id);
                request.setAttribute("error", "Không tìm thấy dịch vụ cần chỉnh sửa!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            String page = request.getParameter("page");
            String status = request.getParameter("status");
            if (page == null || page.isEmpty()) page = "1";
            if (status == null || status.isEmpty()) status = "all";

            request.setAttribute("page", page);
            request.setAttribute("status", status);
            request.setAttribute("wellnessService", ws);
            request.getRequestDispatcher("wellness_edit.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        boolean hasError = false;
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {}
        }

        String statusFilter = request.getParameter("status");
        if (statusFilter == null || statusFilter.trim().isEmpty()) {
            statusFilter = "all";
        }
            int id = Integer.parseInt(request.getParameter("wellnessId"));
            String name = request.getParameter("serviceName");
            String description = request.getParameter("description");
            String basePriceStr = request.getParameter("basePrice");
            String durationStr = request.getParameter("durationMinutes");
            String capacityStr = request.getParameter("capacity");
            String operatingHours = request.getParameter("operatingHours");
            String status = request.getParameter("status");

            double basePrice = 0;
            int duration = 0;
            int capacity = 0;

            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("errorServiceName", "Tên dịch vụ không được để trống.");
                hasError = true;
            }

            if (description == null || description.trim().isEmpty()) {
                request.setAttribute("errorDescription", "Mô tả không được để trống.");
                hasError = true;
            }

            if (basePriceStr == null || basePriceStr.trim().isEmpty()) {
                request.setAttribute("errorBasePrice", "Giá cơ bản không được để trống.");
                hasError = true;
            } else {
                try {
                    basePrice = Double.parseDouble(basePriceStr);
                    if (basePrice < 0) {
                        request.setAttribute("errorBasePrice", "Giá cơ bản phải >= 0.");
                        hasError = true;
                    } else if (basePrice < 80000) {
                        request.setAttribute("errorBasePrice", "Giá cơ bản phải từ 80.000 VND trở lên.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorBasePrice", "Giá cơ bản phải là số.");
                    hasError = true;
                }
            }

            if (durationStr == null || durationStr.trim().isEmpty()) {
                request.setAttribute("errorDuration", "Thời lượng không được để trống.");
                hasError = true;
            } else {
                try {
                    duration = Integer.parseInt(durationStr);
                    if (duration <= 0) {
                        request.setAttribute("errorDuration", "Thời lượng phải > 0 phút.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorDuration", "Thời lượng phải là số nguyên.");
                    hasError = true;
                }
            }

            if (capacityStr == null || capacityStr.trim().isEmpty()) {
                request.setAttribute("errorCapacity", "Sức chứa không được để trống.");
                hasError = true;
            } else {
                try {
                    capacity = Integer.parseInt(capacityStr);
                    if (capacity <= 0) {
                        request.setAttribute("errorCapacity", "Sức chứa phải > 0.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorCapacity", "Sức chứa phải là số nguyên.");
                    hasError = true;
                }
            }

            if (operatingHours == null || operatingHours.trim().isEmpty()) {
                request.setAttribute("errorOperatingHours", "Giờ hoạt động không được để trống.");
                hasError = true;
            } else {
                String pattern = "^(\\d{2}):(\\d{2})\\s*[-–]\\s*(\\d{2}):(\\d{2})$";
                java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
                java.util.regex.Matcher matcher = regex.matcher(operatingHours.trim());

                if (!matcher.matches()) {
                    request.setAttribute("errorOperatingHours",
                            "Giờ hoạt động phải đúng định dạng (VD: 08:00–21:00).");
                    hasError = true;
                } else {
                    try {
                        int startHour = Integer.parseInt(matcher.group(1));
                        int startMin = Integer.parseInt(matcher.group(2));
                        int endHour = Integer.parseInt(matcher.group(3));
                        int endMin = Integer.parseInt(matcher.group(4));

                        boolean outOfRange = (startHour < 8 || endHour > 21
                                || (startHour == 21 && startMin > 0)
                                || (endHour == 21 && endMin > 0));

                        if (outOfRange) {
                            request.setAttribute("errorOperatingHours",
                                    "Giờ hoạt động phải nằm trong khoảng 08:00–21:00.");
                            hasError = true;
                        } else if ((endHour < startHour) || (endHour == startHour && endMin <= startMin)) {
                            request.setAttribute("errorOperatingHours",
                                    "Giờ kết thúc phải lớn hơn giờ bắt đầu.");
                            hasError = true;
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("errorOperatingHours",
                                "Giờ hoạt động không hợp lệ.");
                        hasError = true;
                    }
                }
            }

            if (status == null || status.trim().isEmpty()) {
                request.setAttribute("errorStatus", "Phải chọn trạng thái.");
                hasError = true;
            }

            if (hasError) {
                WellnessService ws = dao.getById(id);
                if (ws != null) {
                    ws.setServiceName(name);
                    ws.setDescription(description);
                    ws.setBasePrice(basePrice);
                    ws.setDurationMinutes(duration);
                    ws.setCapacity(capacity);
                    ws.setOperatingHours(operatingHours);
                    ws.setStatus(status);
                }

                request.setAttribute("error", "Vui lòng kiểm tra lại thông tin bên dưới.");
                request.setAttribute("wellnessService", ws);
                request.setAttribute("page", page);
                request.setAttribute("status", statusFilter);
                request.getRequestDispatcher("wellness_edit.jsp").forward(request, response);
                return;
            }

            double basePrice = Double.parseDouble(request.getParameter("basePrice"));
            int duration = Integer.parseInt(request.getParameter("durationMinutes"));
            String operatingHours = request.getParameter("operatingHours");
            int capacity = Integer.parseInt(request.getParameter("capacity"));
            String status = request.getParameter("status");

            WellnessService ws = dao.getById(id);
            if (ws == null) {
                request.setAttribute("error", "Không tìm thấy dịch vụ cần cập nhật!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            ws.setServiceName(name);
            ws.setDescription(description);
            ws.setBasePrice(basePrice);
            ws.setDurationMinutes(duration);
            ws.setOperatingHours(operatingHours);
            ws.setCapacity(capacity);
            ws.setStatus(status);

            boolean updated = dao.updateWellnessService(ws);

            if (updated) {
                int totalRecords = dao.countAll(statusFilter);
                int totalPages = (int) Math.ceil((double) totalRecords / DEFAULT_PAGE_SIZE);
                if (page > totalPages && totalPages > 0) page = totalPages;
                if (page <= 0) page = 1;

                List<WellnessService> list = dao.getAll(page, DEFAULT_PAGE_SIZE, statusFilter);

                request.setAttribute("message", "Cập nhật dịch vụ thành công!");
                request.setAttribute("list", list);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("statusFilter", statusFilter);
                request.getRequestDispatcher("wellness_list.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Cập nhật thất bại!");
                request.setAttribute("wellnessService", ws);
                request.setAttribute("page", page);
                request.setAttribute("status", statusFilter);
                request.getRequestDispatcher("wellness_edit.jsp").forward(request, response);
            }

                response.sendRedirect("wellness-service?action=list&message=update_success");
            } else {
                request.setAttribute("error", "Cập nhật thất bại!");
                request.setAttribute("wellnessService", ws);
                request.getRequestDispatcher("wellness_edit.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
