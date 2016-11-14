package com.yidou.wandou.example_11.utils;

import com.yidou.wandou.example_11.bean.News;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/13.
 */

public interface HttpGets
{
    @GET("index")
    Observable<News> getNews(@Query("type") String type, @Query("key") String key);
}
