package model;

import java.util.Date;

public class Lecturer extends Person{
	private String lecturerID;
	//phương án lưu List<Course> trong lớp Lecturer không khả thi -> bỏ
//	private List<Course> courses;
 
	public Lecturer(String name, boolean gender, Date dateOfBirth, String lecturerID) {
		super(name, gender, dateOfBirth);
		this.lecturerID = lecturerID;
//		this.courses = new ArrayList<>();
	}
	
    public void setLecturerID(String lecturerID) {
    	this.lecturerID = lecturerID;
    }
    
    public String getLecturerID() {
    	return lecturerID;
    }
    
    @Override
    public void view() {
        System.out.println("Lecturer Information:");
        System.out.println("Name: " + getName());
        System.out.println("Gender: " + (getGender() ? "Male" : "Female"));
        System.out.println("Date of Birth: " + getDOB());
        System.out.println("Lecturer ID: " + getLecturerID());
    }

//	public void addCourse(Course course) {
//		this.courses.add(course);
//	}
//
//	public List<Course> getCourses() {
//		return courses;
//	}

}
