package modeling;

public class ExponentialIntervalGetter implements IntervalGetter {
    private double i;

    public ExponentialIntervalGetter(double i) {
        this.i = i;
    }

    @Override
    public double getInterval() {
        return Distribution.getExponentialDistribution(i);
    }
}
