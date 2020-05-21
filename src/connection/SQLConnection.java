package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnection {
	public static Connection getConnection() throws Exception {
		return getConnection("localhost", "javaproject", "javaproject");

	}

	/**
	 * Connecting oracleDB
	 * 
	 * @param host     ip adress
	 * @param user     account
	 * @param password password
	 * @return connection object
	 */
	public static Connection getConnection2(String host, String user, String password) {
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@" + host + ":1521:xe";
			con = DriverManager.getConnection(url, user, password);

			return con;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			return con;
		}
	}

	/**
	 * Connecting oracle db
	 * 
	 * @param host     IP
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(String host, String user, String password) throws Exception {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			throw new Exception("Fail to load JDBCDriver");
		}
		Connection con = null;

		String url = "jdbc:oracle:thin:@" + host + ":1521:xe";
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new Exception(user + " Account Connection Failed");
		}
		return con;
	}

	public static void close(Connection con, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
