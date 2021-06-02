package StudentCourse;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StudentCoursesController implements Initializable {
    
    ObservableList<Course> coursesStudentList = FXCollections.observableArrayList();
    static int studentIndex = -1;
    
    @FXML
    private TableColumn<Course, String> name;
    
    @FXML
    private TableColumn<Course, String> id;
    
    @FXML
    private TableColumn<Course, String> room;
    
    @FXML
    private TableView<Course> CoursesTable;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<Course, String>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
        room.setCellValueFactory(new PropertyValueFactory<Course, String>("room"));
        
        coursesStudentList = MyDatabase.getCourseStudents(studentIndex);
        CoursesTable.setItems(coursesStudentList);
    }
    
}
