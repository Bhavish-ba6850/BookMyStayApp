import java.util.*;

class Reservation {
    String reservationId;
    String guestName;
    String roomType;

    Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingHistory {

    private List<Reservation> confirmedBookings;

    BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedBookings);
    }
}

class BookingReportService {

    private BookingHistory history;

    BookingReportService(BookingHistory history) {
        this.history = history;
    }

    void displayReport() {
        List<Reservation> bookings = history.getAllReservations();

        if (bookings.isEmpty()) {
            System.out.println("No confirmed reservations yet.");
            return;
        }

        System.out.println("Booking History Report:");
        for (Reservation r : bookings) {
            System.out.println("Reservation ID: " + r.reservationId +
                    " | Guest: " + r.guestName +
                    " | Room Type: " + r.roomType);
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v8.0\n");

        BookingHistory history = new BookingHistory();

        // Simulate confirmed reservations
        history.addReservation(new Reservation("RES101", "Alice", "Single Room"));
        history.addReservation(new Reservation("RES102", "Bob", "Double Room"));
        history.addReservation(new Reservation("RES103", "Charlie", "Suite Room"));

        BookingReportService reportService = new BookingReportService(history);

        reportService.displayReport();
    }
}
