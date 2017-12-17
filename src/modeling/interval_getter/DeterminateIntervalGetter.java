package modeling.interval_getter;

import modeling.interval_getter.IntervalGetter;

public class DeterminateIntervalGetter implements IntervalGetter {

    private double interval;

    public DeterminateIntervalGetter(double interval) {
        this.interval = interval;
    }

    @Override
    public double getInterval() {
        return interval;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
