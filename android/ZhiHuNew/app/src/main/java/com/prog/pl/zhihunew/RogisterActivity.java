package com.prog.pl.zhihunew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.prog.pl.bean.User;
import com.prog.pl.utils.GlobalVarApplication;
import com.prog.pl.utils.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RogisterActivity extends ActionBarActivity {
    private Button btnRogister;
    private EditText txtPhone, txtPassword,txtName;
    private TextView txtLogin;
    private String responseMsg = "";
    private ProgressDialog mDialog;
    /**
     * mob.com创建应用时候生成APP_KEY
     */
    private static String APP_KEY = "18118704a6c11";
    /**
     * 　APP_SECRET
     */
    private static String APP_SECRET = "76ab30f65c0e881a9a22e921264fe50a";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rogister);
        SMSSDK.initSDK(this, APP_KEY, APP_SECRET);//初始化短信验证工具
        btnRogister= (Button) findViewById(R.id.btn_rogister_rog);
        txtPhone = (EditText) findViewById(R.id.edit_rogister_phone);
        txtPassword = (EditText) findViewById(R.id.edit_rogister_password);
        txtName=(EditText)findViewById(R.id.edit_rogister_name);
        txtLogin=(TextView)findViewById(R.id.text_rogister_login);

        btnRogister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDialog = new ProgressDialog(RogisterActivity.this);
                mDialog.setTitle("登陆");
                mDialog.setMessage("正在登陆服务器，请稍后...");
                mDialog.show();
                Thread rogisterThread = new Thread(new RogisterThread());
                rogisterThread.start();
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(RogisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        EventHandler eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        if(result == SMSSDK.RESULT_COMPLETE) {
                            boolean smart = (Boolean)data;
                            if(smart) {
                                //通过智能验证
                            } else {
                                //依然走短信验证
                            }
                        }
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调


    }
    private boolean contactServer(String phone, String password,String name) {
        boolean loginValidate = false;
        // 使用apache HTTP客户端实现
        String urlStr = HttpUtil.BASE_URL + "login.do";
        HttpPost request = new HttpPost(urlStr);
        // 如果传递参数多的话，可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 添加用户名和密码
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("name",name));
        params.add(new BasicNameValuePair("action", "phoneverify"));
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
                   // Toast.makeText(getApplicationContext(), "注册成功！",
                      //      Toast.LENGTH_SHORT).show();
                    User auser=new User();
                    auser.setName(txtName.getText().toString());
                    auser.setPassword(txtPassword.getText().toString());
                    auser.setPhone(txtPhone.getText().toString());
                    //5>验证成功，显示成功
                            String number = txtPhone.getText().toString().trim();
                            if(number.length()==0){
                                Toast.makeText(getApplicationContext(), "请输入手机号",
                                     Toast.LENGTH_SHORT).show();
                            }else {
                                if(number.length()!=11){
                                    Toast.makeText(getApplicationContext(), "请输入正确的手机号",
                                            Toast.LENGTH_SHORT).show();
                                }else {
                                  //  new TimerUtil(mButton,1000*60,1000).start();
                                    SMSSDK.getVerificationCode("86",number);
                                 //   SMSSDK.unregisterAllEventHandler();
                                    Intent intent=new Intent(RogisterActivity.this,MessageVerifyActivity.class);
                                    intent.putExtra("phone", txtPhone.getText().toString());
                                    intent.putExtra("password", txtPassword.getText().toString());
                                    intent.putExtra("name", txtName.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                            }
                    break;
                case 1:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "该账户已经存在",
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

    // RogisterThread线程类
    class RogisterThread implements Runnable {

        @Override
        public void run() {

            String phone = txtPhone.getText().toString();
            String password = txtPassword.getText().toString();
            String name=txtName.getText().toString();
            // Log.i("Assert", "username=" + username + ":password=" + password);

            // URL合法，但是这一步并不验证密码是否正确
            boolean loginValidate = contactServer(phone, password,name);

            Message msg = handler.obtainMessage();
            if (loginValidate) {
                String jjString = responseMsg;
                if (jjString.equals("this phone can be rogistered")) {
                    msg.what = 0;
                    handler.sendMessage(msg);
                } else {
                    if (responseMsg.equals("this phone was rogistered")) {
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
