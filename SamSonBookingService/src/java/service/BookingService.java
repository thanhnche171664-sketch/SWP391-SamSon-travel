package service;

import dao.BookingDAO;
import dao.MealServiceDAO;
import dao.WellnessServiceDAO;
import dao.RoomDAO;
import entity.MealService;
import entity.WellnessService;
import entity.Room;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingService {

    private final BookingDAO bookingDAO;
    private final RoomDAO roomDAO;
    private final MealServiceDAO mealServiceDAO;
    private final WellnessServiceDAO wellnessServiceDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.roomDAO = new RoomDAO();
        this.mealServiceDAO = new MealServiceDAO();
        this.wellnessServiceDAO = new WellnessServiceDAO();
    }

    public void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights < 1 || nights > 30) {
            throw new IllegalArgumentException("Invalid nights range");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in must be today or later");
        }
    }

    public Room findRoomOrThrow(int hotelId, String roomType) {
        List<Room> rooms = roomDAO.getRoomsByHotelId(hotelId);
        return rooms.stream()
                .filter(r -> roomType.equalsIgnoreCase(r.getRoomType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid room type"));
    }

    public double calculateRoomSubtotal(Room room, int numberOfRooms, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return room.getPrice() * nights * numberOfRooms;
    }

    public double calculateAddonsTotal(List<Integer> mealIds, List<Integer> mealQtys,
                                       List<Integer> wellnessIds, List<Integer> wellnessQtys) {
        double total = 0.0;
        if (mealIds != null && mealQtys != null) {
            for (int i = 0; i < mealIds.size(); i++) {
                int id = mealIds.get(i);
                int qty = mealQtys.get(i);
                if (qty <= 0) continue;
                MealService ms = mealServiceDAO.getMealServiceById(id);
                if (ms != null) total += ms.getPrice() * qty;
            }
        }
        if (wellnessIds != null && wellnessQtys != null) {
            for (int i = 0; i < wellnessIds.size(); i++) {
                int id = wellnessIds.get(i);
                int qty = wellnessQtys.get(i);
                if (qty <= 0) continue;
                WellnessService ws = wellnessServiceDAO.getById(id);
                if (ws != null) total += ws.getBasePrice() * qty;
            }
        }
        return total;
    }

    public String generateBookingCode() {
        return ("B" + System.currentTimeMillis()).toUpperCase();
    }

    public String buildVietQrUrl(double amount, String bookingCode) {
        String base = "https://img.vietqr.io/image/970422-529042003-compact2.png";
        String description = "BOOK-" + bookingCode;
        return base + "?amount=" + (long) Math.ceil(amount)
                + "&addInfo=" + URLEncoder.encode(description, StandardCharsets.UTF_8)
                + "&accountName=DO%20DANG%20LONG";
    }

    public boolean isAvailable(int hotelId, String roomType, LocalDate checkIn, LocalDate checkOut, int requestedRooms) {
        int sumBooked = bookingDAO.sumBookedRooms(hotelId, roomType, checkIn, checkOut);
        Room room = findRoomOrThrow(hotelId, roomType);
        int remaining = room.getTotalRooms() - sumBooked;
        return requestedRooms <= remaining;
    }
}



