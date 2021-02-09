package com.soufianekre.smallhub.ui.user.repos;

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
import com.soufianekre.smallhub.helper.BundleConstant;
import com.soufianekre.smallhub.helper.AppBundler;
import com.soufianekre.smallhub.ui.base.BaseFragment;
import com.soufianekre.smallhub.ui.repos.RepoPagerMvp;
import com.soufianekre.smallhub.ui.repos.RepoPagerActivity;
import com.soufianekre.smallhub.ui.widgets.StateLayout;
import com.soufianekre.smallhub.ui.adapters.ReposAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.DynamicRecyclerView;
import com.soufianekre.smallhub.ui.widgets.recycler_view.scroll.RecyclerViewFastScroller;

import java.util.List;

public class UserReposFragment extends BaseFragment implements UserReposMvp.View {

    //@Inject
    private UserReposPresenter<UserReposMvp.View> mPresenter;

    @BindView(R.id.recycler_view)
    DynamicRecyclerView recycler;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.fastScroller)
    RecyclerViewFastScroller fastScroller;

    private OnLoadMore<String> onLoadMore;
    private ReposAdapter adapter;

    public static UserReposFragment newInstance(@NonNull String username) {

        //Bundle args = new Bundle();

        UserReposFragment fragment = new UserReposFragment();
        fragment.setArguments(AppBundler.start().put(BundleConstant.EXTRA,username).end());
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserReposPresenter<>();
        //getActivityComponent().inject(this);

    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View v = inflater.inflate(R.layout.fragment_user_repos,container,false);
        setUnBinder(ButterKnife.bind(this,v));
        mPresenter.onAttach(UserReposFragment.this);
        return v;
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateLayout.setEmptyText(R.string.no_repos);
        stateLayout.setOnReloadListener(this);
        refresh.setOnRefreshListener(this);
        getLoadMore().initialize(mPresenter.getCurrentPage(), mPresenter.getPreviousTotal());
        adapter = new ReposAdapter(mPresenter.getRepos(), false);
        adapter.setListener(mPresenter);

        recycler.setEmptyView(stateLayout, refresh);
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(getLoadMore());
        recycler.addDivider();
        if (mPresenter.getRepos().isEmpty() && !mPresenter.isApiCalled()) {
            onRefresh();
        }
        fastScroller.attachRecyclerView(recycler);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override public void onNotifyAdapter(@Nullable List<Repo> items, int page) {
        hideProgress();
        if (items == null || items.isEmpty()) {
            adapter.clear();
            return;
        }
        if (page <= 1) {
            adapter.insertItems(items);
        } else {
            adapter.addItems(items);
        }
    }


    @Override public void showMessage(int msgRes) {
        showReload();
        super.showMessage(msgRes);
    }

    @NonNull @Override public OnLoadMore<String> getLoadMore() {
        if (onLoadMore == null) {
            onLoadMore = new OnLoadMore<>(mPresenter, getArguments().getString(BundleConstant.EXTRA));
        }
        return onLoadMore;
    }

    @Override
    public void showRepo(Repo item) {
        String repoId = item.getName();
        String login = item.getOwner().getLogin();
        showMessage("Still under development " + login + "  " + repoId);
        startActivity(RepoPagerActivity.createIntent(getContext(),repoId,login, RepoPagerMvp.Presenter.CODE));
    }


    public void onRefresh() {
        mPresenter.onCallApi(1, getArguments().getString(BundleConstant.EXTRA));
    }

    public void onClick(android.view.View view) {
        onRefresh();
    }
    @Override
    public void showProgress(@StringRes int resId) {

        refresh.setRefreshing(true);
        stateLayout.showProgress();
    }
    @Override
    public void hideProgress() {
        refresh.setRefreshing(false);
        stateLayout.hideProgress();
    }

    /*
    @Override public void onScrollTop(int index) {
        super.onScrollTop(index);
        if (recycler != null) recycler.scrollToPosition(0);
    }

     */

    private void showReload() {
        hideProgress();
        stateLayout.showReload(adapter.getItemCount());
    }
    /*

    @Override public void onRepoFilterClicked() {
        ProfileReposFilterBottomSheetDialog.newInstance(getPresenter().getFilterOptions())
                .show(getChildFragmentManager(), "ProfileReposFilterBottomSheetDialog");
    }


    @Override
    public void onFilterApply() {
        getPresenter().onFilterApply();
    }

    @Override
    public void onTypeSelected(String selectedType) {
        getPresenter().onTypeSelected(selectedType);
    }

    @Override
    public void onSortOptionSelected(String selectedSortOption) {
        getPresenter().onSortOptionSelected(selectedSortOption);
    }

    @Override
    public void onSortDirectionSelected(String selectedSortDirection) {
        getPresenter().onSortDirectionSelected(selectedSortDirection);
    }



    @Override
    public String getLogin() {
        return getArguments().getString(BundleConstant.EXTRA);
    }

     */

}
