package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("University Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton studentButton = new JButton("Student");
        studentButton.setPreferredSize(new Dimension(150, 30));
        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
                new StudentView().setVisible(true);
            }
        });

        JButton lecturerButton = new JButton("Lecturer");
        lecturerButton.setPreferredSize(new Dimension(150, 30));
        lecturerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
                new LecturerView().setVisible(true);
            }
        });

        JButton studentEnrollButton = new JButton("Student Enrollment");
        studentEnrollButton.setPreferredSize(new Dimension(150, 30));
        studentEnrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
                new StudentEnrollCourse().setVisible(true);
            }
        });

        JButton assignLecturerButton = new JButton("Assign Lecturer");
        assignLecturerButton.setPreferredSize(new Dimension(150, 30));
        assignLecturerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
                new LecturerTeachCourse().setVisible(true); 
            }
        });

        JButton courseButton = new JButton("Course");
        courseButton.setPreferredSize(new Dimension(150, 30));
        courseButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		new CourseView().setVisible(true);
        	}
        });
        
        panel.add(studentButton);
        panel.add(lecturerButton);
        panel.add(studentEnrollButton);
        panel.add(assignLecturerButton);
        panel.add(courseButton);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }
}
