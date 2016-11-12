package com.prog.pl.zhihunew;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.prog.pl.fragment.GengDuoFragment;
import com.prog.pl.fragment.LiuLanFragment;
import com.prog.pl.fragment.SiXinFragment;
import com.prog.pl.fragment.TongZhiFragment;
import com.prog.pl.fragment.ZongLanFragment;
import com.prog.pl.services.AddBtnService;

public class MainNaviActivity extends Activity implements OnClickListener
{
    private LinearLayout linearZonglan;
    private LinearLayout linearLiulan;
    private LinearLayout linearTongzhi;
    private LinearLayout linearSixin;
    private LinearLayout linearGengduo;

    private ZongLanFragment fragZonglan;
    private LiuLanFragment fragLiulan;
    private TongZhiFragment fragTongzhi;
    private SiXinFragment fragSixin;
    private GengDuoFragment fragGengduo;

    private ImageView imgZonglan,imgLiulan,imgTongzhi,imgSixin,imgGengduo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_navi);

        imgZonglan=(ImageView) findViewById(R.id.img_bottombar_zonglan);
        imgLiulan=(ImageView)findViewById(R.id.img_bottombar_liulan);
        imgTongzhi=(ImageView) findViewById(R.id.img_bottombar_tongzhi);
        imgSixin=(ImageView) findViewById(R.id.img_bottombar_sixin);
        imgGengduo=(ImageView)findViewById(R.id.img_bottombar_gengduo);

        // 初始化控件和声明事件
        linearZonglan = (LinearLayout) findViewById(R.id.linear_bottombar_zonglan);
        linearLiulan = (LinearLayout) findViewById(R.id.linear_bottombar_liulan);
        linearTongzhi = (LinearLayout) findViewById(R.id.linear_bottombar_tongzhi);
        linearSixin = (LinearLayout) findViewById(R.id.linear_bottombar_sixin);
        linearGengduo = (LinearLayout) findViewById(R.id.linear_bottombar_gengduo);

        linearZonglan.setOnClickListener(this);
        linearLiulan.setOnClickListener(this);
        linearTongzhi.setOnClickListener(this);
        linearSixin.setOnClickListener(this);
        linearGengduo.setOnClickListener(this);

        imgZonglan.setBackgroundResource(R.drawable.zonglan_blue);
        // 设置默认的Fragment
        setDefaultFragment();
    }

    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragZonglan = new ZongLanFragment();
        transaction.replace(R.id.id_content, fragZonglan);
        transaction.commit();
    }

    @Override
    public void onClick(View v)
    {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId())
        {
            case R.id.linear_bottombar_zonglan:
                if (fragZonglan == null)
                {
                    fragZonglan = new ZongLanFragment();
                }
                imgZonglan.setBackgroundResource(R.drawable.zonglan_blue);
                imgLiulan.setBackgroundResource(R.drawable.liulan_gray);
                imgTongzhi.setBackgroundResource(R.drawable.tongzhi_gray);
                imgSixin.setBackgroundResource(R.drawable.sixin_gray);
                imgGengduo.setBackgroundResource(R.drawable.gengduo_gray);
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.id_content, fragZonglan);
                break;
            case R.id.linear_bottombar_liulan:
                if (fragLiulan == null)
                {
                    fragLiulan = new LiuLanFragment();
                }
                imgZonglan.setBackgroundResource(R.drawable.zonglan_gray);
                imgLiulan.setBackgroundResource(R.drawable.liulan_blue);
                imgTongzhi.setBackgroundResource(R.drawable.tongzhi_gray);
                imgSixin.setBackgroundResource(R.drawable.sixin_gray);
                imgGengduo.setBackgroundResource(R.drawable.gengduo_gray);
                transaction.replace(R.id.id_content, fragLiulan);
                break;
            case R.id.linear_bottombar_tongzhi:
                if (fragTongzhi == null)
                {
                    fragTongzhi = new TongZhiFragment();
                }
                imgZonglan.setBackgroundResource(R.drawable.zonglan_gray);
                imgLiulan.setBackgroundResource(R.drawable.liulan_gray);
                imgTongzhi.setBackgroundResource(R.drawable.tongzhi_blue);
                imgSixin.setBackgroundResource(R.drawable.sixin_gray);
                imgGengduo.setBackgroundResource(R.drawable.gengduo_gray);
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.id_content, fragTongzhi);
                break;
            case R.id.linear_bottombar_sixin:
                if (fragSixin== null)
                {
                    fragSixin = new SiXinFragment();
                }
                imgZonglan.setBackgroundResource(R.drawable.zonglan_gray);
                imgLiulan.setBackgroundResource(R.drawable.liulan_gray);
                imgTongzhi.setBackgroundResource(R.drawable.tongzhi_gray);
                imgSixin.setBackgroundResource(R.drawable.sixin_blue);
                imgGengduo.setBackgroundResource(R.drawable.gengduo_gray);
                transaction.replace(R.id.id_content, fragSixin);
                break;
            case R.id.linear_bottombar_gengduo:
                if (fragGengduo== null)
                {
                    fragGengduo = new GengDuoFragment();
                }
                imgZonglan.setBackgroundResource(R.drawable.zonglan_gray);
                imgLiulan.setBackgroundResource(R.drawable.liulan_gray);
                imgTongzhi.setBackgroundResource(R.drawable.tongzhi_gray);
                imgSixin.setBackgroundResource(R.drawable.sixin_gray);
                imgGengduo.setBackgroundResource(R.drawable.gengduo_blue);
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.id_content, fragGengduo);
                break;
        }
        // transaction.addToBackStack();
        // 事务提交
        transaction.commit();
    }

}