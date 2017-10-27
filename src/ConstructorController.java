import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import parking.Parking;
import parking.template.Template;

import java.io.IOException;

public class ConstructorController {
    private Template template;
    private Parking parking;
    private int size;
    private GraphicsContext graphicsContext;

    private void clearContext(){
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
    }

    @FXML
    Pane centerPane;

    @FXML
    public void initialize() {
        template = Template.Null;
        size = 50;
        Canvas canvas = new Canvas(815, 575);
        graphicsContext = canvas.getGraphicsContext2D();
        parking = new Parking(0, 0, graphicsContext, 0);
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

}
