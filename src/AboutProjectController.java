import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class AboutProjectController{
    @FXML
    WebView webViewProject;

    @FXML
    public void initialize(){
        File file = new File("D:/Java/Paid_Parking/Paid_Parking/out/production/Paid_Parking/about/pages/AboutSystem.html");
        URL url= null;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        webViewProject.getEngine().load(url.toString());
        //webViewProject.getEngine().load("about/pages/AboutSystem.html");
    }
}
