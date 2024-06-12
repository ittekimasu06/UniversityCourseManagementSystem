package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import model.Lecturer;

public class LecturerDAO {
    private Connection connection;

    public LecturerDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    //thêm lecturer mới
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
                e.printStackTrace();
        }
        return false;
    }

    //trả về list các lecturer
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

    //trả về lecturer theo id
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

    //cập nhật thông tin lecturer
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

    //xóa lecturer
    public void deleteLecturer(String lecturerID) {
        String sql = "DELETE FROM Lecturer WHERE lecturerID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lecturerID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    //tìm kiếm lecturer bằng map, dùng để tìm kiếm bằng tên
    public Map<String, Lecturer> getAllLecturersMap() {
        Map<String, Lecturer> lecturerMap = new HashMap<>();
        String sql = "SELECT * FROM Lecturer";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                boolean gender = resultSet.getBoolean("gender");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                String lecturerID = resultSet.getString("lecturerID");

                lecturerMap.put(name, new Lecturer(name, gender, dateOfBirth, lecturerID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturerMap;
    }
    
 // lấy id từ Lecturer
    public List<String> getAllLecturerIDs() {
        List<String> lecturerIDs = new ArrayList<>();
        String sql = "SELECT lecturerID FROM Lecturer";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                lecturerIDs.add(resultSet.getString("lecturerID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturerIDs;
    }
}