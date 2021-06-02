package StudentCourse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CoursesController implements Initializable {

    static ObservableList<Student> studentCoursesList = FXCollections.observableArrayList(); // show just students who has participated in the specified Course.
    static String courseID;

    @FXML
    private TableColumn<Student, String> major;

    @FXML
    private Label courseLabel;

    @FXML
    private Button showCoursesButton;

    @FXML
    private TableColumn<Student, String> name;

    @FXML
    private TableColumn<Student, Double> grade;

    @FXML
    private TableColumn<Student, Integer> id;

    @FXML
    private TableView<Student> CoursesTable;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showCoursesButton.setVisible(false);
        CoursesTable.getSelectionModel().selectedItemProperty().addListener(event -> {
            if (CoursesTable.getSelectionModel().getSelectedIndex() != -1) {
                Student tempStudent = CoursesTable.getSelectionModel().getSelectedItem();
                StudentCoursesController.studentIndex = tempStudent.getId();
                showCoursesButton.setText("Show " + tempStudent.getName().split(" ")[0] + "'s Courses"); // Ahmed Alashi >> Ahmed
                showCoursesButton.setVisible(true);
            }

        });
        loadStudentCoursesFromDatabase();
    }

    public void loadStudentCoursesFromDatabase() {

        id.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        grade.setCellValueFactory(new PropertyValueFactory<Student, Double>("grade"));
        major.setCellValueFactory(new PropertyValueFactory<Student, String>("major"));

        studentCoursesList = MyDatabase.getStudentCourses(courseID);
        if (calculateNumberStudents() != 0) {
            courseLabel.setText("Number Of Students : " + calculateNumberStudents());
        } else {
            courseLabel.setText("There is no any Student has participated in this course!");
        }
        CoursesTable.setItems(studentCoursesList);
    }

    public static int calculateNumberStudents() {
        int numOfStudents = 0;
        for (Student student : studentCoursesList) {
            numOfStudents++;
        }
        return numOfStudents;
    }

    @FXML
    public void showCoursesHandle(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentCoursesDesign.fxml"));
        Scene scene = new Scene(root);

        MainSystem.studentCoursesStage.setScene(scene);
        MainSystem.studentCoursesStage.setTitle("Student Courses");
        MainSystem.studentCoursesStage.setResizable(false);
        MainSystem.studentCoursesStage.show();
    }
}
