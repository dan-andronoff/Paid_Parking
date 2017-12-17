package modeling.interval_getter;

public interface IntervalGetter extends Cloneable {

    double getInterval();
    default void generateNext(){};
    Object clone() throws CloneNotSupportedException;
}
