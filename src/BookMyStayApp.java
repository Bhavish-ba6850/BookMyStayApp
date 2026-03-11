import java.util.*;

class InvalidBookingException extends Exception {
    InvalidBookingException(String message) {
        super(message);
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

    void validateRoomType(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    void validateAvailability(String roomType) throws InvalidBookingException {
        if (inventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }
    }

    void bookRoom(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        validateAvailability(roomType);
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v9.0\n");

        RoomInventory inventory = new RoomInventory();

        List<Reservation> requests = new ArrayList<>();
        requests.add(new Reservation("Alice", "Single Room"));
        requests.add(new Reservation("Bob", "Double Room"));
        requests.add(new Reservation("Charlie", "Suite Room"));
        requests.add(new Reservation("David", "Suite Room")); // Should fail
        requests.add(new Reservation("Eve", "Penthouse")); // Invalid room type

        for (Reservation r : requests) {
            try {
                inventory.bookRoom(r.roomType);
                System.out.println("Booking confirmed for " + r.guestName +
                        " (" + r.roomType + ")");
            } catch (InvalidBookingException e) {
                System.out.println("Booking failed for " + r.guestName +
                        " (" + r.roomType + "): " + e.getMessage());
            }
        }

        System.out.println("\nRemaining Inventory:");
        System.out.println("Single Room: " + inventory.getAvailability("Single Room"));
        System.out.println("Double Room: " + inventory.getAvailability("Double Room"));
        System.out.println("Suite Room: " + inventory.getAvailability("Suite Room"));
    }
}
