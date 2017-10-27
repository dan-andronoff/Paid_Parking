import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

public class ConstructorSettingsController {
    @FXML
    private Spinner<Integer> parkingWidth;
    @FXML
    private Spinner<Integer> parkingHeight;
    private Stage dialogStage;
    private boolean isSubmitClicked = false;


    public int getSelectedWidth() {
        return parkingWidth.getValue();
    }

    public int getSelectedHeight() {
        return parkingHeight.getValue();
    }

    public void setInitialWidth(int width){
        parkingWidth.getValueFactory().setValue(width);
    }

    public void setInitialHeight(int height){
        parkingHeight.getValueFactory().setValue(height);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSubmitClicked(){
        return isSubmitClicked;
    }

    @FXML
    public void onSubmit() {
        isSubmitClicked = true;
        dialogStage.close();
    }
}
