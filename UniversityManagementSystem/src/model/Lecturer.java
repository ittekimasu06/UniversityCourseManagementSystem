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
    public String view() {
        StringBuilder sb = new StringBuilder(); //StringBuilder dùng để xây dựng chuỗi
        sb.append("Lecturer Information:\n"); //append dùng để nối các chuỗi vào đối tượng StringBuilder
        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Gender: ").append(getGender() ? "Male" : "Female").append("\n");
        sb.append("Date of Birth: ").append(getDOB()).append("\n");
        sb.append("Lecturer ID: ").append(getLecturerID()).append("\n");
        return sb.toString();
    }

//	public void addCourse(Course course) {
//		this.courses.add(course);
//	}
//
//	public List<Course> getCourses() {
//		return courses;
//	}

}
