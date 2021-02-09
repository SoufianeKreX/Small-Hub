package com.soufianekre.smallhub.ui.user;

import com.soufianekre.smallhub.ui.base.BaseMvpPresenter;
import com.soufianekre.smallhub.ui.base.BaseMvpView;

public interface UserPagerMvp {


    interface Presenter<V extends View> extends BaseMvpPresenter<V> {
        //void checkOrgMembership(@NonNull String org);
    }

    interface View extends BaseMvpView {

        void onInitOrg(boolean isMember);
    }
    interface TabsBadgeListener {
        void onSetBadge(int tabIndex, int count);
    }
}
