package modeling.car;

import graph.Graph;
import graph.Node;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
//Класс машина
public abstract class Car extends ImageView {

    private String arrivalTime;
    private String departureTime;

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Graph.ParkingPlaceNode getParkingPlace() {
        return parkingPlace;
    }

    private Graph.ParkingPlaceNode parkingPlace;

    public Car(double x, double y, String path, int width, int height) {
        super(path);
        setFitWidth(width);
        setFitHeight(height);
        setX(x);
        setY(y);
    }

    public abstract double getRate();

    public abstract String getType();

    public ArrayList<Node> getPathToEntry() {
        return parkingPlace.getPathToEntry();
    }

    public ArrayList<Node> getPathToDeparture() {
        return parkingPlace.getPathToDeparture();
    }

    public void setParkingPlace(Graph.ParkingPlaceNode parkingPlace) {
        this.parkingPlace = parkingPlace;
    }
}

