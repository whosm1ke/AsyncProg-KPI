package PR_1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class ParkingLot {
    private Semaphore parkingSpots;
    private final int daySpots;
    private final int nightSpots;

    public ParkingLot(int daySpots, int nightSpots) {
        this.daySpots = daySpots;
        this.nightSpots = nightSpots;
        this.parkingSpots = new Semaphore(daySpots);
    }

    public void parkCar(Car car) throws InterruptedException {
        if (isParkingAllowed()) {
            parkingSpots.acquire();
            try {
                System.out.println(car.getCarName() + " has parked.");
//                TimeUnit.MILLISECONDS.sleep(car.getParkingTime());
                Thread.sleep(car.getParkingTime());
                System.out.println(car.getCarName() + " has left the parking lot.");
            } finally {
                parkingSpots.release();
            }
        } else {
            System.out.println(car.getCarName() + " cannot park. Parking is closed.");
        }
    }

    public void adjustParkingSpots(boolean isDayTime) {
        int availableSpots = isDayTime ? daySpots : nightSpots;
        parkingSpots = new Semaphore(availableSpots);
        System.out.println("Adjusted parking spots to " + availableSpots);
    }

    private boolean isParkingAllowed() {
        int currentHour = java.time.LocalTime.now().getHour();
        return currentHour >= 6 && currentHour < 21;
    }
}