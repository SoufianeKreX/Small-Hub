package com.soufianekre.smallhub.ui.repos.code.contributors;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.soufianekre.smallhub.data.network.loadmore.OnLoadMore;
import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.ui.base.BaseMvpView;

import java.util.List;

public interface RepoContributorsMvpView extends BaseMvpView
        ,android.view.View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    void onNotifyAdapter(@Nullable List<User> items, int page);

    @NonNull OnLoadMore getLoadMore();
}