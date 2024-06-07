package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Lecturer extends Person{
	private String lecturerID;
	private List<Course> courses;

	public Lecturer(String name, boolean gender, Date dateOfBirth, String lecturerID) {
		super(name, gender, dateOfBirth);
		this.lecturerID = lecturerID;
		this.courses = new ArrayList<>();
	}
	
    public void setLecturerID(String lecturerID) {
    	this.lecturerID = lecturerID;
    }
    
    public String getLecturerID() {
    	return lecturerID;
    }

	public void addCourse(Course course) {
		this.courses.add(course);
	}

	public List<Course> getCourses() {
		return courses;
	}

}
