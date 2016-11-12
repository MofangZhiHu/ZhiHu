package com.prog.pl.fragment;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prog.pl.adapter.QuestionAdapter;
import com.prog.pl.bean.Question;
import com.prog.pl.services.AddBtnService;
import com.prog.pl.utils.HttpUtil;
import com.prog.pl.zhihunew.LoginActivity;
import com.prog.pl.zhihunew.MainNaviActivity;
import com.prog.pl.zhihunew.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class ZongLanFragment extends Fragment
{
    Boolean fabOpened=false;
    private QuestionAdapter listAdapterQue;
    private LayoutInflater layoutInflater;
    private Context context;
    private View thisView;
    private ListView listView;  //声明一个ListView对象
    private List<Question> mlistQue = new ArrayList<Question>();  //声明一个list，动态存储要显示的信息
    private String responseMsg = "";
    private FloatingActionButton addFab;
    private TextView fuzzyText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_zonglan, container, false);
        listView=(ListView)view.findViewById(R.id.list_question);
        Thread zonglanThread = new Thread(new ZonglanThread());
        zonglanThread.start();
        context=this.context;
        listAdapterQue = new QuestionAdapter(getActivity(), mlistQue);
        listView.setAdapter(listAdapterQue);
        mlistQue.clear();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addFab = (FloatingActionButton) getActivity().findViewById(R.id.imgbtn_zonglan_add);
        fuzzyText=(TextView) getActivity().findViewById(R.id.text_zonglan_fuzzy);
        assert addFab != null;
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!fabOpened)
               {
                   openMenu(v);
               }
               else{
                    closeMenu(v);
                }
            }
        });
        fuzzyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu(addFab);
            }
        });
    }

    public void openMenu(View view)
    {
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",0,-155,-135);
        animator.setDuration(500);
        animator.start();
        fuzzyText.setVisibility(view.VISIBLE);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,0.7f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        fuzzyText.startAnimation(alphaAnimation);
        fabOpened=true;
    }
    public void closeMenu(View view)
    {
        ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotation",-135,20,0);
        animator.setDuration(500);
        animator.start();
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.7f,0);
        alphaAnimation.setDuration(500);
        fuzzyText.startAnimation(alphaAnimation);
        fuzzyText.setVisibility(view.GONE);
        fabOpened=false;
    }
    private boolean loginServer() {
        boolean loginValidate = false;
        // 使用apache HTTP客户端实现
        String urlStr = HttpUtil.BASE_URL + "zonglan.do";
        HttpPost request = new HttpPost(urlStr);
        // 如果传递参数多的话，可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "remenzonglan"));
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
                    setListViewData(responseMsg);
                    listAdapterQue = new QuestionAdapter(
                            getActivity(), mlistQue);
                    listView.setAdapter(listAdapterQue);
                break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;

            }

        }
    };

    // ZonglanThread线程类
    class ZonglanThread implements Runnable {

        @Override
        public void run() {
            // URL合法，但是这一步并不验证密码是否正确
            boolean loginValidate = loginServer();

            Message msg = handler.obtainMessage();
            if (loginValidate) {
                String jjString = responseMsg;
                if (jjString.equals("fail")) {
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else {
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            } else {
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }
    }
    public void setListViewData(String jsonString) {
        try
        {
            JSONObject jsonObject =new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("QuestinSeaResult");
            for (int i = 0; i < jsonArray.length(); i++) {
                Question que = new Question();
                JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
                que.setAnswer(jsonObject2.get("answer").toString());
                que.setComment(Integer.parseInt(jsonObject2.get("comment").toString()));
                que.setQuestion(jsonObject2.get("question").toString());
                que.setQuestionuser(jsonObject2.get("questionuser").toString());
                que.setSupport(Integer.parseInt(jsonObject2.get("support").toString()));
                que.setTopic(jsonObject2.get("topic").toString());
                mlistQue.add(que);
            }
        }
        catch (Exception e){

        }
    }

}