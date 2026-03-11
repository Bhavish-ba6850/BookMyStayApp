import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    String guestName;
    String roomType;
    String roomId;

    Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return guestName + " -> " + roomType + " (" + roomId + ")";
    }
}

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    Map<String, Integer> inventory = new HashMap<>();
    Map<String, Integer> roomCounters = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);

        roomCounters.put("Single Room", 1);
        roomCounters.put("Double Room", 1);
        roomCounters.put("Suite Room", 1);
    }

    public Reservation allocateRoom(String guestName, String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available <= 0) return null;

        inventory.put(roomType, available - 1);
        int counter = roomCounters.get(roomType);
        String roomId = roomType.substring(0, 2).toUpperCase() + String.format("%02d", counter);
        roomCounters.put(roomType, counter + 1);

        return new Reservation(guestName, roomType, roomId);
    }

    public void printInventory() {
        System.out.println("Inventory Snapshot:");
        inventory.forEach((type, count) -> System.out.println(type + ": " + count));
    }
}

class PersistenceService {
    private static final String FILE_NAME = "hotel_state.ser";

    public static void saveState(RoomInventory inventory, List<Reservation> bookings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(bookings);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadState() {
        Map<String, Object> state = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            List<Reservation> bookings = (List<Reservation>) ois.readObject();
            state.put("inventory", inventory);
            state.put("bookings", bookings);
            System.out.println("System state loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state: " + e.getMessage());
        }
        return state;
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("Book My Stay - Hotel Booking System v12.0\n");

        Map<String, Object> savedState = PersistenceService.loadState();
        RoomInventory inventory = (RoomInventory) savedState.getOrDefault("inventory", new RoomInventory());
        List<Reservation> bookings = (List<Reservation>) savedState.getOrDefault("bookings", new ArrayList<>());

        // Simulate some bookings
        Reservation r1 = inventory.allocateRoom("Alice", "Single Room");
        Reservation r2 = inventory.allocateRoom("Bob", "Double Room");
        if (r1 != null) bookings.add(r1);
        if (r2 != null) bookings.add(r2);

        System.out.println("Confirmed Bookings:");
        bookings.forEach(System.out::println);

        inventory.printInventory();

        // Save state to file for persistence
        PersistenceService.saveState(inventory, bookings);

        System.out.println("\n--- Application Restart Simulation ---\n");

        // Load state again to simulate recovery
        Map<String, Object> recoveredState = PersistenceService.loadState();
        RoomInventory recoveredInventory = (RoomInventory) recoveredState.get("inventory");
        List<Reservation> recoveredBookings = (List<Reservation>) recoveredState.get("bookings");

        System.out.println("Recovered Bookings:");
        recoveredBookings.forEach(System.out::println);
        recoveredInventory.printInventory();
    }
}
