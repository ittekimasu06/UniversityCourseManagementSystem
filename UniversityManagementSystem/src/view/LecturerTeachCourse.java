package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controller.DAO;
import model.Teach;

public class LecturerTeachCourse extends JFrame {
    private JComboBox<String> lecturerIDComboBox;
    private JComboBox<String> courseIDComboBox;
    private JTable teachTable;
    private DAO dao;

    public LecturerTeachCourse() {
        dao = new DAO();
        setTitle("Assign Lecture to Course");
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

        formPanel.add(new JLabel("Lecturer ID:"));
        lecturerIDComboBox = new JComboBox<>();
        populateLecturerIDComboBox();
        formPanel.add(lecturerIDComboBox);

        formPanel.add(new JLabel("Course ID:"));
        courseIDComboBox = new JComboBox<>();
        populateCourseIDComboBox();
        formPanel.add(courseIDComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton assignButton = new JButton("Assign");
        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignLecturerToCourse();
            }
        });
        buttonPanel.add(assignButton);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new MainMenu().setVisible(true);
			}
		});
        buttonPanel.add(backButton);

        leftPanel.add(formPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        teachTable = new JTable();
        mainPanel.add(new JScrollPane(teachTable), BorderLayout.CENTER);

        add(mainPanel);
        displayTeaches();
    }

    private void populateLecturerIDComboBox() {
        List<String> lecturerIDs = dao.getAllLecturerIDs();
        for (String id : lecturerIDs) {
            lecturerIDComboBox.addItem(id);
        }
    }

    private void populateCourseIDComboBox() {
        List<String> courseIDs = dao.getAllCourseIDs();
        for (String id : courseIDs) {
            courseIDComboBox.addItem(id);
        }
    }

    private void assignLecturerToCourse() {
        String lecturerID = (String) lecturerIDComboBox.getSelectedItem();
        String courseID = (String) courseIDComboBox.getSelectedItem();

        boolean success = dao.addTeach(lecturerID, courseID);
        if (success) {
            JOptionPane.showMessageDialog(this, "Lecturer assigned to course successfully!");
            displayTeaches();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to assign lecturer to course. Please try again.");
        }
    }

    private void displayTeaches() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Lecturer ID");
        model.addColumn("Course ID");

        List<Teach> teaches = dao.getAllTeach();
        for (Teach teach : teaches) {
            Object[] rowData = {teach.getLectureID(), teach.getCourseID()};
            model.addRow(rowData);
        }

        teachTable.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LecturerTeachCourse().setVisible(true);
            }
        });
    }
}
