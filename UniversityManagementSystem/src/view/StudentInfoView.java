package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controller.EnrollmentDAO;
import controller.StudentDAO;
import controller.CourseDAO;
import model.Enrollment;
import model.Student;

public class StudentInfoView extends JFrame {
	private static final long serialVersionUID = -639128112748827760L;
	private JComboBox<String> studentComboBox;
    private JTextArea studentDetailsArea;
    private JTable courseTable;
    private StudentDAO studentDAO;
    private EnrollmentDAO enrollmentDAO;
    private CourseDAO courseDAO;
 
    public StudentInfoView() {
        studentDAO = new StudentDAO();
        enrollmentDAO = new EnrollmentDAO();
        courseDAO = new CourseDAO();

        setTitle("Student Info");
        setSize(600, 400);
        setLayout(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        studentComboBox = new JComboBox<>();
        List<String> studentNames = studentDAO.getAllStudentNames();
        for (String name : studentNames) {
            studentComboBox.addItem(name);
        }
        studentComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStudentInfo((String) studentComboBox.getSelectedItem());
            }
        });
        studentDetailsArea = new JTextArea();
        studentDetailsArea.setEditable(false);

        leftPanel.add(studentComboBox, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(studentDetailsArea), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        courseTable = new JTable(new DefaultTableModel(new Object[]{"Course Name", "Course ID", "Mark"}, 0));

        rightPanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        add(leftPanel);
        add(rightPanel);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void displayStudentInfo(String studentName) {
        if (studentName == null || studentName.isEmpty()) {
            return;
        }

        Student student = studentDAO.getAllStudentsMap().get(studentName);
        if (student != null) {    
            studentDetailsArea.setText(student.view());
            updateCourseTable(student.getStudentID());
        } else {
            studentDetailsArea.setText("Student not found");
            clearCourseTable();
        }
    }

    private void updateCourseTable(String studentID) {
        DefaultTableModel model = (DefaultTableModel) courseTable.getModel();
        model.setRowCount(0); // clear dữ liệu

        List<Enrollment> enrollments = enrollmentDAO.getAllEnrollments();
        for (Enrollment enrollment : enrollments) {
        	String courseName = courseDAO.getCourseNameByID(enrollment.getCourseID());
            if (enrollment.getStudentID().equals(studentID)) {
            	Object[] rowData = {courseName, enrollment.getCourseID(), enrollment.getMark()};
            	model.addRow(rowData);
            }
        }
    }

    private void clearCourseTable() {
        DefaultTableModel model = (DefaultTableModel) courseTable.getModel();
        model.setRowCount(0);
    }
}
