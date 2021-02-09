package com.soufianekre.smallhub.ui.search.users;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.RestProvider;
import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.helper.RxHelper;
import com.soufianekre.smallhub.ui.base.BasePresenter;

import java.util.ArrayList;

public class SearchUsersPresenter<V extends SearchUsersMvp.View> extends BasePresenter<V>
        implements SearchUsersMvp.Presenter<V> {

    private static final String TAG = "SearchUsersPresenter";
    private ArrayList<User> users = new ArrayList<>();
    private int page;
    private int previousTotal;
    private int lastPage = Integer.MAX_VALUE;


    public SearchUsersPresenter() {

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
        this.page= page;
    }

    @Override
    public void setPreviousTotal(int previousTotal) {
        this.previousTotal = previousTotal;
    }

    @Override
    public boolean onCallApi(int page, @Nullable String parameter) {
        if(page == 1){
            lastPage = Integer.MAX_VALUE;
            getMvpView().getLoadMore().reset();
        }
        setCurrentPage(page);
        if (page> lastPage|| lastPage == 0||parameter == null){
            getMvpView().hideProgress();
            return false;
        }
        getCompositeDisposable().add(RxHelper.safeObservable(RestProvider.getSearchService().searchUsers(parameter,page))
                .subscribe(userPageable -> {
                    lastPage = userPageable.getLast();
                    getMvpView().onNotifyAdapter(!userPageable.isIncompleteResults()?
                            userPageable.getItems() : null, page);
                    if (!userPageable.isIncompleteResults()) {
                        getMvpView().onSetTabCount(userPageable.getTotalCount());
                    } else {
                        getMvpView().onSetTabCount(0);
                        getMvpView().showMessage(R.string.search_results_warning);
                    }
                }, throwable -> {
                    getMvpView().showMessage(R.string.max_rate_request_error_msg);
                    getMvpView().hideProgress();
                }));
        return true;
    }

    @Override
    public void onItemClick(int position, android.view.View v, User item) {

    }

    @Override
    public void onItemLongClick(int position, android.view.View v, User item) {

    }
}