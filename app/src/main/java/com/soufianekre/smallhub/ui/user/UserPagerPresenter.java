package com.soufianekre.smallhub.ui.user;


import com.soufianekre.smallhub.ui.base.BasePresenter;

public class UserPagerPresenter<V extends UserPagerMvp.View> extends BasePresenter<V>
        implements UserPagerMvp.Presenter<V> {

    private static final String TAG = "UserPagerPresenter";

    public UserPagerPresenter() {
    }

    /*
    @Override public void checkOrgMembership(@NonNull String org) {
        makeRestCall(RestProvider.getOrgService(isEnterprise()).isMember(org, Login.getUser().getLogin()),
                booleanResponse -> sendToView(view -> {
                    isMember = booleanResponse.code() == 204 ? 1 : 0;
                    view.onInitOrg(isMember == 1);
                }));
    }

     */

}