import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parking.Parking;
import parking.Verificator;
import parking.VerificatorError;
import parking.template.Template;

import java.io.IOException;
import java.util.ArrayList;

public class ConstructorController {

    private Stage stage;
    private Template template;
    private Parking parking;
    private int size;
    private GraphicsContext graphicsContext;

    private void clearContext(){
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    Pane centerPane;

    @FXML
    public void initialize() {
        template = Template.Null;
        size = 50;
        Canvas canvas = new Canvas(815, 575);
        graphicsContext = canvas.getGraphicsContext2D();
        parking = new Parking(4, 4, graphicsContext, size);
        canvas.setOnMouseClicked(event -> {
                    if (template!=Template.Null) {
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        if (parking.isInParking(x, y)) {
                            parking.createFunctionalBlock(x, y, template);
                            parking.drawFunctionalBlock(x, y);
                        }
                    }
                }
        );
        centerPane.getChildren().add(canvas);
        parking.drawMarkup();
        parking.drawHighway();
        parking.drawFunctionalBlocks();
    }

    @FXML
    public void onChooseCashBox() { template = Template.CashBox; }

    @FXML
    public void onChooseInfoTable() {template = Template.InfoTable; }

    @FXML
    public void onChooseDeparture() {template = Template.Departure; }

    @FXML
    public void onChooseEntry() {template = Template.Entry; }

    @FXML
    public void onChooseParkingPlace() {template = Template.ParkingPlace; }

    @FXML
    public void onChooseLawn() {template = Template.Lawn; }

    @FXML
    public void onChooseRoad() {template = Template.Road; }

    @FXML
    public void onSettingsClick(){
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("constructor_settings.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Размер парковки");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            // Передаём адресата в контроллер.
            ConstructorSettingsController controller = loader.getController();
            controller.setInitialWidth(parking.getFunctionalBlockH());
            controller.setInitialHeight(parking.getFunctionalBlockV());
            controller.setDialogStage(dialogStage);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            if (controller.isSubmitClicked()) {
                clearContext();
                parking = new Parking(controller.getSelectedWidth(), controller.getSelectedHeight(), graphicsContext, size, parking);
                parking.drawMarkup();
                parking.drawHighway();
                parking.drawFunctionalBlocks();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onSave(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(stage);
    }

    @FXML
    public void onCheck(){
        ArrayList<VerificatorError> errorList = Verificator.checkAll(parking);
        if (errorList.size()==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология соответствует правилам организации парковки!");
            alert.showAndWait();
        }
        else{
            StringBuilder message = new StringBuilder();
            if (errorList.contains(VerificatorError.MultiCashBox)){
                message.append("\n - На парковке должна быть одна касса.");
            }
            if (errorList.contains(VerificatorError.MultiInfoTable)){
                message.append("\n - На парковке должно быть одно информационное табло.");
            }
            if (errorList.contains(VerificatorError.IncorrectEntryDeparturePlacement)){
                message.append("\n - Въезд и выезд должны прилегать к шоссе. Выезд должен находиться дальше по ходу движения автомобилей по шоссе.");
            }
            if (errorList.contains(VerificatorError.UnrelatedGraph)){
                message.append("\n - Требуется наличие пути от въезда до всех парковочных мест и дорог.");
            }
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология не соответствует правилам организации парковки: " + message.toString());
            alert.showAndWait();
        }

    }


}
