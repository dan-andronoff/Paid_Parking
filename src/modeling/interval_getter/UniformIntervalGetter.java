package modeling.interval_getter;

import modeling.Distribution;
import modeling.interval_getter.IntervalGetter;

public class UniformIntervalGetter implements IntervalGetter {
    private double l;
    private double r;
    private double interval;

    public UniformIntervalGetter(double l, double r) {
        this.l = l;
        this.r = r;
        generateNext();
    }

    @Override
    public double getInterval() {
        return interval;
    }

    @Override
    public void generateNext(){
        interval = Distribution.getUniformDistribution(l, r);
        System.out.println(interval);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public double getL() {
        return l;
    }

    public double getR() {
        return r;
    }
}
