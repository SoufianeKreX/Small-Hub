package com.soufianekre.smallhub.ui.search.users;

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
import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.ui.base.BaseFragment;
import com.soufianekre.smallhub.ui.widgets.StateLayout;
import com.soufianekre.smallhub.ui.adapters.UsersAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.DynamicRecyclerView;
import com.soufianekre.smallhub.ui.widgets.recycler_view.scroll.RecyclerViewFastScroller;

import java.util.List;

public class SearchUsersFragment extends BaseFragment implements SearchUsersMvp.View {

    //@Inject
    SearchUsersPresenter<SearchUsersMvp.View> mPresenter;
    // widgets
    String searchQuery = "";
    @BindView(R.id.search_users_recycler_view)
    DynamicRecyclerView searchUsersRecyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.fastScroller)
    RecyclerViewFastScroller fastScroller;


    private OnLoadMore<String> onLoadMore;
    private UsersAdapter usersAdapter;

    public static SearchUsersFragment newInstance() {
        Bundle args = new Bundle();

        SearchUsersFragment fragment = new SearchUsersFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SearchUsersPresenter<>();
        //getActivityComponent().inject(this);

    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View v = inflater.inflate(R.layout.fragment_search_users,container,false);
        setUnBinder(ButterKnife.bind(this,v));
        mPresenter.onAttach(SearchUsersFragment.this);
        return v;
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateLayout.setEmptyText(R.string.no_search_results);
        getLoadMore().initialize(mPresenter.getCurrentPage(), mPresenter.getPreviousTotal());
        stateLayout.setOnReloadListener(this);
        refreshLayout.setOnRefreshListener(this);
        searchUsersRecyclerView.setEmptyView(stateLayout, refreshLayout);
        usersAdapter = new UsersAdapter(mPresenter.getUsers());
        usersAdapter.setListener(mPresenter);
        searchUsersRecyclerView.setAdapter(usersAdapter);
        searchUsersRecyclerView.addKeyLineDivider();
        if (savedInstanceState != null) {
            if (!InputHelper.isEmpty(searchQuery) && mPresenter.getUsers().isEmpty() && !mPresenter.isApiCalled()) {
                onRefresh();
            }
        }
        if (InputHelper.isEmpty(searchQuery)) {
            stateLayout.showEmptyState();
        }

        fastScroller.attachRecyclerView(searchUsersRecyclerView);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void onNotifyAdapter(@Nullable List<User> items, int page) {
        hideProgress();
        if (items == null || items.isEmpty()){
            usersAdapter.clear();
            return;
        }
        if (page <= 1) {
            usersAdapter.insertItems(items);
        }else{
            usersAdapter.addItems(items);
        }

    }

    @Override
    public void onSetTabCount(int count) {

    }

    @Override
    public void onQueueSearch(@NonNull String query) {
        this.searchQuery = query;
        if (getView() != null)
            onSetSearchQuery(query);
    }

    @Override
    public void onSetSearchQuery(@NonNull String query) {
        this.searchQuery = query;
        getLoadMore().reset();
        usersAdapter.clear();
        if (!InputHelper.isEmpty(query)) {
            searchUsersRecyclerView.removeOnScrollListener(getLoadMore());
            searchUsersRecyclerView.addOnScrollListener(getLoadMore());
            onRefresh();
        }
    }



    @NonNull
    @Override
    public OnLoadMore<String> getLoadMore() {
        if (onLoadMore == null) {
            onLoadMore = new OnLoadMore<>(mPresenter, searchQuery);
        }
        onLoadMore.setParameter(searchQuery);
        return onLoadMore;
    }

    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
        stateLayout.hideProgress();
    }
    @Override
    public void showProgress(@StringRes int resId) {
        refreshLayout.setRefreshing(true);
        stateLayout.showProgress();
    }

    @Override
    public void onClick(android.view.View v) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (searchQuery.length() == 0) {
            refreshLayout.setRefreshing(false);
            return;
        }
        refreshLayout.setRefreshing(false);
        mPresenter.onCallApi(1, searchQuery);
    }
}