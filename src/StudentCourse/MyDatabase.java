package StudentCourse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MyDatabase {

    // By Default I registered courses for all studetns so when you register any course to any student >> it will be shown to you (You Can't Register This Course twice!) so to solve this problem you should delete a student and readd him ..
    static Connection conn = null;

    // Connection
    // Prepared Statement (Sql Statement)
    // ResultSet
    public static void getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/iug", "root", "");
            System.out.println("Log : Connection to Database Students has been established");
        } catch (ClassNotFoundException ex) {
            System.err.println("Class Not Found!");
        } catch (SQLException ex) {
            System.err.println("SQL Exception : Can't Connect to Database!");
        }
    }

    public static ObservableList<Student> getAllStudents() {
        ObservableList<Student> resultList = FXCollections.observableArrayList();
        if (conn == null) {
            getConnection();
        }
        try {
            PreparedStatement stat = conn.prepareStatement("SELECT * FROM student"); // Sql
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String major = rs.getString("major");
                double grade = (double) Math.round(rs.getDouble("grade") * 100) / 100; // 99,63892734 >> 99.64
                Student tempStudent = new Student(id, name, major, grade);
                resultList.add(tempStudent);
            }
        } catch (SQLException ex) {
            System.err.println("Error in Selecting Students!");
        }
        return resultList;
    }

    public static ObservableList<Course> getAllCourses() {
        ObservableList<Course> resultList = FXCollections.observableArrayList();
        if (conn == null) {
            getConnection();
        }
        try {
            PreparedStatement stat = conn.prepareStatement("SELECT DISTINCT * FROM course");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String room = rs.getString("room");
                Course course = new Course(id, name, room);
                resultList.add(course);
            }
        } catch (SQLException ex) {
            System.err.println("Error in Selecting Courses!");
        }
        return resultList;
    }

    public static ObservableList<Student> getStudentCourses(String tempCourseID) { // This method is to get all students who participated in the specified course ..
        ObservableList<Student> resultList = FXCollections.observableArrayList();
        if (conn == null) {
            getConnection();
        }
        try {
            PreparedStatement stat = conn.prepareStatement("SELECT S.* FROM student S,registration R WHERE S.id = R.studentID AND R.courseID = ?"); // Sql
            stat.setString(1, tempCourseID); // first ?
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String major = rs.getString("major");
                double grade = (double) Math.round(rs.getDouble("grade") * 100) / 100;
                Student student = new Student(id, name, major, grade);
                resultList.add(student);
            }
        } catch (SQLException ex) {
            System.err.println("Error in Selecting Students who participated in CourseID : " + tempCourseID);
        }
        return resultList;
    }

    public static ObservableList<Course> getCourseStudents(int tempStudentID) { // This method is to get all courses who the specific student participated in ..
        ObservableList<Course> resultList = FXCollections.observableArrayList();
        if (conn == null) {
            getConnection();
        }
        try {
            PreparedStatement stat = conn.prepareStatement("SELECT C.* FROM course C,registration R WHERE C.id = R.courseID AND R.studentID = ?"); // Sql
            stat.setInt(1, tempStudentID); // first ?
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String room = rs.getString("room");
                Course course = new Course(id, name, room);
                resultList.add(course);
            }
        } catch (SQLException ex) {
            System.err.println("Error in Selecting Students who participated in CourseID : " + tempStudentID);
        }
        return resultList;
    }

    public static ObservableList<String> checkCourseAvailability(Student student) { // This Method is just for check if the course is available to register it or not (if it's registered before >> then we can't register it again (Logic) ^_^ )
        ObservableList<String> resultList = FXCollections.observableArrayList();
        if (conn == null) {
            getConnection();
        }
        try {
            PreparedStatement stat = conn.prepareStatement("SELECT courseID FROM registration WHERE studentID = ?");
            stat.setInt(1, student.getId()); // first ?
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                String courseID = rs.getString("courseID");
                resultList.add(courseID);
            }
        } catch (SQLException ex) {
            System.err.println("Error in Check Course Availabilty!");
        }
        return resultList;
    }

    public static boolean registerCourse(Student student, String courseID, String semester) {
        if (conn == null) {
            getConnection();
        }
        try {
            PreparedStatement insertStat = conn.prepareStatement("INSERT INTO registration VALUES(?,?,?)");
            insertStat.setInt(1, student.getId()); // first ?
            insertStat.setString(2, courseID); // second ?
            insertStat.setString(3, semester); // third ?
            insertStat.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Error in Inserting Course!");
        }
        return false;
    }

    public static void addStudentRow(Student student) {
        if (conn == null) {
            getConnection();
        }
        try {
            PreparedStatement stat = conn.prepareStatement("INSERT INTO student VALUES(?,?,?,?)");
            stat.setInt(1, student.getId()); // first ?
            stat.setString(2, student.getName()); // second ?
            stat.setString(3, student.getMajor()); // third ?
            stat.setDouble(4, student.getGrade()); // fourth ?
            stat.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error in Inserting Student!");
        }
    }

    public static void deleteStudentRow(int id) {
        if (conn == null) {
            getConnection();
        }
        try {
            PreparedStatement stat = conn.prepareStatement("DELETE FROM student WHERE id = ?");
            stat.setInt(1, id); // first ?
            stat.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error in Deleting Student!");
        }
    }

    public static void updateStudentRow(int id, Student std) {
        if (conn == null) {
            getConnection();
        }
        try {
            PreparedStatement stat = conn.prepareStatement("UPDATE student SET id = ? , name = ? , major = ? , grade = ? WHERE id = ?");
            stat.setInt(1, std.getId()); // first ?
            stat.setString(2, std.getName()); // second ?
            stat.setString(3, std.getMajor()); // third ?
            stat.setDouble(4, std.getGrade()); // fourth ?
            stat.setInt(5, id); // fifth ?
            stat.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error in Updating Student!");
        }
    }

    public static ObservableList<Student> performAQuery(String sqlStat, int specfiy) {
        ObservableList<Student> resultList = FXCollections.observableArrayList();
        if (conn == null) {
            getConnection();
        }
        try {
            if (specfiy == 1) { // 1 >> select
                PreparedStatement stat = conn.prepareStatement(sqlStat);
                ResultSet rs = stat.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String major = rs.getString("major");
                    double grade = (double) Math.round(rs.getDouble("grade") * 100) / 100;
                    Student tempStudent = new Student(id, name, major, grade);
                    resultList.add(tempStudent);
                }
            } else { // 0 >> update , insert , delete
                PreparedStatement stat = conn.prepareStatement(sqlStat);
                stat.executeUpdate();
            }
        } catch (SQLException ex) {
            System.err.println("Error in Selecting Students!");
        }
        return resultList;
    }
}
