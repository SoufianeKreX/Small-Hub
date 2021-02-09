package com.soufianekre.smallhub.ui.search.repos;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.RestProvider;
import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.helper.RxHelper;
import com.soufianekre.smallhub.ui.base.BasePresenter;

import java.util.ArrayList;

public class SearchReposPresenter<V extends SearchReposMvp.View> extends BasePresenter<V>
        implements SearchReposMvp.Presenter<V> {

    private static final String TAG = "SearchReposPresenter";
    private ArrayList<Repo> repos = new ArrayList<>();
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;

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
        if (page == 1) {
            lastPage = Integer.MAX_VALUE;
            getMvpView().getLoadMore().reset();
        }
        setCurrentPage(page);
        if (page > lastPage || lastPage == 0 || parameter == null) {
            getMvpView().hideProgress();
            return false;
        }
        getCompositeDisposable().add(RxHelper.safeObservable(
                RestProvider.getSearchService().searchRepositories(parameter, page))
                .subscribe(response -> {
                    lastPage = response.getLast();
                    getMvpView().onNotifyAdapter(response.isIncompleteResults()
                            ? null : response.getItems(), page);
                    if (!response.isIncompleteResults()) {
                        getMvpView().onSetTabCount(response.getTotalCount());
                    } else {
                        getMvpView().onSetTabCount(0);
                        getMvpView().showMessage(R.string.search_results_warning);
                    }
                }, throwable -> {
                    handleApiError(throwable);
                    //getMvpView().showMessage(R.string.max_rate_request_error_msg);
                    getMvpView().hideProgress();

                })
        );

        return true;
    }

    @Override
    public void onItemClick(int position, android.view.View v, Repo item) {
        // show repo Information
        getMvpView().showRepo(position,item);
    }

    @Override
    public void onItemLongClick(int position, android.view.View v, Repo item) {
        //show popup view even though i don't know why im adding this in the first place .
    }
}