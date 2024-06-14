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
	private static final long serialVersionUID = -5094750251777697767L;
	private JComboBox<String> lecturerNameComboBox;
    private JComboBox<String> courseNameComboBox;
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
        lecturerNameComboBox = new JComboBox<>();
        populateLecturerNameComboBox();
        formPanel.add(lecturerNameComboBox);

        formPanel.add(new JLabel("Course ID:"));
        courseNameComboBox = new JComboBox<>();
        populateCourseNameComboBox();
        formPanel.add(courseNameComboBox);

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
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTeachesTable();
            }
        });
        buttonPanel.add(refreshButton);

        leftPanel.add(formPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        teachTable = new JTable();
        add(new JScrollPane(teachTable), BorderLayout.CENTER);

        displayTeaches();
    }

    private void populateLecturerNameComboBox() {
        List<String> lecturerNames = lecturerDAO.getAllLecturerNames();
        for (String name : lecturerNames) {
            lecturerNameComboBox.addItem(name);
        }
    }

    private void populateCourseNameComboBox() {
        List<String> courseNames = courseDAO.getAllCourseNames();
        for (String name : courseNames) {
            courseNameComboBox.addItem(name);
        }
    }

    private void assignLecturerToCourse() {
        String lecturerName = (String) lecturerNameComboBox.getSelectedItem();
        String courseName = (String) courseNameComboBox.getSelectedItem();

        String lecturerID = lecturerDAO.getLecturerIDByName(lecturerName);
        String courseID = courseDAO.getCourseIDByName(courseName);
        
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
        model.addColumn("Lecturer Name");
        model.addColumn("Lecturer ID");
        model.addColumn("Course Name");
        model.addColumn("Course ID");

        List<Teach> teaches = teachDAO.getAllTeach();
        for (Teach teach : teaches) {
        	String lecturerName = lecturerDAO.getLecturerNameByID(teach.getLecturerID());
        	String courseName = courseDAO.getCourseNameByID(teach.getCourseID());
            Object[] rowData = {lecturerName, teach.getLecturerID(), courseName, teach.getCourseID()};
            model.addRow(rowData);
        }

        teachTable.setModel(model);
    }
    
    private void refreshTeachesTable() {
    	displayTeaches();
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