package com.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.bean.Question;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import utils.DbHandle;
import utils.JsonHelper;;

public class ZongLanController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		// 处理登陆请求
		if (action.equals("remenzonglan")) {
			System.out.println("ddddddddddddd");
			String quetionSql = "select * from Question left join Topic on Question.TopicID=Topic.TopicID left join Answer on Answer.QuestionID=Question.QuestionID left join UserInfo on Question.UserID=UserInfo.UserID";
			DbHandle dbhan = new DbHandle();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			List<Question> queList=dbhan.questioSearch(quetionSql);
			if (queList==null) {

				out.print("fail");
			} else {
				JSONArray json = JSONArray.fromObject(queList);
				JSONObject jb = new JSONObject();
				jb.put("QuestinSeaResult", json);
				System.out.println(jb.toString());
				out.print(jb);
			}
		}
		if (action.equals("aubmitquestion")) {
			String question=request.getParameter("question").trim();
			String topic=request.getParameter("topic").trim();
			String supplement=request.getParameter("supplement").trim();
			String phone=request.getParameter("phone").trim();
			String gettopicIDSql = "select * from Topic where TopicName='"+topic+"'";
			String getuserIDSql="select * from UserInfo where UserPhoneNum='"+phone+"'";
			DbHandle dbhan = new DbHandle();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			int topicID=dbhan.gettopicID(gettopicIDSql);
			int userID=dbhan.getuserID(getuserIDSql);
			String insertSql="insert into Question (UserID,TopicID,QuestionDescription,FurtherExplanations,QuestionDate) values ("+userID+","+topicID+",'"+question+"','"+supplement+"',2016-05-05)";
			if(topicID==-1)
			{
				out.print("topic is not exist");
			}
			else
			{
				if(userID!=-1)
				{
					boolean result=dbhan.update(insertSql);
					if(result)
					out.print("submit question success");
				}
			}
		}
		return null;
	}

}
