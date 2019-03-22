package com.lp.sidebar_master.base.api;


import com.google.gson.Gson;
import com.lp.sidebar_master.base.BaseContent;
import com.lp.sidebar_master.utils.L;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.internal.Util.UTF_8;


/**
 * File descripition:
 *
 * @author lp
 * @date 2018/6/19
 */

public class ApiRetrofit {
    public final String BASE_SERVER_URL = BaseContent.baseUrl;

    private static ApiRetrofit apiRetrofit;
    private Retrofit retrofit;
    private ApiServer apiServer;
    private static final int DEFAULT_TIMEOUT = 15;

    public ApiRetrofit() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder
                .addInterceptor(interceptor)
                .addInterceptor(new MockInterceptor())
                //设置请求超时时长
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)//错误重联
        ;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .build();

        apiServer = retrofit.create(ApiServer.class);
    }

    public static ApiRetrofit getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }

    public ApiServer getApiService() {
        return apiServer;
    }

    /**
     * 请求访问quest
     * response拦截器
     */
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
//            analyzeJson("data", "", content);
            L.e("----------Request Start----------------");
            printParams(request.body());
            L.e("| " + request.toString() + "===========" + request.headers().toString());
            L.e(content);
            L.e("----------Request End:" + duration + "毫秒----------");

            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    };

    /**
     * 请求参数日志打印
     *
     * @param body
     */
    private void printParams(RequestBody body) {
        if (body != null) {
            Buffer buffer = new Buffer();
            try {
                body.writeTo(buffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF_8);
                }
                String params = buffer.readString(charset);
                L.e("请求参数： | " + params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class MockInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Gson gson = new Gson();
            Response response = null;
            Response.Builder responseBuilder = new Response.Builder()
                    .code(200)
                    .message("")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .addHeader("content-type", "application/json");
            Request request = chain.request();
            if(request.url().toString().contains(BASE_SERVER_URL)) { //拦截指定地址
                String responseString = "{\n" +
                        "\t\"code\": 0,\n" +
                        "\t\"result\": [{\n" +
                        "\t\t\"code\": 86,\n" +
                        "\t\t\"name\": \"\\u4e2d\\u56fd\\u5927\\u9646\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 1,\n" +
                        "\t\t\"name\": \"\\u7f8e\\u56fd\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 81,\n" +
                        "\t\t\"name\": \"\\u65e5\\u672c\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 49,\n" +
                        "\t\t\"name\": \"\\u5fb7\\u56fd\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 44,\n" +
                        "\t\t\"name\": \"\\u82f1\\u56fd\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 33,\n" +
                        "\t\t\"name\": \"\\u6cd5\\u56fd\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 91,\n" +
                        "\t\t\"name\": \"\\u5370\\u5ea6\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 39,\n" +
                        "\t\t\"name\": \"\\u610f\\u5927\\u5229\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 55,\n" +
                        "\t\t\"name\": \"\\u5df4\\u897f\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 1,\n" +
                        "\t\t\"name\": \"\\u52a0\\u62ff\\u5927\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 82,\n" +
                        "\t\t\"name\": \"\\u97e9\\u56fd\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 34,\n" +
                        "\t\t\"name\": \"\\u897f\\u73ed\\u7259\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 61,\n" +
                        "\t\t\"name\": \"\\u6fb3\\u5927\\u5229\\u4e9a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 52,\n" +
                        "\t\t\"name\": \"\\u58a8\\u897f\\u54e5\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 73,\n" +
                        "\t\t\"name\": \"\\u4fc4\\u7f57\\u65af\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 62,\n" +
                        "\t\t\"name\": \"\\u5370\\u5ea6\\u5c3c\\u897f\\u4e9a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 31,\n" +
                        "\t\t\"name\": \"\\u8377\\u5170\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 90,\n" +
                        "\t\t\"name\": \"\\u571f\\u8033\\u5176\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 41,\n" +
                        "\t\t\"name\": \"\\u745e\\u58eb\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 966,\n" +
                        "\t\t\"name\": \"\\u6c99\\u7279\\u963f\\u62c9\\u4f2f\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 54,\n" +
                        "\t\t\"name\": \"\\u963f\\u6839\\u5ef7\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 886,\n" +
                        "\t\t\"name\": \"\\u4e2d\\u56fd\\u53f0\\u6e7e\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 48,\n" +
                        "\t\t\"name\": \"\\u6ce2\\u5170\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 46,\n" +
                        "\t\t\"name\": \"\\u745e\\u5178\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 234,\n" +
                        "\t\t\"name\": \"\\u5c3c\\u65e5\\u5229\\u4e9a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 32,\n" +
                        "\t\t\"name\": \"\\u6bd4\\u5229\\u65f6\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 98,\n" +
                        "\t\t\"name\": \"\\u4f0a\\u6717\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 47,\n" +
                        "\t\t\"name\": \"\\u632a\\u5a01\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 66,\n" +
                        "\t\t\"name\": \"\\u6cf0\\u56fd\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 43,\n" +
                        "\t\t\"name\": \"\\u5965\\u5730\\u5229\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 60,\n" +
                        "\t\t\"name\": \"\\u9a6c\\u6765\\u897f\\u4e9a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 63,\n" +
                        "\t\t\"name\": \"\\u83f2\\u5f8b\\u5bbe\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 27,\n" +
                        "\t\t\"name\": \"\\u5357\\u975e\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 852,\n" +
                        "\t\t\"name\": \"\\u4e2d\\u56fd\\u9999\\u6e2f\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 972,\n" +
                        "\t\t\"name\": \"\\u4ee5\\u8272\\u5217\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 65,\n" +
                        "\t\t\"name\": \"\\u65b0\\u52a0\\u5761\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 45,\n" +
                        "\t\t\"name\": \"\\u4e39\\u9ea6\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 57,\n" +
                        "\t\t\"name\": \"\\u54e5\\u4f26\\u6bd4\\u4e9a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 353,\n" +
                        "\t\t\"name\": \"\\u7231\\u5c14\\u5170\\u5171\\u548c\\u56fd\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 56,\n" +
                        "\t\t\"name\": \"\\u667a\\u5229\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 358,\n" +
                        "\t\t\"name\": \"\\u82ac\\u5170\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 880,\n" +
                        "\t\t\"name\": \"\\u5b5f\\u52a0\\u62c9\\u56fd\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 84,\n" +
                        "\t\t\"name\": \"\\u8d8a\\u5357\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 351,\n" +
                        "\t\t\"name\": \"\\u8461\\u8404\\u7259\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 30,\n" +
                        "\t\t\"name\": \"\\u5e0c\\u814a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 420,\n" +
                        "\t\t\"name\": \"\\u6377\\u514b\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 213,\n" +
                        "\t\t\"name\": \"\\u963f\\u5c14\\u53ca\\u5229\\u4e9a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 51,\n" +
                        "\t\t\"name\": \"\\u79d8\\u9c81\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 964,\n" +
                        "\t\t\"name\": \"\\u4f0a\\u62c9\\u514b\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 853,\n" +
                        "\t\t\"name\": \"\\u4e2d\\u56fd\\u6fb3\\u95e8\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": \"1-649\",\n" +
                        "\t\t\"name\": \"\\u571f\\u514b\\u51ef\\u53ef\\u7fa4\\u5c9b\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": \"1-787\",\n" +
                        "\t\t\"name\": \"\\u6ce2\\u591a\\u9ece\\u5404\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": \"1-684\",\n" +
                        "\t\t\"name\": \"\\u7f8e\\u5c5e\\u8428\\u6469\\u4e9a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": \"1-664\",\n" +
                        "\t\t\"name\": \"\\u8499\\u585e\\u62c9\\u7279\\u5c9b\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": \"1-441\",\n" +
                        "\t\t\"name\": \"\\u767e\\u6155\\u5927\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": \"1-345\",\n" +
                        "\t\t\"name\": \"\\u5f00\\u66fc\\u7fa4\\u5c9b\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 350,\n" +
                        "\t\t\"name\": \"\\u76f4\\u5e03\\u7f57\\u9640\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 687,\n" +
                        "\t\t\"name\": \"\\u65b0\\u5580\\u91cc\\u591a\\u5c3c\\u4e9a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 594,\n" +
                        "\t\t\"name\": \"\\u6cd5\\u5c5e\\u572d\\u4e9a\\u90a3\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 689,\n" +
                        "\t\t\"name\": \"\\u6cd5\\u5c5e\\u6ce2\\u5229\\u5c3c\\u897f\\u4e9a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 596,\n" +
                        "\t\t\"name\": \"\\u9a6c\\u63d0\\u5c3c\\u514b\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 262,\n" +
                        "\t\t\"name\": \"\\u7559\\u5c3c\\u6c6a\"\n" +
                        "\t}, {\n" +
                        "\t\t\"code\": 508,\n" +
                        "\t\t\"name\": \"\\u5723\\u76ae\\u8036\\u4e0e\\u871c\\u514b\\u9686\\u7fa4\\u5c9b\"\n" +
                        "\t}],\n" +
                        "\t\"message\": \"\\u8bf7\\u6c42\\u6210\\u529f\"\n" +
                        "}";
                responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()));//将数据设置到body中
                response = responseBuilder.build(); //builder模式构建response
            }else{
                response = chain.proceed(request);
            }
            return response;
        }
    }

}
