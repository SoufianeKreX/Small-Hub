package com.soufianekre.smallhub.ui.repos.code.contributors;

import android.os.Bundle;
//import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.loadmore.OnLoadMore;
import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.helper.AppBundler;
import com.soufianekre.smallhub.helper.BundleConstant;
import com.soufianekre.smallhub.ui.base.BaseFragment;
import com.soufianekre.smallhub.ui.widgets.StateLayout;
import com.soufianekre.smallhub.ui.adapters.UsersAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.DynamicRecyclerView;
import com.soufianekre.smallhub.ui.widgets.recycler_view.scroll.RecyclerViewFastScroller;

import java.util.List;

public class RepoContributorsFragment extends BaseFragment implements RepoContributorsMvpView {

    @BindView(R.id.repo_contributors_recycler_view)
    DynamicRecyclerView recycler;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.fastScroller)
    RecyclerViewFastScroller fastScroller;
    private OnLoadMore onLoadMore;
    private UsersAdapter adapter;


    RepoContributorsPresenter<RepoContributorsMvpView> mPresenter;


    public static RepoContributorsFragment newInstance(@NonNull String repoId,@NonNull String login) {

        //Bundle args = new Bundle();
        RepoContributorsFragment fragment = new RepoContributorsFragment();
        fragment.setArguments(AppBundler.start()
                .put(BundleConstant.ID,repoId)
                .put(BundleConstant.EXTRA,login)
                .end());
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_contributors,container,false);
        mPresenter = new RepoContributorsPresenter<>();
        setUnBinder(ButterKnife.bind(this,v));
        mPresenter.onAttach(RepoContributorsFragment.this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() == null){
            throw new NullPointerException("Bundle is null");
        }
        stateLayout.setEmptyText(R.string.no_contributors);
        stateLayout.setOnReloadListener(this);
        refresh.setOnRefreshListener(this);
        recycler.setEmptyView(stateLayout,refresh);
        recycler.addKeyLineDivider();
        adapter = new UsersAdapter(mPresenter.getUsers(),true);
        adapter.setListener(mPresenter);
        getLoadMore().initialize(mPresenter.getCurrentPage(),mPresenter.getPreviousTotal());
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(getLoadMore());
        if (savedInstanceState == null){
            mPresenter.onFragmentCreated(getArguments());
        }else if (mPresenter.getUsers().isEmpty() && !mPresenter.isApiCalled()){
            onRefresh();
        }
        fastScroller.attachRecyclerView(recycler);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onNotifyAdapter(@Nullable List<User> items, int page) {
        hideProgress();
        if(items == null || items.isEmpty()){
            adapter.clear();
            return;
        }
        if (page<=1){
            adapter.insertItems(items);
        }else{
            adapter.addItems(items);
        }
    }



    @NonNull
    @Override
    public OnLoadMore getLoadMore() {
        if( onLoadMore == null){
            onLoadMore = new OnLoadMore(mPresenter);
        }
        return onLoadMore;
    }

    @Override
    public void showProgress(int ResId) {
        refresh.setRefreshing(true);
        stateLayout.showProgress();
    }

    @Override
    public void hideProgress() {
        refresh.setRefreshing(false);
        stateLayout.hideProgress();
    }

    public void onRefresh(){
        mPresenter.onCallApi(1,getArguments().getString(BundleConstant.EXTRA));
    }

    @Override
    public void onClick(View v) {
        onRefresh();
    }

    private void showReload(){
        hideProgress();
        stateLayout.showReload(adapter.getItemCount());
    }

    @Override
    public void showMessage(int resId) {
        showReload();
        super.showMessage(resId);
    }
}