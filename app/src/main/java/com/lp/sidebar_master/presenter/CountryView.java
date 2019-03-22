package com.lp.sidebar_master.presenter;


import com.lp.sidebar_master.base.mvp.BaseModel;
import com.lp.sidebar_master.base.mvp.BaseView;

import java.util.List;

/**
 * File descripition:
 *
 * @author lp
 * @date 2018/6/19
 */

public interface CountryView extends BaseView {
    void onInternationalCodeSuccess(BaseModel<List<CountryBean>> o);
}
