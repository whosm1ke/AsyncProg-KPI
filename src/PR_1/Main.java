package PR_1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ParkingLot parkingLot = new ParkingLot(5, 8);
        List<Thread> threads = new ArrayList<>();

        Car[] cars = {
                new Car(parkingLot, "Car1", 3000),
                new Car(parkingLot, "Car2", 2000),
                new Car(parkingLot, "Car3", 2500),
                new Car(parkingLot, "Car4", 4000),
                new Car(parkingLot, "Car5", 2700),
                new Car(parkingLot, "Car6", 1900),
                new Car(parkingLot, "Car7", 3000),
                new Car(parkingLot, "Car8", 500),
                new Car(parkingLot, "Car9", 2900),
                new Car(parkingLot, "Car10", 2400)
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
