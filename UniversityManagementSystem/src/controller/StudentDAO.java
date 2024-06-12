package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import model.Student;

public class StudentDAO {
    private Connection connection;

    public StudentDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    //thêm student 
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
                e.printStackTrace();
        }
        return false;
    }

    //trả về list các student
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
                double gpa = calculateGPA(studentID);
                
                students.add(new Student(name, gender, dateOfBirth, studentID, gpa));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    //trả về student theo id
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
                double gpa = calculateGPA(studentID);
                
                return new Student(name, gender, dateOfBirth, studentID, gpa);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //cập nhật thông tin student
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

    //xóa student
    public void deleteStudent(String studentID) {
        String sql = "DELETE FROM Student WHERE studentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentID);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    //tìm kiếm student bằng map, dùng để tìm kiếm bằng tên
    public Map<String, Student> getAllStudentsMap() {
        Map<String, Student> studentMap = new HashMap<>();
        String sql = "SELECT * FROM Student";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                boolean gender = resultSet.getBoolean("gender");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                String studentID = resultSet.getString("studentID");
                double gpa = calculateGPA(studentID);
                
                studentMap.put(name, new Student(name, gender, dateOfBirth, studentID, gpa));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return studentMap;
    }
    
    //lấy id từ Student
    public List<String> getAllStudentIDs() {
        List<String> studentIDs = new ArrayList<>();
        String sql = "SELECT studentID FROM Student";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                studentIDs.add(resultSet.getString("studentID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentIDs;
    }
    
    //lấy tên từ Student
    public List<String> getAllStudentNames() {
        List<String> studentNames = new ArrayList<>();
        String sql = "SELECT name FROM Student";
        try (PreparedStatement statement = connection.prepareStatement(sql); 
        	ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                studentNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentNames;
    }
    
    //lấy tên từ StudentID
    public String getStudentNameByID(String studentID) {
        String sql = "SELECT name FROM Student WHERE studentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //lấy StudentID từ tên
    public String getStudentIDByName(String name) {
        String sql = "SELECT studentID FROM Student WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("studentID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //tính gpa cho sinh viên
    public double calculateGPA(String studentID) {
        double gpa = 0.0;
        int count = 0;
        String sql = "SELECT mark FROM Enroll WHERE studentID = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentID);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                gpa += rs.getDouble("mark");
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count > 0 ? gpa / count : 0.0;
    }
}