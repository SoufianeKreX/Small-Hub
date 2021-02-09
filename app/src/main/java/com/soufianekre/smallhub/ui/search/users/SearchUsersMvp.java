package com.soufianekre.smallhub.ui.search.users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.soufianekre.smallhub.data.network.loadmore.OnLoadMore;
import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.ui.base.BaseMvpPresenter;
import com.soufianekre.smallhub.ui.base.BaseMvpView;
import com.soufianekre.smallhub.ui.base.PaginationListener;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

interface SearchUsersMvp {
    interface Presenter<V extends View> extends BaseMvpPresenter<V>,
            BaseViewHolder.OnItemClickListener<User>,
            PaginationListener<String> {

            @NonNull
            ArrayList<User> getUsers();
    }

    interface View extends BaseMvpView,
            SwipeRefreshLayout.OnRefreshListener, android.view.View.OnClickListener{
        void onNotifyAdapter(@Nullable List<User> items, int page);

        void onSetTabCount(int count);

        void onSetSearchQuery(@NonNull String query);

        void onQueueSearch(@NonNull String query);

        @NonNull
        OnLoadMore<String> getLoadMore();

    }
}
