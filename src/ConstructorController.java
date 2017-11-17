import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parking.Parking;
import parking.Verificator;
import parking.VerificatorError;
import parking.template.Template;

import java.io.*;
import java.util.ArrayList;

public class ConstructorController {

    private Stage stage;
    private Template template;
    private Parking constructorParking;
    private Parking modelingParking;
    private int size;

    private GraphicsContext graphicsContextConstructor;
    private GraphicsContext graphicsContextModeling;

    private void clearConstructorContext(){
        graphicsContextConstructor.clearRect(0, 0, graphicsContextConstructor.getCanvas().getWidth(), graphicsContextConstructor.getCanvas().getHeight());
    }

    private void clearModelingContext(){
        graphicsContextModeling.clearRect(0,0,graphicsContextModeling.getCanvas().getWidth(),graphicsContextModeling.getCanvas().getHeight());
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    Pane constructor;
    @FXML
    Pane modeling;
    @FXML
    TabPane tabPane;

    @FXML
    public void initialize() {
        //Инициализация вкладки конструирования
        template = Template.Null;
        size = 50;
        Canvas canvas = new Canvas(815, 575);
        graphicsContextConstructor = canvas.getGraphicsContext2D();
        constructorParking = new Parking(4, 4, graphicsContextConstructor, size);
        canvas.setOnMouseClicked(event -> {
                    if (template!=Template.Null) {
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        if (constructorParking.isInParking(x, y)) {
                            constructorParking.createFunctionalBlock(x, y, template);
                            constructorParking.drawFunctionalBlock(x, y);
                        }
                    }
                }
        );
        constructor.getChildren().add(canvas);
        constructorParking.drawMarkup();
        constructorParking.drawHighway();
        constructorParking.drawFunctionalBlocks();
        //Инициализация вкладки моделирования
        canvas = new Canvas(815, 575);
        graphicsContextModeling = canvas.getGraphicsContext2D();
        modeling.getChildren().add(canvas);
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

    //Обработчики для вкладки конструирования
    @FXML
    public void onConstructorSettingsClick(){
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
            controller.setInitialWidth(constructorParking.getFunctionalBlockH());
            controller.setInitialHeight(constructorParking.getFunctionalBlockV());
            controller.setDialogStage(dialogStage);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            if (controller.isSubmitClicked()) {
                clearConstructorContext();
                constructorParking = new Parking(controller.getSelectedWidth(), controller.getSelectedHeight(), graphicsContextConstructor, size, constructorParking);
                constructorParking.drawMarkup();
                constructorParking.drawHighway();
                constructorParking.drawFunctionalBlocks();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onConstructorLoad(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузка топологии:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл топологии парковки","*.top"));
        File file = fileChooser.showOpenDialog(stage);
        if (file!=null) {
            try (FileInputStream in = new FileInputStream(file.getPath())) {
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                constructorParking = (Parking) objectInputStream.readObject();
                constructorParking.setGraphicsContext(graphicsContextConstructor);
                for (int i = 0; i < constructorParking.getFunctionalBlockH(); i++) {
                    for (int j = 0; j < constructorParking.getFunctionalBlockV(); j++) {
                        if (constructorParking.getFunctionalBlock(i, j) != null) {
                            constructorParking.getFunctionalBlock(i, j).setGraphicsContext(graphicsContextConstructor);
                        }
                    }
                }
                clearConstructorContext();
                constructorParking.drawHighway();
                constructorParking.drawMarkup();
                constructorParking.drawFunctionalBlocks();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onSave(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранение топологию:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл топологии парковки","*.top"));
        File file = fileChooser.showSaveDialog(stage);
        if(file!=null) {
            try (FileOutputStream out = new FileOutputStream(file.getPath())) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                objectOutputStream.writeObject(constructorParking);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getErrorMessage(Parking parking) {
        ArrayList<VerificatorError> errorList = Verificator.checkAll(parking);
        if (errorList.size() == 0)
            return null;
        else {
            StringBuilder message = new StringBuilder();
            if (errorList.contains(VerificatorError.MultiCashBox)) {
                message.append("\n - На парковке должна быть одна касса.");
            }
            if (errorList.contains(VerificatorError.MultiInfoTable)) {
                message.append("\n - На парковке должно быть одно информационное табло.");
            }
            if (errorList.contains(VerificatorError.IncorrectEntryDeparturePlacement)) {
                message.append("\n - Въезд и выезд должны прилегать к шоссе. Выезд должен находиться дальше по ходу движения автомобилей по шоссе.");
            }
            if (errorList.contains(VerificatorError.UnrelatedGraph)) {
                message.append("\n - Требуется наличие пути от въезда до всех парковочных мест и дорог.");
            }
            if (errorList.contains(VerificatorError.hasNullFunctionalBlock)) {
                message.append("\n - Требуется заполнить все функциональные блоки.");
            }
            return message.toString();
        }
    }

    @FXML
    public void onCheck(){
        String message = getErrorMessage(constructorParking);
        if (message==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология соответствует правилам организации парковки!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология не соответствует правилам организации парковки: " + message);
            alert.showAndWait();
        }
    }

    @FXML
    public void onGoToModeling(){
        String message = getErrorMessage(constructorParking);
        if (message == null) {
            modelingParking = new Parking(constructorParking.getFunctionalBlockH(), constructorParking.getFunctionalBlockV(), graphicsContextModeling, size, constructorParking);
            clearModelingContext();
            modelingParking.drawFunctionalBlocks();
            modelingParking.drawHighway();
            SingleSelectionModel<Tab> singleSelectionModel = tabPane.getSelectionModel();
            singleSelectionModel.select(1);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология не соответствует правилам организации парковки: " + message);
            alert.showAndWait();
        }

    }

    //Обработчики для вкладки моделирования

    @FXML
    public void onModelingLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузка топологии:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл топологии парковки", "*.top"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (FileInputStream in = new FileInputStream(file.getPath())) {
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                modelingParking = (Parking) objectInputStream.readObject();
                modelingParking.setGraphicsContext(graphicsContextModeling);
                for (int i = 0; i < modelingParking.getFunctionalBlockH(); i++) {
                    for (int j = 0; j < modelingParking.getFunctionalBlockV(); j++) {
                        if (modelingParking.getFunctionalBlock(i, j) != null) {
                            modelingParking.getFunctionalBlock(i, j).setGraphicsContext(graphicsContextModeling);
                        }
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String message = getErrorMessage(modelingParking);
            if (message == null) {
                clearModelingContext();
                modelingParking.drawHighway();
                modelingParking.drawMarkup();
                modelingParking.drawFunctionalBlocks();

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Топология сконструирована неверно, попробуйте изменить топологию в конструировании.");
                alert.showAndWait();
            }
        }
    }
}
