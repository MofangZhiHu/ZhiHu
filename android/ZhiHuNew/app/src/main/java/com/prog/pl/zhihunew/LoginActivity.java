package com.prog.pl.zhihunew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.prog.pl.utils.HttpUtil;

public class LoginActivity extends ActionBarActivity {
    private Button btnLogin;
    private EditText txtPhoneormail, txtPassword;
    private TextView txtRogister;
    private String responseMsg = "";
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btn_login_log);
        txtPhoneormail = (EditText) findViewById(R.id.edit_login_phoneormail);
        txtPassword = (EditText) findViewById(R.id.edit_login_password);
        txtRogister=(TextView)findViewById(R.id.text_login_rogister);
        btnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setTitle("登陆");
                mDialog.setMessage("正在登陆服务器，请稍后...");
                mDialog.show();
                Thread usloginThread = new Thread(new LoginThread());
                usloginThread.start();
            }
        });
        txtRogister.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                      startActivity(new Intent(LoginActivity.this,RogisterActivity.class));
                finish();
            }
        });
    }
    private boolean loginServer(String phoneormail, String password) {
        boolean loginValidate = false;
        // 使用apache HTTP客户端实现
        String urlStr = HttpUtil.BASE_URL + "login.do";
        HttpPost request = new HttpPost(urlStr);
        // 如果传递参数多的话，可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 添加用户名和密码
        params.add(new BasicNameValuePair("phoneormail", phoneormail));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("action", "login"));
        try {
            // 设置请求参数项
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            responseMsg = new String(HttpUtil.queryStringForPost(request).getBytes("ISO-8859-1"),"utf-8") ;
            if (!responseMsg.equals("No Response"))
                loginValidate = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loginValidate;
    }


    // Handler
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mDialog.cancel();
                    String rrString = responseMsg;
                    Toast.makeText(getApplicationContext(), "登录成功！",
                            Toast.LENGTH_SHORT).show();
                   Intent intent=new Intent(LoginActivity.this,MainNaviActivity.class);
                    intent.putExtra("phone", txtPhoneormail.getText().toString());
                   startActivity(intent);
                    finish();
                    break;
                case 1:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "密码错误",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "未知错误",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "URL验证失败",
                            Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    };

    // LoginThread线程类
    class LoginThread implements Runnable {

        @Override
        public void run() {

            String phoneormail = txtPhoneormail.getText().toString();
            String password = txtPassword.getText().toString();
            // Log.i("Assert", "username=" + username + ":password=" + password);

            // URL合法，但是这一步并不验证密码是否正确
            boolean loginValidate = loginServer(phoneormail, password);

            Message msg = handler.obtainMessage();
            if (loginValidate) {
                String jjString = responseMsg;
                if (jjString.equals("success")) {
                    msg.what = 0;
                    handler.sendMessage(msg);
                } else {
                    if (responseMsg.equals("Login failed, please re-enter!")) {
                        msg.what = 1;
                        handler.sendMessage(msg);
                    } else {
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                }
            } else {
                msg.what = 3;
                handler.sendMessage(msg);
            }
        }
    }
}
