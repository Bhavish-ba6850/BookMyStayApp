import java.util.*;

class AddOnService {
    String serviceName;
    double cost;

    AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }
}

class AddOnServiceManager {

    private Map<String, List<AddOnService>> reservationServices;

    AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    void addService(String reservationId, AddOnService service) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);
    }

    void displayServices(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        double total = 0;

        System.out.println("Selected Services for Reservation: " + reservationId);

        for (AddOnService s : services) {
            System.out.println(s.serviceName + " - " + s.cost);
            total += s.cost;
        }

        System.out.println("Total Add-On Cost: " + total);
    }
}
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v7.0\n");

        String reservationId = "RES101";

        AddOnServiceManager manager = new AddOnServiceManager();

        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1200);
        AddOnService spa = new AddOnService("Spa Access", 1500);

        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, airportPickup);
        manager.addService(reservationId, spa);

        manager.displayServices(reservationId);
    }
}
