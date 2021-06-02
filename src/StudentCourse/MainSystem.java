package StudentCourse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainSystem extends Application {

    static Stage studentsStage = new Stage();
    static Stage coursesStage = new Stage();
    static Stage studentCoursesStage = new Stage();

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("StudentsDesign.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("StudentCourse/style.css");
        studentsStage.setScene(scene);
        studentsStage.setTitle("Students Managment System");
        studentsStage.setResizable(false);
        studentsStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
