package kim;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class SearchFormController implements Initializable {
    
    // Member vars 
    private Stage stage;
    private CourseInventoryModel model; 
    private String selectedCourseId; 
    
    @FXML
    private TextField tfSearch;
    @FXML
    private ListView<String> lvSearchOutput;
    @FXML
    private Button btSearchExecute;
    @FXML
    private Button btSelect;
    @FXML
    private Button btCancel;
    @FXML
    private ToggleGroup searchGroup;
    @FXML
    private RadioButton rdoId;
    @FXML
    private RadioButton rdoTitle;
    
    public void setStage(Stage stage) {
        this.stage = stage; 
    }
    
    public void setModel(CourseInventoryModel model) {
        this.model = model; 
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Set toggle group 
        rdoId.setToggleGroup(searchGroup);
        rdoTitle.setToggleGroup(searchGroup);        
    }    

    @FXML
    private void handleBtSearchExecute(ActionEvent event) { 
        
        String keyword = tfSearch.getText().trim();
        if (keyword.isEmpty()) 
            return;
        
        ArrayList<Course> courses; 
        
        courses = rdoId.isSelected()?model.findCoursesById(keyword.toUpperCase()):
                model.findCoursesByTitle(keyword);
        
        lvSearchOutput.getItems().clear();  //reset 
        
        if (courses.isEmpty())             
            return;
                        
        ArrayList<String> courseInfos = new ArrayList<>(); 
        String id; 
        String title; 
        
        for(int i = 0; i < courses.size(); i++) {
            id = courses.get(i).getId(); 
            title = courses.get(i).getTitle();
            courseInfos.add(id + ": " + title);
        }
        
        ObservableList listItems = FXCollections.observableList(courseInfos);
        lvSearchOutput.setItems(listItems);        
    }

    @FXML
    private void handleBtSelect(ActionEvent event) {
        
        String str;
        // Set selected Course id of model & close 
        if (lvSearchOutput.getSelectionModel().getSelectedIndex() >= 0) {
            // when an item selected   
            str = lvSearchOutput.getSelectionModel().getSelectedItem();
            model.setSelectedCourseId(str.substring(0, str.indexOf(":"))); 
        } else
            // No item 
            model.setSelectedCourseId(null);
        
        stage.close();  // Close window 
    }           

    @FXML
    private void handleBtCancel(ActionEvent event) {
        
        model.setSelectedCourseId(null);
        stage.close(); // Close window 
    }
    
}
