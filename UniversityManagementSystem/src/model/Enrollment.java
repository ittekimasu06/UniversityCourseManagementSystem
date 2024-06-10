package model;

public class Enrollment {
    private String studentID;
    private String courseID;
    private double mark;

    public Enrollment(String studentID, String courseID, double mark) {
        this.studentID = studentID;
        this.courseID = courseID;
        this.mark = mark;
    }

    public void setStudentID(String studentID) {
    	this.studentID = studentID;
    }
    
    public String getStudentID() {
        return studentID;
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
