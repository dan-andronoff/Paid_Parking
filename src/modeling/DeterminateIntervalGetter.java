package modeling;

public class DeterminateIntervalGetter implements IntervalGetter {

    private double interval;

    public DeterminateIntervalGetter(double interval) {
        this.interval = interval;
    }

    @Override
    public double getInterval() {
        return interval;
    }
}