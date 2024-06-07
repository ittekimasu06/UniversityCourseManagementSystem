package model;

public class Course {
	 private String courseName;
	    private String courseID;
	    private double mark;

	    public Course(String courseName, String courseID, double mark) {
	        this.courseName = courseName;
	        this.courseID = courseID;
	        this.mark = mark;
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
	    
	    public void setMark(double mark) {
	    	this.mark = mark;
	    }
	    
	    public double getMark() {
	    	return mark;
	    }
}
