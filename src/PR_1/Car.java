package PR_1;

class Car implements Runnable {
    private final ParkingLot parkingLot;
    private final String carName;
    private final int parkingTime;

    public Car(ParkingLot parkingLot, String carName, int parkingTime) {
        this.parkingLot = parkingLot;
        this.carName = carName;
        this.parkingTime = parkingTime;
    }

    @Override
    public void run() {
        try {
            parkingLot.parkCar(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getCarName() {
        return carName;
    }

    public int getParkingTime() {
        return parkingTime;
    }
}