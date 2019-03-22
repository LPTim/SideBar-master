package com.lp.sidebar_master.base.api;

import com.lp.sidebar_master.base.mvp.BaseModel;
import com.lp.sidebar_master.presenter.CountryBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * File descripition:   接口地址之后可能统一为一个  统一的时候头部把另一个地址删除掉  这里没有做分头处理
 *
 * @author lp
 * @date 2018/6/19
 */

public interface ApiServer {
    /**
     * 用户注册时获取国际码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("abcdefg")
    Observable<BaseModel<List<CountryBean>>> InternationalCode(@Field("requestType") String requestType,
                                                               @Field("lang") String lang);

}
