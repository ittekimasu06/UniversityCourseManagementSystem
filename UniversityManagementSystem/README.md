#My OOP Final Project
This Project demonstrate how a UCMS (University Course Management System) would work, or at least how I demonstrate it :)

Everything used in this project:
- Eclipse IDE, jdk 22.0.1
- MySQL for database
- JDBC for database connectivity
- Swing for GUI 

##How to run 
Since I'm a newbie, you have to do all the following steps in order to run the program.

- Create a MySQL database with the following query:

```sql
CREATE DATABASE UniversityDB;

USE UniversityDB;

CREATE TABLE Student (
    studentID VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender BOOLEAN NOT NULL,
    dateOfBirth DATE NOT NULL,
    CHECK (YEAR(dateOfBirth) > 1930 AND YEAR(dateOfBirth) < 2006),
    CHECK (studentID REGEXP '^SV[0-9]{4}$')
);

CREATE TABLE Lecturer (
    lecturerID VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender BOOLEAN NOT NULL,
    dateOfBirth DATE NOT NULL,
    CHECK (YEAR(dateOfBirth) > 1930 AND YEAR(dateOfBirth) < 1999),
    CHECK (lecturerID REGEXP '^GV[0-9]{4}$')
);

CREATE TABLE Course (
    courseID VARCHAR(50) PRIMARY KEY,
    courseName VARCHAR(100) NOT NULL,
    CHECK (courseID REGEXP '^[A-Z]{3}[0-9]{4}$')
);

CREATE TABLE Enroll (
    studentID VARCHAR(50),
    courseID VARCHAR(50),
    mark DOUBLE,
    PRIMARY KEY (studentID, courseID),
    FOREIGN KEY (studentID) REFERENCES Student(studentID) ON DELETE CASCADE,
    FOREIGN KEY (courseID) REFERENCES Course(courseID) ON DELETE CASCADE,
    CHECK (mark >= 0.0 AND mark <= 10.0)
);

CREATE TABLE Teach (
    lecturerID VARCHAR(50),
    courseID VARCHAR(50),
    PRIMARY KEY (lecturerID, courseID),
    FOREIGN KEY (lecturerID) REFERENCES Lecturer(lecturerID) ON DELETE CASCADE,
    FOREIGN KEY (courseID) REFERENCES Course(courseID) ON DELETE CASCADE
);

```

- Modifying Database connection information

Locate the `DatabaseConnection` class at the path:

`UniversityManagementSystem/src/controller/DatabaseConnection.java`

Update the information corresponding to the database in your computer:

```java
private static final String JDBCURL = "jdbc:mysql://127.0.0.1:3306/universitydb";
private static final String DBUSER = "root";
private static final String DBPASSWORD = "your_password";
```

- Run the Program

Run the `MainMenu` class located at:

`UniversityManagementSystem/src/view/MainMenu.java`

 


