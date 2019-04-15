package com.test.githubrepos.main;

import android.util.Log;

import com.test.githubrepos.api.GithubAPI;
import com.test.githubrepos.api.GithubService;
import com.test.githubrepos.models.GithubRepos;
import com.test.githubrepos.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amresh on 14/04/2019
 */
public class GetGitReposInteractorImpl implements MainContract.getGithubRepoInteractor, MainContract.Repository {

    @Override
    public void getGithubReposList(final onFinishedListener onFinishedListener, int page) {

        GithubService service = GithubAPI.getRetrofitInstance().create(GithubService.class);

        Call<List<GithubRepos>> call = service.getRepos(page, 6);

        Log.i("URL Called", call.request().url() + "");

        call.enqueue(new Callback<List<GithubRepos>>() {
            @Override
            public void onResponse(Call<List<GithubRepos>> call, Response<List<GithubRepos>> response) {
                List<GithubRepos> githubRepos = new ArrayList();
                githubRepos.addAll(response.body());
                onFinishedListener.onFinish(githubRepos);
                Log.i("response", "" + response.body().size());

            }

            @Override
            public void onFailure(Call<List<GithubRepos>> call, Throwable t) {
                onFinishedListener.onFailed(t);
                Log.i("error", t.getMessage());
            }
        });
    }

    @Override
    public void insertOrUpdateRepos(List<GithubRepos> githubRepos) {
        Realm localDB = AppUtils.getRealmGitReposInstance();
        try {
            if (githubRepos.size() > 0) {
                localDB.executeTransaction((realm1) -> {
                            githubRepos.forEach(repos -> {
                                GithubRepos gitReposHelper = new GithubRepos();
                                gitReposHelper.setId(repos.getId());
                                gitReposHelper.setName(repos.getName());
                                gitReposHelper.setDescription(repos.getDescription());
                                gitReposHelper.setLanguage(repos.getLanguage());
                                gitReposHelper.setOpen_issues(repos.getOpen_issues());
                                gitReposHelper.setWatchers(repos.getWatchers());

                                realm1.copyToRealmOrUpdate(gitReposHelper);

                            });
                        }
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            AppUtils.closeRealm(localDB);
        }

    }

    @Override
    public void getRepoList(onQueryFinishedListener onQueryFinishedListener) {
        Realm localDB = AppUtils.getRealmGitReposInstance();

        try {
            RealmResults<GithubRepos> gitReposHelpers = localDB.where(GithubRepos.class)
                    .findAll();
            if (gitReposHelpers.size() > 0) {
                List<GithubRepos> githubRepos = localDB.copyFromRealm(gitReposHelpers);

                onQueryFinishedListener.onQueryFinish(githubRepos);
            }else{
                onQueryFinishedListener.onQueryFailed("No data");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            AppUtils.closeRealm(localDB);
        }
    }
}
