package modeling.car;

public class Cargo extends Car {

    private static final int width = 50;
    private static final int height = 40;
    private static double rate = 300;
    public Cargo(double x, double y) {
        super(x, y, "lawn.jpg", width, height);
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
