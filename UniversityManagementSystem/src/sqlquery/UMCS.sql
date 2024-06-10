CREATE DATABASE UniversityDB;

USE UniversityDB;

CREATE TABLE Student (
    studentID VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender BOOLEAN NOT NULL,
    dateOfBirth DATE NOT NULL
);

CREATE TABLE Lecturer (
    lecturerID VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender BOOLEAN NOT NULL,
    dateOfBirth DATE NOT NULL
);

CREATE TABLE Course (
    courseID VARCHAR(50) PRIMARY KEY,
    courseName VARCHAR(100) NOT NULL
);
CREATE TABLE Enroll (
    studentID VARCHAR(50),
    courseID VARCHAR(50),
    mark DOUBLE,
    PRIMARY KEY (studentID, courseID),
    FOREIGN KEY (studentID) REFERENCES Student(studentID) ON DELETE CASCADE,
    FOREIGN KEY (courseID) REFERENCES Course(courseID) ON DELETE CASCADE
);

CREATE TABLE Teach (
    lecturerID VARCHAR(50),
    courseID VARCHAR(50),
    PRIMARY KEY (lecturerID, courseID),
    FOREIGN KEY (lecturerID) REFERENCES Lecturer(lecturerID) ON DELETE CASCADE,
    FOREIGN KEY (courseID) REFERENCES Course(courseID) ON DELETE CASCADE
);
