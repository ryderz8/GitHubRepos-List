package com.test.githubrepos.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.githubrepos.R;
import com.test.githubrepos.models.GithubRepos;

import java.util.List;

/**
 * Created by amresh on 14/04/2019
 */
public class GitReposAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<GithubRepos> mItemList;


    public GitReposAdapter(List<GithubRepos> itemList) {

        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView repoNameTV, languageTV, issuesCountTV, watcherCountTV;

        TextView descriptionTV;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            repoNameTV = itemView.findViewById(R.id.repo_name_tv);
            descriptionTV = itemView.findViewById(R.id.description_tv);
            languageTV = itemView.findViewById(R.id.language_tv);
            issuesCountTV = itemView.findViewById(R.id.issue_count_tv);
            watcherCountTV = itemView.findViewById(R.id.watcher_count_tv);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        viewHolder.repoNameTV.setText(mItemList.get(position).getName());
        viewHolder.descriptionTV.setText(mItemList.get(position).getDescription());
        viewHolder.languageTV.setText(mItemList.get(position).getLanguage());
        viewHolder.issuesCountTV.setText("" + mItemList.get(position).getOpen_issues());
        viewHolder.watcherCountTV.setText("" + mItemList.get(position).getWatchers());


    }


}