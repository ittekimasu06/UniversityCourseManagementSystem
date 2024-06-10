package model;

import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.Date;

public class Student extends Person {
	private String studentID;
//    private List<Course> courses;
 
    public Student(String name, boolean gender, Date dateOfBirth, String studentID) {
        super(name, gender, dateOfBirth);
        this.studentID = studentID;
//        this.courses = new ArrayList<>();
    }
    
    public void setStudentID(String studentID) {
    	this.studentID = studentID;
    }
    
    public String getStudentID() {
    	return studentID;
    }

//    public void enrollCourse(Course course) {
//        this.courses.add(course);
//    }
//    
//    public List<Course> getCourses() {
//		return courses;
//	}
//    
//    public double getAverageMark() {
//        if (courses.isEmpty()) {
//            return 0.0;
//        }
//
//        double totalMarks = 0.0;
//        for (Course course : courses) {
//            totalMarks += course.getMark();
//        }
//        return totalMarks / courses.size();
//    }
}
