package com.test.githubrepos.api;

import com.test.githubrepos.models.GithubRepos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by amresh on 13/04/2019
 */
public interface GithubService {

    @GET("/users/JakeWharton/repos")
    Call<List<GithubRepos>> getRepos(@Query("page") long page, @Query("per_page") int perPage);
}
