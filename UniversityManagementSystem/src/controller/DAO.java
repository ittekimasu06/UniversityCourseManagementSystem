package controller;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import model.Student;
import model.Teach;
import model.Course;
import model.Lecturer;
import model.Enrollment;

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
                double gpa = calculateGPA(studentID);
                
                students.add(new Student(name, gender, dateOfBirth, studentID, gpa));
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
                double gpa = calculateGPA(studentID);
                
                return new Student(name, gender, dateOfBirth, studentID, gpa);
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
                Lecturer lecturer = new Lecturer(name, gender, dateOfBirth, lecturerID);
                lecturerMap.put(name, lecturer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturerMap;
    }
    
    //
 // Thêm khóa học mới
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

    // Cập nhật thông tin khóa học
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

    // Xóa khóa học
    public void deleteCourse(String courseID) {
        String sql = "DELETE FROM Course WHERE courseID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, courseID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm kiếm khóa học theo tên
    public Map<String, Course> getAllCoursesMap(String courseName) {
        Map<String, Course> courseMap = new HashMap<>();
        String sql = "SELECT * FROM Course WHERE courseName LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + courseName + "%");
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

    // Lấy danh sách tất cả các khóa học
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
    
    //thêm các trường thông tin vào bảng Enroll ()
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
    
    //lấy hết dữ liệu từ bảng Enroll
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

    // thêm các trường thông tin vào bảng Teach
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

    public static void main(String[] args) {
        DAO dao = new DAO();
        
    }
}