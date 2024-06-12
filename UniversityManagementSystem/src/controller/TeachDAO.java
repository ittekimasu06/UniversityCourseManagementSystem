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

    // Method to get all Teach records
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
    
}
