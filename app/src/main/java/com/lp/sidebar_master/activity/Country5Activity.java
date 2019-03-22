package com.lp.sidebar_master.activity;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
import com.lp.sidebar_master.utils.StatusBarUtil;
import com.lp.sidebar_master.widget.WaveSideBar;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * File descripition:   侧滑栏拼音全部显示，有动态效果，拼音字母顶部停留，顶部拼音搜索
 *
 * @author lp
 * @date 2018/8/4
 */

public class Country5Activity extends BaseActivity<CountryPresenter> implements CountryView, WaveSideBar.OnSelectIndexItemListener {
    @BindView(R.id.sidebar)
    WaveSideBar mSidebar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_index)
    TextView mTvIndex;
    @BindView(R.id.ll_index)
    LinearLayout mLlIndex;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private CountryRvAdapter mAdapter;
    private ArrayList<CountryBean> mCountryList;
    private ArrayList<CountryBean> mCountryListAll;
    private LinearLayoutManager layoutManager;

    @Override
    protected CountryPresenter createPresenter() {
        return new CountryPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_country5;
    }


    @Override
    protected void initData() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);

        setSupportActionBar(mToolbar);
        mSidebar.setOnSelectIndexItemListener(this);

        mCountryList = new ArrayList<>();
        mCountryListAll = new ArrayList<>();
        //创建布局管理
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CountryRvAdapter(R.layout.item_country, mCountryList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new mScrollListener());

        mPresenter.internationalCode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_view, menu);

        //找到searchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

//        searchView.setSubmitButtonEnabled(true);//显示提交按钮
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交按钮的点击事件
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //当输入框内容改变的时候回调
                try {
                    filerData(newText.toString());
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onInternationalCodeSuccess(BaseModel<List<CountryBean>> o) {
        try {
            mCountryList.clear();
            mCountryListAll.clear();
            mCountryList.addAll(PinYinKit.filledData(o.getData()));
            mCountryListAll.addAll(PinYinKit.filledData(o.getData()));
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

//            if (mCurrentPosition != firstVisibleItemPosition) {
                mCurrentPosition = firstVisibleItemPosition;
                if (mCountryList.size() > 0) {
                    mTvIndex.setText(mCountryList.get(mCurrentPosition).getSortLetters());
                    mLlIndex.setVisibility(View.VISIBLE);
                } else {
                    mLlIndex.setVisibility(View.GONE);
                }
//            }
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
//                layoutManager.setStackFromEnd(true);
                /**
                 * 滚动到指定位置（有滚动效果）
                 */
//                LinearSmoothScroller s1 = new TopSmoothScroller(this);
//                s1.setTargetPosition(position);
//                layoutManager.startSmoothScroll(s1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void filerData(String str) throws BadHanyuPinyinOutputFormatCombination {
        if (TextUtils.isEmpty(str)) {
            mCountryList.clear();
            mCountryList.addAll(mCountryListAll);
        } else {
            mCountryList.clear();
            for (CountryBean ms : mCountryListAll) {
                String name = ms.getName();
                String code = ms.getCode();
                if (name.indexOf(str.toString()) != -1
                        || PinYinKit.getPingYin(name).startsWith(str.toString())
                        || PinYinKit.getPingYin(name).startsWith(str.toUpperCase().toString())
                        || name.contains(str)

                        || PinYinKit.getPingYin(code).startsWith(str.toString())
                        || PinYinKit.getPingYin(code).startsWith(str.toUpperCase().toString())
                        || code.contains(str)
                        ) {
                    mCountryList.add(ms);
                }
            }
        }
        PinYinKit.initLetter(mCountryList);
        layoutManager.scrollToPositionWithOffset(0, 0);
        mAdapter.notifyDataSetChanged();
    }
}
