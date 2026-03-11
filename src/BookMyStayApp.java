import java.util.*;

class Reservation {
    String reservationId;
    String guestName;
    String roomType;
    String roomId;

    Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }
}

class RoomInventory {
    private Map<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class BookingHistory {
    private Map<String, Reservation> confirmedBookings;

    BookingHistory() {
        confirmedBookings = new HashMap<>();
    }

    void addReservation(Reservation reservation) {
        confirmedBookings.put(reservation.reservationId, reservation);
    }

    Reservation getReservation(String reservationId) {
        return confirmedBookings.get(reservationId);
    }

    void removeReservation(String reservationId) {
        confirmedBookings.remove(reservationId);
    }

    Collection<Reservation> getAllReservations() {
        return confirmedBookings.values();
    }
}

class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> releasedRoomIds;

    CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        releasedRoomIds = new Stack<>();
    }

    void cancelBooking(String reservationId) {
        Reservation r = history.getReservation(reservationId);

        if (r == null) {
            System.out.println("Cancellation failed: Reservation " + reservationId + " does not exist.");
            return;
        }

        releasedRoomIds.push(r.roomId);
        inventory.increment(r.roomType);
        history.removeReservation(reservationId);

        System.out.println("Booking cancelled successfully:");
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + r.guestName +
                " | Room Type: " + r.roomType +
                " | Released Room ID: " + r.roomId);
    }

    void displayReleasedRooms() {
        System.out.println("\nRecently Released Room IDs (LIFO Order):");
        for (String id : releasedRoomIds) {
            System.out.println(id);
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v10.0\n");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Simulate confirmed reservations
        Reservation r1 = new Reservation("RES101", "Alice", "Single Room", "SI01");
        Reservation r2 = new Reservation("RES102", "Bob", "Double Room", "DO01");
        Reservation r3 = new Reservation("RES103", "Charlie", "Suite Room", "SU01");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        CancellationService cancellationService = new CancellationService(inventory, history);

        // Cancel a booking
        cancellationService.cancelBooking("RES102"); // Bob
        cancellationService.cancelBooking("RES105"); // Non-existent
        cancellationService.cancelBooking("RES103"); // Charlie

        // Display remaining inventory
        System.out.println("\nCurrent Inventory:");
        System.out.println("Single Room: " + inventory.getAvailability("Single Room"));
        System.out.println("Double Room: " + inventory.getAvailability("Double Room"));
        System.out.println("Suite Room: " + inventory.getAvailability("Suite Room"));

        cancellationService.displayReleasedRooms();
    }
}
