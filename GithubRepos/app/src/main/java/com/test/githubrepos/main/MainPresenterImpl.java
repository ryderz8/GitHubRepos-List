package com.test.githubrepos.main;

import com.test.githubrepos.models.GithubRepos;

import java.util.List;

/**
 * Created by amresh on 14/04/2019
 */
public class MainPresenterImpl implements MainContract.presenter, MainContract.getGithubRepoInteractor.onFinishedListener,
        MainContract.getGithubRepoInteractor.onQueryFinishedListener{

    private MainContract.MainView mainView;
    private MainContract.getGithubRepoInteractor getNoticeInteractor;

    public MainPresenterImpl(MainContract.MainView mainView, MainContract.getGithubRepoInteractor noticeInteractor) {
        this.mainView = mainView;
        this.getNoticeInteractor = noticeInteractor;

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
            getNoticeInteractor.getGithubReposList(this, page);
        } else {
            getNoticeInteractor.getRepoList(this);
        }

    }

    @Override
    public void onFinish(List<GithubRepos> githubRepos) {
        if (mainView != null) {
            mainView.setDataToRecyclerView(githubRepos);
            mainView.hideProgress();
        }
        getNoticeInteractor.insertOrUpdateRepos(githubRepos);
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
