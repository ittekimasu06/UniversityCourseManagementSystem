CREATE DATABASE UniversityDB;

USE UniversityDB;

CREATE TABLE Student (
    studentID VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender BOOLEAN NOT NULL,
    dateOfBirth DATE NOT NULL,
-- năm sinh không được trước 1930 và sau 2007 (việc học là việc cả đời :v)
    CHECK (YEAR(dateOfBirth) > 1930 AND YEAR(dateOfBirth) < 2006),
-- mã sinh viên phải có định dạng 'SVxxx' ('SV' và 4 chữ số)
    CHECK (studentID REGEXP '^SV[0-9]{4}$')
);

CREATE TABLE Lecturer (
    lecturerID VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender BOOLEAN NOT NULL,
    dateOfBirth DATE NOT NULL,
-- năm sinh không được trước 1930 và sau 1999
    CHECK (YEAR(dateOfBirth) > 1930 AND YEAR(dateOfBirth) < 1999),
-- mã giảng viên phải có định dạng 'GVxxxx' ('GV' và 4 chữ số)
    CHECK (lecturerID REGEXP '^GV[0-9]{4}$')
);

CREATE TABLE Course (
    courseID VARCHAR(50) PRIMARY KEY,
    courseName VARCHAR(100) NOT NULL,
-- mã môn học phải có định dạng 'XXXxxxx' (3 chữ cái và 4 chữ số, VD: 'OOP1234')
    CHECK (courseID REGEXP '^[A-Z]{3}[0-9]{4}$')
);

CREATE TABLE Enroll (
    studentID VARCHAR(50),
    courseID VARCHAR(50),
    mark DOUBLE,
    PRIMARY KEY (studentID, courseID),
    FOREIGN KEY (studentID) REFERENCES Student(studentID) ON DELETE CASCADE,
    FOREIGN KEY (courseID) REFERENCES Course(courseID) ON DELETE CASCADE,
-- điểm không được thấp hơn 0.0 và quá 10.0
    CHECK (mark >= 0.0 AND mark <= 10.0)
);

CREATE TABLE Teach (
    lecturerID VARCHAR(50),
    courseID VARCHAR(50),
    PRIMARY KEY (lecturerID, courseID),
    FOREIGN KEY (lecturerID) REFERENCES Lecturer(lecturerID) ON DELETE CASCADE,
    FOREIGN KEY (courseID) REFERENCES Course(courseID) ON DELETE CASCADE
);
