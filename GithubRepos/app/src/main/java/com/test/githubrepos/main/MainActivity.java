package com.test.githubrepos.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.test.githubrepos.BuildConfig;
import com.test.githubrepos.R;
import com.test.githubrepos.adapter.GitReposAdapter;
import com.test.githubrepos.models.GithubRepos;
import com.test.githubrepos.utils.AppUtils;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {

    private MainPresenterImpl mainPresenter;
    private ProgressBar progressBar;
    public GitReposAdapter gitReposAdapter;
    private List<GithubRepos> reposArrayList = new ArrayList<>();
    boolean isLoading = false;
    private int currentPage = 0;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);

        initStetho();

        ButterKnife.bind(this);

        initProgressBar();

        initAdapter();

        initScrollListener();

        mainPresenter = new MainPresenterImpl(this, new GetGitReposInteractorImpl(), new GetGitReposInteractorImpl());
        mainPresenter.onRequestDataFromServer(currentPage);

    }


    private void initAdapter() {
        gitReposAdapter = new GitReposAdapter(reposArrayList);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(gitReposAdapter);
    }

    private void initProgressBar() {

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setDataToRecyclerView(List<GithubRepos> data) {
        if (currentPage == 0) {
            reposArrayList.addAll(data);
            gitReposAdapter.notifyDataSetChanged();
        } else {
            reposArrayList.remove(reposArrayList.size() - 1);
            int scrollPosition = reposArrayList.size();
            gitReposAdapter.notifyItemRemoved(scrollPosition);
            reposArrayList.addAll(data);
            gitReposAdapter.notifyDataSetChanged();
            isLoading = false;

        }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean checkConnectivity() {
        return AppUtils.isNetworkAvailable(this);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.
                            findLastCompletelyVisibleItemPosition() == reposArrayList.size() - 3) {
                        //bottom of list!
                        currentPage += 1;
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {

        reposArrayList.add(null);
        recyclerView.post(() -> {
            gitReposAdapter.notifyItemInserted(reposArrayList.size() - 1);

        });
        mainPresenter.onRequestDataFromServer(currentPage);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
    }

    private void initStetho() {
        try {
            if (BuildConfig.DEBUG) {
                Stetho.initialize(
                        Stetho.newInitializerBuilder(this)
                                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)
                                        .withMetaTables()
                                        .databaseNamePattern(Pattern.compile(".+\\.realm"))
                                        .withLimit(500)
                                        .build())
                                .build());
            }
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
    }
}
