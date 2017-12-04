import javafx.beans.property.*;

public class Record {

    private IntegerProperty number;
    private StringProperty type;
    private DoubleProperty price;

    public Record(Integer number, String type, Double price) {
        this.number = new SimpleIntegerProperty(number);
        this.type = new SimpleStringProperty(type);
        this.price = new SimpleDoubleProperty(price);
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

}
