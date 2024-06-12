package view;

import javax.swing.*;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.util.Map;


import controller.DAO;
import model.Course;

public class CourseView extends JFrame {
    private JTextField courseNameField;
    private JTextField courseIdField;
    private JTable courseTable;
    private DAO dao;

    public CourseView() {
        dao = new DAO();
        setTitle("Course Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Course Name:"));
        courseNameField = new JTextField();
        formPanel.add(courseNameField);

        formPanel.add(new JLabel("Course ID:"));
        courseIdField = new JTextField();
        formPanel.add(courseIdField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCourse();
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update by ID");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCourse();
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete by ID");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCourse();
            }
        });
        buttonPanel.add(deleteButton);

        JButton searchButton = new JButton("Search by Name");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSearchByNameDialog();
            }
        });
        buttonPanel.add(searchButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // đóng cửa sổ hiện tại
                new MainMenu().setVisible(true); // hiển thị MainMenu
            }
        });
        buttonPanel.add(backButton);

        leftPanel.add(formPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        courseTable = new JTable();
        mainPanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        add(mainPanel);
        displayCourses();
    }

    private void openSearchByNameDialog() {
        JDialog searchDialog = new JDialog(this, "Search by Name", true);
        searchDialog.setLayout(new BorderLayout());
        searchDialog.setSize(300, 150);
        searchDialog.setLocationRelativeTo(this);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField searchNameField = new JTextField();
        inputPanel.add(new JLabel("Enter name:"), BorderLayout.NORTH);
        inputPanel.add(searchNameField, BorderLayout.CENTER);

        searchDialog.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCourseByName(searchNameField.getText());
                searchDialog.dispose();
            }
        });
        buttonPanel.add(okButton);

        searchDialog.add(buttonPanel, BorderLayout.SOUTH);

        searchDialog.setVisible(true);
    }

    private void addCourse() {
        String name = courseNameField.getText();
        String id = courseIdField.getText();
        Course course = new Course(name, id);
        boolean success = dao.addCourse(course);
        if (success) {
            JOptionPane.showMessageDialog(this, "Course added successfully!");
            displayCourses();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add course.");
        }
    }

    private void updateCourse() {
        String name = courseNameField.getText();
        String id = courseIdField.getText();
        Course course = new Course(name, id);
        dao.updateCourse(course);
        JOptionPane.showMessageDialog(this, "Course updated successfully!");
        displayCourses();
    }

    private void deleteCourse() {
        String id = courseIdField.getText();
        dao.deleteCourse(id);
        JOptionPane.showMessageDialog(this, "Course deleted successfully!");
        displayCourses();
    }

    private void searchCourseByName(String name) {
        Map<String, Course> courseMap = dao.getAllCoursesMap(name);
        if (courseMap != null && !courseMap.isEmpty()) {
            Course course = courseMap.values().iterator().next();
            courseNameField.setText(course.getCourseName());
            courseIdField.setText(course.getCourseID());
            JOptionPane.showMessageDialog(this, "Course found!");
        } else {
            JOptionPane.showMessageDialog(this, "Course not found");
        }
    }

    private void displayCourses() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Course Name");
        model.addColumn("Course ID");

        List<Course> courses = dao.getAllCourses();

        for (Course course : courses) {
            Object[] rowData = {course.getCourseName(), course.getCourseID()};
            model.addRow(rowData);
        }

        courseTable.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CourseView().setVisible(true);
            }
        });
    }
}