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


import controller.StudentDAO;
import model.Student;

public class StudentView extends JPanel {
    private JTextField nameField;
    private JTextField genderField;
    private JTextField dobField;
    private JTextField studentIDField;
    private JTable studentTable;
    private StudentDAO studentDAO;
    private Map<String, Student> studentMap;

    public StudentView() {
        studentDAO = new StudentDAO();
        studentMap = studentDAO.getAllStudentsMap();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        studentTable = new JTable();
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        displayStudents();
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
        boolean success = studentDAO.addStudent(student);

        if (success) {
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            studentMap = studentDAO.getAllStudentsMap(); //cập nhật lại studentMap sau khi thêm
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
        studentDAO.updateStudent(student);
        JOptionPane.showMessageDialog(this, "Student updated successfully!");
        studentMap = studentDAO.getAllStudentsMap(); //cập nhật lại studentMap sau khi cập nhật thông tin
        displayStudents(); // hiển thị lại sau khi thêm
    }

    private void deleteStudent() {
        String studentID = studentIDField.getText();
        Student student = studentDAO.searchStudentByStudentID(studentID);

        if (student != null) {
            studentDAO.deleteStudent(student.getStudentID());
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            studentMap = studentDAO.getAllStudentsMap(); //cập nhật lại studentMap sau khi xóa
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

        List<Student> students = studentDAO.getAllStudents();
        studentMap.clear(); //clear map trước khi tổ chức lại
        for (Student student : students) {
            studentMap.put(student.getName(), student);
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
                JFrame frame = new JFrame("Student Management");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setContentPane(new StudentView());
                frame.setVisible(true);
            }
        });
    }
}