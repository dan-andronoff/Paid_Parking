package parking.template;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

abstract public class FunctionalBlock implements Serializable {
    protected transient GraphicsContext graphicsContext;
    public abstract void render(double x, double y, int size);

    public FunctionalBlock(GraphicsContext graphicsContext){
        this.graphicsContext = graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext){
        this.graphicsContext=graphicsContext;
    }
}
