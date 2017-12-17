package modeling.car;

import parking.Parking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Passenger extends Car {

    private static final int width = 45;
    private static final int height = 25;
    private static double rate = 200;
    private static ArrayList<String> cars = new ArrayList<>();
    private static Random random = new Random();

    static {
        cars.addAll(Arrays.asList("car1.png", "car2.png", "car3.png", "car4.png", "car5.png", "car6.png", "car7.png", "car8.png", "car9.png", "car10.png"));
    }

    public Passenger(double x, double y) {
        super(x, y, cars.get(random.nextInt(cars.size())), width, height);
    }

    public static void setRate(double rate){
        Passenger.rate = rate;
    }

    @Override
    public double getRate() {
        return rate;
    }

    @Override
    public String getType() {
        return "Легковой";
    }
}
