package com.lp.sidebar_master.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lp.sidebar_master.R;
import com.lp.sidebar_master.adapter.CountryRvAdapter;
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

public class Country3RvActivity extends BaseActivity<CountryPresenter> implements CountryView, WaveSideBar.OnSelectIndexItemListener {
    @BindView(R.id.sidebar)
    WaveSideBar mSidebar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_index)
    TextView mTvIndex;
    @BindView(R.id.ll_index)
    LinearLayout mLlIndex;

    private CountryRvAdapter mAdapter;
    private ArrayList<CountryBean> mCountryList;

    private LinearLayoutManager layoutManager;

    @Override
    protected CountryPresenter createPresenter() {
        return new CountryPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_country3;
    }


    @Override
    protected void initData() {
        mSidebar.setOnSelectIndexItemListener(this);

        //创建布局管理
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mCountryList = new ArrayList<>();
        mAdapter = new CountryRvAdapter(R.layout.item_country, mCountryList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new mScrollListener());

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


    private class mScrollListener extends RecyclerView.OnScrollListener {

        private int mFlowHeight = 0;
        private int mCurrentPosition = -1;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mLlIndex != null || mFlowHeight < 1) {
                mFlowHeight = mLlIndex.getMeasuredHeight();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            View view = layoutManager.findViewByPosition(firstVisibleItemPosition + 1);

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
                    mLlIndex.setVisibility(View.VISIBLE);
                } else {
                    mLlIndex.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onSelectIndexItem(String str) {
        try {
            int position = PinYinKit.getPositionForSection(mCountryList, str.charAt(0));
            if (position != -1) {
                /**
                 * 直接到指定位置
                 */
                layoutManager.scrollToPositionWithOffset(position, 0);
                layoutManager.setStackFromEnd(true);
                /**
                 * 滚动到指定位置（有滚动效果）
                 */
//                linearSmoothScroller.setTargetPosition(position);
//                layoutManager.startSmoothScroll(linearSmoothScroller);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
