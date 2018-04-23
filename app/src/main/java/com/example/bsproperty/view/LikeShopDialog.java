package com.example.bsproperty.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.ShopBean;
import com.example.bsproperty.ui.MerchantDetailActivity;
import com.example.bsproperty.utils.DenstityUtils;

/**
 * Created by yezi on 2018/1/27.
 */

public class LikeShopDialog extends Dialog {

    private ShopBean shopBean;
    private OnViewClickListener onViewClickListener;

    public LikeShopDialog(Context context, final ShopBean shopBean, final OnViewClickListener onViewClickListener) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.dialog_like);

        this.shopBean = shopBean;
        this.onViewClickListener = onViewClickListener;

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DenstityUtils.screenWidth((Activity) context) - DenstityUtils.dp2px(context, 80);
        window.setAttributes(params);

        initView();
    }

    public interface OnViewClickListener {
        void onItemClick(ShopBean shopBean);

        void onChangeClick();
    }

    public void initView() {
        ((TextView) findViewById(R.id.tv_name)).setText(shopBean.getName());
        ((TextView) findViewById(R.id.tv_top)).setText(shopBean.getName().substring(0, 1));
        ((TextView) findViewById(R.id.tv_msg)).setText(shopBean.getInfo());
        ((TextView) findViewById(R.id.tv_addr)).setText("地址：" + shopBean.getAddr());
        ((TextView) findViewById(R.id.tv_tel)).setText("tel：" + shopBean.getTel());
        findViewById(R.id.rl_shop_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onViewClickListener.onItemClick(shopBean);
            }
        });
        ((TextView) findViewById(R.id.tv_tip)).setText("更多'" +
                MyApplication.getInstance().getUserBean().getAddr()
                + "'的用户选择该商家");
        findViewById(R.id.ll_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClickListener.onChangeClick();
            }
        });
    }

    public void changeItem(ShopBean shopBean) {
        this.shopBean = shopBean;
        initView();
    }
}
