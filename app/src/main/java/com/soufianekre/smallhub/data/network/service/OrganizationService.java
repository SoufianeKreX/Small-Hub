package com.soufianekre.smallhub.data.network.service;

import androidx.annotation.NonNull;

import com.soufianekre.smallhub.data.network.model.Pageable;
import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.data.network.model.User;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface OrganizationService {

    @GET("orgs/{org}/members/{username}")
    Observable<Response<Boolean>> isMember(@NonNull @Path("org") String org, @NonNull @Path("username") String username);

    @GET("orgs/{org}") Observable<User> getOrganization(@NonNull @Path("org") String org);

    @GET("users/{user}/orgs") Observable<Pageable<User>> getUserOrganizations(@NonNull @Path("user") String user);

    //@GET("orgs/{org}/teams") Observable<Pageable<TeamsModel>> getOrgTeams(@NonNull @Path("org") String org, @Query("page") int page);

    @GET("orgs/{org}/members") Observable<Pageable<User>> getOrgMembers(@NonNull @Path("org") String org, @Query("page") int page);

    @GET("teams/{id}/members") Observable<Pageable<User>> getTeamMembers(@Path("id") long id, @Query("page") int page);

    @GET("teams/{id}/repos") Observable<Pageable<Repo>> getTeamRepos(@Path("id") long id, @Query("page") int page);
    /*
    @GET("users/{username}/events/orgs/{org}")
    Observable<Pageable<Event>> getReceivedEvents(@NonNull @Path("username") String userName,
                                                  @NonNull @Path("org") String org, @Query("page") int page);


     */
    @GET("orgs/{org}/repos")
    Observable<Pageable<Repo>> getOrgRepos(@NonNull @Path("org") String org,
                                           @QueryMap(encoded = true) Map<String, String> filterParams,
                                           @Query("page") int page);

}
