package com.soufianekre.smallhub.data.network.service;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ScrapService {

    @GET("{lan}")
    Observable<Response<String>> getTrending(@Path("lan")String lan, @Query("since")String since);

    @GET("{path}") Observable<String> getWiki(@Path(value = "path", encoded = true) String path);
}
