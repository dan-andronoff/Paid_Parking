package parking.template;

import javafx.scene.canvas.GraphicsContext;

abstract public class FunctionalBlock {
    protected GraphicsContext graphicsContext;
    public abstract void render(double x, double y, int size);

    public FunctionalBlock(GraphicsContext graphicsContext){
        this.graphicsContext = graphicsContext;
    }

    public GraphicsContext getGraphicsContext(){
        return this.graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext){
        this.graphicsContext=graphicsContext;
    }
}
