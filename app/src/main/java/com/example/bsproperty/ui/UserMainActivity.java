package com.example.bsproperty.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.ShopBean;
import com.example.bsproperty.bean.ShopObjBean;
import com.example.bsproperty.eventbus.LoginEvent;
import com.example.bsproperty.fragment.UserFragment01;
import com.example.bsproperty.fragment.UserFragment02;
import com.example.bsproperty.fragment.UserFragment03;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.utils.SpUtils;
import com.example.bsproperty.view.LikeShopDialog;
import com.example.bsproperty.view.ViewDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;

public class UserMainActivity extends BaseActivity {


    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.tb_bottom)
    TabLayout tbBottom;


    private long backTime;
    private UserFragment01 fragment01;
    private UserFragment02 fragment02;
    private UserFragment03 fragment03;
    private ArrayList<Fragment> fragments;
    private MyFragmentPagerAdapter adapter;
    private String[] tabs = new String[]{
            "商家", "订单", "我的"
    };
    private LikeShopDialog likeDialog;

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        MyApplication.getInstance().setUserBean(SpUtils.getUserBean(this));

        fragment01 = new UserFragment01();
        fragment02 = new UserFragment02();
        fragment03 = new UserFragment03();
        fragments = new ArrayList<>();
        fragments.add(fragment01);
        fragments.add(fragment02);
        fragments.add(fragment03);


        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(adapter);

        tbBottom.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < fragments.size(); i++) {
            if (i == 0) {
                tbBottom.addTab(tbBottom.newTab().setText(tabs[i]), true);
            } else {
                tbBottom.addTab(tbBottom.newTab().setText(tabs[i]), false);
            }
        }
        tbBottom.setupWithViewPager(vpContent);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected int getRootViewId() {
        return R.layout.activity_user_main;
    }

    @Override
    protected void loadData() {
        OkHttpTools.sendGet(mContext, ApiManager.SHOP_LIKE)
                .addParams("addr", MyApplication.getInstance().getUserBean().getAddr())
                .build()
                .execute(new BaseCallBack<ShopObjBean>(mContext, ShopObjBean.class) {
                    @Override
                    public void onResponse(final ShopObjBean shopObjBean) {
                        if (shopObjBean.getData() != null) {
                            likeDialog = new LikeShopDialog(mContext, shopObjBean.getData(),
                                    new LikeShopDialog.OnViewClickListener() {
                                        @Override
                                        public void onItemClick(ShopBean shopBean) {
                                            Intent intent = new Intent(mContext, MerchantDetailActivity.class);
                                            intent.putExtra("data", shopBean);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onChangeClick() {
                                            changeLike();
                                        }
                                    });
                            likeDialog.show();
                        }
                    }
                });
    }

    private void changeLike() {
        OkHttpTools.sendGet(mContext, ApiManager.SHOP_LIKE)
                .addParams("addr", MyApplication.getInstance().getUserBean().getAddr())
                .build()
                .execute(new BaseCallBack<ShopObjBean>(mContext, ShopObjBean.class) {
                    @Override
                    public void onResponse(final ShopObjBean shopObjBean) {
                        likeDialog.changeItem(shopObjBean.getData());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backTime < 2000) {
            super.onBackPressed();
        } else {
            showToast(this, "再按一次，退出程序");
            backTime = System.currentTimeMillis();
        }
        backTime = System.currentTimeMillis();
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

}
