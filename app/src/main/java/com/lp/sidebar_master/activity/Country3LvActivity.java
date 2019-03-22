package com.lp.sidebar_master.activity;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.LinearLayout;
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
import com.lp.sidebar_master.widget.WaveSideBar;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * File descripition:   侧滑栏拼音全部显示，有动态效果，拼音字母顶部停留
 *
 * @author lp
 * @date 2018/8/4
 */

public class Country3LvActivity extends BaseActivity<CountryPresenter> implements CountryView, WaveSideBar.OnSelectIndexItemListener {
    @BindView(R.id.sidebar)
    WaveSideBar mSidebar;
    @BindView(R.id.recyclerView)
    ListView mRecyclerView;
    @BindView(R.id.tv_index)
    TextView mTvIndex;
    @BindView(R.id.ll_index)
    LinearLayout mLlIndex;

    private CountryLvAdapter mAdapter;
    private ArrayList<CountryBean> mCountryList;

    private int mFlowHeight = 0;

    @Override
    protected CountryPresenter createPresenter() {
        return new CountryPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_country32;
    }


    @Override
    protected void initData() {
        mSidebar.setOnSelectIndexItemListener(this);

        mCountryList = new ArrayList<>();
        mAdapter = new CountryLvAdapter(this, mCountryList, R.layout.item_country);
        mRecyclerView.setAdapter(mAdapter);


        /**
         * 获取顶部布局的高度  listview的OnScrollListener 回调onScrollStateChanged方法是手势松开后回调  故这个高度进入页面就需要获取
         */
        mLlIndex.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mFlowHeight = mLlIndex.getHeight();
                mLlIndex.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mRecyclerView.setOnScrollListener(new mScrollListener());
            }
        });

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

    private class mScrollListener implements AbsListView.OnScrollListener {

        private int mCurrentPosition = -1;

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            if (mLlIndex != null || mFlowHeight < 1) {
                mFlowHeight = mLlIndex.getMeasuredHeight();
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int position, int i1, int i2) {
            int firstVisibleItemPosition = absListView.getFirstVisiblePosition();
            View view = absListView.getChildAt(position + 1 - absListView.getFirstVisiblePosition());

            if (view != null) {
                if (view.getTop() <= mFlowHeight && mCountryList.get(firstVisibleItemPosition + 1).getLetter()) {
                    mLlIndex.setY(view.getTop() - mFlowHeight);
                } else {
                    mLlIndex.setY(0);
                }
            }

            if (mCurrentPosition != firstVisibleItemPosition) {
                mCurrentPosition = firstVisibleItemPosition;
                if (mCountryList.size() > 0) {
                    mTvIndex.setText(mCountryList.get(mCurrentPosition).getSortLetters());
                }
            }
        }
    }

    @Override
    public void onSelectIndexItem(String str) {
        try {
            int position = PinYinKit.getPositionForSection(mCountryList, str.charAt(0));
            if (position != -1) {
                mRecyclerView.setSelection(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
