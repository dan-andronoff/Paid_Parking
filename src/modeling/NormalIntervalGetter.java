package modeling;

public class NormalIntervalGetter implements IntervalGetter {
    private double m;
    private double d;

    public NormalIntervalGetter(double m, double d) {
        this.m = m;
        this.d = d;
    }

    @Override
    public double getInterval() {
        return Distribution.getNormalDistribution(m,d);
    }
}
