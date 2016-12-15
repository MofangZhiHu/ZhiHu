package utils;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.DbConnection;

public class DbHandle {
	public boolean verify(String sql) {
		boolean bol = false;
		DbConnection dbHandle = new DbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbHandle.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();// 执行查询
			if (rs.next()) {
				bol = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return bol;
		} finally {
			dbHandle.closeConnection();
		}
		return bol;
	}

	public boolean update(String sql) {
		boolean result = false;
		DbConnection dbHandle = new DbConnection();
		Statement stm = null;
		try {
			stm = dbHandle.getConnection().createStatement();
			stm.executeUpdate(sql);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
			return result;
		} finally {
			dbHandle.closeConnection();
		}
		return result;
	}
}
