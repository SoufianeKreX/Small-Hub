package com.soufianekre.smallhub.ui.user.overview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.ui.base.BaseMvpPresenter;
import com.soufianekre.smallhub.ui.base.BaseMvpView;
import com.soufianekre.smallhub.ui.widgets.contributions.ContributionsDay;
import com.soufianekre.smallhub.ui.widgets.contributions.GitHubContributionsView;

import java.util.ArrayList;
import java.util.List;

interface UserOverviewMvp {
    interface Presenter<V extends View> extends BaseMvpPresenter<V> {


        void onFragmentCreated(@Nullable Bundle bundle);

        void onCheckFollowStatus(@NonNull String login);

        boolean isSuccessResponse();

        void onSendUserToView(@Nullable User userModel);

        void onLoadContributionWidget(@NonNull GitHubContributionsView view);

        @NonNull
        ArrayList<User> getOrgs();

        @NonNull ArrayList<ContributionsDay> getContributions();

        //@NonNull ArrayList<GetPinnedReposQuery.Node> getNodes();

        @NonNull String getLogin();

    }

    interface View extends BaseMvpView {

        void onInitViews(@Nullable User userModel);

        void onInitContributions(boolean show);

        void onInitOrgs(@Nullable List<User> orgs);

        void onUserNotFound();

        //void onInitPinnedRepos(@NonNull List<GetPinnedReposQuery.Node> nodes);
    }
}
