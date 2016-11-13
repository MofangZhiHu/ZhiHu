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

import com.bean.Question;

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
	//查询问题list
	public List<Question> questioSearch(String sql)
	{
		List<Question> questionlist=new ArrayList<Question>();
		DbConnection dbHandle = new DbConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbHandle.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();// 执行查询
			 while(rs.next()){//判断是否还有下一行  
				    Question que=new Question();
		            que.setTopic(rs.getString("TopicName"));
		            que.setQuestion(rs.getString("QuestionDescription"));
		            String ss=rs.getString("AnswerContent");
		            if(ss!=null){
		            	if(ss.length()>80)
		            	ss=ss.substring(0, 80);
		            }
		            que.setAnswer(ss);
		            que.setQuestionuser(rs.getString("UserName"));
		            que.setSupport(0);
		            que.setComment(0);
		            questionlist.add(que);
		        }  
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbHandle.closeConnection();
		}
		return questionlist;
	}
	//获取topic的编号
		public int gettopicID(String sql)
		{
			int topicID=-1;
			DbConnection dbHandle = new DbConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = dbHandle.getConnection().prepareStatement(sql);
				rs = ps.executeQuery();// 执行查询
				 while(rs.next()){//判断是否还有下一行  
					   topicID=Integer.parseInt(rs.getString("TopicID"));
			        }  
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbHandle.closeConnection();
			}
			return topicID;
		}
		//获取用户的编号
				public int getuserID(String sql)
				{
					int userID=-1;
					DbConnection dbHandle = new DbConnection();
					PreparedStatement ps = null;
					ResultSet rs = null;
					try {
						ps = dbHandle.getConnection().prepareStatement(sql);
						rs = ps.executeQuery();// 执行查询
						 while(rs.next()){//判断是否还有下一行  
							 userID=Integer.parseInt(rs.getString("UserID"));
					        }  
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						dbHandle.closeConnection();
					}
					return userID;
				}
}
