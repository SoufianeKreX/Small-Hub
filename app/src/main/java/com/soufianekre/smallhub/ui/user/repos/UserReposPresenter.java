package com.soufianekre.smallhub.ui.user.repos;


import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.soufianekre.smallhub.data.models.FilterOptionsModel;
import com.soufianekre.smallhub.data.network.RestProvider;
import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.ui.base.BasePresenter;

import java.util.ArrayList;

public class UserReposPresenter<V extends UserReposMvp.View>
        extends BasePresenter<V> implements UserReposMvp.Presenter<V> {

    private static final String TAG = "UserReposPresenter";
    private ArrayList<Repo> repos = new ArrayList<>();
    private int page;
    private int previousTotal;
    private String username;
    private int lastPage = Integer.MAX_VALUE;
    private String currentLoggedIn;
    private FilterOptionsModel filterOptions = new FilterOptionsModel();


    @NonNull
    @Override
    public ArrayList<Repo> getRepos() {
        return repos;
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
    public boolean onCallApi(int page, @Nullable String parameter) {
        if (parameter == null) {
            throw new NullPointerException("Username is null");
        }
        username = parameter;
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            getMvpView().getLoadMore().reset();
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0) {
            //sendToView(ProfileReposMvp.View::hideProgress);
            return false;
        }
        boolean isProfile = TextUtils.equals(currentLoggedIn, username);
        filterOptions.setIsPersonalProfile(isProfile);
        makeRestCall(isProfile
                        ? RestProvider.getUserRestService().getRepos(filterOptions.getQueryMap(), page)
                        : RestProvider.getUserRestService().getRepos(parameter, filterOptions.getQueryMap(), page),
                repoModelPageable -> {
                    lastPage = repoModelPageable.getLast();
                    getMvpView().onNotifyAdapter(repoModelPageable.getItems(), page);
                });
        return true;
    }

    @Override
    public void onItemClick(int position, android.view.View v, Repo item) {
        getMvpView().showRepo(item);
    }

    @Override
    public void onItemLongClick(int position, android.view.View v, Repo item) {

    }
}