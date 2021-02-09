package com.soufianekre.smallhub.ui.repos.code.contributors;


import android.os.Bundle;

import androidx.annotation.NonNull;

import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.ui.base.BaseMvpPresenter;
import com.soufianekre.smallhub.ui.base.PaginationListener;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import java.util.ArrayList;

public interface RepoContributorsMvpPresenter<V extends RepoContributorsMvpView> extends BaseMvpPresenter<V>
    , BaseViewHolder.OnItemClickListener<User>,
        PaginationListener {

        void onFragmentCreated(@NonNull Bundle bundle);

        @NonNull ArrayList<User> getUsers();

}