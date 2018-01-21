package parking;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import parking.template.*;

import java.io.Serializable;
//Класс парковка
public class Parking implements Serializable {

    private FunctionalBlock[][] parking;
    private transient GraphicsContext graphicsContext;
    private int size;

    private int entryI = -1;
    private int entryJ = -1;
    private int departureI = -1;
    private int departureJ = -1;
    private int infoTableI = -1;
    private int infoTableJ = -1;
    private int cashBoxI = -1;
    private int cashBoxJ = -1;

    public int getEntryI() {
        return entryI;
    }

    public int getEntryJ() {
        return entryJ;
    }

    public int getDepartureI() {
        return departureI;
    }

    public int getDepartureJ() {
        return departureJ;
    }

    public int getInfoTableI() {
        return infoTableI;
    }

    public int getInfoTableJ() {
        return infoTableJ;
    }

    public int getCashBoxI() {
        return cashBoxI;
    }

    public int getCashBoxJ() {
        return cashBoxJ;
    }

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

    //Конструктор парковки
    public Parking(int functionalBlockH, int functionalBlockV, GraphicsContext graphicsContext, int size) {
        parking = new FunctionalBlock[functionalBlockH][functionalBlockV];
        this.graphicsContext = graphicsContext;
        this.size = size;
        this.functionalBlockH = functionalBlockH;
        this.functionalBlockV = functionalBlockV;
        HIGHWAY_SIZE = 2 * size;
        HORIZONTAL_MARGIN = graphicsContext.getCanvas().getWidth() / 2 - functionalBlockH * size / 2;
        VERTICAL_MARGIN = graphicsContext.getCanvas().getHeight() / 2 - functionalBlockV * size / 2 - HIGHWAY_SIZE / 2;
        for (int i = 0; i < functionalBlockH; i++)
            for (int j = 0; j < functionalBlockV; j++) {
                parking[i][j] = new Road(graphicsContext);
            }
    }

    public Parking(int functionalBlockH, int functionalBlockV, GraphicsContext graphicsContext, int size, Parking oldParking) {
        this(functionalBlockH, functionalBlockV, graphicsContext, size);
        int minH = (functionalBlockH > oldParking.functionalBlockH) ? oldParking.functionalBlockH : functionalBlockH;
        int minV = (functionalBlockV > oldParking.functionalBlockV) ? oldParking.functionalBlockV : functionalBlockV;
        for (int i = 0; i < minH; i++)
            for (int j = 0; j < minV; j++) {
                try {
                    parking[i][j] = (FunctionalBlock)oldParking.parking[i][j].clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                parking[i][j].setGraphicsContext(graphicsContext);
            }
        for (int i = minH; i < functionalBlockH; i++)
            for (int j = minV; j < functionalBlockV; j++)
                parking[i][j]=new Road(graphicsContext);

        if (oldParking.entryI<functionalBlockH&&oldParking.entryJ<functionalBlockV) {
            entryI = oldParking.entryI;
            entryJ = oldParking.entryJ;
        }
        if (oldParking.departureI<functionalBlockH&&oldParking.departureJ<functionalBlockV) {
            departureI = oldParking.departureI;
            departureJ = oldParking.departureJ;
        }
        if (oldParking.infoTableI<functionalBlockH&&oldParking.infoTableJ<functionalBlockV) {
            infoTableI = oldParking.infoTableI;
            infoTableJ = oldParking.infoTableJ;
        }
        if (oldParking.cashBoxI<functionalBlockH&&oldParking.cashBoxJ<functionalBlockV) {
            cashBoxI = oldParking.cashBoxI;
            cashBoxJ = oldParking.cashBoxJ;
        }
    }

    public FunctionalBlock getFunctionalBlock(int i, int j) {
        return parking[i][j];
    }

    //Отрисовка разметки
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

    //Отрисовка функционального блока
    public void drawFunctionalBlock(double x, double y) {
        int i = ((int) (x - HORIZONTAL_MARGIN)) / size;
        int j = ((int) (y - VERTICAL_MARGIN)) / size;
        graphicsContext.clearRect(HORIZONTAL_MARGIN + i * size + 1, VERTICAL_MARGIN + j * size + 1, size - 2, size - 2);
        parking[i][j].render(HORIZONTAL_MARGIN + i * size + 1, VERTICAL_MARGIN + j * size + 1, size - 1);
    }


    //Создание функционального блока
    public void createFunctionalBlock(double x, double y, Template template) {
        int i = ((int) (x - HORIZONTAL_MARGIN)) / size;
        int j = ((int) (y - VERTICAL_MARGIN)) / size;
        if(i==entryI&&j==entryJ){
            entryI=-1;
            entryJ=-1;
        }
        if(i==departureI&&j==departureJ){
            departureI=-1;
            departureJ=-1;
        }
        if(i==infoTableI&&j==infoTableJ){
            infoTableI=-1;
            infoTableJ=-1;
        }
        if(i==cashBoxI&&j==cashBoxJ){
            cashBoxI=-1;
            cashBoxJ=-1;
        }
        switch (template) {
            case CashBox:
                parking[i][j] = new CashBox(graphicsContext);
                if (cashBoxI!=-1&&cashBoxJ!=-1){
                    parking[cashBoxI][cashBoxJ]=new Road(graphicsContext);
                    drawFunctionalBlock(cashBoxI*size+HORIZONTAL_MARGIN, cashBoxJ*size+VERTICAL_MARGIN);
                }
                cashBoxI=i;
                cashBoxJ=j;
                break;
            case InfoTable:
                parking[i][j] = new InfoTable(graphicsContext);
                if (infoTableI!=-1&&infoTableJ!=-1){
                    parking[infoTableI][infoTableJ]=new Road(graphicsContext);
                    drawFunctionalBlock(infoTableI*size+HORIZONTAL_MARGIN, infoTableJ*size+VERTICAL_MARGIN);
                }
                infoTableI=i;
                infoTableJ=j;
                break;
            case Road:
                parking[i][j] = new Road(graphicsContext);
                break;
            case Lawn:
                parking[i][j] = new Lawn(graphicsContext);
                break;
            case Entry:
                parking[i][j] = new Entry(graphicsContext);
                if (entryI!=-1&&entryJ!=-1){
                    parking[entryI][entryJ]=new Road(graphicsContext);
                    drawFunctionalBlock(entryI*size+HORIZONTAL_MARGIN, entryJ*size+VERTICAL_MARGIN);
                }
                entryI=i;
                entryJ=j;
                break;
            case Departure:
                parking[i][j] = new Departure(graphicsContext);
                if (departureI!=-1&&departureJ!=-1){
                    parking[departureI][departureJ]=new Road(graphicsContext);
                    drawFunctionalBlock(departureI*size+HORIZONTAL_MARGIN, departureJ*size+VERTICAL_MARGIN);
                }
                departureI=i;
                departureJ=j;
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
                    graphicsContext.clearRect(HORIZONTAL_MARGIN + i * size + 1, VERTICAL_MARGIN + j * size + 1, size - 2, size - 2);
                    parking[i][j].render(HORIZONTAL_MARGIN + i * size + 1, VERTICAL_MARGIN + j * size + 1, size - 1);
                }
    }

    public void drawFunctionalBlocksInModeling() {
        for (int i = 0; i < functionalBlockH; i++)
            for (int j = 0; j < functionalBlockV; j++)
                if (parking[i][j] != null) {
                    parking[i][j].render(HORIZONTAL_MARGIN + i * size, VERTICAL_MARGIN + j * size, size);
                }
    }

    //Отрисовка шоссе
    public void drawHighway() {
        String imagePath = "highway_road.jpg";
        Image image = new Image(imagePath);
        for (int i = 0; i < graphicsContext.getCanvas().getWidth() / HIGHWAY_SIZE; i++) {
            graphicsContext.drawImage(image, 2 * i * size, VERTICAL_MARGIN + functionalBlockV * size + 1, HIGHWAY_SIZE, HIGHWAY_SIZE);
        }
    }

    public void drawHighwayInModeling() {
        String imagePath = "highway_road.jpg";
        Image image = new Image(imagePath);
        for (int i = 0; i < entryI+HORIZONTAL_MARGIN/size; i++) {
            graphicsContext.drawImage(image, i * size, VERTICAL_MARGIN + functionalBlockV * size, size, HIGHWAY_SIZE);
        }
        for (int i = entryI+1+(int)(Math.floor(HORIZONTAL_MARGIN/size)); i < departureI+(int)(Math.ceil(HORIZONTAL_MARGIN/size)); i++) {
            graphicsContext.drawImage(image, i * size, VERTICAL_MARGIN + functionalBlockV * size, size, HIGHWAY_SIZE);
        }
        for (int i = departureI+1+(int)HORIZONTAL_MARGIN/size; i < graphicsContext.getCanvas().getWidth() / size; i++) {
            graphicsContext.drawImage(image, i * size, VERTICAL_MARGIN + functionalBlockV * size, size, HIGHWAY_SIZE);
        }
        imagePath = "highway_road_ed.jpg";
        image = new Image(imagePath);
        graphicsContext.drawImage(image, entryI * size+HORIZONTAL_MARGIN, VERTICAL_MARGIN + functionalBlockV * size, size, HIGHWAY_SIZE);
        graphicsContext.drawImage(image, departureI * size+HORIZONTAL_MARGIN, VERTICAL_MARGIN + functionalBlockV * size, size, HIGHWAY_SIZE);
    }

    public boolean isInParking(int x, int y){
        return x>HORIZONTAL_MARGIN && x < HORIZONTAL_MARGIN + functionalBlockH*size
                && y>VERTICAL_MARGIN && y < VERTICAL_MARGIN + functionalBlockV*size;
    }

    public void drawInfoTableInModeling(int count){
        ((InfoTable)parking[infoTableI][infoTableJ]).renderInModeling(HORIZONTAL_MARGIN+infoTableI*size,VERTICAL_MARGIN+ infoTableJ*size, size, count);
    }

    //Отрисовка фона
    public void drawBackground(){
        String imagePath = "tree.png";
        Image image = new Image(imagePath);
        String imagePath2 = "lawn2.png";
        Image image2 = new Image(imagePath2);
        for (int i=0; i <graphicsContext.getCanvas().getWidth()/size; i++){
            for(int j=0; j<graphicsContext.getCanvas().getHeight()/size; j++){
                graphicsContext.drawImage(image2,i*size,j*size, size,size);
                //graphicsContext.drawImage(image,i*size, j*size,size,size);
            }
        }
        for (int i=-1; i<1+functionalBlockH;i++){
            graphicsContext.drawImage(image,HORIZONTAL_MARGIN+i*size, VERTICAL_MARGIN-size,size,size);
        }
        for (int i=0; i<functionalBlockV;i++){
            graphicsContext.drawImage(image, HORIZONTAL_MARGIN-size, VERTICAL_MARGIN+size*i,size,size);
        }
        for (int i=0; i<functionalBlockV;i++){
            graphicsContext.drawImage(image, HORIZONTAL_MARGIN+functionalBlockH*size, VERTICAL_MARGIN+size*i,size,size);
        }
        for (int i=1; i<=HORIZONTAL_MARGIN/size+1;i++){
            graphicsContext.drawImage(image, HORIZONTAL_MARGIN - i*size, VERTICAL_MARGIN+functionalBlockV*size-size,size,size);
        }
        for (int i=0; i<HORIZONTAL_MARGIN/size;i++){
            graphicsContext.drawImage(image, HORIZONTAL_MARGIN+functionalBlockH*size+i*size, VERTICAL_MARGIN+functionalBlockV*size-size,size,size);
        }
        for (int i=0; i<graphicsContext.getCanvas().getWidth()/size;i++){
            graphicsContext.drawImage(image, i*size, VERTICAL_MARGIN+functionalBlockV*size+2*size,size,size);
        }
    }

    //Отрисовка номеров парковочных мест
    public void drawParkingNumbers(){
        for (int j=0, number=1; j<functionalBlockV; j++)
            for (int i=0; i<functionalBlockH; i++){
             if (parking[i][j] instanceof ParkingPlace)
             {
                 graphicsContext.setFill(Color.web("#e59711"));
                 graphicsContext.setFont(Font.font("Arial", size/2));
                 ((ParkingPlace)parking[i][j]).setNumber(number);
                 if (number<10){
                     graphicsContext.fillText(Integer.toString(number), i*size+HORIZONTAL_MARGIN + size / 2 - size / 6.998138688, j*size+VERTICAL_MARGIN + size / 2 + size / 6);
                 }
                 else {
                     graphicsContext.fillText(Integer.toString(number), i*size+HORIZONTAL_MARGIN + size / 2 - size / 3.5, j*size+VERTICAL_MARGIN + size / 2 + size / 6);
                 }

                 graphicsContext.setFill(Color.BLACK);
                 number++;
             }
        }
    }
    public double getHORIZONTAL_MARGIN() {
        return HORIZONTAL_MARGIN;
    }

    public double getVERTICAL_MARGIN() {
        return VERTICAL_MARGIN;
    }
}
