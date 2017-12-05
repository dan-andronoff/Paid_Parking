import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import modeling.Car;


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

    private Stage stage;

    @FXML
    public void initialize(){
        number.setCellValueFactory(c -> c.getValue().numberProperty().asObject());
        type.setCellValueFactory(c -> c.getValue().typeProperty());
        price.setCellValueFactory(c -> c.getValue().priceProperty());
        arrivalTime.setCellValueFactory(c -> c.getValue().arrivalTimeProperty());
        departureTime.setCellValueFactory(c -> c.getValue().departureTimeProperty());
    }

    public void addRecord(Record record){
        statistic.getItems().add(record);
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
}
