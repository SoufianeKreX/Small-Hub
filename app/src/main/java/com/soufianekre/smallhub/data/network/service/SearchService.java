package com.soufianekre.smallhub.data.network.service;

import com.soufianekre.smallhub.data.network.model.Pageable;
import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.data.network.model.User;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchService {

    @GET("search/repositories")
    Observable<Pageable<Repo>> searchRepositories(@Query(value = "q", encoded = true) String query,
                                                  @Query("page") long page);

    @GET("search/users")
    Observable<Pageable<User>> searchUsers(@Query(value = "q", encoded = true) String query,
                                           @Query("page") long page);
}