package model;

public class Teach {
	private String lectureID;
	private String courseID;
	
	public Teach(String lectureID, String courseID) {
		this.lectureID = lectureID;
		this.courseID = courseID;
	}
	
	public void setLecturerID(String lectureID) {
    	this.lectureID = lectureID;
    }
	
	public String getLecturerID() {
		return lectureID;
	}
	
	public void setCourseID(String courseID) {
    	this.courseID = courseID;
    }
	
	public String getCourseID() {
		return courseID;
	}

}
