package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import model.Enrollment;

public class EnrollmentDAO {
	private Connection connection;

	public EnrollmentDAO() {
		this.connection = DatabaseConnection.getConnection();
	}
	
	//tạo một enrollment mới (cho một student enroll một course)
    public boolean enrollStudentInCourseAndAssignMark(String studentID, String courseID, double mark) {
    	String sql = "INSERT INTO Enroll (studentID, courseID, mark) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentID);
            statement.setString(2, courseID);
            statement.setDouble(3, mark);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // kiểm tra nếu một course đã đầy
    public boolean isCourseFull(String courseID) {
        String sql = "SELECT COUNT(*) FROM Enroll WHERE courseID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, courseID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count >= 100;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //trả về list các  bản ghi Enrollment 
    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM Enroll";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String studentID = resultSet.getString("studentID");
                String courseID = resultSet.getString("courseID");
                double mark = resultSet.getDouble("mark");
                enrollments.add(new Enrollment(studentID, courseID, mark));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }
    
    
    //cập nhật điểm cho sinh viên
    public boolean updateEnrollmentMark(String studentID, String courseID, double newMark) {
        String sql = "UPDATE Enroll SET mark = ? WHERE studentID = ? AND courseID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newMark);
            statement.setString(2, studentID);
            statement.setString(3, courseID);
            int rowCount = statement.executeUpdate();
            return rowCount > 0; // nếu rowCount > 0, câu lệnh đã thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //xóa một bản ghi enrollment
    public boolean deleteEnrollment(String studentID, String courseID) {
        String sql = "DELETE FROM Enroll WHERE studentID = ? AND courseID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentID);
            statement.setString(2, courseID);
            int rowCount = statement.executeUpdate();
            return rowCount > 0; // nếu rowCount > 0, câu lệnh đã thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
