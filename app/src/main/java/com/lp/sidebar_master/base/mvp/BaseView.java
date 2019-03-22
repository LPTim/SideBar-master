package com.lp.sidebar_master.base.mvp;

/**
 * File descripition:
 *
 * @author lp
 * @date 2018/6/19
 */

public interface BaseView {
    /**
     * 显示dialog
     */
    void showLoading();

    /**
     * 隐藏 dialog
     */

    void hideLoading();

    /**
     * 显示错误信息
     *
     * @param msg
     */
    void showError(String msg);

    /**
     * 错误码
     */
    void onErrorCode(BaseModel model);
}
