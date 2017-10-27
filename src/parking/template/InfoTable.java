package parking.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class InfoTable extends FunctionalBlock {

    public InfoTable (GraphicsContext graphicsContext){
        super(graphicsContext);
    }

    @Override
    public void render (double x, double y, int size){
        String imagePath = "infoTable.png";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }
}
