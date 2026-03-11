import java.util.HashMap;

abstract class Room {
    String roomType;
    double price;

    Room(String roomType, double price) {
        this.roomType = roomType;
        this.price = price;
    }

    void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Price per night: " + price);
    }
}

class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1000);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2000);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 5000);
    }
}

class RoomInventory {
    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v4.0");
        System.out.println();

        RoomInventory inventory = new RoomInventory();

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("Available Rooms:");
        System.out.println();

        if (inventory.getAvailability(single.roomType) > 0) {
            single.displayDetails();
            System.out.println("Available: " + inventory.getAvailability(single.roomType));
            System.out.println();
        }

        if (inventory.getAvailability(doubleRoom.roomType) > 0) {
            doubleRoom.displayDetails();
            System.out.println("Available: " + inventory.getAvailability(doubleRoom.roomType));
            System.out.println();
        }

        if (inventory.getAvailability(suite.roomType) > 0) {
            suite.displayDetails();
            System.out.println("Available: " + inventory.getAvailability(suite.roomType));
            System.out.println();
        }
    }
}
