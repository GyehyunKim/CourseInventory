package kim;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AboutFormController implements Initializable {

    @FXML
    private Button btClose;
        
    @FXML
    private ImageView ivSheridan;
    
    //Instance Vars 
    private Stage stage; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Get the path of image file         
        String path = "file:/" + System.getProperty("user.dir") + "\\Sheridan_S_alpha.png";
        
        // Show image 
        Image image = new Image(path);
        
        ivSheridan.setImage(image);
        
    }    
    
    @FXML
    private void handleClose(ActionEvent event) {
        
        if (stage != null) 
            stage.close();
    }
        
    public void setStage(Stage stage) {
        this.stage = stage;       
    }
}
