package com.soufianekre.smallhub.ui.search.repos;

import android.os.Bundle;


import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.loadmore.OnLoadMore;
import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.ui.base.BaseFragment;

import com.soufianekre.smallhub.ui.repos.RepoPagerMvp;
import com.soufianekre.smallhub.ui.repos.RepoPagerActivity;
import com.soufianekre.smallhub.ui.widgets.StateLayout;
import com.soufianekre.smallhub.ui.adapters.ReposAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.DynamicRecyclerView;
import com.soufianekre.smallhub.ui.widgets.recycler_view.scroll.RecyclerViewFastScroller;

import java.util.List;

public class SearchReposFragment extends BaseFragment implements SearchReposMvp.View {

    SearchReposPresenter<SearchReposMvp.View> mPresenter;

    //widgets
    @BindView(R.id.search_repos_recycler_view)
    DynamicRecyclerView searchReposRecyclerView;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fastScroller)
    RecyclerViewFastScroller fastScroller;


    private String searchQuery = "";
    private ReposAdapter reposAdapter;
    private OnLoadMore<String> onLoadMore;

    public static SearchReposFragment newInstance() {
        Bundle args = new Bundle();
        SearchReposFragment fragment = new SearchReposFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SearchReposPresenter<>();
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                          @Nullable Bundle savedInstanceState) {
        android.view.View v = inflater.inflate(R.layout.fragment_search_repos,container,false);
        setUnBinder(ButterKnife.bind(this,v));
        mPresenter.onAttach(SearchReposFragment.this);
        return v;
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            stateLayout.hideProgress();
        }
        stateLayout.setEmptyText(R.string.no_search_results);
        getLoadMore().initialize(mPresenter.getCurrentPage()
                ,mPresenter.getPreviousTotal());

        stateLayout.setOnReloadListener(this);
        refreshLayout.setOnRefreshListener(this);
        searchReposRecyclerView.setEmptyView(stateLayout, refreshLayout);
        reposAdapter = new ReposAdapter(mPresenter.getRepos(), false, true);
        reposAdapter.setListener(mPresenter);
        searchReposRecyclerView.setAdapter(reposAdapter);
        searchReposRecyclerView.addKeyLineDivider();
        if (!InputHelper.isEmpty(searchQuery) && mPresenter.getRepos().isEmpty() &&!mPresenter.isApiCalled()) {
            onRefresh();
        }
        if (InputHelper.isEmpty(searchQuery)) {
            stateLayout.showEmptyState();
        }
        fastScroller.attachRecyclerView(searchReposRecyclerView);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onNotifyAdapter(@Nullable List<Repo> items, int page) {
        hideProgress();
        if (items == null || items.isEmpty()) {
            reposAdapter.clear();
            return;
        }
        if (page <= 1) {
            reposAdapter.insertItems(items);
        } else {
            reposAdapter.addItems(items);
        }

    }

    @Override public void onSetSearchQuery(@NonNull String query) {
        this.searchQuery = query;
        getLoadMore().reset();
        reposAdapter.clear();
        if (!InputHelper.isEmpty(query)) {
            searchReposRecyclerView.removeOnScrollListener(getLoadMore());
            searchReposRecyclerView.addOnScrollListener(getLoadMore());
            onRefresh();
        }
    }

    @Override
    public void showRepo(int position, Repo item) {
        String repoId = item.getName();
        String login = item.getOwner().getLogin();
        startActivity(RepoPagerActivity.createIntent(getContext(),repoId,login, RepoPagerMvp.Presenter.CODE));
    }

    @Override public void onQueueSearch(@NonNull String query) {
        this.searchQuery = query;
        if (getView() != null)
            onSetSearchQuery(query);
    }

    @NonNull @Override public OnLoadMore<String> getLoadMore() {
        if (onLoadMore == null) {
            onLoadMore = new OnLoadMore<>(mPresenter, searchQuery);
        }
        onLoadMore.setParameter(searchQuery);
        return onLoadMore;
    }
    // swipeRefreshListener
    @Override
    public void onRefresh() {
        if (searchQuery.length() == 0) {
            refreshLayout.setRefreshing(false);
            return;
        }
        refreshLayout.setRefreshing(true);
        mPresenter.onCallApi(1, searchQuery);
    }

    @Override public void onClick(android.view.View view) {
        onRefresh();
    }

    @Override
    public void onSetTabCount(int count) {

    }

    @Override
    public void showProgress(@StringRes int resId) {
        refreshLayout.setRefreshing(true);
        stateLayout.showProgress();
    }
    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
        stateLayout.hideProgress();
    }


    @Override public void showMessage(int msgRes) {
        showReload();
        super.showMessage(msgRes);
    }

    private void showReload() {
        hideProgress();
        stateLayout.showReload(reposAdapter.getItemCount());
    }

}