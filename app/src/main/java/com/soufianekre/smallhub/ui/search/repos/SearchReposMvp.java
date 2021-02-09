package com.soufianekre.smallhub.ui.search.repos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.soufianekre.smallhub.data.network.loadmore.OnLoadMore;
import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.ui.base.BaseMvpPresenter;
import com.soufianekre.smallhub.ui.base.BaseMvpView;
import com.soufianekre.smallhub.ui.base.PaginationListener;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public interface SearchReposMvp {

    interface Presenter<V extends View>
            extends BaseMvpPresenter<V>,
            BaseViewHolder.OnItemClickListener<Repo>,
            PaginationListener<String> {
            @NonNull
            ArrayList<Repo> getRepos();

    }

    interface View extends BaseMvpView,
            SwipeRefreshLayout.OnRefreshListener, android.view.View.OnClickListener{

        void onNotifyAdapter(@Nullable List<Repo> items, int page);

        void onSetTabCount(int count);

        void onSetSearchQuery(@NonNull String query);

        void showRepo(int position, Repo item);

        void onQueueSearch(@NonNull String query);

        @NonNull
        OnLoadMore<String> getLoadMore();
    }
}
