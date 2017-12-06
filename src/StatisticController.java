import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modeling.Car;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StatisticController {
    @FXML
    private TableView<Record> statistic;
    @FXML
    private TableColumn<Record, Integer> number;
    @FXML
    private TableColumn<Record, String> type;
    @FXML
    private TableColumn<Record, String> price;
    @FXML
    private TableColumn<Record, String> arrivalTime;
    @FXML
    private TableColumn<Record, String> departureTime;
    @FXML
    private TextField fromTimeField;
    @FXML
    private TextField toTimeField;
    @FXML
    private Label profitTimeLabel;
    @FXML
    private Label profitFullLabel;

    private Stage stage;

    private double fullPrice;

    @FXML
    public void initialize(){
        number.setCellValueFactory(c -> c.getValue().numberProperty().asObject());
        type.setCellValueFactory(c -> c.getValue().typeProperty());
        price.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f",c.getValue().priceProperty().get())));
        arrivalTime.setCellValueFactory(c -> c.getValue().arrivalTimeProperty());
        departureTime.setCellValueFactory(c -> c.getValue().departureTimeProperty());
    }

    public void addRecord(Record record){
        statistic.getItems().add(record);
        fullPrice+=record.priceProperty().get();
        profitFullLabel.setText(String.format("%.2f",fullPrice));
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void show(){
        stage.show();
    }

    public void close(){
        stage.close();
    }

    @FXML
    public void getTimeProfit(){
        double timeProfit = 0;
        Pattern pattern = Pattern.compile("\\A[0-5][0-9]:[0-5][0-9]\\z");
        if (pattern.matcher(fromTimeField.getText()).find()&&pattern.matcher(toTimeField.getText()).find()&&fromTimeField.getText().compareTo(toTimeField.getText())<0) {
            for (Record record : statistic.getItems()
                    ) {
                if (fromTimeField.getText().compareTo(record.arrivalTimeProperty().get()) <= 0 && toTimeField.getText().compareTo(record.departureTimeProperty().get()) >= 0) {
                    timeProfit += record.priceProperty().get();
                }
            }
            profitTimeLabel.setText(String.format("%.2f", timeProfit));
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Неправильный формат ввода времени (ожидаемый формат: мм:сс)! Начало интервала времени должно быть меньше конца интервала!");
            alert.showAndWait();
        }
    }
}
