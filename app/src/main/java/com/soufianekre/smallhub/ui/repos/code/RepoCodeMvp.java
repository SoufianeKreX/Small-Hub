package com.soufianekre.smallhub.ui.repos.code;

import com.soufianekre.smallhub.ui.base.BaseMvpPresenter;
import com.soufianekre.smallhub.ui.base.BaseMvpView;
import com.soufianekre.smallhub.ui.user.UserPagerMvp;

interface RepoCodeMvp {
    interface Presenter<V extends View> extends BaseMvpPresenter<V> {

    }

    interface View extends BaseMvpView, UserPagerMvp.TabsBadgeListener {
        boolean canPressBack();
        void onBackPressed();
        void onScrollTop(int position);
    }
}
