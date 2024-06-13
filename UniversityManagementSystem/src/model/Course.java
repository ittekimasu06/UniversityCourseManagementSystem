package model;

public class Course {
	private String courseName;
	private String courseID;

	public Course(String courseName, String courseID) {
		this.courseName = courseName;
		this.courseID = courseID;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getCourseID() {
		return courseID;
	}
}
