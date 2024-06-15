package model;
import java.util.Date;

public abstract class Person {
	protected String name;
    protected boolean gender;
    protected Date dateOfBirth;

    public Person(String name, boolean gender, Date dateOfBirth) {
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setGender(boolean gender) {
    	this.gender = gender;
    }
    
    public boolean getGender() {
    	return gender;
    }
    
    public void setDOB(Date dateOfBirth) {
    	this.dateOfBirth = dateOfBirth;
    }
    
    public Date getDOB() {
    	return dateOfBirth;
    }
    
    public abstract String view();
}
