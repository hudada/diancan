package com.example.bsproperty.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.ProductBean;
import com.example.bsproperty.bean.ProductObjBean;
import com.example.bsproperty.bean.ShopBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.view.MostProductDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MerchantDetailActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;

    private ArrayList<ProductBean> mdata = new ArrayList<>();
    private MyAdapter adapter;
    private ShopBean shopBean;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("店铺详情");

        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
        shopBean = (ShopBean) getIntent().getSerializableExtra("data");
        mdata = (ArrayList<ProductBean>) shopBean.getProductBeans();
        adapter = new MyAdapter(mContext, R.layout.item_shop, mdata);
        adapter.setmHeadView(R.layout.head_merchant_detail, new BaseAdapter.OnInitHead() {
            @Override
            public void onInitHeadData(View headView, Object o) {
                initHeadView(headView);
            }
        });
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("预定")
                        .setMessage("是否预定？")
                        .setNegativeButton("再看看", null)
                        .setPositiveButton("定了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createOrder(mdata.get(position));
                            }
                        }).show();
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setAdapter(adapter);
    }

    private void createOrder(ProductBean productBean) {
        OkHttpTools.sendPost(mContext, ApiManager.ORDER_ADD)
                .addParams("price", productBean.getPrice() + "")
                .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                .addParams("pid", productBean.getId() + "")
                .addParams("sid", shopBean.getId() + "")
                .addParams("addr", MyApplication.getInstance().getUserBean().getAddr())
                .addParams("feel", productBean.getFeel())
                .addParams("type", productBean.getType())
                .build()
                .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                    @Override
                    public void onResponse(BaseResponse baseResponse) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("预定成功")
                                .setMessage("请凭订单到商店取餐")
                                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    }
                });
    }

    private void initHeadView(View headView) {
        TextView tv_top = (TextView) headView.findViewById(R.id.tv_top);
        TextView tv_name = (TextView) headView.findViewById(R.id.tv_name);
        TextView tv_tel = (TextView) headView.findViewById(R.id.tv_tel);
        TextView tv_msg = (TextView) headView.findViewById(R.id.tv_msg);
        TextView tv_addr = (TextView) headView.findViewById(R.id.tv_addr);
        tv_top.setText(shopBean.getName().substring(0, 1));
        tv_name.setText(shopBean.getName());
        tv_tel.setText("tel：" + shopBean.getTel());
        tv_msg.setText(shopBean.getInfo());
        tv_addr.setText("地址：" + shopBean.getAddr());
        headView.findViewById(R.id.rl_addr_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setTitle("详细地址")
                        .setMessage(shopBean.getAddr())
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_merchant_detail;
    }

    @Override
    protected void loadData() {
        OkHttpTools.sendGet(mContext,ApiManager.SHOP_MORE)
                .addParams("uid",MyApplication.getInstance().getUserBean().getId()+"")
                .addParams("sid",shopBean.getId()+"")
                .build()
                .execute(new BaseCallBack<ProductObjBean>(mContext,ProductObjBean.class) {
                    @Override
                    public void onResponse(ProductObjBean productObjBean) {
                        if (productObjBean.getData() != null){
                            MostProductDialog dialog = new MostProductDialog(mContext, productObjBean.getData(),
                                    new MostProductDialog.OnViewClickListener() {
                                        @Override
                                        public void onChangeClick(final ProductBean productBean) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                            builder.setTitle("预定")
                                                    .setMessage("是否预定？")
                                                    .setNegativeButton("再看看", null)
                                                    .setPositiveButton("定了", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            createOrder(productBean);
                                                        }
                                                    }).show();
                                        }
                                    });
                            dialog.show();
                        }
                    }
                });
    }


    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private class MyAdapter extends BaseAdapter<ProductBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<ProductBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, ProductBean productBean, int position) {
            holder.setText(R.id.tv_name, productBean.getName());
            holder.setText(R.id.tv_top, productBean.getName().substring(0, 1));
            holder.setText(R.id.tv_price, "￥" + productBean.getPrice());
            holder.setText(R.id.tv_info, productBean.getInfo());
            holder.setText(R.id.tv_type, productBean.getType());
            holder.setText(R.id.tv_feel, productBean.getFeel());
        }
    }

}
