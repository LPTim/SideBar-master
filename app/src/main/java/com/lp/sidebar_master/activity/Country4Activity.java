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
import com.lp.sidebar_master.widget.IndexBar;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import butterknife.BindView;


/**
 * File descripition:   侧滑栏拼音部分显示，有拼音显示，没有不显示
 *
 * @author lp
 * @date 2018/8/4
 */

public class Country4Activity extends BaseActivity<CountryPresenter> implements CountryView, IndexBar.OnIndexChangedListener {
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.sidebar)
    IndexBar mSidebar;
    @BindView(R.id.tv_word)
    TextView mTvWord;

    private CountryLvAdapter mAdapter;
    private ArrayList<CountryBean> mCountryList;

    //字母集合
    private List<String> mLetter = new ArrayList<>();

    @Override
    protected CountryPresenter createPresenter() {
        return new CountryPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_country4;
    }


    @Override
    protected void initData() {
        mSidebar.setOnIndexChangedListener(this);
        mSidebar.setSelectedIndexTextView(mTvWord);

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

            mLetter.clear();
            for (int i = 0; i < mCountryList.size(); i++) {
                mLetter.add(mCountryList.get(i).getSortLetters());
            }
            removeDuplicate(mLetter);
            String[] letters = mLetter.toArray(new String[mLetter.size()]);
            mSidebar.setIndexs(letters);

            mAdapter.notifyDataSetChanged();
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
    }


    private static void removeDuplicate(List<String> list) {
        LinkedHashSet<String> set = new LinkedHashSet<String>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
    }

    @Override
    public void onIndexChanged(String str) {
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
