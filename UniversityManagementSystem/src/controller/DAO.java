package controller;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import com.mysql.cj.xdevapi.Statement;

import model.Student;

public class DAO {
    String jdbcURL = "jdbc:mysql://127.0.0.1:3306/universitydb";
    String dbUser = "root";
    String dbPassword = "@orenosql0604";
    Connection connection;

    public DAO() {
        try {
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // CRUD operations for Student
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO Student (name, gender, dateOfBirth, studentID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getName());
            statement.setBoolean(2, student.getGender());
            statement.setDate(3, new Date(student.getDOB().getTime()));
            statement.setString(4, student.getStudentID());
            statement.executeUpdate();
            return true;
        } catch(SQLException e) {
            if (e.getSQLState().equals("23000")) { // SQLState 23000 indicates a unique constraint violation
                System.out.println("Lỗi khi thêm Student!");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                boolean gender = resultSet.getBoolean("gender");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                String studentID = resultSet.getString("studentID");
                students.add(new Student(name, gender, dateOfBirth, studentID));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return students;
    }


    public Student searchStudentByStudentID(String studentID) {
        String sql = "SELECT * FROM Student WHERE studentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                boolean gender = resultSet.getBoolean("gender");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                return new Student(name, gender, dateOfBirth, studentID);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateStudent(Student student) {
        String sql = "UPDATE Student SET name = ?, gender = ?, dateOfBirth = ? WHERE studentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getName());
            statement.setBoolean(2, student.getGender());
            statement.setDate(3, new java.sql.Date(student.getDOB().getTime()));
            statement.setString(4, student.getStudentID());
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(String studentID) {
        String sql = "DELETE FROM Student WHERE studentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentID);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DAO dao = new DAO();
        // Example usage:
        // Create a new student
        // Student student = new Student("John Doe", true, new Date(), "S12345");
        // dao.addStudent(student);
        // Get a student by ID
        // Student retrievedStudent = dao.getStudentById(1);
        // System.out.println("Retrieved Student: " + retrievedStudent.getName());
        // Update a student
        // retrievedStudent.setName("John Updated");
        // dao.updateStudent(retrievedStudent);
        // Delete a student
        // dao.deleteStudent(1);
    }
}