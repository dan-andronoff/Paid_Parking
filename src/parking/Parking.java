package parking;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import parking.template.*;

import javax.sql.rowset.serial.SerialArray;
import java.io.Serializable;

public class Parking implements Serializable {

    private FunctionalBlock[][] parking;
    private transient GraphicsContext graphicsContext;
    private int size;

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    private int functionalBlockH;
    private int functionalBlockV;
    private final double HORIZONTAL_MARGIN;
    private final double VERTICAL_MARGIN;
    private final double HIGHWAY_SIZE;

    public FunctionalBlock[][] getParking() {
        return parking;
    }

    public int getFunctionalBlockH() {
        return functionalBlockH;
    }

    public int getFunctionalBlockV() {
        return functionalBlockV;
    }

    public Parking(int functionalBlockH, int functionalBlockV, GraphicsContext graphicsContext, int size) {
        parking = new FunctionalBlock[functionalBlockH][functionalBlockV];
        this.graphicsContext = graphicsContext;
        this.size = size;
        this.functionalBlockH = functionalBlockH;
        this.functionalBlockV = functionalBlockV;
        HIGHWAY_SIZE = 2 * size;
        HORIZONTAL_MARGIN = graphicsContext.getCanvas().getWidth() / 2 - functionalBlockH * size / 2;
        VERTICAL_MARGIN = graphicsContext.getCanvas().getHeight() / 2 - functionalBlockV * size / 2 - HIGHWAY_SIZE / 2 - 1;
    }

    public Parking(int functionalBlockH, int functionalBlockV, GraphicsContext graphicsContext, int size, Parking oldParking) {
        this(functionalBlockH, functionalBlockV, graphicsContext, size);
        int minH = (functionalBlockH > oldParking.functionalBlockH) ? oldParking.functionalBlockH : functionalBlockH;
        int minV = (functionalBlockV > oldParking.functionalBlockV) ? oldParking.functionalBlockV : functionalBlockV;
        for (int i = 0; i < minH; i++)
            for (int j = 0; j < minV; j++)
                parking[i][j] = oldParking.parking[i][j];
    }

    public FunctionalBlock getFunctionalBlock(int i, int j) {
        return parking[i][j];
    }

    public void drawMarkup() {
        for (int i = 0; i <= functionalBlockH; i++) {
            graphicsContext.strokeLine(
                    HORIZONTAL_MARGIN + i * size, VERTICAL_MARGIN, HORIZONTAL_MARGIN + i * size,
                    VERTICAL_MARGIN + functionalBlockV * size);
        }
        for (int i = 0; i <= functionalBlockV; i++) {
            graphicsContext.strokeLine(
                    HORIZONTAL_MARGIN, VERTICAL_MARGIN + i * size,
                    HORIZONTAL_MARGIN + functionalBlockH * size,
                    VERTICAL_MARGIN + i * size);
        }
    }

    public void drawFunctionalBlock(double x, double y) {
        int i = ((int) (x - HORIZONTAL_MARGIN)) / size;
        int j = ((int) (y - VERTICAL_MARGIN)) / size;
        graphicsContext.clearRect(HORIZONTAL_MARGIN + i * size + 2, VERTICAL_MARGIN + j * size + 2, size - 4, size - 4);
        parking[i][j].render(HORIZONTAL_MARGIN + i * size + 2, VERTICAL_MARGIN + j * size + 2, size - 4);
    }

    public void createFunctionalBlock(double x, double y, Template template) {
        int i = ((int) (x - HORIZONTAL_MARGIN)) / size;
        int j = ((int) (y - VERTICAL_MARGIN)) / size;
        switch (template) {
            case CashBox:
                parking[i][j] = new CashBox(graphicsContext);
                break;
            case InfoTable:
                parking[i][j] = new InfoTable(graphicsContext);
                break;
            case Road:
                parking[i][j] = new Road(graphicsContext);
                break;
            case Lawn:
                parking[i][j] = new Lawn(graphicsContext);
                break;
            case Entry:
                parking[i][j] = new Entry(graphicsContext);
                break;
            case Departure:
                parking[i][j] = new Departure(graphicsContext);
                break;
            case ParkingPlace:
                parking[i][j] = new ParkingPlace(graphicsContext);
                break;
        }
    }

    public void drawFunctionalBlocks() {
        for (int i = 0; i < functionalBlockH; i++)
            for (int j = 0; j < functionalBlockV; j++)
                if (parking[i][j] != null) {
                    graphicsContext.clearRect(HORIZONTAL_MARGIN + i * size + 2, VERTICAL_MARGIN + j * size + 2, size - 4, size - 4);
                    parking[i][j].render(HORIZONTAL_MARGIN + i * size + 2, VERTICAL_MARGIN + j * size + 2, size - 4);
                }
    }

    public void drawHighway() {
        String imagePath = "highway_road.jpg";
        Image image = new Image(imagePath);
        for (int i = 0; i < graphicsContext.getCanvas().getWidth() / HIGHWAY_SIZE; i++) {
            graphicsContext.drawImage(image, 2 * i * size, VERTICAL_MARGIN + functionalBlockV * size + 2, HIGHWAY_SIZE, HIGHWAY_SIZE);
        }
    }

    public boolean isInParking(int x, int y){
        return x>HORIZONTAL_MARGIN && x < HORIZONTAL_MARGIN + functionalBlockH*size
                && y>VERTICAL_MARGIN && y < VERTICAL_MARGIN + functionalBlockV*size;
    }
}
