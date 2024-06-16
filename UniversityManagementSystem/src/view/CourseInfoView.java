package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import controller.CourseDAO;
import controller.EnrollmentDAO;
import controller.LecturerDAO;
import controller.StudentDAO;
import controller.TeachDAO;
import model.Course;
import model.Enrollment;
import model.Teach;

public class CourseInfoView extends JFrame {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> courseComboBox;
    private JTextArea courseDetailsArea;
    private JTable studentTable;
    private JTable lecturerTable;
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;
    private TeachDAO teachDAO;
    private StudentDAO studentDAO;
    private LecturerDAO lecturerDAO;

    public CourseInfoView() {
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();
        teachDAO = new TeachDAO();
        studentDAO = new StudentDAO();
        lecturerDAO = new LecturerDAO();

        setTitle("Course Info");
        setSize(800, 600);
        setLayout(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        courseComboBox = new JComboBox<>();
        List<String> courseNames = courseDAO.getAllCourseNames();
        for (String name : courseNames) {
            courseComboBox.addItem(name);
        }
        courseComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCourseInfo((String) courseComboBox.getSelectedItem());
            }
        });
        courseDetailsArea = new JTextArea();
        courseDetailsArea.setEditable(false);
        

        leftPanel.add(courseComboBox, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(courseDetailsArea), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(2, 1));

        studentTable = new JTable(new DefaultTableModel(new Object[]{"Student Name", "Student ID", "Mark"}, 0));
        lecturerTable = new JTable(new DefaultTableModel(new Object[]{"Lecturer Name", "Lecturer ID"}, 0));

        rightPanel.add(new JScrollPane(studentTable));
        rightPanel.add(new JScrollPane(lecturerTable));

        add(leftPanel);
        add(rightPanel);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void displayCourseInfo(String courseName) {
        if (courseName == null || courseName.isEmpty()) {
            return;
        }
        Course course = courseDAO.getAllCoursesMap().get(courseName);
        if (course != null) {
        	courseDetailsArea.setText(course.viewCourse());
            updateStudentTable(course.getCourseID());
            updateLecturerTable(course.getCourseID());
        } else {
            clearStudentTable();
            clearLecturerTable();
        }
    }

    private void updateStudentTable(String courseID) {
        DefaultTableModel studentModel = (DefaultTableModel) studentTable.getModel();
        clearStudentTable();
        List<Enrollment> enrollments = enrollmentDAO.getAllEnrollments();
        for (Enrollment enrollment : enrollments) {
            String studentName = studentDAO.getStudentNameByID(enrollment.getStudentID());
        	if (enrollment.getCourseID().equals(courseID)) {
        		Object[] rowData = {studentName, enrollment.getStudentID(), enrollment.getMark()};
        		studentModel.addRow(rowData);
        	}
        }
    }

    private void updateLecturerTable(String courseID) {
        DefaultTableModel lecturerModel = (DefaultTableModel) lecturerTable.getModel();
        clearLecturerTable();
        List<Teach> teaches = teachDAO.getAllTeach();
        for (Teach teach : teaches) {
           String lecturerName = lecturerDAO.getLecturerNameByID(teach.getLecturerID());
           if (teach.getCourseID().equals(courseID)) {
       		Object[] rowData = {lecturerName, teach.getLecturerID()};
       		lecturerModel.addRow(rowData);
       	}
        }
    }

    private void clearStudentTable() {
        DefaultTableModel studentModel = (DefaultTableModel) studentTable.getModel();
        studentModel.setRowCount(0);
    }

    private void clearLecturerTable() {
        DefaultTableModel lecturerModel = (DefaultTableModel) lecturerTable.getModel();
        lecturerModel.setRowCount(0);
    }
}
