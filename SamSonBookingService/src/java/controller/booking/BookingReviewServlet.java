package controller.booking;

import dao.HotelDAO;
import dao.RoomDAO;
import dao.MealServiceDAO;
import dao.WellnessServiceDAO;
import dao.TransportServiceDAO;
import entity.Hotel;
import entity.Room;
import entity.MealService;
import entity.WellnessService;
import entity.TransportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = {"/bookings/review"})
public class BookingReviewServlet extends HttpServlet {

    private final HotelDAO hotelDAO = new HotelDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final MealServiceDAO mealServiceDAO = new MealServiceDAO();
    private final WellnessServiceDAO wellnessServiceDAO = new WellnessServiceDAO();
    private final TransportServiceDAO transportServiceDAO = new TransportServiceDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Clear previous booking session data
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("booking_hotel");
            session.removeAttribute("booking_room_type");
            session.removeAttribute("booking_number_of_rooms");
            session.removeAttribute("booking_num_adults");
            session.removeAttribute("booking_num_children");
            session.removeAttribute("booking_check_in_date");
            session.removeAttribute("booking_check_out_date");
            session.removeAttribute("booking_meal_ids");
            session.removeAttribute("booking_meal_qtys");
            session.removeAttribute("booking_wellness_ids");
            session.removeAttribute("booking_wellness_qtys");
            session.removeAttribute("booking_transport_id");
        }

        String hotelParam = request.getParameter("hotel");
        String roomType = request.getParameter("room_type");
        String roomsCountParam = request.getParameter("number_of_rooms");
        String checkInStr = request.getParameter("check_in_date");
        String checkOutStr = request.getParameter("check_out_date");
        String numAdultsParam = request.getParameter("num_adults");
        String numChildrenParam = request.getParameter("num_children");

        if (hotelParam == null || roomType == null || roomsCountParam == null ||
            checkInStr == null || checkOutStr == null || numAdultsParam == null || numChildrenParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
            return;
        }

        try {
            int hotelId = Integer.parseInt(hotelParam);
            int numberOfRooms = Integer.parseInt(roomsCountParam);
            int numAdults = Integer.parseInt(numAdultsParam);
            int numChildren = Integer.parseInt(numChildrenParam);
            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);

            if (!checkIn.isBefore(checkOut)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date range");
                return;
            }
            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (nights < 1 || nights > 30) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid nights");
                return;
            }

            Hotel hotel = hotelDAO.getHotelById(hotelId);
            if (hotel == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hotel not found");
                return;
            }

            // Load base pricing info
            List<Room> rooms = roomDAO.getRoomsByHotelId(hotelId);
            Room selectedRoom = rooms.stream().filter(r -> roomType.equalsIgnoreCase(r.getRoomType())).findFirst().orElse(null);
            if (selectedRoom == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid room type");
                return;
            }

            double roomSubtotal = selectedRoom.getPrice() * nights * numberOfRooms;

            // Parse selected addons (ids and quantities)
            // Note: Only checked checkboxes submit their values, but all quantity inputs submit
            // So we need to match by index or use a different approach
            String[] mealIds = request.getParameterValues("meal_id");
            String[] mealQtys = request.getParameterValues("meal_qty");
            String[] wellnessIds = request.getParameterValues("wellness_id");
            String[] wellnessQtys = request.getParameterValues("wellness_qty");

            double addonsTotal = 0.0;
            List<MealService> chosenMeals = new ArrayList<>();
            List<Integer> chosenMealQtys = new ArrayList<>();
            if (mealIds != null && mealQtys != null) {
                // Match meal IDs with quantities by index
                // Since only checked checkboxes submit, we need to find the corresponding quantity
                // We'll use the meal ID to find the index in the original list
                List<MealService> allMeals = mealServiceDAO.getMealServicesByHotelId(hotelId);
                for (String mealIdStr : mealIds) {
                    try {
                        int mealId = Integer.parseInt(mealIdStr);
                        // Find the meal in the list to get its index
                        int mealIndex = -1;
                        for (int j = 0; j < allMeals.size(); j++) {
                            if (allMeals.get(j).getMealId() == mealId) {
                                mealIndex = j;
                                break;
                            }
                        }
                        if (mealIndex >= 0 && mealIndex < mealQtys.length) {
                            int qty = Integer.parseInt(mealQtys[mealIndex]);
                            if (qty > 0) {
                                MealService ms = mealServiceDAO.getMealServiceById(mealId);
                                if (ms != null) {
                                    chosenMeals.add(ms);
                                    chosenMealQtys.add(qty);
                                    addonsTotal += ms.getPrice() * qty;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid meal ID
                    }
                }
            }

            List<WellnessService> chosenWellness = new ArrayList<>();
            List<Integer> chosenWellnessQtys = new ArrayList<>();
            if (wellnessIds != null && wellnessQtys != null) {
                // Match wellness IDs with quantities by index
                List<WellnessService> allWellness = wellnessServiceDAO.getWellnessServicesByHotelId(hotelId);
                for (String wellnessIdStr : wellnessIds) {
                    try {
                        int wellnessId = Integer.parseInt(wellnessIdStr);
                        // Find the wellness service in the list to get its index
                        int wellnessIndex = -1;
                        for (int j = 0; j < allWellness.size(); j++) {
                            if (allWellness.get(j).getWellnessId() == wellnessId) {
                                wellnessIndex = j;
                                break;
                            }
                        }
                        if (wellnessIndex >= 0 && wellnessIndex < wellnessQtys.length) {
                            int qty = Integer.parseInt(wellnessQtys[wellnessIndex]);
                            if (qty > 0) {
                                WellnessService ws = wellnessServiceDAO.getById(wellnessId);
                                if (ws != null) {
                                    chosenWellness.add(ws);
                                    chosenWellnessQtys.add(qty);
                                    addonsTotal += ws.getBasePrice() * qty;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid wellness ID
                    }
                }
            }

            // Parse transport selection (multiple transports can be selected)
            List<TransportService> chosenTransports = new ArrayList<>();
            List<Integer> chosenTransportQtys = new ArrayList<>();
            double transportFee = 0.0;
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
                                    chosenTransports.add(ts);
                                    chosenTransportQtys.add(qty);
                                    transportFee += ts.getPrice() * qty;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid transport ID
                    }
                }
            }

            double total = roomSubtotal + addonsTotal + transportFee;

            String bookingCode = ("B" + System.currentTimeMillis()).toUpperCase();
            String description = "BOOK-" + bookingCode;
            String qrUrl = "https://img.vietqr.io/image/MB-0972391380-compact.png"
                    + (long) Math.ceil(total)
                    + "&addInfo=" + URLEncoder.encode(description, StandardCharsets.UTF_8)
                    + "&accountName=DO%20DANG%20LONG";

            // Convert LocalDate to Date for JSP formatting
            Date checkInDate = Date.from(checkIn.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date checkOutDate = Date.from(checkOut.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            request.setAttribute("hotel", hotel);
            request.setAttribute("roomType", roomType);
            request.setAttribute("numberOfRooms", numberOfRooms);
            request.setAttribute("numAdults", numAdults);
            request.setAttribute("numChildren", numChildren);
            request.setAttribute("checkIn", checkInDate);
            request.setAttribute("checkOut", checkOutDate);
            request.setAttribute("nights", nights);
            request.setAttribute("roomSubtotal", roomSubtotal);
            request.setAttribute("addonsTotal", addonsTotal);
            request.setAttribute("total", total);
            request.setAttribute("bookingCode", bookingCode);
            request.setAttribute("qrUrl", qrUrl);
            request.setAttribute("chosenMeals", chosenMeals);
            request.setAttribute("chosenMealQtys", chosenMealQtys);
            request.setAttribute("chosenWellness", chosenWellness);
            request.setAttribute("chosenWellnessQtys", chosenWellnessQtys);
            request.setAttribute("chosenTransports", chosenTransports);
            request.setAttribute("chosenTransportQtys", chosenTransportQtys);
            request.setAttribute("transportFee", transportFee);

            request.getRequestDispatcher("/bookings/booking_review.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input: " + ex.getMessage());
        }
    }
}



