package modeling;

public class UniformIntervalGetter implements IntervalGetter {
    private double l;
    private double r;

    public UniformIntervalGetter(double l, double r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public double getInterval() {
        return Distribution.getUniformDistribution(l,r);
    }
}
