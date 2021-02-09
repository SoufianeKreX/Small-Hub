package com.soufianekre.smallhub.ui.repos.code.contributors;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.soufianekre.smallhub.data.network.RestProvider;
import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.helper.BundleConstant;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.RxHelper;
import com.soufianekre.smallhub.ui.base.BasePresenter;

import java.util.ArrayList;

public class RepoContributorsPresenter<V extends RepoContributorsMvpView> extends BasePresenter<V> implements RepoContributorsMvpPresenter<V> {

    private static final String TAG = "RepoContributorsPresenter";
    private ArrayList<User> users = new ArrayList<>();
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;
    @com.evernote.android.state.State String repoId;
    @com.evernote.android.state.State String login;

    public RepoContributorsPresenter() {
    }

    @Override
    public void onFragmentCreated(@NonNull Bundle bundle) {
        repoId = bundle.getString(BundleConstant.ID);
        login = bundle.getString(BundleConstant.EXTRA);
        if (!InputHelper.isEmpty(login) && !InputHelper.isEmpty(repoId)) {
            onCallApi(1, null);
        }
    }

    @NonNull
    @Override
    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public int getCurrentPage() {
        return page;
    }

    @Override
    public int getPreviousTotal() {
        return previousTotal;
    }

    @Override
    public void setCurrentPage(int page) {
        this.page = page;
    }

    @Override
    public void setPreviousTotal(int previousTotal) {
        this.previousTotal = previousTotal;
    }

    @Override
    public boolean onCallApi(int page, @Nullable Object parameter) {
        if( page == 1){
            lastPage = Integer.MAX_VALUE;
            getMvpView().getLoadMore().reset();
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0){
            getMvpView().hideProgress();
            return false;
        }
        getCompositeDisposable().add(RxHelper.getObservable(RestProvider
                .getRepoService()
                .getContributors(login,repoId,page))
                .subscribe(userPageable -> {
                    if (userPageable != null){
                        lastPage = userPageable.getLast();
                    }
                    getMvpView().onNotifyAdapter(userPageable != null? userPageable.getItems() : null ,page);
                }));
        return true;
    }

    @Override
    public void onItemClick(int position, View v, User item) {

    }

    @Override
    public void onItemLongClick(int position, View v, User item) {

    }
}