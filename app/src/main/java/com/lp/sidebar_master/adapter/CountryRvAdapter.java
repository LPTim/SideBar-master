package com.lp.sidebar_master.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lp.sidebar_master.R;
import com.lp.sidebar_master.presenter.CountryBean;

import java.util.List;


/**
 * 俩种adapter封装框架实现  供演示
 * File descripition:   选择国家
 *
 * @author lp
 * @date 2018/8/4
 */

public class CountryRvAdapter extends BaseQuickAdapter<CountryBean, BaseViewHolder> {

    public CountryRvAdapter(int layoutResId, @Nullable List<CountryBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder holder, CountryBean countryBean) {
        holder.setText(R.id.tv_name, countryBean.getName());
        holder.setText(R.id.tv_number, countryBean.getCode());
        TextView tv_letter = holder.getView(R.id.tv_letter);
        View view = holder.getView(R.id.view);
        if (countryBean.getLetter()) {
            tv_letter.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_letter, countryBean.getSortLetters());
        } else {
            tv_letter.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
    }
}
