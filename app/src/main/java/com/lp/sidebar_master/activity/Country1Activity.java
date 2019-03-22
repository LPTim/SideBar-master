package com.lp.sidebar_master.activity;

import android.widget.ListView;
import android.widget.TextView;

import com.lp.sidebar_master.R;
import com.lp.sidebar_master.adapter.CountryLvAdapter;
import com.lp.sidebar_master.base.BaseActivity;
import com.lp.sidebar_master.base.mvp.BaseModel;
import com.lp.sidebar_master.presenter.CountryBean;
import com.lp.sidebar_master.presenter.CountryPresenter;
import com.lp.sidebar_master.presenter.CountryView;
import com.lp.sidebar_master.utils.PinYinKit;
import com.lp.sidebar_master.widget.SideBar;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * File descripition:侧滑栏拼音全部显示
 *
 * @author lp
 * @date 2018/8/4
 */

public class Country1Activity extends BaseActivity<CountryPresenter> implements CountryView, SideBar.OnTouchingLetterChangedListener {
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.sidebar)
    SideBar mSidebar;
    @BindView(R.id.tv_word)
    TextView mTvWord;

    private CountryLvAdapter mAdapter;
    private ArrayList<CountryBean> mCountryList;

    @Override
    protected CountryPresenter createPresenter() {
        return new CountryPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_country1;
    }


    @Override
    protected void initData() {
        mSidebar.setOnTouchingLetterChangedListener(this);
        mSidebar.setmTextDialog(mTvWord);

        mCountryList = new ArrayList<>();
        mAdapter = new CountryLvAdapter(this, mCountryList, R.layout.item_country);
        mListView.setAdapter(mAdapter);

        mPresenter.internationalCode();
    }

    @Override
    public void onInternationalCodeSuccess(BaseModel<List<CountryBean>> o) {
        try {
            mCountryList.clear();
            mCountryList.addAll(PinYinKit.filledData(o.getData()));
            mAdapter.notifyDataSetChanged();
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
    }

    @Override
    public void onTouchingLetterChanged(String str) {
        try {
            int position = PinYinKit.getPositionForSection(mCountryList, str.charAt(0));
            if (position != -1) {
                mListView.setSelection(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
