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
import model.Lecturer;

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
    

    // CRUD operations for Lecturer
    public boolean addLecturer(Lecturer lecturer) {
        String sql = "INSERT INTO Lecturer (name, gender, dateOfBirth, lecturerID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lecturer.getName());
            statement.setBoolean(2, lecturer.getGender());
            statement.setDate(3, new Date(lecturer.getDOB().getTime()));
            statement.setString(4, lecturer.getLecturerID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // SQLState 23000 indicates a unique constraint violation
                System.out.println("Lỗi khi thêm Lecturer!");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<Lecturer> getAllLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT * FROM Lecturer";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                boolean gender = resultSet.getBoolean("gender");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                String lecturerID = resultSet.getString("lecturerID");
                lecturers.add(new Lecturer(name, gender, dateOfBirth, lecturerID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturers;
    }

    public Lecturer searchLecturerByLecturerID(String lecturerID) {
        String sql = "SELECT * FROM Lecturer WHERE lecturerID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lecturerID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                boolean gender = resultSet.getBoolean("gender");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                return new Lecturer(name, gender, dateOfBirth, lecturerID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateLecturer(Lecturer lecturer) {
        String sql = "UPDATE Lecturer SET name = ?, gender = ?, dateOfBirth = ? WHERE lecturerID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lecturer.getName());
            statement.setBoolean(2, lecturer.getGender());
            statement.setDate(3, new java.sql.Date(lecturer.getDOB().getTime()));
            statement.setString(4, lecturer.getLecturerID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLecturer(String lecturerID) {
        String sql = "DELETE FROM Lecturer WHERE lecturerID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lecturerID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    // Cho học sinh đăng ký một Course
    public void studentEnrollCourse(String studentID, String courseID) {
        String sql = "INSERT INTO StudentCourse (studentID, courseID) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentID);
            statement.setString(2, courseID);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Assign lecturer to a course
    public void lecturerAssignCourse(String lecturerID, String courseID) {
        String sql = "INSERT INTO LecturerCourse (lecturerID, courseID) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lecturerID);
            statement.setString(2, courseID);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DAO dao = new DAO();
        
    }
}