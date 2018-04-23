package com.example.bsproperty.fragment;

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
import com.example.bsproperty.bean.OrderBean;
import com.example.bsproperty.bean.OrderListBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.view.ViewDialog;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by wdxc1 on 2018/3/21.
 */

public class UserFragment02 extends BaseFragment {
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
    private ArrayList<OrderBean> mData;
    private MyAdapter adapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mContext != null) {
            OkHttpTools.sendGet(mContext, ApiManager.ORDER_LIST)
                    .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                    .build()
                    .execute(new BaseCallBack<OrderListBean>(mContext, OrderListBean.class) {
                        @Override
                        public void onResponse(OrderListBean orderListBean) {
                            mData = orderListBean.getData();
                            adapter.notifyDataSetChanged(mData);
                        }
                    });
        }
    }

    @Override
    protected void loadData() {
        OkHttpTools.sendGet(mContext, ApiManager.ORDER_LIST)
                .addParams("uid", MyApplication.getInstance().getUserBean().getId() + "")
                .build()
                .execute(new BaseCallBack<OrderListBean>(mContext, OrderListBean.class) {
                    @Override
                    public void onResponse(OrderListBean orderListBean) {
                        mData = orderListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("订单列表");
        btnBack.setVisibility(View.GONE);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        adapter = new MyAdapter(mContext, R.layout.item_order, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, final int position) {
                ViewDialog dialog = new ViewDialog(mContext, R.layout.dialog_token) {
                    @Override
                    protected void initView() {
                        ((TextView) findViewById(R.id.tv_shop)).setText(mData.get(position).getSname());
                        ((TextView) findViewById(R.id.tv_product)).setText(mData.get(position).getPname());
                        ((TextView) findViewById(R.id.tv_price)).setText(mData.get(position).getPrice() + "");
                        ((TextView) findViewById(R.id.tv_time)).setText(MyApplication.format.format(mData.get(position).getTime()));
                        findViewById(R.id.tv_get).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("提示")
                                        .setMessage("领取后将删除该订单，是否领取？")
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("领取", new OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {
                                                OkHttpTools.sendGet(mContext, ApiManager.ORDER_DEL)
                                                        .addParams("oid", mData.get(position).getId() + "")
                                                        .build()
                                                        .execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                                                            @Override
                                                            public void onResponse(BaseResponse baseResponse) {
                                                                dialog.dismiss();
                                                                dismiss();
                                                                mData.remove(position);
                                                                adapter.notifyDataSetChanged(mData);
                                                                showToast("领取成功");
                                                            }
                                                        });
                                            }
                                        }).show();
                            }
                        });
                    }
                };
                dialog.show();
            }
        });
        rvList.setAdapter(adapter);
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                slList.setRefreshing(false);
            }
        });
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_user02;
    }

    private class MyAdapter extends BaseAdapter<OrderBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<OrderBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, OrderBean orderBean, int position) {
            holder.setText(R.id.tv_name, orderBean.getPname());
            holder.setText(R.id.tv_time, MyApplication.format.format(orderBean.getTime()));
            holder.setText(R.id.tv_store, orderBean.getSname());
            holder.setText(R.id.tv_addr, "地址：" + orderBean.getSaddr());
            holder.setText(R.id.tv_money, "总价：￥" + orderBean.getPrice() + "元");
        }
    }
}
