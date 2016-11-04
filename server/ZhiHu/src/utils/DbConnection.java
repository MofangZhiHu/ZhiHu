package utils;
/*
 * 连接sql数据库
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	// 连接
	private Connection conn = null;

	// 静态块
	static {
		// 实例化驱动类
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL SERVER驱动装载失败");
			e.printStackTrace();
		}
	}

	// 取得数据库连接
	public Connection getConnection() {
		String url = "jdbc:sqlserver://localhost:1433;databaseName=ZhiHuNewDb";
		String userName = "pengli";
		String userPwd = "penli2008";

		try {
			conn = DriverManager.getConnection(url, userName, userPwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("无法取得数据库连接");
			e.printStackTrace();
		}

		return conn;
	}

	// 关闭连接
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("无法关闭数据库联接");
			e.printStackTrace();
		}
	}
}