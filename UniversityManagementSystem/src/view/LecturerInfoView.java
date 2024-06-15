package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controller.TeachDAO;
import controller.LecturerDAO;
import controller.CourseDAO;
import model.Teach;
import model.Lecturer;

public class LecturerInfoView extends JFrame {
	private static final long serialVersionUID = 773502527291055696L;
	private JComboBox<String> lecturerComboBox;
	private JTextArea lecturerDetailsArea;
	private JTable courseTable;
	private LecturerDAO lecturerDAO;
	private TeachDAO teachDAO;
	private CourseDAO courseDAO;
 
	public LecturerInfoView() {
		lecturerDAO = new LecturerDAO();
		teachDAO = new TeachDAO();
		courseDAO = new CourseDAO();

		setTitle("Lecturer Info");
		setSize(600, 400);
		setLayout(new GridLayout(1, 2));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());

		lecturerComboBox = new JComboBox<>();
		List<String> lecturerNames = lecturerDAO.getAllLecturerNames();
		for (String name : lecturerNames) {
			lecturerComboBox.addItem(name);
		}
		lecturerComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayLecturerInfo((String) lecturerComboBox.getSelectedItem());
			}
		});

		lecturerDetailsArea = new JTextArea();
		lecturerDetailsArea.setEditable(false);

		leftPanel.add(lecturerComboBox, BorderLayout.NORTH);
		leftPanel.add(new JScrollPane(lecturerDetailsArea), BorderLayout.CENTER);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		courseTable = new JTable(new DefaultTableModel(new Object[] { "Course Name", "Course ID" }, 0));

		rightPanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);

		add(leftPanel);
		add(rightPanel);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void displayLecturerInfo(String lecturerName) {
		if (lecturerName == null || lecturerName.isEmpty()) {
			return;
		}

		Lecturer lecturer = lecturerDAO.getAllLecturersMap().get(lecturerName);
		if (lecturer != null) {
			lecturerDetailsArea.setText(lecturer.view());
			updateCourseTable(lecturer.getLecturerID());
		} else {
			lecturerDetailsArea.setText("Lecturer not found");
			clearCourseTable();
		}
	}
	
	  private void updateCourseTable(String lecturerID) {
	        DefaultTableModel model = (DefaultTableModel) courseTable.getModel();
	        model.setRowCount(0); // clear dữ liệu

	        List<Teach> teaches = teachDAO.getAllTeach();
	        for (Teach teach : teaches) {
	        	String courseName = courseDAO.getCourseNameByID(teach.getCourseID());
	            if (teach.getLecturerID().equals(lecturerID)) {
	            	Object[] rowData = {courseName, teach.getCourseID()};
	            	model.addRow(rowData);
	            }
	        }
	    }
	  
	  private void clearCourseTable() {
	        DefaultTableModel model = (DefaultTableModel) courseTable.getModel();
	        model.setRowCount(0);
	    }

}
