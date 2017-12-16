package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
	private static Connection conn;

	public static Connection getConnection() throws SQLException {
		if (conn == null) {
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/workshop2?useSSL=false", "root",
					"coderslab");
		}
		return conn;
	}
}
