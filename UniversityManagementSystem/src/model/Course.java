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
	
	public String viewCourse() {
		StringBuilder sb = new StringBuilder(); //StringBuilder dùng để xây dựng chuỗi
        sb.append("Course Information:\n"); //append dùng để nối các chuỗi vào đối tượng StringBuilder
        sb.append("Course Name: ").append(getCourseName()).append("\n");
        sb.append("Course ID: ").append(getCourseID()).append("\n");
        return sb.toString();
	}
}
