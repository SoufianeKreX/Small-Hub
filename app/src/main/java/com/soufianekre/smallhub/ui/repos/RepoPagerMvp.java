package com.soufianekre.smallhub.ui.repos;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.ui.base.BaseMvpPresenter;
import com.soufianekre.smallhub.ui.base.BaseMvpView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public interface RepoPagerMvp {


    interface Presenter<V extends View> extends BaseMvpPresenter<V>
            , BottomNavigation.OnMenuItemSelectionListener {

        int CODE = 0;
        int ISSUES = 1;
        int PULL_REQUEST = 2;
        int PROJECTS = 3;
        int PROFILE = 4;

        @IntDef({
                CODE,
                ISSUES,
                PULL_REQUEST,
                PROJECTS,
                PROFILE })
        @Retention(RetentionPolicy.SOURCE) @interface RepoNavigationType {}


        void onUpdatePinnedEntry(@NonNull String repoId, @NonNull String login);

        void onActivityCreate(@NonNull String repoId, @NonNull String login, @RepoNavigationType int navTyp);

        @NonNull String repoId();

        @NonNull String login();

        @Nullable
        Repo getRepo();

        void onModuleChanged(@NonNull FragmentManager fragmentManager, @RepoNavigationType int type);

        void onShowHideFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment toShow, @NonNull Fragment toHide);

        void onAddAndHide(@NonNull FragmentManager fragmentManager, @NonNull Fragment toAdd, @NonNull Fragment toHide);





    }

    interface View extends BaseMvpView {


        void onNavigationChanged(@Presenter.RepoNavigationType int navType);

        void onFinishActivity();

        void onInitRepo();

        //boolean hasUserInteractedWithView();

        //void disableIssueTab();

        void openUserProfile();

        void onScrolled(boolean isUp);

        //boolean isCollaborator();
    }
}
