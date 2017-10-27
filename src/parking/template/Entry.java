package parking.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Entry extends FunctionalBlock {

    public Entry (GraphicsContext graphicsContext){super(graphicsContext);}

    @Override
    public void render(double x, double y, int size){
        String imagePath = "entry.png";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }
}
