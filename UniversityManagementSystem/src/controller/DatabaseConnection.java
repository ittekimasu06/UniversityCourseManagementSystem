package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	 	private static final String JDBCURL = "jdbc:mysql://127.0.0.1:3306/universitydb";
	    private static final String DBUSER = "root";
	    private static final String DBPASSWORD = "@orenosql0604";
	    private static Connection connection;

	    static {
	        try {
	            connection = DriverManager.getConnection(JDBCURL, DBUSER, DBPASSWORD);
	            Class.forName("com.mysql.cj.jdbc.Driver");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public static Connection getConnection() {
	        return connection;
	    }
}


