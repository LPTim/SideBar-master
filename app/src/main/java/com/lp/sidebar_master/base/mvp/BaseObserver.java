package com.lp.sidebar_master.base.mvp;

import com.google.gson.JsonParseException;
import com.lp.sidebar_master.base.BaseContent;


import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * File descripition:   数据处理基类
 *
 * @author lp
 * @date 2018/6/19
 */

public abstract class BaseObserver<T> extends DisposableObserver<T> {
    protected BaseView view;
    /**
     * 成功状态值
     */
    private int successCode = BaseContent.successCode;
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1008;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1007;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1006;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1005;
    /**
     * data为null
     */
    public static final int CONNECT_NULL = 5555;


    public BaseObserver(BaseView view) {
        this.view = view;
    }

    @Override
    protected void onStart() {
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void onNext(T o) {
        try {
            if (view != null) {
                view.hideLoading();
            }
            BaseModel model = (BaseModel) o;
            if (model.getData() != null) {
                if (model.getErrcode() == successCode) {
                    onSuccess(o);
                } else {
                    if (view != null) {
                        view.onErrorCode(model);
                        onException(model.getErrcode(), model.getErrmsg());
                    }
                }
            } else {
                if (view != null) {
                    view.onErrorCode(model);
                    onException(CONNECT_NULL,"");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e.toString());
        }
    }

    @Override
    public void onError(Throwable e) {
        if (view != null) {
            view.hideLoading();
        }
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK, "");
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR, "");
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT, "");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR, "");
            e.printStackTrace();
        } else {
            if (e != null) {
                onError(e.toString());
            }
        }
    }

    private void onException(int unknownError, String message) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError("连接错误");
                break;
            case CONNECT_TIMEOUT:
                onError("连接超时");
                break;
            case BAD_NETWORK:
                onError("网络超时");
                break;
            //数据为空，显示异常
            case CONNECT_NULL:
                onError("数据为空，显示异常");
                break;
            //解析失败
            case PARSE_ERROR:
                onError("数据解析失败");
                break;
            //其他code值
            default:
                onError(message);
                break;
        }
    }

    //消失写到这 有一定的延迟  对dialog显示有影响
    @Override
    public void onComplete() {
       /* if (view != null) {
            view.hideLoading();
        }*/
    }

    public abstract void onSuccess(T o);

    public abstract void onError(String msg);
}
