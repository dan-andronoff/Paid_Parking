package modeling;

import graph.Graph;
import graph.Node;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Car extends ImageView {
    private static final int width = 45;
    private static final int height = 25;
    private static Random random = new Random();
    private static ArrayList<String> cars = new ArrayList<>();

    static {
        cars.addAll(Arrays.asList("car1.png", "car2.png","car3.png","car4.png","car5.png", "car6.png","car7.png","car8.png","car9.png","car10.png"));
    }

    public Graph.ParkingPlaceNode getParkingPlace() {
        return parkingPlace;
    }

    private Graph.ParkingPlaceNode parkingPlace;

    public Car(double x, double y) {
        super(cars.get(random.nextInt(cars.size())));
        setFitWidth(width);
        setFitHeight(height);
        setX(x);
        setY(y);
    }

    public ArrayList<Node> getPathToEntry() {
        return parkingPlace.getPathToEntry();
    }

    public ArrayList<Node> getPathToDeparture() { return parkingPlace.getPathToDeparture(); }


    public double getxPosition() {
        return getTranslateX();
    }

    public double getyPosition() {
        return getTranslateY();
    }

    public void setParkingPlace(Graph.ParkingPlaceNode parkingPlace) {
        this.parkingPlace = parkingPlace;
    }
}
