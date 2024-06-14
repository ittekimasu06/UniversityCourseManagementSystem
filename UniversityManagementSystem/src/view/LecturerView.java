package view;

import javax.swing.*;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

import controller.LecturerDAO;
import model.Lecturer;

public class LecturerView extends JPanel {
	private static final long serialVersionUID = -7543262858433732687L;
	private JTextField nameField;
    private JTextField genderField;
    private JTextField dobField;
    private JTextField lecturerIDField;
    private JTable lecturerTable;
    private LecturerDAO lecturerDAO;
    private Map<String, Lecturer> lecturerMap;

    public LecturerView() {
        lecturerDAO = new LecturerDAO();
        lecturerMap = lecturerDAO.getAllLecturersMap();

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

//        JButton backButton = new JButton("Back");
//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ((CardLayout) getParent().getLayout()).show(getParent(), "MainMenu");
//            }
//        });
//        buttonPanel.add(backButton);
        
        JButton lecturerInfoButton = new JButton("Lecturer Info");
        lecturerInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLecturerInfoWindow();
            }
        });
        buttonPanel.add(lecturerInfoButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshLecturerTable();
            }
        });
        buttonPanel.add(refreshButton);

        leftPanel.add(formPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        lecturerTable = new JTable();
        add(new JScrollPane(lecturerTable), BorderLayout.CENTER);

        displayLecturers();
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
        boolean success = lecturerDAO.addLecturer(lecturer);

        if (success) {
            JOptionPane.showMessageDialog(this, "Lecturer added successfully!");
            lecturerMap = lecturerDAO.getAllLecturersMap(); //cập nhật lại lecturerMap sau khi thêm
            displayLecturers(); // hiển thị lại sau khi thêm
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add lecturer");
        }
    }

    private void updateLecturer() {
        String name = nameField.getText();
        boolean gender = Boolean.parseBoolean(genderField.getText());
        Date dob = Date.valueOf(dobField.getText());
        String lecturerID = lecturerIDField.getText();

        Lecturer lecturer = new Lecturer(name, gender, dob, lecturerID);
        lecturerDAO.updateLecturer(lecturer);
        JOptionPane.showMessageDialog(this, "Lecturer updated successfully!");
        lecturerMap = lecturerDAO.getAllLecturersMap(); //cập nhật lại lecturerMap sau khi cập nhật thông tin
        displayLecturers(); // hiển thị lại sau khi update
    }

    private void deleteLecturer() {
        String lecturerID = lecturerIDField.getText();
        Lecturer lecturer = lecturerDAO.searchLecturerByLecturerID(lecturerID);

        if (lecturer != null) {
            lecturerDAO.deleteLecturer(lecturer.getLecturerID());
            JOptionPane.showMessageDialog(this, "Lecturer deleted successfully!");
            lecturerMap = lecturerDAO.getAllLecturersMap(); //cập nhật lại lecturerMap sau khi xóa
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
            JOptionPane.showMessageDialog(this, "Lecturer not found.");
        }
    }

    private void displayLecturers() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Gender");
        model.addColumn("Date of Birth");
        model.addColumn("Lecturer ID");

        List<Lecturer> lecturers = lecturerDAO.getAllLecturers();

        for (Lecturer lecturer : lecturers) {
            Object[] rowData = {lecturer.getName(), (lecturer.getGender() ? "Male" : "Female"), lecturer.getDOB(), lecturer.getLecturerID()};
            model.addRow(rowData);
        }

        lecturerTable.setModel(model); // đặt mô hình dữ liệu cho JTable
    }
    
    private void refreshLecturerTable() {
        lecturerMap = lecturerDAO.getAllLecturersMap(); // cập nhật lại lecturerMap
        displayLecturers(); //hiển thị lại bảng
    }
    
    private void openLecturerInfoWindow() {
        LecturerInfoView lecturerInfoView = new LecturerInfoView();
        lecturerInfoView.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Lecturer Management");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setContentPane(new LecturerView());
                frame.setVisible(true);
            }
        });
    }
}