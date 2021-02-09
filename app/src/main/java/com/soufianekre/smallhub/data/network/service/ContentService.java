package com.soufianekre.smallhub.data.network.service;

import com.soufianekre.smallhub.data.models.GitHubStatusModel;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ContentService {

    @GET("api/v2/status.json")
    Observable<GitHubStatusModel> checkStatus();
}
