package com.lp.sidebar_master.presenter;


import com.lp.sidebar_master.base.mvp.BaseModel;
import com.lp.sidebar_master.base.mvp.BaseObserver;
import com.lp.sidebar_master.base.mvp.BasePresenter;

import java.util.List;

/**
 * File descripition:  注册
 *
 * @author lp
 * @date 2018/6/19
 */

public class CountryPresenter extends BasePresenter<CountryView> {
    public CountryPresenter(CountryView baseView) {
        super(baseView);
    }

    /**
     * 用户注册时获取国际码
     */
    public void internationalCode() {
        addDisposable(apiServer.InternationalCode("Android", "zh"), new BaseObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                baseView.onInternationalCodeSuccess((BaseModel<List<CountryBean>>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }

}
