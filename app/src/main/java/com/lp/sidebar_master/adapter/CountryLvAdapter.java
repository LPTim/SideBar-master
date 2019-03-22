package com.lp.sidebar_master.adapter;

import android.content.Context;

import com.lp.sidebar_master.R;
import com.lp.sidebar_master.base.viewholder.CommonAdapter;
import com.lp.sidebar_master.base.viewholder.ViewHolder;
import com.lp.sidebar_master.presenter.CountryBean;

import java.util.List;


/**
 * 俩种adapter封装框架实现  供演示
 * File descripition:   选择国家
 *
 * @author lp
 * @date 2018/8/4
 */

public class CountryLvAdapter extends CommonAdapter<CountryBean> {

    public CountryLvAdapter(Context context, List<CountryBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, CountryBean countryBean) {
        holder.setText(R.id.tv_name, countryBean.getName());
        holder.setText(R.id.tv_number, countryBean.getCode());
        if (countryBean.getLetter()) {
            holder.setVisible(R.id.tv_letter, true);
            holder.setVisible(R.id.view, true);
            holder.setText(R.id.tv_letter, countryBean.getSortLetters());
        } else {
            holder.setVisible(R.id.tv_letter, false);
            holder.setVisible(R.id.view, false);
        }
    }
}
