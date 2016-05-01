package kim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainFormController implements Initializable {
    
    private Stage stage;                
    private File file;                  // for current working file 
    private CourseInventoryModel model; // for Model object 
    private Course selectedCourse; 
    private String selectedId; 
    private String str; 
    
    @FXML
    private MenuItem menuOpen;
    @FXML
    private MenuItem menuSaveAs;
    @FXML
    private MenuItem menuExit;
    @FXML
    private MenuItem menuEdit;
    @FXML
    private MenuItem menuAdd;
    @FXML
    private MenuItem menuDelete;
    @FXML
    private MenuItem menuSearch;
    @FXML
    private ComboBox<String> cboCategory;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfCredit;
    @FXML
    private ComboBox<String> cboCategoryInfo;
    @FXML
    private ListView<String> lvCourseId;
    @FXML
    private MenuItem menuAbout;
    @FXML
    private Button btSearch;
    @FXML
    private Button btEdit;
    @FXML
    private Button btDelete;
    @FXML
    private Button btAdd;
    @FXML
    private Button btCancel;
    @FXML
    private Button btSave;
    @FXML
    private Label lbSelected;
    @FXML
    private GridPane paneEdit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Create Model object 
        model = new CourseInventoryModel(); 
        
        // Show Course Ids with sepecified value by combo 
        cboCategory.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    populateCourses(newValue);
                    lbSelected.setText(newValue + " category is selected.");
                }); 
        
        // Show Course info. with specified value by user selection 
        SelectionModel<String> smList = lvCourseId.getSelectionModel();
        smList.selectedItemProperty().addListener(
                (ov, oldValue, newValue) -> {
                    selectedId = newValue; 
                    showCourseInfo(newValue);
                }
        );
        
        // Initialize the status of controls 
        initControls();
    }    
    
    private void populateCourses(String category) {

        //Get the courses with specified category 
        ArrayList<String> courseIds = new ArrayList<>(); 
        if (category == null || category.equals("All categories")) 
            courseIds = model.getCourseIds();
        else 
            courseIds = model.getCourseIdsByCat(category);
        
        ObservableList listItems = FXCollections.observableArrayList(courseIds);        
        lvCourseId.setItems(listItems);
        
        // Clear the selected course info. 
        clearCourseInfo();        
    }

    private void populateCategories() {

        // display Categories on ComboBox 
        cboCategory.getItems().clear();
        ArrayList<String> cats = model.getCategories();
        cboCategory.setItems(FXCollections.observableArrayList(cats));  
        cboCategory.getItems().add(0, "All categories");
        cboCategoryInfo.setItems(FXCollections.observableArrayList(cats));
    }
    
    public void showCourseInfo(String id) {

        // Display the information of the selected Course 
        if (id != null) 
        {
            //model.setSelectedCourseId(id);          // Set selected of model
            Course course = model.getCourse(id);  
            
            if (course == null) return; 

            tfTitle.setText(course.getTitle());
            tfCredit.setText(String.valueOf(course.getCredit()));
            cboCategoryInfo.setValue(course.getCategory()); 
        } 
        
        // Enable controls if selcted 
        enableControls();
    }
    
    private void clearCourseInfo() {
            selectedId = null;
            tfTitle.setText("");
            tfCredit.setText("");
            cboCategoryInfo.setValue(""); 
    }
    
    @FXML
    private void handleMenuOpen(ActionEvent event) throws FileNotFoundException {
        
        //Open file 
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Course List File..."); 
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All files", "*.*"),
                                              new ExtensionFilter("DAT", "*.dat"), 
                                              new ExtensionFilter("TXT", "*.txt"));
        
        file = fileChooser.showOpenDialog(stage); 
        if (file == null) 
            return;
        
        //Pass file to Model 
        model.readCourseFile(file);
        
        if (!model.getCourseIds().isEmpty()) {            
            // populate combo box for Categories, then automatically list View populated  
            populateCategories();             
            
            //Set the combo item 
            cboCategory.getSelectionModel().select(0);
        
            // Message for user 
            lbSelected.setText("Loaded " + lvCourseId.getItems().size() 
                    + " courses from " + file.getName()); 
            
            // Enable controlls 
            enableControls();            
        }   
    }

    @FXML
    private void handleMenuSaveAs(ActionEvent event) {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Course List File As...");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All files", "*.*"), 
                new ExtensionFilter("DAT", "*.dat"), 
                new ExtensionFilter("TEXT", "*.txt"));
        
        file = fileChooser.showSaveDialog(stage); 
        
        if (file == null) 
            return; 
        
        model.saveCourseFile(file);
        
        // Message for user 
        lbSelected.setText("Saved " + model.getCourseCount()
                + " courses to " + file.getName()); 
        
    }

    @FXML
    private void handleMenuExit(ActionEvent event) {
        
        //Create Alert & show dialogue and wait
        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you really want to quit?");
        Optional<ButtonType> result = alert.showAndWait();
        
        //Handle with user response 
        if (result.isPresent() && result.get() == ButtonType.OK) 
            Platform.exit();    // Exit program 
    }    
    
    public void setStage(Stage stage) {
        // Set the owner of windows
        this.stage = stage; 
        
        // Lambda exp. for closing event handling 
        stage.setOnCloseRequest(e -> { 
            Alert alert = new Alert(AlertType.CONFIRMATION, "Do you really want to quit?");
            Optional<ButtonType> result = alert.showAndWait(); 
            if (result.isPresent() && result.get() == ButtonType.OK) 
                //quit the app 
                Platform.exit();
            else 
                // do nothing 
                e.consume(); // ignore the close event  
        });        
    }   
    
    @FXML
    private void handleCourseId(MouseEvent event) {
    }

    @FXML
    private void handleMenuAbout(ActionEvent event) throws IOException {
        
        // Load Scene graph from FXML file 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AboutForm.fxml"));
        Parent root = loader.load();
        
        // Create scene and attach the root node of my scene builder 
        Scene scene = new Scene(root);
        
        // Create a stage and attach the scene 
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("About");
        stage.initModality(Modality.APPLICATION_MODAL);    
        
        // Set icon of stage
        String path = "file:/" + System.getProperty("user.dir") + "\\SheridanS_32.png";
        Image image = new Image(path);
        stage.getIcons().add(image);
        
        stage.show(); 
        
        //pass stage to controller : tell controller who is your owner 
        // just ask loader for controller because it's already created when loaded 
        AboutFormController controller = loader.getController(); 
        controller.setStage(stage);
    }

    @FXML
    private void handleBtSearch(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchForm.fxml"));
        Parent root = loader.load(); 
        
        Scene scene = new Scene(root);
        
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Search");
        
        SearchFormController controller = loader.getController();
        controller.setStage(stage);  
        controller.setModel(model);        
        
        stage.setScene(scene);
        
        //Set Icon of stage
        setIcon(stage);        
        
        stage.showAndWait();   
        
        //Set list view with current searched course          
        String currentCourseId = model.getSelectedCourseId();
        int newIdx = model.getCourseIdx(currentCourseId);

        SelectionModel<String> sm = lvCourseId.getSelectionModel();
        
        cboCategory.getSelectionModel().select(0);
        
        if (newIdx < 0) {            
            lbSelected.setText("Search is cancelled");
            sm.clearSelection();
            clearCourseInfo();
        } else {             
            sm.select(newIdx);    
            lvCourseId.getFocusModel().focus(newIdx);
            lvCourseId.scrollTo(newIdx);            
            lbSelected.setText("Selected " + currentCourseId);
        }        
    }

    @FXML
    private void handleBtEdit(ActionEvent event) {
        
        // enable controls of course info 
        enableEditCourse();
    }

    @FXML
    private void handleBtDelete(ActionEvent event) {
        
        String id = selectedId; 
        
        if (id == null) return; 
        
        Alert alert = new Alert(AlertType.CONFIRMATION, 
                "Do you want to delete the course " + id + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            model.removeCourse(id);
            model.saveCourseFile(file);            
            
            cboCategory.getSelectionModel().select(0);
            populateCourses(null);
            lvCourseId.getSelectionModel().clearSelection();
            
            lbSelected.setText("Deleted " + id + " from the list");
            disableEditCourse(); 
        } else {
            event.consume();
        }        
    }

    @FXML
    private void handleBtAdd(ActionEvent event) throws IOException { 
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddForm.fxml"));
        Parent root = loader.load(); 
        
        Scene scene = new Scene(root); 
        Stage stage = new Stage(); 
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Add New Course"); 
        
        //Pass stage to the controller & model
        AddFormController controller = loader.getController(); 
        controller.setStage(stage);
        controller.setModel(model);  
        
        //Set Icon of stage
        setIcon(stage); 
        
        stage.showAndWait();
        
        // Set the current item with new one 
        String id = model.getNewCourseId();        
        if (id == null) return; 
        
        int idx = model.getCourseIdx(id);
                
        populateCategories();
        cboCategory.getSelectionModel().select(0);
        lvCourseId.getSelectionModel().select(idx);
        lvCourseId.getFocusModel().focus(idx);
        lvCourseId.scrollTo(idx);  
        
        lbSelected.setText("Added a new course " + id + " to the list");      
    }

    @FXML
    private void handleBtCancel(ActionEvent event) {
        
        lbSelected.setText("Canceled editing " + selectedId);
        disableEditCourse(); 
    }

    @FXML
    private void handleBtSave(ActionEvent event) {
        
        if (!model.isChanged()) {
            disableEditCourse(); 
            return;            
        } 
        
        Course course = model.getCourse(selectedId);
        if (course == null) return;        
        
        // Dialog for saving confirm 
        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to save the changes?"); 
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {  
            
            Alert alertErr = new Alert(AlertType.ERROR);
            
            // Check title 
            String title = tfTitle.getText().trim(); 
            if (title.isEmpty()) {
                alertErr.setContentText("Title cannot be empty.");
                alertErr.show();
                return;
            }
            
            // Check credit if it is an integer
            String tempCredit = tfCredit.getText().trim();
            if (!tempCredit.matches("[1-9][0-9]{0,1}")) {
                alertErr.setContentText("Credit must be a positive integer less than 100.");
                alertErr.show();
                return;
            }            
            int credit = Integer.parseInt(tempCredit);
            
            // Check category 
            String cat = cboCategoryInfo.getValue();
            if (cat == null) {
                alertErr.setContentText("Category must be selected.");
                alertErr.show();
                return;
            }
                
            model.updateCourse(selectedId, title, credit, cat);
            model.saveCourseFile(file);            
            lbSelected.setText("Updated the course " + selectedId); 
            paneEdit.setDisable(true);
        } else {
            event.consume(); 
            lbSelected.setText("Canceled editing " + selectedId);
        }
        
        model.setChanged(false);
        disableEditCourse(); 
    }
    
    private void initControls() {
        
        // Initialize the status of controls         
        cboCategory.setDisable(true);
        // bind livst view, Search button, Add button and menu to combo box 
        lvCourseId.disableProperty().bind(cboCategory.disableProperty());
        
        btSearch.disableProperty().bind(cboCategory.disableProperty());
        btAdd.disableProperty().bind(cboCategory.disableProperty());
        
        menuSearch.disableProperty().bind(btSearch.disableProperty());
        menuAdd.disableProperty().bind(btAdd.disableProperty());
        
        // bind Credit, Category, Save button, and Cancel button
        paneEdit.setDisable(true);        
        tfTitle.disableProperty().bind(paneEdit.disableProperty());
        tfCredit.disableProperty().bind(paneEdit.disableProperty());
        cboCategoryInfo.disableProperty().bind(paneEdit.disableProperty());
        btSave.disableProperty().bind(paneEdit.disableProperty());
        btCancel.disableProperty().bind(paneEdit.disableProperty());   
        
        btEdit.setDisable(true);
        menuEdit.disableProperty().bind(btEdit.disableProperty());
        btDelete.setDisable(true);
        menuDelete.disableProperty().bind(btDelete.disableProperty());
        
        lbSelected.setText("Open a course list from File > Open menu.");
    }
    
    private void enableControls() {
        
        // Enable if loaded courses list 
        cboCategory.setDisable(false);
        
        // Enable if selcted course 
        btEdit.setDisable(selectedId == null);
        btDelete.setDisable(selectedId == null);
    }   
    
    private void enableEditCourse() {
        
        // log message 
        lbSelected.setText("Modifiy title, credit, and category "
                + "then click \"Save\" or \"Cancel\" button");
        
        // Enable controls of edit 
        paneEdit.setDisable(false);
        
        // Disable the rest of controls except for Edit button 
        cboCategory.setDisable(true);
        btDelete.setDisable(true);
    }

    private void disableEditCourse() {
        
        paneEdit.setDisable(true);
        enableControls();
    }    
    
    private void setIcon(Stage stage)  {
        // Set icon of stage
        String path = "file:/" + System.getProperty("user.dir") + "\\SheridanS_32.png";
        Image image = new Image(path);
        stage.getIcons().add(image);
    }

    @FXML
    private void handleCourseInfoChanged(KeyEvent event) {

        if (event.getTarget().equals(tfTitle) || event.getTarget().equals(tfCredit)) 
            model.setChanged(true);
    }

    @FXML
    private void handleCatInfoChanged(ActionEvent event) {

        if (event.getTarget().equals(cboCategoryInfo)) 
            model.setChanged(true);
    }
}

