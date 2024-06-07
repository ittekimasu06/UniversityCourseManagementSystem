package view;

import javax.swing.*;
import java.util.List;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import controller.DAO;
import model.Student;


public class StudentView extends JFrame {
    private JTextField nameField;
    private JTextField genderField;
    private JTextField dobField;
    private JTextField studentIDField;
    private JTable studentTable;
    private DAO dao;

    public StudentView() {
        dao = new DAO();
        setTitle("Student Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Gender (true: male/false: female:"));
        genderField = new JTextField();
        formPanel.add(genderField);

        formPanel.add(new JLabel("Date of Birth (yyyy-mm-dd):"));
        dobField = new JTextField();
        formPanel.add(dobField);

        formPanel.add(new JLabel("Student ID:"));
        studentIDField = new JTextField();
        formPanel.add(studentIDField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        formPanel.add(addButton);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });
        formPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });
        formPanel.add(deleteButton);

        JButton searchButton = new JButton("Search by ID");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        formPanel.add(searchButton);

        panel.add(formPanel, BorderLayout.NORTH);

        // Add table to display students
        studentTable = new JTable();
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        add(panel);
        displayStudents();
    }

    private void addStudent() {
        String name = nameField.getText();
        boolean gender = Boolean.parseBoolean(genderField.getText());
        Date dob = Date.valueOf(dobField.getText());
        String studentID = studentIDField.getText();

        Student student = new Student(name, gender, dob, studentID);
        boolean success = dao.addStudent(student);

        if (success) {
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            displayStudents(); // Refresh student list after adding
        } else {
            JOptionPane.showMessageDialog(this, "Student ID bị trùng");
        }
    }

    private void updateStudent() {
        String name = nameField.getText();
        boolean gender = Boolean.parseBoolean(genderField.getText());
        Date dob = Date.valueOf(dobField.getText());
        String studentID = studentIDField.getText();

        Student student = new Student(name, gender, dob, studentID);
        dao.updateStudent(student);
        JOptionPane.showMessageDialog(this, "Student updated successfully!");
        displayStudents(); // Refresh student list after updating
    }

    private void deleteStudent() {
        String studentID = studentIDField.getText();
        Student student = dao.searchStudentByStudentID(studentID);

        if (student != null) {
            dao.deleteStudent(student.getStudentID());
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            displayStudents(); // Refresh student list after deleting
        } else {
            JOptionPane.showMessageDialog(this, "Student not found");
        }
    }

    private void searchStudent() {
        String studentID = studentIDField.getText();
        Student student = dao.searchStudentByStudentID(studentID);

        if (student != null) {
            nameField.setText(student.getName());
            genderField.setText(String.valueOf(student.getGender()));
            dobField.setText(student.getDOB().toString());
            JOptionPane.showMessageDialog(this, "Student found!");
        } else {
            JOptionPane.showMessageDialog(this, "Student not found");
        }
    }

    private void displayStudents() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Gender");
        model.addColumn("Date of Birth");
        model.addColumn("Student ID");

        List<Student> students = dao.getAllStudents(); // Sử dụng phương thức đã có trong DAO để lấy danh sách sinh viên

        for (Student student : students) {
            Object[] rowData = {student.getName(), student.getGender(), student.getDOB(), student.getStudentID()};
            model.addRow(rowData);
        }

        studentTable.setModel(model); // Đặt mô hình dữ liệu cho JTable
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentView().setVisible(true);
            }
        });
    }
}
