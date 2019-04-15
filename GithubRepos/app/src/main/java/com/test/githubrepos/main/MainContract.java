package com.test.githubrepos.main;

import com.test.githubrepos.models.GithubRepos;

import java.util.List;

/**
 * Created by amresh on 14/04/2019
 */
public class MainContract {
    /**
     * Call when user interact with the view and other when view OnDestroy()
     */
    interface presenter {

        void onDestroy();

        void onRequestDataFromServer(int page);

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetNoticeInteractorImpl class
     **/
    interface MainView {
        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(List<GithubRepos> data);

        void onResponseFailure(Throwable throwable);

        boolean checkConnectivity();
    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface getGithubRepoInteractor {

        interface onFinishedListener {
            void onFinish(List<GithubRepos> githubRepos);

            void onFailed(Throwable t);
        }

        void getGithubReposList(onFinishedListener onFinishedListener, int page);

        //Querying internal database
        interface onQueryFinishedListener {
            void onQueryFinish(List<GithubRepos> noticeArrayList);

            void onQueryFailed(String msg);
        }

        void insertOrUpdateRepos(List<GithubRepos> githubRepos);

        void getRepoList(onQueryFinishedListener onQueryFinishedListener);

    }
}
