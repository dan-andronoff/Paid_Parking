import javafx.beans.property.*;

public class Record {

    private IntegerProperty number;
    private StringProperty type;
    private StringProperty price;
    private StringProperty arrivalTime;
    private StringProperty departureTime;

    public Record(Integer number, String type, Double price, long arrivalTime, long departureTime) {
        this.number = new SimpleIntegerProperty(number);
        this.type = new SimpleStringProperty(type);
        this.price = new SimpleStringProperty(String.format("%.2f", price));
        this.arrivalTime = new SimpleStringProperty(Long.toString(arrivalTime));
        this.departureTime = new SimpleStringProperty(Long.toString(departureTime));
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public StringProperty priceProperty() {
        return price;
    }

    public StringProperty arrivalTimeProperty() {
        return arrivalTime;
    }

    public StringProperty departureTimeProperty() {
        return departureTime;
    }
}
