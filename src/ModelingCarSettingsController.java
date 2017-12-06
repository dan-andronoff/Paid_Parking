import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modeling.*;

import java.util.Arrays;
import java.util.Collections;

public class ModelingCarSettingsController {
    @FXML
    private Spinner<Double> probability;
    @FXML
    private ComboBox<String> type;

    @FXML
    private Spinner<Double> intervalSpinner;
    @FXML
    private Label intervalLabel;
    @FXML
    private ComboBox<String> distributionChoiceBox;
    @FXML
    private Label distributionLabel;

    @FXML
    private Label mLabel;
    @FXML
    private Spinner<Double> mSpinner;
    @FXML
    private Label dLabel;
    @FXML
    private Spinner<Double> dSpinner;
    @FXML
    private Label iLabel;
    @FXML
    private Spinner<Double> iSpinner;
    @FXML
    private Label lLabel;
    @FXML
    private Spinner<Double> lSpinner;
    @FXML
    private Label rLabel;
    @FXML
    private Spinner<Double> rSpinner;

    private boolean isSubmitClicked = false;
    private Stage dialogStage;

    private IntervalGetter intervalGetter;

    @FXML
    public void initialize() {
        type.getItems().add("Детерминированный");
        type.getItems().add("Случайный");
        type.getSelectionModel().select(0);
        distributionChoiceBox.getItems().add("Нормальный");
        distributionChoiceBox.getItems().add("Показательный");
        distributionChoiceBox.getItems().add("Равномерный");
        lSpinner.valueProperty().addListener((ObservableValue<? extends Double> observable, Double oldValue, Double newValue) -> {
            ((SpinnerValueFactory.DoubleSpinnerValueFactory)rSpinner.getValueFactory()).setMin(newValue);
        });
        mSpinner.valueProperty().addListener((ObservableValue<? extends Double> observable, Double oldValue, Double newValue) -> {
            ((SpinnerValueFactory.DoubleSpinnerValueFactory)dSpinner.getValueFactory()).setMax(newValue/3 - ((newValue/3)%0.1));
        });
    }

    @FXML
    public void onChooseType(){
        switch (type.getSelectionModel().getSelectedItem()){
            case "Детерминированный":
                intervalLabel.setVisible(true);
                intervalSpinner.setVisible(true);
                distributionChoiceBox.setVisible(false);
                distributionLabel.setVisible(false);
                mLabel.setVisible(false);
                mSpinner.setVisible(false);
                dLabel.setVisible(false);
                dSpinner.setVisible(false);
                iLabel.setVisible(false);
                iSpinner.setVisible(false);
                lLabel.setVisible(false);
                lSpinner.setVisible(false);
                rLabel.setVisible(false);
                rSpinner.setVisible(false);
                break;
            case "Случайный":
                distributionChoiceBox.getSelectionModel().selectFirst();
                intervalLabel.setVisible(false);
                intervalSpinner.setVisible(false);
                distributionChoiceBox.setVisible(true);
                distributionLabel.setVisible(true);
                mLabel.setVisible(true);
                mSpinner.setVisible(true);
                dLabel.setVisible(true);
                dSpinner.setVisible(true);
                iLabel.setVisible(false);
                iSpinner.setVisible(false);
                lLabel.setVisible(false);
                lSpinner.setVisible(false);
                rLabel.setVisible(false);
                rSpinner.setVisible(false);
                break;
        }
    }

    public double getSelectedCarEnterProbability(){
        return probability.getValue();
    }

    public IntervalGetter getIntervalGetter() {
        return intervalGetter;
    }

    public void setDialogStage(Stage stage){
        dialogStage = stage;
    }

    public void setProbability(double probability){
        this.probability.getValueFactory().setValue(probability);
    }

    public boolean isSubmitClicked(){
        return isSubmitClicked;
    }

    @FXML
    public void onChooseDistribution(){
        switch (distributionChoiceBox.getSelectionModel().getSelectedItem()){
            case "Нормальный":
                mLabel.setVisible(true);
                mSpinner.setVisible(true);
                dLabel.setVisible(true);
                dSpinner.setVisible(true);
                iLabel.setVisible(false);
                iSpinner.setVisible(false);
                lLabel.setVisible(false);
                lSpinner.setVisible(false);
                rLabel.setVisible(false);
                rSpinner.setVisible(false);
                break;
            case "Показательный":
                mLabel.setVisible(false);
                mSpinner.setVisible(false);
                dLabel.setVisible(false);
                dSpinner.setVisible(false);
                iLabel.setVisible(true);
                iSpinner.setVisible(true);
                lLabel.setVisible(false);
                lSpinner.setVisible(false);
                rLabel.setVisible(false);
                rSpinner.setVisible(false);
                break;
            case "Равномерный":
                mLabel.setVisible(false);
                mSpinner.setVisible(false);
                dLabel.setVisible(false);
                dSpinner.setVisible(false);
                iLabel.setVisible(false);
                iSpinner.setVisible(false);
                lLabel.setVisible(true);
                lSpinner.setVisible(true);
                rLabel.setVisible(true);
                rSpinner.setVisible(true);
                break;
        }
    }

    @FXML
    public void onSubmitClicked(){
        switch (type.getSelectionModel().getSelectedItem()){
            case "Детерминированный":
                intervalGetter = new DeterminateIntervalGetter(intervalSpinner.getValue());
                break;
            case "Случайный":
                switch (distributionChoiceBox.getSelectionModel().getSelectedItem()){
                    case "Нормальный":
                        intervalGetter = new NormalIntervalGetter(mSpinner.getValue(), dSpinner.getValue());
                        break;
                    case "Показательный":
                        intervalGetter = new ExponentialIntervalGetter(iSpinner.getValue());
                        break;
                    case "Равномерный":
                        intervalGetter = new UniformIntervalGetter(lSpinner.getValue(), rSpinner.getValue());
                        break;
                }
                break;
        }
        isSubmitClicked = true;
        dialogStage.close();
    }
}
