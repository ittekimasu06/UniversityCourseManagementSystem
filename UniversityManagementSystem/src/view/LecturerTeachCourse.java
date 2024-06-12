package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controller.LecturerDAO;
import controller.CourseDAO;
import controller.TeachDAO;
import model.Teach;

public class LecturerTeachCourse extends JPanel {
    private JComboBox<String> lecturerIDComboBox;
    private JComboBox<String> courseIDComboBox;
    private JTable teachTable;
    private LecturerDAO lecturerDAO;
    private CourseDAO courseDAO;
    private TeachDAO teachDAO;

    public LecturerTeachCourse() {
        lecturerDAO = new LecturerDAO();
        courseDAO = new CourseDAO();
        teachDAO = new TeachDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        teachTable = new JTable();
        add(new JScrollPane(teachTable), BorderLayout.CENTER);

        displayTeaches();
    }

    private void populateLecturerIDComboBox() {
        List<String> lecturerIDs = lecturerDAO.getAllLecturerIDs();
        for (String id : lecturerIDs) {
            lecturerIDComboBox.addItem(id);
        }
    }

    private void populateCourseIDComboBox() {
        List<String> courseIDs = courseDAO.getAllCourseIDs();
        for (String id : courseIDs) {
            courseIDComboBox.addItem(id);
        }
    }

    private void assignLecturerToCourse() {
        String lecturerID = (String) lecturerIDComboBox.getSelectedItem();
        String courseID = (String) courseIDComboBox.getSelectedItem();

        boolean success = teachDAO.addTeach(lecturerID, courseID);
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

        List<Teach> teaches = teachDAO.getAllTeach();
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
                JFrame frame = new JFrame("Assign Lecturer to Course");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setContentPane(new LecturerTeachCourse());
                frame.setVisible(true);
            }
        });
    }
}