package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import model.Course;

public class CourseDAO {
    private Connection connection;

    public CourseDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    // tạo course mới
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO Course (courseID, courseName) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, course.getCourseID());
            statement.setString(2, course.getCourseName());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // cập nhật thông tin course
    public void updateCourse(Course course) {
        String sql = "UPDATE Course SET courseName = ? WHERE courseID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getCourseID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // xóa course
    public void deleteCourse(String courseID) {
        String sql = "DELETE FROM Course WHERE courseID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, courseID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // tìm kiếm course bằng map, dùng để tìm kiếm theo tên
    public Map<String, Course> getAllCoursesMap() {
        Map<String, Course> courseMap = new HashMap<>();
        String sql = "SELECT * FROM Course";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("courseID");
                String name = resultSet.getString("courseName");
                Course course = new Course(name, id);
                courseMap.put(name, course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseMap;
    }

    // trả về list các course
    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<>();
        String sql = "SELECT * FROM Course";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String courseID = resultSet.getString("courseID");
                String courseName = resultSet.getString("courseName");
                Course course = new Course(courseName, courseID);
                courseList.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }
    
    //lấy id từ Course
    public List<String> getAllCourseIDs() {
        List<String> courseIDs = new ArrayList<>();
        String sql = "SELECT courseID FROM Course";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseIDs.add(resultSet.getString("courseID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseIDs;
    }
    
    //lấy tên từ Course
    public List<String> getAllCourseNames() {
        List<String> courseNames = new ArrayList<>();
        String sql = "SELECT courseName FROM Course";
        try (PreparedStatement statement = connection.prepareStatement(sql);
        	ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                courseNames.add(rs.getString("courseName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseNames;
    }
    
    //lấy tên từ CourseID
    public String getCourseNameByID(String courseID) {
        String sql = "SELECT courseName FROM Course WHERE courseID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, courseID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("courseName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //lấy CourseID từ tên
    public String getCourseIDByName(String courseName) {
        String sql = "SELECT courseID FROM Course WHERE courseName = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, courseName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("courseID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}