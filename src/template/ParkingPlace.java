package template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import template.FunctionalBlock;

public class ParkingPlace extends FunctionalBlock {

    public ParkingPlace (GraphicsContext graphicsContext){super(graphicsContext);}

    public void render(double x, double y, int size){
        String imagePath = "parking_place.png";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }
}
