import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Distribution {
    private static Random random = new Random();

    public static double getUniformDistribution(double a, double b){
        return random.nextDouble()*(b-a)+a;
    }

    public static double getExponentialDistribution(double y){
        return (-Math.exp(random.nextDouble()))/y;
    }

    public static double getNormalDistribution(double mean, double deviation){
        return random.nextGaussian()*deviation+mean;
    }
}
