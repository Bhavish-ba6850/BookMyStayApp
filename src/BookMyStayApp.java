import java.util.*;

class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class RoomInventory {
    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

class BookingService {

    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;
    private Set<String> usedRoomIds;

    BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
        usedRoomIds = new HashSet<>();
    }

    void processQueue(Queue<Reservation> queue) {

        while (!queue.isEmpty()) {

            Reservation r = queue.poll();
            String type = r.roomType;

            if (inventory.getAvailability(type) > 0) {

                String roomId = generateRoomId(type);

                usedRoomIds.add(roomId);

                allocatedRooms.putIfAbsent(type, new HashSet<>());
                allocatedRooms.get(type).add(roomId);

                inventory.decrement(type);

                System.out.println("Reservation Confirmed");
                System.out.println("Guest: " + r.guestName);
                System.out.println("Room Type: " + type);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println();

            } else {
                System.out.println("No rooms available for " + r.guestName + " (" + type + ")");
                System.out.println();
            }
        }
    }

    private String generateRoomId(String type) {

        String prefix = type.replace(" ", "").substring(0, 2).toUpperCase();
        String id;

        do {
            int number = new Random().nextInt(100);
            id = prefix + number;
        } while (usedRoomIds.contains(id));

        return id;
    }
}
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v6.0\n");

        RoomInventory inventory = new RoomInventory();

        Queue<Reservation> requestQueue = new LinkedList<>();

        requestQueue.add(new Reservation("Alice", "Single Room"));
        requestQueue.add(new Reservation("Bob", "Double Room"));
        requestQueue.add(new Reservation("Charlie", "Suite Room"));
        requestQueue.add(new Reservation("David", "Suite Room"));

        BookingService bookingService = new BookingService(inventory);

        bookingService.processQueue(requestQueue);
    }
}
