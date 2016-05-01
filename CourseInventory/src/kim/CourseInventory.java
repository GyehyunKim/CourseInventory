package kim;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CourseInventory extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
        Parent root = loader.load();
        
        //pass the stage to controller 
        MainFormController controller = loader.getController();
        controller.setStage(stage);                
        
        Scene scene = new Scene(root);
        
        // Set the title of Main Form 
        stage.setScene(scene);
        stage.setTitle("Sheridan Course Inventory");
        
        // Set icon of stage
        String path = "file:/" + System.getProperty("user.dir") + "\\SheridanS_32.png";
        Image image = new Image(path);
        stage.getIcons().add(image);
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
