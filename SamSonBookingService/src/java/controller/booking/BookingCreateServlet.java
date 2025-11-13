package controller.booking;

import dao.BookingDAO;
import dao.MealServiceDAO;
import dao.WellnessServiceDAO;
import dao.TransportServiceDAO;
import entity.Booking;
import entity.BookingDetail;
import entity.MealService;
import entity.WellnessService;
import entity.TransportService;
import service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/bookings/confirm"})
public class BookingCreateServlet extends HttpServlet {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final BookingService bookingService = new BookingService();
    private final MealServiceDAO mealServiceDAO = new MealServiceDAO();
    private final WellnessServiceDAO wellnessServiceDAO = new WellnessServiceDAO();
    private final TransportServiceDAO transportServiceDAO = new TransportServiceDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        entity.User user = (session != null) ? (entity.User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int hotelId = Integer.parseInt(request.getParameter("hotel"));
            String roomType = request.getParameter("room_type");
            int numberOfRooms = Integer.parseInt(request.getParameter("number_of_rooms"));
            int numAdults = Integer.parseInt(request.getParameter("num_adults"));
            int numChildren = Integer.parseInt(request.getParameter("num_children"));
            LocalDate checkIn = LocalDate.parse(request.getParameter("check_in_date"));
            LocalDate checkOut = LocalDate.parse(request.getParameter("check_out_date"));
            String bookingCode = request.getParameter("booking_code");
            double total = Double.parseDouble(request.getParameter("total"));

            // Validate dates
            bookingService.validateDates(checkIn, checkOut);
            
            // Check availability
            if (!bookingService.isAvailable(hotelId, roomType, checkIn, checkOut, numberOfRooms)) {
                request.setAttribute("errorMessage", "Không đủ phòng trống cho khoảng thời gian này. Vui lòng chọn ngày khác.");
                request.getRequestDispatcher("/bookings/booking_review.jsp").forward(request, response);
                return;
            }

            // Parse addons with names
            List<BookingDetail> addons = new ArrayList<>();
            Map<Integer, String> addonNames = new HashMap<>();
            
            // Parse meal services
            String[] mealIds = request.getParameterValues("meal_id");
            String[] mealQtys = request.getParameterValues("meal_qty");
            if (mealIds != null && mealQtys != null && mealIds.length == mealQtys.length) {
                for (int i = 0; i < mealIds.length; i++) {
                    try {
                        int mealId = Integer.parseInt(mealIds[i]);
                        int qty = Integer.parseInt(mealQtys[i]);
                        if (qty > 0) {
                            MealService ms = mealServiceDAO.getMealServiceById(mealId);
                            if (ms != null) {
                                BookingDetail addon = new BookingDetail();
                                addon.setCategoryName("MEAL");
                                addon.setCategoryId(mealId);
                                addon.setPrice(ms.getPrice());
                                addon.setQuantity(qty);
                                addons.add(addon);
                                // Store name for this addon
                                addonNames.put(mealId, ms.getMealType() + " (" + ms.getMealDate() + ")");
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid meal ID
                    }
                }
            }
            
            // Parse wellness services
            String[] wellnessIds = request.getParameterValues("wellness_id");
            String[] wellnessQtys = request.getParameterValues("wellness_qty");
            if (wellnessIds != null && wellnessQtys != null && wellnessIds.length == wellnessQtys.length) {
                for (int i = 0; i < wellnessIds.length; i++) {
                    try {
                        int wellnessId = Integer.parseInt(wellnessIds[i]);
                        int qty = Integer.parseInt(wellnessQtys[i]);
                        if (qty > 0) {
                            WellnessService ws = wellnessServiceDAO.getById(wellnessId);
                            if (ws != null) {
                                BookingDetail addon = new BookingDetail();
                                addon.setCategoryName("WELLNESS");
                                addon.setCategoryId(wellnessId);
                                addon.setPrice(ws.getBasePrice());
                                addon.setQuantity(qty);
                                addons.add(addon);
                                // Store name for this addon
                                addonNames.put(wellnessId, ws.getServiceName());
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid wellness ID
                    }
                }
            }

            // Parse transport selection (multiple transports can be selected)
            String[] transportIds = request.getParameterValues("transport_id");
            String[] transportQtys = request.getParameterValues("transport_qty");
            if (transportIds != null && transportQtys != null) {
                // Match transport IDs with quantities by index
                List<TransportService> allTransports = transportServiceDAO.getTransportServicesByHotelId(hotelId);
                for (String transportIdStr : transportIds) {
                    try {
                        int transportId = Integer.parseInt(transportIdStr);
                        // Find the transport in the list to get its index
                        int transportIndex = -1;
                        for (int j = 0; j < allTransports.size(); j++) {
                            if (allTransports.get(j).getTransportId() == transportId) {
                                transportIndex = j;
                                break;
                            }
                        }
                        if (transportIndex >= 0 && transportIndex < transportQtys.length) {
                            int qty = Integer.parseInt(transportQtys[transportIndex]);
                            if (qty > 0) {
                                TransportService ts = transportServiceDAO.getById(transportId);
                                if (ts != null) {
                                    BookingDetail addon = new BookingDetail();
                                    addon.setCategoryName("TRANSPORT");
                                    addon.setCategoryId(transportId);
                                    addon.setPrice(ts.getPrice());
                                    addon.setQuantity(qty);
                                    addons.add(addon);
                                    // Store name for this addon
                                    addonNames.put(transportId, ts.getVehicleName() + " (" + ts.getVehicleType() + ")");
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid transport ID
                    }
                }
            }

            // Create booking
            Booking booking = new Booking();
            booking.setUserId(user.getId());
            booking.setHotelId(hotelId);
            booking.setRoomType(roomType);
            booking.setNumberOfRooms(numberOfRooms);
            booking.setBookingSource("ONLINE");
            booking.setStatus("pending");
            booking.setTotalPrice(total);
            booking.setTransportId(null); // Transport is now stored in Booking_Addons
            booking.setTransportFee(0); // Transport fee is included in total, calculated from addons
            booking.setBookingDate(new java.util.Date());
            booking.setCreatedBy(user.getId());

            int bookingId = bookingDAO.createBookingTransactional(
                    booking,
                    bookingCode,
                    checkIn,
                    checkOut,
                    numAdults,
                    numChildren,
                    addons.isEmpty() ? null : addons,
                    addonNames.isEmpty() ? null : addonNames
            );
            
            if (bookingId <= 0) {
                request.setAttribute("errorMessage", "Tạo đặt phòng thất bại. Vui lòng thử lại.");
                request.getRequestDispatcher("/bookings/booking_review.jsp").forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/bookings/success?id=" + bookingId);

        } catch (NumberFormatException | DateTimeParseException ex) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.");
            request.getRequestDispatcher("/bookings/booking_review.jsp").forward(request, response);
        } catch (IllegalArgumentException ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/bookings/booking_review.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/bookings/booking_review.jsp").forward(request, response);
        }
    }
}
