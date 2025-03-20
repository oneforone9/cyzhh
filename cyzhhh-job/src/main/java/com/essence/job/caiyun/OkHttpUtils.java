package com.essence.job.caiyun;

import lombok.extern.log4j.Log4j2;
import okhttp3.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Log4j2
public class OkHttpUtils {
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                private int maxRetry = 3;//最大重试次数
                private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试

                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    while (!response.isSuccessful() && retryNum < maxRetry) {
                        retryNum++;
                        response = chain.proceed(request);
                    }
                    return response;
                }
            })
            .connectionPool(new ConnectionPool(200, 300, TimeUnit.SECONDS))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public static void asyncGet(String url, Consumer<String> consumer) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        //异步
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("get异步响应失败==" + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    consumer.accept(response.body().string());
                } catch (Exception e) {
                    log.error("get异步响应失败==" + e.getMessage());
                }

            }
        });

    }

    public static void syncGet(String url, Consumer<String> consumer) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        //异步
        try {
            consumer.accept(okHttpClient.newCall(request).execute().body().string());
        } catch (IOException e) {
            log.error("get异步响应失败==" + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
