package com.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import net.sf.json.JSONObject;
import utils.DbHandle;
import utils.GetMessageVerify;

/*
 * 处理登陆和注册请求
 */
public class LoginController extends AbstractController {
	String appkey = "18118704a6c11";
	String zone = "86";

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String action = request.getParameter("action");
		// 处理登陆请求
		if (action.equals("login")) {
			// 获取的号码
			String phoneormail = request.getParameter("phoneormail");
			// 获取的密码
			String password = request.getParameter("password");
			System.out.println("::::::::" + phoneormail + ":kkkk:" + password);
			// 验证
			String logindVerifySql = "select * from UserInfo where UserPhoneNum='" + phoneormail.trim()
					+ "' and PassWord='" + password.trim() + "'";
			DbHandle dbhan = new DbHandle();

			PrintWriter out = response.getWriter();
			if (dbhan.verify(logindVerifySql)) {

				out.print("success");
			} else {
				out.print("Login failed, please re-enter!");
			}
		}
		// 判断手机号是否已被注册
		if (action.equals("phoneverify")) {
			String phone = request.getParameter("phone");
			String phoneVerifySql = "select * from UserInfo where UserPhoneNum='" + phone.trim() + "'";
			DbHandle dbhan = new DbHandle();

			PrintWriter out = response.getWriter();
			if (dbhan.verify(phoneVerifySql)) {

				out.print("this phone was rogistered");
			} else {
				out.print("this phone can be rogistered");
			}
		}
		// 处理注册请求，验证收到的验证码是否正确
		// 若正确便存入数据库，即注册成功。
		if (action.equals("rogister")) {
			String phone = request.getParameter("phone");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String code = request.getParameter("code");
			GetMessageVerify verifyResult = new GetMessageVerify();
			String result = verifyResult.requestData("https://webapi.sms.mob.com/sms/verify",
					"appkey=" + appkey + "&phone=" + phone + "&zone=" + zone + "&&code=" + code + "");
			System.out.println(result);
			System.out.println("https://webapi.sms.mob.com/sms/verify"+"appkey=" + appkey + "&phone=" + phone + "&zone=" + zone + "&&code=" + code + "");
			JSONObject jsonObject = JSONObject.fromObject(result);
            String status = jsonObject.getString("status");
			//String status = jo.getString("status");
			System.out.println("status: " + status);
			if (status.equals("200")) {
				String addUserSql = "insert into UserInfo (UserName,PassWord,UserPhoneNum) values ('" + name + "','"
						+ password + "','" + phone + "')";
				System.out.println(addUserSql);
				DbHandle dbhan = new DbHandle();
				PrintWriter out = response.getWriter();
				if (dbhan.update(addUserSql)) {

					out.print("rogister success");
				} else {
					out.print("rogister failed, please re-enter!");
				}
			}
			else
			{
				PrintWriter out = response.getWriter();
				out.print("rogister failed, please re-enter!");
			}
		}
		return null;
	}

}
