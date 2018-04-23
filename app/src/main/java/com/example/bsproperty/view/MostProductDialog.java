package com.example.bsproperty.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.ProductBean;
import com.example.bsproperty.bean.ShopBean;
import com.example.bsproperty.utils.DenstityUtils;

/**
 * Created by yezi on 2018/1/27.
 */

public class MostProductDialog extends Dialog {

    private ProductBean productBean;
    private OnViewClickListener onViewClickListener;

    public MostProductDialog(Context context, final ProductBean productBean, final OnViewClickListener onViewClickListener) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.dialog_most);

        this.productBean = productBean;
        this.onViewClickListener = onViewClickListener;

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DenstityUtils.screenWidth((Activity) context) - DenstityUtils.dp2px(context, 80);
        window.setAttributes(params);

        initView();
    }

    public interface OnViewClickListener {

        void onChangeClick(ProductBean productBean);
    }

    public void initView() {
        ((TextView) findViewById(R.id.tv_name)).setText(productBean.getName());
        ((TextView) findViewById(R.id.tv_top)).setText(productBean.getName().substring(0, 1));
        ((TextView) findViewById(R.id.tv_price)).setText("￥" + productBean.getPrice());
        ((TextView) findViewById(R.id.tv_info)).setText(productBean.getInfo());
        ((TextView) findViewById(R.id.tv_type)).setText(productBean.getType());
        ((TextView) findViewById(R.id.tv_feel)).setText(productBean.getFeel());

        ((TextView) findViewById(R.id.tv_tip)).setText("你一共预定了" + productBean.getAddSum() + "次");
        findViewById(R.id.ll_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onViewClickListener.onChangeClick(productBean);
            }
        });
    }

}
