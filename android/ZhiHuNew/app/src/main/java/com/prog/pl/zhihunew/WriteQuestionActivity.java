package com.prog.pl.zhihunew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.prog.pl.utils.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

public class WriteQuestionActivity extends AppCompatActivity {
   private EditText quesEdit,topicEdit,supplementEdit;
    private ImageButton cancelImgbtn,submitImgbtn;
    private String responseMsg = "";
    private ProgressDialog mDialog;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_write_question);
        Bundle extras = getIntent().getExtras();
        phone=extras.getString("phone");
        quesEdit=(EditText)findViewById(R.id.edit_writequestion_ques);
        topicEdit=(EditText)findViewById(R.id.edit_writequestion_topic);
        supplementEdit=(EditText)findViewById(R.id.edit_writequestion_supplement);
        cancelImgbtn=(ImageButton)findViewById(R.id.img_writequestion_cancel);
        submitImgbtn=(ImageButton)findViewById(R.id.img_writequestion_submit);
        cancelImgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        submitImgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quesEdit.getText().toString().trim().length()!=0&&topicEdit.getText().toString().trim().length()!=0)
                {
                    mDialog = new ProgressDialog(WriteQuestionActivity.this);
                    mDialog.setTitle("提交问题");
                    mDialog.setMessage("正在提交问题，请稍后...");
                    mDialog.show();
                    Thread subquestionThread = new Thread(new subquestionThread());
                    subquestionThread.start();
                }
                else{
                    Toast.makeText(getApplicationContext(), "问题或者话题不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean contatctServer(String question,String topic, String supplement,String phone) {
        boolean contactValidate = false;
        // 使用apache HTTP客户端实现
        String urlStr = HttpUtil.BASE_URL + "zonglan.do";
        HttpPost request = new HttpPost(urlStr);
        // 如果传递参数多的话，可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 添加用户名和密码
        params.add(new BasicNameValuePair("question", question));
        params.add(new BasicNameValuePair("topic", topic));
        params.add(new BasicNameValuePair("supplement", supplement));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("action", "aubmitquestion"));
        try {
            // 设置请求参数项
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            responseMsg = new String(HttpUtil.queryStringForPost(request).getBytes("ISO-8859-1"),"utf-8") ;
            if (!responseMsg.equals("No Response"))
                contactValidate = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contactValidate;
    }


    // Handler
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mDialog.cancel();
                    String rrString = responseMsg;
                    Toast.makeText(getApplicationContext(), "提交成功",
                            Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(WriteQuestionActivity.this,ShowQuestion.class);
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "话题不存在",
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
    class subquestionThread implements Runnable {

        @Override
        public void run() {

            String quesiton = quesEdit.getText().toString();
            String topic = topicEdit.getText().toString();
            String supplement=supplementEdit.getText().toString();

            // URL合法，但是这一步并不验证密码是否正确
            boolean loginValidate = contatctServer(quesiton,topic,supplement,phone);

            Message msg = handler.obtainMessage();
            if (loginValidate) {
                String jjString = responseMsg;
                if (jjString.equals("submit question success")) {
                    msg.what = 0;
                    handler.sendMessage(msg);
                } else {
                    if (responseMsg.equals("topic is not exist")) {
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
