package kim;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddFormController implements Initializable {

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfCredit;
    @FXML
    private ComboBox<String> cboCategory;
    @FXML
    private Button btCancel;
    @FXML
    private Button btAdd;
    
    //Instance vars 
    private Stage stage;
    private CourseInventoryModel model;

    @Override
    public void initialize(URL url, ResourceBundle rb) {       
               
    }    

    @FXML
    private void handleBtCancel(ActionEvent event) {
        
        // Set the current item with new one 
        model.setNewCourseId(null);
        if (stage != null) 
            stage.close();
    }

    @FXML
    private void handleBtAdd(ActionEvent event) { 
        
        boolean isError = true;
        String errMsg = ""; 
        String id = tfId.getText().trim().toUpperCase(); 
        String title = tfTitle.getText().trim(); 
        String credit = tfCredit.getText().trim(); 
        String cat = cboCategory.getValue();
        
        if (id.isEmpty()) 
            errMsg = "Please, enter the ID.";
        else if (!model.validateCourseId(id)) 
            errMsg = model.getErrorMessage();        
        else if (title.isEmpty()) 
            errMsg = "Please, enter the title.";
        else if (!credit.matches("[1-9][0-9]{0,1}")) 
            errMsg = "Credit must be a positive integer less than 100.";        
        else if (cat == null)
            errMsg = "Please, select the category";
        else 
            isError = false; 
        
        if (isError) {
            Alert alert = new Alert(AlertType.ERROR, errMsg);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.show();
            return;
        } else {                                    
            model.addCourse(id, title,Integer.parseInt(credit), cat);            
        }
        
        stage.close();
    }
    
    public void setStage(Stage stage) {
        
        this.stage = stage;
    }
    
    public void setModel(CourseInventoryModel model) {
        
        this.model = model; 
        
        // populate categories       
        cboCategory.getItems().clear();      
        ArrayList<String> cats = model.getCategories();
        cboCategory.setItems(FXCollections.observableArrayList(cats));
    }
    
}
