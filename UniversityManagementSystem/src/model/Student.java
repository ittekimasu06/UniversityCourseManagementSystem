package model;

import java.util.Date;

public class Student extends Person {
	private String studentID;
	private double gpa;
	//phương án lưuList<Course> trong lớp Student không khả thi -> bỏ
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
    
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder(); //StringBuilder dùng để xây dựng chuỗi
        sb.append("Student Information:\n"); //append dùng để nối các chuỗi vào đối tượng StringBuilder
        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Gender: ").append(getGender() ? "Male" : "Female").append("\n");
        sb.append("Date of Birth: ").append(getDOB()).append("\n");
        sb.append("Student ID: ").append(getStudentID()).append("\n");
        sb.append("GPA: ").append(String.format("%.3f", getGpa())).append("\n"); // giới hạn GPA đến 3 chữ số thập phân
        return sb.toString();
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
