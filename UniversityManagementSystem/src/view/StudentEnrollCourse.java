package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controller.DAO;
import model.Enrollment;

public class StudentEnrollCourse extends JFrame {
    private JComboBox<String> studentIDComboBox;
    private JComboBox<String> courseIDComboBox;
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

        formPanel.add(new JLabel("Student ID:"));
        studentIDComboBox = new JComboBox<>();
        populateStudentIDComboBox();
        formPanel.add(studentIDComboBox);

        formPanel.add(new JLabel("Course ID:"));
        courseIDComboBox = new JComboBox<>();
        populateCourseIDComboBox();
        formPanel.add(courseIDComboBox);

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

        leftPanel.add(formPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        enrollTable = new JTable();
        mainPanel.add(new JScrollPane(enrollTable), BorderLayout.CENTER);

        add(mainPanel);
        displayEnrollments();
    }

    private void populateStudentIDComboBox() {
        List<String> studentIDs = dao.getAllStudentIDs();
        for (String id : studentIDs) {
            studentIDComboBox.addItem(id);
        }
    }

    private void populateCourseIDComboBox() {
        List<String> courseIDs = dao.getAllCourseIDs();
        for (String id : courseIDs) {
            courseIDComboBox.addItem(id);
        }
    }

    private void enrollStudentInCourse() {
        String studentID = (String) studentIDComboBox.getSelectedItem();
        String courseID = (String) courseIDComboBox.getSelectedItem();
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

    private void displayEnrollments() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Student ID");
        model.addColumn("Course ID");
        model.addColumn("Mark");

        List<Enrollment> enrollments = dao.getAllEnrollments();
        for (Enrollment enrollment : enrollments) {
            Object[] rowData = {enrollment.getStudentID(), enrollment.getCourseID(), enrollment.getMark()};
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