package modeling.car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Cargo extends Car {

    private static final int width = 50;
    private static final int height = 24;
    private static double rate = 300;
    private static Random random = new Random();

    private static ArrayList<String> cars = new ArrayList<>();
    static {
        cars.addAll(Arrays.asList("cargo1.png", "cargo2.png", "cargo3.png", "cargo4.png"));
    }
    public Cargo(double x, double y) {
        super(x, y, cars.get(random.nextInt(cars.size())), width, height);
    }

    public static void setRate(double rate){
        Cargo.rate = rate;
    }

    @Override
    public double getRate() {
        return rate;
    }

    @Override
    public String getType() {
        return "Грузовой";
    }
}
