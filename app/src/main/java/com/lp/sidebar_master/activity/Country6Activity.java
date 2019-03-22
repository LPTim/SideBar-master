package com.lp.sidebar_master.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * File descripition:   侧滑栏拼音全部显示，有动态效果，拼音字母顶部停留，顶部拼音搜索
 *
 * @author lp
 * @date 2018/8/4
 */

public class Country6Activity extends BaseActivity<CountryPresenter> implements CountryView, WaveSideBar.OnSelectIndexItemListener {
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
    private ArrayList<CountryBean> mCountryShowList;
    private PinyinComparatorAdmin comparator;
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
        comparator = new PinyinComparatorAdmin();
        mCountryList = new ArrayList<>();
        mCountryShowList = new ArrayList<>();

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
        mCountryList.clear();
        mCountryList.addAll(o.getData());

        mCountryShowList = (ArrayList<CountryBean>) mCountryList.clone();
        initDatas(mCountryShowList);
    }

    private void initDatas(List<CountryBean> mList) {
        String[] strings = new String[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            strings[i] = new String(mList.get(i).getName());
        }
        try {
            mCountryShowList = (ArrayList<CountryBean>) filledData(strings).clone();
            mCountryList.clear();
            mCountryList.addAll(mCountryShowList);
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }

        //创建布局管理
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CountryRvAdapter(R.layout.item_country, mCountryShowList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new mScrollListener());
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
                int section = mCountryShowList.get(firstVisibleItemPosition + 1).getSortLetters().charAt(0);
                if (view.getTop() <= mFlowHeight && firstVisibleItemPosition + 1 == getPositionForSection(mCountryShowList,section)) {
                    mLlIndex.setY(view.getTop() - mFlowHeight);
                } else {
                    mLlIndex.setY(0);
                }
            }

            if (mCurrentPosition != firstVisibleItemPosition) {
                mCurrentPosition = firstVisibleItemPosition;
                if (mCountryShowList.size() > 0) {
                    mTvIndex.setText(mCountryShowList.get(mCurrentPosition).getSortLetters());
                    mLlIndex.setVisibility(View.VISIBLE);
                } else {
                    mLlIndex.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 方法含义：将当前字母传入方法体中， 来获取当前字母在集合中第一次出现的位置position  如果等于当前item的position，UI字母栏
     * 显示，如果不是，UI字母栏隐藏
     *
     * @param section
     * @return 对应集合中第一个出现的字母
     */
    public static int getPositionForSection(List<CountryBean> mList,int section) {
        for (int i = 0; i < mList.size(); i++) {
            String sortStr = mList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    //排序
    private ArrayList<CountryBean> filledData(String[] date) throws BadHanyuPinyinOutputFormatCombination {
        for (int i = 0; i < date.length; i++) {
            mCountryList.get(i).setName(date[i]);
            String pinyin = PinYinKit.getPingYin(date[i]);
            String sortString = "";
            if (!TextUtils.isEmpty(pinyin)) {
                sortString = pinyin.substring(0, 1).toUpperCase();
            }
            if (sortString.matches("[A-Z]")) {
                mCountryList.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                mCountryList.get(i).setSortLetters("#");
            }
        }
        //排序
        Collections.sort(mCountryList, new PinyinComparatorAdmin());

        for (int i = 0; i < mCountryList.size(); i++) {
            if (i == getPositionForSection(mCountryList,mCountryList.get(i).getSortLetters().charAt(0))) {
                mCountryList.get(i).setLetter(true);
            } else {
                mCountryList.get(i).setLetter(false);
            }
        }
        return mCountryList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public void onSelectIndexItem(String str) {
        try {
            int position = getPositionForSection(mCountryShowList,str.charAt(0));
            if (position != -1) {
                /**
                 * 直接到指定位置
                 */
//                layoutManager.scrollToPositionWithOffset(position, 0);
                ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(position,0);
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


    public class PinyinComparatorAdmin implements Comparator<CountryBean> {
        @Override
        public int compare(CountryBean o1, CountryBean o2) {
            if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }
    }

    public class TopSmoothScroller extends LinearSmoothScroller {
        TopSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int getHorizontalSnapPreference() {
            return SNAP_TO_START;
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }

    private void filerData(String str) throws BadHanyuPinyinOutputFormatCombination {
        if (TextUtils.isEmpty(str)) {
            mCountryShowList.clear();
            mCountryShowList.addAll(mCountryList);
        } else {
            mCountryShowList.clear();
            for (CountryBean ms : mCountryList) {
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
                    mCountryShowList.add(ms);
                }
            }
        }
        PinYinKit.initLetter(mCountryShowList);
        mAdapter.notifyDataSetChanged();
    }
}
