package com.example.bsproperty.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RgActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_user)
    EditText etUser;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_0)
    Button btn0;
    @BindView(R.id.tv_addr)
    TextView tvAddr;


    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_rg;
    }

    @Override
    protected void loadData() {
    }


    @OnClick({R.id.btn_0, R.id.rl_addr_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_addr_click:
                Intent intent = new Intent(mContext, AddrActivity.class);
                startActivityForResult(intent, 521);
                break;
            case R.id.btn_0:
                String user = etUser.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                String addr = tvAddr.getText().toString().trim();
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd) ||
                        TextUtils.isEmpty(addr)) {
                    showToast("请输入完整信息");
                    return;
                }
                OkHttpTools.sendPost(mContext, ApiManager.USER_RG)
                        .addParams("name", user)
                        .addParams("pwd", pwd)
                        .addParams("addr", addr)
                        .build()
                        .execute(new BaseCallBack<UserObjBean>(mContext, UserObjBean.class) {
                            @Override
                            public void onResponse(UserObjBean userObjBean) {
                                showToast("注册成功");
                                finish();
                            }
                        });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            tvAddr.setText(data.getStringExtra("addr"));
        }
    }
}
