import java.util.*;
import java.util.concurrent.*;

class Reservation {
    String guestName;
    String roomType;
    String roomId;

    Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }
}

class RoomInventory {
    private final Map<String, Integer> inventory;
    private final Map<String, Integer> roomCounters;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);

        roomCounters = new HashMap<>();
        roomCounters.put("Single Room", 1);
        roomCounters.put("Double Room", 1);
        roomCounters.put("Suite Room", 1);
    }

    // Synchronized method to allocate room safely
    public synchronized Reservation allocateRoom(String guestName, String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available <= 0) return null;

        inventory.put(roomType, available - 1);
        int counter = roomCounters.get(roomType);
        String roomId = roomType.substring(0, 2).toUpperCase() + String.format("%02d", counter);
        roomCounters.put(roomType, counter + 1);

        return new Reservation(guestName, roomType, roomId);
    }

    public synchronized Map<String, Integer> getInventorySnapshot() {
        return new HashMap<>(inventory);
    }
}

class BookingQueue {
    private final Queue<ReservationRequest> queue = new LinkedList<>();

    public synchronized void addRequest(ReservationRequest request) {
        queue.add(request);
    }

    public synchronized ReservationRequest pollRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

class ReservationRequest {
    String guestName;
    String roomType;

    ReservationRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingProcessor implements Runnable {
    private final BookingQueue queue;
    private final RoomInventory inventory;
    private final List<Reservation> confirmedReservations;

    BookingProcessor(BookingQueue queue, RoomInventory inventory, List<Reservation> confirmedReservations) {
        this.queue = queue;
        this.inventory = inventory;
        this.confirmedReservations = confirmedReservations;
    }

    @Override
    public void run() {
        while (true) {
            ReservationRequest request;
            synchronized (queue) {
                if (queue.isEmpty()) break;
                request = queue.pollRequest();
            }

            Reservation res = inventory.allocateRoom(request.guestName, request.roomType);
            synchronized (confirmedReservations) {
                if (res != null) {
                    confirmedReservations.add(res);
                    System.out.println("Booking confirmed: " + res.guestName + " -> " + res.roomType + " (" + res.roomId + ")");
                } else {
                    System.out.println("Booking failed: " + request.guestName + " -> " + request.roomType + " (No availability)");
                }
            }
        }
    }
}
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Book My Stay - Hotel Booking System v11.0\n");

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        List<Reservation> confirmedReservations = Collections.synchronizedList(new ArrayList<>());

        // Simulate multiple concurrent booking requests
        queue.addRequest(new ReservationRequest("Alice", "Single Room"));
        queue.addRequest(new ReservationRequest("Bob", "Double Room"));
        queue.addRequest(new ReservationRequest("Charlie", "Suite Room"));
        queue.addRequest(new ReservationRequest("David", "Double Room"));
        queue.addRequest(new ReservationRequest("Eve", "Single Room"));
        queue.addRequest(new ReservationRequest("Frank", "Suite Room")); // May fail

        int numThreads = 3;
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new BookingProcessor(queue, inventory, confirmedReservations));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\nFinal Inventory:");
        inventory.getInventorySnapshot().forEach((roomType, available) ->
                System.out.println(roomType + ": " + available));
    }
}
