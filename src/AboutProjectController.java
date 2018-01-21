import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class AboutProjectController {
    @FXML
    WebView webViewProject;

    @FXML
    public void initialize() {
        try {
            webViewProject.getEngine().load(getClass().getResource("AboutSystem.html").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
