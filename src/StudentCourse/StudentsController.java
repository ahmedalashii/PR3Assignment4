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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class StudentsController implements Initializable {

    ObservableList<Student> studentsList = FXCollections.observableArrayList();
    ObservableList<String> majorsList = FXCollections.observableArrayList();
    ObservableList<Course> coursesList = FXCollections.observableArrayList();

    private int index = -1;

    @FXML
    private TableView<Student> StudentsTable;

    @FXML
    private TableColumn<Student, String> major;

    @FXML
    private TableColumn<Student, String> name;

    @FXML
    private TableColumn<Student, Double> grade;

    @FXML
    private TableColumn<Student, Integer> id;
    
    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField majorTextField;

    @FXML
    private TextArea queryTextArea;

    @FXML
    private Label gradeLabel;

    @FXML
    private Button addButton;

    @FXML
    private Button showCourseButton;

    @FXML
    private Button performQuery;

    @FXML
    private Button updateButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ChoiceBox<String> courseChoiceBox;

    @FXML
    private ChoiceBox<String> semesterChoiceBox;

    @FXML
    private Slider gradeSlider;

    @FXML
    public void addStudentHandle(ActionEvent event) {
        if (verifyStudentData()) {
            boolean idFound = false;
            for (Student student : MyDatabase.getAllStudents()) {
                if (student.getId() == Integer.parseInt(idTextField.getText())) {
                    idFound = true;
                    break;
                }
            }
            if (idFound == false) { // don't add unless the student's id is not duplicated
                Student addedStudent = new Student(Integer.parseInt(idTextField.getText()), nameTextField.getText(), majorTextField.getText(), (double) Math.round(gradeSlider.getValue()*100)/100);
                MyDatabase.addStudentRow(addedStudent);
                // ممكن نحل انو يضيف على التيبل قيو من مجرد ما يضيف على الداتا بيز بطريقتين :
                //  First Way : 
//                loadFromDatabase(); // reloading from database for each student has been added
                // Second Way which is more flexible ^_^:
                studentsList.add(addedStudent); // add addedStudent obejct also to studentsList ^_^
                JOptionPane.showMessageDialog(new JFrame(), "Added Sucessfully!", "Alert", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "it's kind of logic that there are should not be two students holds the same id ^_^", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "All Fields are Required!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    public void updateStudentHandle(ActionEvent event) {
        if (verifyStudentData()) {
            if (index != -1) {
                int requiredId = StudentsTable.getSelectionModel().getSelectedItem().getId();
                // New Values :
                int id = Integer.parseInt(idTextField.getText());
                String name = nameTextField.getText();
                String major = majorTextField.getText();
                double grade = (double) Math.round(gradeSlider.getValue()*100)/100;
                
                Student tempStudent = new Student(id, name, major, grade);
                MyDatabase.updateStudentRow(requiredId, tempStudent);
                studentsList.set(index, tempStudent); // just replace with the specified index ^_^
                StudentsTable.refresh();
                JOptionPane.showMessageDialog(new JFrame(), "Updated Sucessfully!", "Alert", JOptionPane.WARNING_MESSAGE);
                index = -1; // this just if we press UPDATE STUDENT again >> then it will show Message Dialog below (You Should Select a Student to Update!) >> Kind Of Logic ^_^
            } else {
                JOptionPane.showMessageDialog(null, "You Should Select a Student to Update!", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "All Fields are Required!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @FXML
    public void deleteStudentHandle(ActionEvent event) {
        if (index != -1) {
            Student tempStudent = StudentsTable.getSelectionModel().getSelectedItem();
            // Once the student is removed >> the courses which this students has participated in will explicitly be removed (it's a relation) (cascade) ..
            MyDatabase.deleteStudentRow(tempStudent.getId());
            studentsList.remove(index);
            JOptionPane.showMessageDialog(new JFrame(), "Deleted Sucessfully!", "Alert", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "You Should Select a Student to Delete!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @FXML
    public void performQueryHandle(ActionEvent event) {
        if (!queryTextArea.getText().isEmpty()) {
            String stat = queryTextArea.getText();
            if (stat.toLowerCase().contains("update") || stat.toLowerCase().contains("insert") || stat.contains("delete")) {
                MyDatabase.performAQuery(stat, 0);
                loadStudentsFromDatabase();
            } else if (stat.toLowerCase().contains("select")) { // it will be select
                // 0 >> update , insert , delete , 1 >> select
                studentsList.setAll(MyDatabase.performAQuery(stat, 1));
                StudentsTable.setItems(studentsList);               
            } else {
                JOptionPane.showMessageDialog(null, "You Should Enter a Correct Query in The TextField!", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
            if (StudentsTable.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are no results with this Query!", "Error Message", JOptionPane.ERROR_MESSAGE);
                loadStudentsFromDatabase();
            }
        } else {
            JOptionPane.showMessageDialog(null, "You Should Enter a Query in The TextField!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @FXML
    public void showCourseStudents(ActionEvent event) throws IOException {
        if (courseChoiceBox.getValue() != null) {
            Parent root = FXMLLoader.load(getClass().getResource("CoursesDesign.fxml"));
            Scene scene = new Scene(root);

            MainSystem.coursesStage.setScene(scene);
            MainSystem.coursesStage.setTitle("Courses Managment System");
            MainSystem.coursesStage.setResizable(false);
            MainSystem.coursesStage.show();
        } else {
            JOptionPane.showMessageDialog(null, "You Should Select a Course to Show its Registered Students!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    public void registerCourse(ActionEvent event) {
        if (courseChoiceBox.getValue() != null && semesterChoiceBox.getValue() != null) {
            if (index != -1) { // we should choose a course and the student who we want to register the course for him
                boolean studentCourseFound = false;
                for (String courseId : MyDatabase.checkCourseAvailability(studentsList.get(index))) {
                    if (courseChoiceBox.getValue().equalsIgnoreCase(courseId)) {
                        studentCourseFound = true;
                        break;
                    }
                }
                if (studentCourseFound == false) {
                    MyDatabase.registerCourse(studentsList.get(index), courseChoiceBox.getValue(), semesterChoiceBox.getValue()); // the specified student and the specified course
                    JOptionPane.showMessageDialog(new JFrame(), "Registered Sucessfully!", "Alert", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "You Can't Register This Course Twice!", "Error Message", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You Should Select a Student to Register!", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You Should Select a Course and The Semester to Register!", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gradeSlider.valueProperty().addListener(e -> {
            gradeLabel.setText(String.valueOf(gradeSlider.getValue()));
        });
        courseChoiceBox.setOnAction((event) -> {
            CoursesController.courseID = courseChoiceBox.getValue();
        });
        
        semesterChoiceBox.getItems().add("First Semester 2020-2021");
        semesterChoiceBox.getItems().add("Second Semester 2020-2021");
        semesterChoiceBox.getItems().add("First Semester 2021-2022");
        semesterChoiceBox.getItems().add("Second Semester 2021-2022");
        semesterChoiceBox.getItems().add("First Semester 2022-2023");
        semesterChoiceBox.getItems().add("Second Semester 2022-2023");
        semesterChoiceBox.getItems().add("First Semester 2023-2024");
        semesterChoiceBox.getItems().add("Second Semester 2023-2024");
        // Load Students and Courses and Colleges From The Database :
        loadStudentsFromDatabase();
        loadCoursesFromDatabase();
    }
    
    public void studentIntoFields() {
        index = StudentsTable.getSelectionModel().getSelectedIndex();
        if (index != -1) { // to avoid null
            Student student = StudentsTable.getSelectionModel().getSelectedItem();
            idTextField.setText(String.valueOf(student.getId()));
            nameTextField.setText(student.getName());
            majorTextField.setText(student.getMajor());
            gradeSlider.setValue(student.getGrade());
        }
    }

    public void loadStudentsFromDatabase() { // الداتا الافتراضية
        id.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        grade.setCellValueFactory(new PropertyValueFactory<Student, Double>("grade"));
        major.setCellValueFactory(new PropertyValueFactory<Student, String>("major"));

        studentsList = MyDatabase.getAllStudents();
        StudentsTable.setItems(studentsList);
    }

    public void loadCoursesFromDatabase() { // الداتا الافتراضية
        coursesList = MyDatabase.getAllCourses();
        coursesList.forEach(course -> {
            courseChoiceBox.getItems().add(course.getId());
        });
    }
    
    public boolean verifyStudentData() {
        return (!idTextField.getText().isEmpty()) && (!nameTextField.getText().isEmpty()) && (!majorTextField.getText().isEmpty());
    }
}
