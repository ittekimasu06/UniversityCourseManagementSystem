package view;

import javax.swing.*;

import java.util.List;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.util.Map;


import controller.CourseDAO;
import model.Course;


public class CourseView extends JPanel {
	private static final long serialVersionUID = 3062527725311502509L;
	private JTextField courseNameField;
    private JTextField courseIDField;
    private JTable courseTable;
    private CourseDAO courseDAO;
    private Map<String, Course> courseMap;

    public CourseView() {
        courseDAO = new CourseDAO();
        courseMap = courseDAO.getAllCoursesMap();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Course Name:"));
        courseNameField = new JTextField();
        formPanel.add(courseNameField);

        formPanel.add(new JLabel("Course ID:"));
        courseIDField = new JTextField();
        formPanel.add(courseIDField);

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

//        JButton backButton = new JButton("Back");
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ((CardLayout) getParent().getLayout()).show(getParent(), "MainMenu");
//            }
//        });
//        buttonPanel.add(backButton);

        leftPanel.add(formPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        courseTable = new JTable();
        add(new JScrollPane(courseTable), BorderLayout.CENTER);

        displayCourses();
    }

    private void openSearchByNameDialog() {
        JDialog searchDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Search by Name", true);
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
        String id = courseIDField.getText();
        Course course = new Course(name, id);
        boolean success = courseDAO.addCourse(course);
        if (success) {
            JOptionPane.showMessageDialog(this, "Course added successfully!");
            courseMap = courseDAO.getAllCoursesMap(); // cập nhật courseMap sau khi thêm
            displayCourses(); // hiển thị lại sau khi thêm
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add course.");
        }
    }

    private void updateCourse() {
        String name = courseNameField.getText();
        String id = courseIDField.getText();
        Course course = new Course(name, id);
        courseDAO.updateCourse(course);
        JOptionPane.showMessageDialog(this, "Course updated successfully!");
        courseMap = courseDAO.getAllCoursesMap(); // cập nhật courseMap sau khi cập nhật thông tin
        displayCourses(); // hiển thị lại sau khi cập nhật
    }

    private void deleteCourse() {
        String id = courseIDField.getText();
        courseDAO.deleteCourse(id);
        JOptionPane.showMessageDialog(this, "Course deleted successfully!");
        courseMap = courseDAO.getAllCoursesMap(); // cập nhật courseMap sau khi xóa
        displayCourses(); // hiển thị lại sau khi xóa
    }

    private void searchCourseByName(String name) {
        Course course = courseMap.get(name);

        if (course != null) {
            courseNameField.setText(course.getCourseName());
            courseIDField.setText(course.getCourseID());
            JOptionPane.showMessageDialog(this, "Course found!");
        } else {
            JOptionPane.showMessageDialog(this, "Course not found.");
        }
    }

    private void displayCourses() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Course Name");
        model.addColumn("Course ID");

        List<Course> courses = courseDAO.getAllCourses();
        courseMap.clear(); // clear map trước khi tổ chức lại
        for (Course course : courses) {
            courseMap.put(course.getCourseName(), course);
            Object[] rowData = {course.getCourseName(), course.getCourseID()};
            model.addRow(rowData);
        }

        courseTable.setModel(model); // đặt mô hình dữ liệu cho JTable
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Course Management");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setContentPane(new CourseView());
                frame.setVisible(true);
            }
        });
    }
}