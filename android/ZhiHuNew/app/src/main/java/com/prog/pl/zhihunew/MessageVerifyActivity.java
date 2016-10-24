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


public class MessageVerifyActivity extends ActionBarActivity {
    private Button btnVerify;
    private EditText txtVerCode;
    private TextView txtTime;
    private String responseMsg = "";
    private ProgressDialog mDialog;
    private int time = 60;
    private boolean flag = true;
    private String phone="";
    private String name="";
    private String password="";
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
        setContentView(R.layout.activity_message_verify);
//        SMSSDK.initSDK(this, APP_KEY, APP_SECRET);//初始化短信验证工具
        btnVerify=(Button)findViewById(R.id.btn_messageverify_verify);
        txtVerCode=(EditText)findViewById(R.id.edit_messageverify_verifycode);
        txtTime=(TextView)findViewById(R.id.text_messageverify_time);
        Bundle extras = getIntent().getExtras();
        phone = extras.getString("phone");
        password = extras.getString("password");
        name = extras.getString("name");
        btnVerify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String verifycode=txtVerCode.getText().toString().trim();
                if(verifycode.length()!=0){
                    if(verifycode.length()==4){
                        mDialog = new ProgressDialog(MessageVerifyActivity.this);
                        mDialog.setTitle("验证");
                        mDialog.setMessage("正在登陆服务器，请稍后...");
                        mDialog.show();
                        Thread verifyThread=new Thread(new VerifyThread());
                        verifyThread.start();
                    }else{
                        Toast.makeText(MessageVerifyActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                        txtVerCode.requestFocus();
                    }
                }else{
                    Toast.makeText(MessageVerifyActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    txtVerCode.requestFocus();
                }
            }
        });
    }

    private boolean contactServer(String phone, String password,String name,String code) {
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
        params.add(new BasicNameValuePair("code",code));
        params.add(new BasicNameValuePair("action", "rogister"));
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
                    Toast.makeText(getApplicationContext(), "注册成功！",
                          Toast.LENGTH_SHORT).show();
                    Intent logintent=new Intent(MessageVerifyActivity.this,LoginActivity.class);
                    startActivity(logintent);
                    finish();
                    break;
                case 1:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "注册失败",
                            Toast.LENGTH_SHORT).show();
                    Intent rogintent=new Intent(MessageVerifyActivity.this,RogisterActivity.class);
                    startActivity(rogintent);
                    finish();
                    break;
            }

        }
    };

    // 验证线程
    class VerifyThread implements Runnable {

        @Override
        public void run() {
            String code=txtVerCode.getText().toString();
            // URL合法，但是这一步并不验证密码是否正确
            boolean loginValidate = contactServer(phone, password,name,code);

            Message msg = handler.obtainMessage();
            if (loginValidate&&responseMsg.equals("rogister success")) {
                    msg.what = 0;
                    handler.sendMessage(msg);
            } else {
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }
    }
}
