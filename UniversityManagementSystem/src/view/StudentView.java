package view;
import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


import controller.DAO;
import model.Student;

public class StudentView extends JFrame {
    private JTextField nameField;
    private JTextField genderField;
    private JTextField dobField;
    private JTextField studentIDField;
    private JTextField gpaField;
    private JTable studentTable;
    private DAO dao;
    private Map<String, Student> studentMap;

    public StudentView() {
        dao = new DAO();
        studentMap = dao.getAllStudentsMap(); 
        setTitle("Student Management");
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

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Gender (true: male/false: female):"));
        genderField = new JTextField();
        formPanel.add(genderField);

        formPanel.add(new JLabel("Date of Birth (yyyy-mm-dd):"));
        dobField = new JTextField();
        formPanel.add(dobField);

        formPanel.add(new JLabel("Student ID:"));
        studentIDField = new JTextField();
        formPanel.add(studentIDField);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update by ID");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete by ID");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
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

        studentTable = new JTable();
        mainPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        add(mainPanel);
        displayStudents();
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
                searchStudentByName(searchNameField.getText());
                searchDialog.dispose();
            }
        });
        buttonPanel.add(okButton);

        searchDialog.add(buttonPanel, BorderLayout.SOUTH);

        searchDialog.setVisible(true);
    }

    private void addStudent() {
        String name = nameField.getText();
        boolean gender = Boolean.parseBoolean(genderField.getText());
        Date dob = Date.valueOf(dobField.getText());
        String studentID = studentIDField.getText();

        Student student = new Student(name, gender, dob, studentID, 0.0); //gpa mặc định là 0.0
        boolean success = dao.addStudent(student);

        if (success) {
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            displayStudents(); // hiển thị lại sau khi thêm
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student.");
        }
    }

    private void updateStudent() {
        String name = nameField.getText();
        boolean gender = Boolean.parseBoolean(genderField.getText());
        Date dob = Date.valueOf(dobField.getText());
        String studentID = studentIDField.getText();

        Student student = new Student(name, gender, dob, studentID, 0.0);
        dao.updateStudent(student);
        JOptionPane.showMessageDialog(this, "Student updated successfully!");
        displayStudents(); // hiển thị lại sau khi thêm
    }

    private void deleteStudent() {
        String studentID = studentIDField.getText();
        Student student = dao.searchStudentByStudentID(studentID);

        if (student != null) {
            dao.deleteStudent(student.getStudentID());
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            displayStudents(); // hiển thị lại sau khi xóa
        } else {
            JOptionPane.showMessageDialog(this, "Student not found");
        }
    }

    private void searchStudentByName(String name) {
        Student student = studentMap.get(name);

        if (student != null) {
            nameField.setText(student.getName());
            genderField.setText(String.valueOf(student.getGender()));
            dobField.setText(student.getDOB().toString());
            studentIDField.setText(student.getStudentID());
            gpaField.setText(String.valueOf(student.getGpa()));
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
        model.addColumn("GPA");
        
        List<Student> students = dao.getAllStudents(); 

        for (Student student : students) {
            Object[] rowData = {student.getName(), student.getGender(), student.getDOB(), student.getStudentID(), student.getGpa()};
            model.addRow(rowData);
        }
        // Đặt mô hình dữ liệu cho JTable
        studentTable.setModel(model); 
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
