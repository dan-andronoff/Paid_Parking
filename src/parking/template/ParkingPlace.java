package parking.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ParkingPlace extends FunctionalBlock {

    private int number;

    public ParkingPlace (GraphicsContext graphicsContext){super(graphicsContext);}

    public void render(double x, double y, int size){
        String imagePath = "parking_place.png";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
