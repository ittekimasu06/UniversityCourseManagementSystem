package model;

import java.util.Date;

public class Student extends Person {
	private String studentID;
	private double gpa;
	//phương án List<Course> không khả thi -> bỏ
//    private List<Course> courses;
 
    public Student(String name, boolean gender, Date dateOfBirth, String studentID, double gpa) {
        super(name, gender, dateOfBirth);
        this.studentID = studentID;
        this.gpa = gpa;
//        this.courses = new ArrayList<>();
    }
    
    public void setStudentID(String studentID) {
    	this.studentID = studentID;
    }
    
    public String getStudentID() {
    	return studentID;
    }
    
    public void setGpa(double gpa) {
    	this.gpa = gpa;
    }
    
    public double getGpa() {
        return gpa;
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
