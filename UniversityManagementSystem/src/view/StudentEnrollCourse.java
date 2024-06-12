package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controller.DAO;
import model.Enrollment;
import model.Student;

public class StudentEnrollCourse extends JFrame {
    private JComboBox<String> studentNameComboBox;
    private JComboBox<String> courseNameComboBox;
    private JTextField markField;
    private JTable enrollTable;
    private DAO dao;

    public StudentEnrollCourse() {
        dao = new DAO();
        setTitle("Enroll Student in Course");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); //đóng cửa sổ hiện tại
                new MainMenu().setVisible(true); //hiển thị MainMenu
            }
        });
        buttonPanel.add(backButton);

        leftPanel.add(formPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        enrollTable = new JTable();
        mainPanel.add(new JScrollPane(enrollTable), BorderLayout.CENTER);

        add(mainPanel);
        displayEnrollments();
    }

    private void populateStudentNameComboBox() {
        List<String> studentNames = dao.getAllStudentNames();
        for (String name : studentNames) {
        	studentNameComboBox.addItem(name);
        }
    }

    private void populateCourseNameComboBox() {
        List<String> courseNames = dao.getAllCourseNames();
        for (String name : courseNames) {
            courseNameComboBox.addItem(name);
        }
    }

    private void enrollStudentInCourse() {
        String studentName = (String) studentNameComboBox.getSelectedItem();
        String courseName = (String) courseNameComboBox.getSelectedItem();
        
     // tìm ID của sinh viên và khóa học tương ứng với tên đã chọn
        String studentID = dao.getStudentIDByName(studentName);
        String courseID = dao.getCourseIDByName(courseName);
        double mark = Double.parseDouble(markField.getText());

        boolean success = dao.enrollStudentInCourseAndAssignMark(studentID, courseID, mark);
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

//    private void displayEnrollments() {
//        DefaultTableModel model = new DefaultTableModel();
//        model.addColumn("Student ID");
//        model.addColumn("Course ID");
//        model.addColumn("Mark");
//
//        List<Enrollment> enrollments = dao.getAllEnrollments();
//        for (Enrollment enrollment : enrollments) {
//            Object[] rowData = {enrollment.getStudentID(), enrollment.getCourseID(), enrollment.getMark()};
//            model.addRow(rowData);
//        }
//
//        enrollTable.setModel(model);
//    }
    
    private void displayEnrollments() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Student Name");
        model.addColumn("Student ID");
        model.addColumn("Course Name");
        model.addColumn("Course ID");
        model.addColumn("Mark");

        List<Enrollment> enrollments = dao.getAllEnrollments();
        for (Enrollment enrollment : enrollments) {
        	String studentName = dao.getStudentNameByID(enrollment.getStudentID());
            String courseName = dao.getCourseNameByID(enrollment.getCourseID());
            Object[] rowData = {studentName, enrollment.getStudentID(), courseName, enrollment.getCourseID(), enrollment.getMark()};
            model.addRow(rowData);
        }

        enrollTable.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentEnrollCourse().setVisible(true);
            }
        });
    }
}