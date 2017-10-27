package template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import template.FunctionalBlock;

public class Lawn extends FunctionalBlock {

    public Lawn (GraphicsContext graphicsContext){ super(graphicsContext);}

    @Override
    public void render (double x, double y, int size){
        String imagePath = "lawn.jpg";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }
}
