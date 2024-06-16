package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controller.StudentDAO;
import controller.CourseDAO;
import controller.EnrollmentDAO;
import model.Enrollment;

public class StudentEnrollCourse extends JPanel {
	private static final long serialVersionUID = 2307855448444060619L;
	private JComboBox<String> studentNameComboBox;
    private JComboBox<String> courseNameComboBox;
    private JTextField markField;
    private JTable enrollTable;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollDAO;
 
    public StudentEnrollCourse() {
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
        enrollDAO = new EnrollmentDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Student Name:"));
        studentNameComboBox = new JComboBox<>();
        populateStudentNameComboBox();
        formPanel.add(studentNameComboBox);

        formPanel.add(new JLabel("Course Name:"));
        courseNameComboBox = new JComboBox<>();
        populateCourseNameComboBox();
        formPanel.add(courseNameComboBox);

        formPanel.add(new JLabel("Mark:"));
        markField = new JTextField();
        formPanel.add(markField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton enrollButton = new JButton("Enroll");
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enrollStudentInCourse();
            }
        });
        buttonPanel.add(enrollButton);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshEnrollTable();
                refreshStudentNameComboBox();
                refreshCourseNameComboBox();
            }
        });
        buttonPanel.add(refreshButton);

        leftPanel.add(formPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        enrollTable = new JTable();
        add(new JScrollPane(enrollTable), BorderLayout.CENTER);

        displayEnrollments();
    }

    private void populateStudentNameComboBox() {
        List<String> studentNames = studentDAO.getAllStudentNames();
        for (String name : studentNames) {
            studentNameComboBox.addItem(name);
        }
    }

    private void populateCourseNameComboBox() {
        List<String> courseNames = courseDAO.getAllCourseNames();
        for (String name : courseNames) {
            courseNameComboBox.addItem(name);
        }
    }

    private void enrollStudentInCourse() {
        String studentName = (String) studentNameComboBox.getSelectedItem();
        String courseName = (String) courseNameComboBox.getSelectedItem();
        
        String studentID = studentDAO.getStudentIDByName(studentName);
        String courseID = courseDAO.getCourseIDByName(courseName);
        double mark = Double.parseDouble(markField.getText());
        
     // Kiểm tra số lượng sinh viên đã đăng ký
        if (enrollDAO.isCourseFull(courseID)) {
            JOptionPane.showMessageDialog(this, "Course is full. Enrollment failed.");
            return;
        }

        boolean success = enrollDAO.enrollStudentInCourseAndAssignMark(studentID, courseID, mark);
        if (success) {
            JOptionPane.showMessageDialog(this, "Student enrolled in course successfully!");
            displayEnrollments();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to enroll student in course. Please try again.");
        }
    }

    private void clearFields() {
        markField.setText("");
    }

    private void displayEnrollments() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Student Name");
        model.addColumn("Student ID");
        model.addColumn("Course Name");
        model.addColumn("Course ID");
        model.addColumn("Mark");

        List<Enrollment> enrollments = enrollDAO.getAllEnrollments();
        for (Enrollment enrollment : enrollments) {
            String studentName = studentDAO.getStudentNameByID(enrollment.getStudentID());
            String courseName = courseDAO.getCourseNameByID(enrollment.getCourseID());
            Object[] rowData = {studentName, enrollment.getStudentID(), courseName, enrollment.getCourseID(), enrollment.getMark()};
            model.addRow(rowData);
        }

        enrollTable.setModel(model);
    }
    
    //làm mới combobox tên student
    private void refreshStudentNameComboBox() {
        studentNameComboBox.removeAllItems();
        populateStudentNameComboBox();
    }
    
    //làm mới combobox tên course
    private void refreshCourseNameComboBox() {
        courseNameComboBox.removeAllItems();
        populateCourseNameComboBox();
    }
    
    //làm mới bảng 
    private void refreshEnrollTable() {
        displayEnrollments();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Enroll Student in Course");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setContentPane(new StudentEnrollCourse());
                frame.setVisible(true);
            }
        });
    }
}