package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddrActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private ArrayList<String> mData = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("地区选择");
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_addr;
    }

    @Override
    protected void loadData() {
        mData.add("东北地区");
        mData.add("西南地区");
        mData.add("沿海地区");
        mData.add("西北地区");
        adapter = new MyAdapter(mContext, R.layout.item_addr, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent();
                intent.putExtra("addr", mData.get(position).toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rvList.setAdapter(adapter);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

    private class MyAdapter extends BaseAdapter<String> {

        public MyAdapter(Context context, int layoutId, ArrayList<String> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, String s, int position) {
            holder.setText(R.id.tv_addr, s);
        }
    }
}
