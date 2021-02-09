package com.soufianekre.smallhub.ui.user.repos;

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

interface UserReposMvp {
    interface Presenter<V extends View> extends BaseMvpPresenter<V>,
            BaseViewHolder.OnItemClickListener<Repo>,
            PaginationListener<String> {

        //void onNotifyAdapter(@Nullable List<Repo> items, int page);

        @NonNull
        ArrayList<Repo> getRepos();
        /*
        void onFilterApply();
        void onTypeSelected(String selectedType);
        void onSortOptionSelected(String selectedSortOption);
        void onSortDirectionSelected(String selectedSortDirection);

         */

    }

    interface View extends BaseMvpView, SwipeRefreshLayout.OnRefreshListener,
     android.view.View.OnClickListener {

        void onNotifyAdapter(@Nullable List<Repo> items, int page);
        @NonNull
        OnLoadMore<String> getLoadMore();
        void showRepo(Repo item);
    }
}
