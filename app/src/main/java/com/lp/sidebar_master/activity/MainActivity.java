package com.lp.sidebar_master.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lp.sidebar_master.R;
import com.lp.sidebar_master.base.BaseActivity;
import com.lp.sidebar_master.base.mvp.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * File descripition:
 *
 * @author lp
 * @date 2019/3/20
 */

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_01)
    TextView mTv01;
    @BindView(R.id.tv_02)
    TextView mTv02;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_01, R.id.tv_02, R.id.tv_03, R.id.tv_032, R.id.tv_04, R.id.tv_05, R.id.tv_06})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_01:
                startActivity(Country1Activity.class);
                break;
            case R.id.tv_02:
                startActivity(Country2Activity.class);
                break;
            case R.id.tv_03:
                startActivity(Country3RvActivity.class);
                break;
            case R.id.tv_032:
                startActivity(Country3LvActivity.class);
                break;
            case R.id.tv_04:
                startActivity(Country4Activity.class);
                break;
            case R.id.tv_05:
                startActivity(Country5Activity.class);
                break;
            case R.id.tv_06:
                startActivity(Country6Activity.class);
                break;
        }
    }
}
