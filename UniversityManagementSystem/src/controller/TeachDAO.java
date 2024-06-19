package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import model.Teach;

public class TeachDAO {
	private Connection connection;

	public TeachDAO() {
		this.connection = DatabaseConnection.getConnection();
	}
	
	// tạo một teach mới (cho một lecturer dạy một course)
    public boolean addTeach(String lecturerID, String courseID) {
        String sql = "INSERT INTO Teach (lecturerID, courseID) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lecturerID);
            statement.setString(2, courseID);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // trả về list các bản ghi Teach
    public List<Teach> getAllTeach() {
        List<Teach> teaches = new ArrayList<>();
        String sql = "SELECT * FROM Teach";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String lecturerID = resultSet.getString("lecturerID");
                String courseID = resultSet.getString("courseID");
                teaches.add(new Teach(lecturerID, courseID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teaches;
    }   
    
    //xóa một bản ghi Teach
    public boolean deleteTeach(String lecturerID, String courseID) {
        String sql = "DELETE FROM Teach WHERE lecturerID = ? AND courseID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lecturerID);
            statement.setString(2, courseID);
            int rowCount = statement.executeUpdate();
            return rowCount > 0; // nếu rowCount > 0, câu lệnh đã thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
