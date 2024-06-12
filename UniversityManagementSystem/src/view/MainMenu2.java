package view;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu2 extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainMenu2() {
        setTitle("University Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo các lựa chọn cho menu bên trái
        String[] menuItems = {"Student", "Lecturer", "Student Enrollment", "Assign Lecturer", "Course"};
        JList<String> menuList = new JList<>(menuItems);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setSelectedIndex(0);

        // Tạo panel chứa nội dung bên phải và sử dụng CardLayout để quản lý các panel khác nhau
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(new StudentView(), "Student");
        contentPanel.add(new LecturerView(), "Lecturer");
        contentPanel.add(new StudentEnrollCourse(), "Student Enrollment");
        contentPanel.add(new LecturerTeachCourse(), "Assign Lecturer");
        contentPanel.add(new CourseView(), "Course");

        // Thêm ListSelectionListener cho menuList để thay đổi panel khi người dùng chọn một mục
        menuList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedMenuItem = menuList.getSelectedValue();
                    cardLayout.show(contentPanel, selectedMenuItem);
                }
            }
        });

        // Tạo JSplitPane để tách màn hình thành hai phần
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(menuList), contentPanel);
        splitPane.setDividerLocation(200);

        // Thêm JSplitPane vào JFrame
        add(splitPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu2().setVisible(true);
            }
        });
    }
}