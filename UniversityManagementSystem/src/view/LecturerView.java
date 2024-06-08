package view;
import javax.swing.*;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import javax.swing.table.DefaultTableModel;

import controller.DAO;
import model.Lecturer;

public class LecturerView extends JFrame {
    private JTextField nameField;
    private JTextField genderField;
    private JTextField dobField;
    private JTextField lecturerIDField;
    private JTable lecturerTable;
    private DAO dao;

    public LecturerView() {
        dao = new DAO();
        setTitle("Lecturer Management");
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

        formPanel.add(new JLabel("Gender (true: male/false: female):"));
        genderField = new JTextField();
        formPanel.add(genderField);

        formPanel.add(new JLabel("Date of Birth (yyyy-mm-dd):"));
        dobField = new JTextField();
        formPanel.add(dobField);

        formPanel.add(new JLabel("Lecturer ID:"));
        lecturerIDField = new JTextField();
        formPanel.add(lecturerIDField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLecturer();
            }
        });
        formPanel.add(addButton);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLecturer();
            }
        });
        formPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteLecturer();
            }
        });
        formPanel.add(deleteButton);

        JButton searchButton = new JButton("Search by ID");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchLecturer();
            }
        });
        formPanel.add(searchButton);

        panel.add(formPanel, BorderLayout.NORTH);

        // Add table to display lecturers
        lecturerTable = new JTable();
        panel.add(new JScrollPane(lecturerTable), BorderLayout.CENTER);

        add(panel);
        displayLecturers();
    }

    private void addLecturer() {
        String name = nameField.getText();
        boolean gender = Boolean.parseBoolean(genderField.getText());
        Date dob = Date.valueOf(dobField.getText());
        String lecturerID = lecturerIDField.getText();

        Lecturer lecturer = new Lecturer(name, gender, dob, lecturerID);
        boolean success = dao.addLecturer(lecturer);

        if (success) {
            JOptionPane.showMessageDialog(this, "Lecturer added successfully!");
            displayLecturers(); // Refresh lecturer list after adding
        } else {
            JOptionPane.showMessageDialog(this, "Lecturer ID bị trùng");
        }
    }

    private void updateLecturer() {
        String name = nameField.getText();
        boolean gender = Boolean.parseBoolean(genderField.getText());
        Date dob = Date.valueOf(dobField.getText());
        String lecturerID = lecturerIDField.getText();

        Lecturer lecturer = new Lecturer(name, gender, dob, lecturerID);
        dao.updateLecturer(lecturer);
        JOptionPane.showMessageDialog(this, "Lecturer updated successfully!");
        displayLecturers(); // Refresh lecturer list after updating
    }

    private void deleteLecturer() {
        String lecturerID = lecturerIDField.getText();
        Lecturer lecturer = dao.searchLecturerByLecturerID(lecturerID);

        if (lecturer != null) {
            dao.deleteLecturer(lecturer.getLecturerID());
            JOptionPane.showMessageDialog(this, "Lecturer deleted successfully!");
            displayLecturers(); // Refresh lecturer list after deleting
        } else {
            JOptionPane.showMessageDialog(this, "Lecturer not found");
        }
    }

    private void searchLecturer() {
        String lecturerID = lecturerIDField.getText();
        Lecturer lecturer = dao.searchLecturerByLecturerID(lecturerID);

        if (lecturer != null) {
            nameField.setText(lecturer.getName());
            genderField.setText(String.valueOf(lecturer.getGender()));
            dobField.setText(lecturer.getDOB().toString());
            JOptionPane.showMessageDialog(this, "Lecturer found!");
        } else {
            JOptionPane.showMessageDialog(this, "Lecturer not found");
        }
    }

    private void displayLecturers() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Gender");
        model.addColumn("Date of Birth");
        model.addColumn("Lecturer ID");

        List<Lecturer> lecturers = dao.getAllLecturers(); // Sử dụng phương thức đã có trong DAO để lấy danh sách giảng viên

        for (Lecturer lecturer : lecturers) {
            Object[] rowData = {lecturer.getName(), lecturer.getGender(), lecturer.getDOB(), lecturer.getLecturerID()};
            model.addRow(rowData);
        }

        lecturerTable.setModel(model); // Đặt mô hình dữ liệu cho JTable
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LecturerView().setVisible(true);
            }
        });
    }
}
