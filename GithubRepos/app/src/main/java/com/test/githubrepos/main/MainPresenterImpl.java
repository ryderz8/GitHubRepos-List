package com.test.githubrepos.main;

import com.test.githubrepos.models.GithubRepos;

import java.util.List;

/**
 * Created by amresh on 14/04/2019
 */
public class MainPresenterImpl implements MainContract.presenter, MainContract.getGithubRepoInteractor.onFinishedListener,
        MainContract.getGithubRepoInteractor.onQueryFinishedListener{

    private MainContract.MainView mainView;
    private MainContract.getGithubRepoInteractor getGithubRepoInteractor;

    public MainPresenterImpl(MainContract.MainView mainView, MainContract.getGithubRepoInteractor githubRepoInteractor) {
        this.mainView = mainView;
        this.getGithubRepoInteractor = githubRepoInteractor;

    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onRequestDataFromServer(int page) {
        if (mainView.checkConnectivity()) {
            if (page == 0) {
                mainView.showProgress();
            }
            getGithubRepoInteractor.getGithubReposList(this, page);
        } else {
            getGithubRepoInteractor.getRepoList(this);
        }

    }

    @Override
    public void onFinish(List<GithubRepos> githubRepos) {
        if (mainView != null) {
            mainView.setDataToRecyclerView(githubRepos);
            mainView.hideProgress();
        }
        getGithubRepoInteractor.insertOrUpdateRepos(githubRepos);
    }

    @Override
    public void onFailed(Throwable t) {
        if (mainView != null) {
            mainView.onResponseFailure(t);
            mainView.hideProgress();
        }
    }

    @Override
    public void onQueryFinish(List<GithubRepos> githubRepos) {
        if (mainView != null) {
            mainView.setDataToRecyclerView(githubRepos);
            mainView.hideProgress();
        }
    }

    @Override
    public void onQueryFailed(String errorMsg) {
        if (mainView != null) {
            mainView.onResponseFailure(new Exception(errorMsg));
            mainView.hideProgress();
        }
    }
}
