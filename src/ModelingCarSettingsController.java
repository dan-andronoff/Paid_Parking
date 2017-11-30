import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

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

    @FXML
    public void initialize(){
        type.getItems().add("Детерминированный");
        type.getItems().add("Случайный");
        distributionChoiceBox.getItems().add("Нормальный");
        distributionChoiceBox.getItems().add("Показательный");
        distributionChoiceBox.getItems().add("Равномерный");
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

    public double getSelectedIntervalLength(){
        return intervalSpinner.getValue();
    }

    public double getSelectedM(){
        return mSpinner.getValue();
    }

    public double getSelectedD(){
        return dSpinner.getValue();
    }

    public double getSelectedI(){
        return iSpinner.getValue();
    }

    public double getSelectedL(){
        return lSpinner.getValue();
    }

    public double getSelectedR(){
        return rSpinner.getValue();
    }

    public String getStreamType(){
        return type.getSelectionModel().getSelectedItem();
    }

    public String getDistribution(){
        return distributionChoiceBox.getSelectionModel().getSelectedItem();
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
        isSubmitClicked = true;
        dialogStage.close();
    }
}
