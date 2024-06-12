package view;

import javax.swing.*;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import javax.swing.table.DefaultTableModel;
import java.util.Map;


import controller.DAO;
import model.Lecturer;

public class LecturerView extends JFrame {
    private JTextField nameField;
    private JTextField genderField;
    private JTextField dobField;
    private JTextField lecturerIDField;
    private JTable lecturerTable;
    private DAO dao;
    private Map<String, Lecturer> lecturerMap;

    public LecturerView() {
        dao = new DAO();
        lecturerMap = dao.getAllLecturersMap(); 
        setTitle("Lecturer Management");
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

        formPanel.add(new JLabel("Lecturer ID:"));
        lecturerIDField = new JTextField();
        formPanel.add(lecturerIDField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLecturer();
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update by ID");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLecturer();
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete by ID");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteLecturer();
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

        lecturerTable = new JTable();
        mainPanel.add(new JScrollPane(lecturerTable), BorderLayout.CENTER);

        add(mainPanel);
        displayLecturers();
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
                searchLecturerByName(searchNameField.getText());
                searchDialog.dispose();
            }
        });
        buttonPanel.add(okButton);

        searchDialog.add(buttonPanel, BorderLayout.SOUTH);

        searchDialog.setVisible(true);
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
            displayLecturers(); // hiển thị lại sau khi thêm
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
        displayLecturers(); // hiển thị lại sau khi update
    }

    private void deleteLecturer() {
        String lecturerID = lecturerIDField.getText();
        Lecturer lecturer = dao.searchLecturerByLecturerID(lecturerID);

        if (lecturer != null) {
            dao.deleteLecturer(lecturer.getLecturerID());
            JOptionPane.showMessageDialog(this, "Lecturer deleted successfully!");
            displayLecturers(); // hiển thị lại sau khi xóa
        } else {
            JOptionPane.showMessageDialog(this, "Lecturer not found");
        }
    }

    private void searchLecturerByName(String name) {
        Lecturer lecturer = lecturerMap.get(name);

        if (lecturer != null) {
            nameField.setText(lecturer.getName());
            genderField.setText(String.valueOf(lecturer.getGender()));
            dobField.setText(lecturer.getDOB().toString());
            lecturerIDField.setText(lecturer.getLecturerID());
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

        List<Lecturer> lecturers = dao.getAllLecturers(); 

        for (Lecturer lecturer : lecturers) {
            Object[] rowData = {lecturer.getName(), lecturer.getGender(), lecturer.getDOB(), lecturer.getLecturerID()};
            model.addRow(rowData);
        }

        lecturerTable.setModel(model); // đặt mô hình dữ liệu cho JTable
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