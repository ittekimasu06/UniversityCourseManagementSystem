package model;

public class Teach {
	String lectureID;
	String courseID;
	
	public Teach(String lectureID, String courseID) {
		this.lectureID = lectureID;
		this.courseID = courseID;
	}
	
	public void setLectureID(String lectureID) {
    	this.lectureID = lectureID;
    }
	
	public String getLectureID() {
		return lectureID;
	}
	
	public void setCourseID(String courseID) {
    	this.courseID = courseID;
    }
	
	public String getCourseID() {
		return courseID;
	}

}
