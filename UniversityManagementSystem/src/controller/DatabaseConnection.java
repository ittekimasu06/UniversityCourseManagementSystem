package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	 private static final String jdbcURL = "jdbc:mysql://127.0.0.1:3306/universitydb";
	    private static final String dbUser = "root";
	    private static final String dbPassword = "@orenosql0604";
	    private static Connection connection;

	    static {
	        try {
	            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
	            Class.forName("com.mysql.cj.jdbc.Driver");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public static Connection getConnection() {
	        return connection;
	    }
}


