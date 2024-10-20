package PR_1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ParkingLot parkingLot = new ParkingLot(5, 8);
        List<Thread> threads = new ArrayList<>();

        Car[] cars = {
                new Car(parkingLot, "Car1", 2000),
                new Car(parkingLot, "Car2", 1000),
                new Car(parkingLot, "Car3", 1500),
                new Car(parkingLot, "Car4", 3000),
                new Car(parkingLot, "Car5", 1700),
                new Car(parkingLot, "Car6", 900),
                new Car(parkingLot, "Car7", 2000),
                new Car(parkingLot, "Car8", 400),
                new Car(parkingLot, "Car9", 1900),
                new Car(parkingLot, "Car10", 1400)
        };

        System.out.println("\nSimulation of day traffic\n");
        parkingLot.adjustParkingSpots(true);
        for (Car car : cars) {
            Thread thread = new Thread(car);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("\nSimulation of night traffic\n");
        threads.clear();
        parkingLot.adjustParkingSpots(false);

        for (Car car : cars) {
            Thread thread = new Thread(car);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Parking lot is closed.");
    }
}
