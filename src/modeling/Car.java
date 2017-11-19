package modeling;

import graph.Node;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Car extends ImageView {
    private static final int width = 45;
    private static final int height = 25;


    public void setPath(ArrayList<Node> path) {
        this.path = path;
    }

    private ArrayList<Node> path;

    public Car(double x, double y) {
        super("car-car.png");
        setFitWidth(width);
        setFitHeight(height);
        setX(x);
        setY(y);
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    public double getxPosition() {
        return getTranslateX();
    }

    public double getyPosition() {
        return getTranslateY();
    }
}
