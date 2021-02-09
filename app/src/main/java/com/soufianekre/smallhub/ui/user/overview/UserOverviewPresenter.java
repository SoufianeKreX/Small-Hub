package com.soufianekre.smallhub.ui.user.overview;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.soufianekre.smallhub.data.network.RestProvider;
import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.helper.BundleConstant;
import com.soufianekre.smallhub.helper.RxHelper;
import com.soufianekre.smallhub.ui.base.BasePresenter;
import com.soufianekre.smallhub.ui.widgets.contributions.ContributionsDay;
import com.soufianekre.smallhub.ui.widgets.contributions.ContributionsProvider;
import com.soufianekre.smallhub.ui.widgets.contributions.GitHubContributionsView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class UserOverviewPresenter<V extends UserOverviewMvp.View>
        extends BasePresenter<V> implements UserOverviewMvp.Presenter<V> {

    private static final String TAG = "UserOverviewPresenter";

    @com.evernote.android.state.State boolean isSuccessResponse;
    @com.evernote.android.state.State boolean isFollowing;
    @com.evernote.android.state.State String login;
    private ArrayList<User> userOrgs = new ArrayList<>();
    //private ArrayList<GetPinnedReposQuery.Node> nodes = new ArrayList<>();
    private ArrayList<ContributionsDay> contributions = new ArrayList<>();
    private static final String URL = "https://github.com/users/%s/contributions";

    @Override public void onCheckFollowStatus(@NonNull String login) {
        if (!TextUtils.equals(login, "")) {
            getCompositeDisposable().add(RxHelper.getObservable(RestProvider.getUserRestService().getFollowStatus(login))
                    .subscribe(booleanResponse -> {
                        isSuccessResponse = true;
                        isFollowing = booleanResponse.code() == 204;
                    }, Throwable::printStackTrace));
        }
    }

    @Override public boolean isSuccessResponse() {
        return isSuccessResponse;
    }


    @Override public void onError(@NonNull Throwable throwable) {
        /*
        int statusCode = RestProvider.getErrorCode(throwable);
        if (statusCode == 404) {
            sendToView(ProfileOverviewMvp.View::onUserNotFound);
            return;
        }
        if (!InputHelper.isEmpty(login)) {
            onWorkOffline(login);
        }


         */
        super.onError(throwable);
    }

    @Override public void onFragmentCreated(@Nullable Bundle bundle) {
        if (bundle == null || bundle.getString(BundleConstant.EXTRA) == null) {
            throw new NullPointerException("Either bundle or User is null");
        }
        login = bundle.getString(BundleConstant.EXTRA);
        if (login != null) {
            makeRestCall(RestProvider.getUserRestService().getUser(login)
                    .doOnComplete(() -> {
                        //loadPinnedRepos(login);
                        loadOrgs();
                    }), userModel -> {
                onSendUserToView(userModel);
                if (userModel != null) {
                    if (userModel.getType() != null && userModel.getType().equalsIgnoreCase("user")) {
                        onCheckFollowStatus(login);
                    }
                }
            });
        }
    }
    /*
    @SuppressWarnings("ConstantConditions")
    private void loadPinnedRepos(@NonNull String login) {
        ApolloCall<GetPinnedReposQuery.Data> apolloCall = ApolloProdivder.INSTANCE.getApollo(isEnterprise())
                .query(GetPinnedReposQuery.builder()
                        .login(login)
                        .build());
        getCompositeDisposable().add(RxHelper.getObservable(Rx2Apollo.from(apolloCall))
                .filter(dataResponse -> !dataResponse.hasErrors())
                .flatMap(dataResponse -> {
                    if (dataResponse.data() != null && dataResponse.data().user() != null) {
                        return Observable.fromIterable(dataResponse.data().user().pinnedRepositories().edges());
                    }
                    return Observable.empty();
                })
                .map(GetPinnedReposQuery.Edge::node)
                .toList()
                .toObservable()
                .subscribe(nodes1 -> {
                    nodes.clear();
                    nodes.addAll(nodes1);
                    view.onInitPinnedRepos(nodes);
                }, Throwable::printStackTrace));
    }

     */


    @Override public void onSendUserToView(@Nullable User userModel) {
        getMvpView().onInitViews(userModel);
    }

    @Override
    public void onLoadContributionWidget(@NonNull GitHubContributionsView gitHubContributionsView) {
        if (contributions == null || contributions.isEmpty()) {
            String url = String.format(URL, login);

            getCompositeDisposable().add(RxHelper.getObservable(RestProvider.getContribution().getContributions(url))
                    .flatMap(s -> Observable.just(new ContributionsProvider().getContributions(s)))
                    .subscribe(lists -> {
                        contributions.clear();
                        contributions.addAll(lists);
                        loadContributions(contributions, gitHubContributionsView);
                    }, Throwable::printStackTrace));

        } else {
            loadContributions(contributions, gitHubContributionsView);
        }

    }

    @NonNull @Override public ArrayList<User> getOrgs() {
        return userOrgs;
    }

    @NonNull @Override public ArrayList<ContributionsDay> getContributions() {
        return contributions;
    }

    /*
    @NonNull @Override public ArrayList<GetPinnedReposQuery.Node> getNodes() {
        return nodes;
    }

     */



    @NonNull @Override public String getLogin() {
        return login;
    }

    private void loadContributions(ArrayList<ContributionsDay> contributions
            , GitHubContributionsView gitHubContributionsView) {

        List<ContributionsDay> filter = gitHubContributionsView.getLastContributions(contributions);
        if (filter != null) {
            Observable<Bitmap> bitmapObservable = Observable
                    .just(gitHubContributionsView.drawOnCanvas(filter, contributions))
                    .doOnNext(bitmap ->
                            getMvpView().onInitContributions(bitmap != null));
            getCompositeDisposable().add(RxHelper.getObservable(bitmapObservable)
                    .subscribe(t ->{}, Throwable::printStackTrace));
        }
    }

    private void loadOrgs() {

        getCompositeDisposable().add(RxHelper.getObservable(RestProvider.getOrgService().getUserOrganizations(login))
                .subscribe(response -> {
                    if (response != null && response.getItems() != null) {
                        userOrgs.addAll(response.getItems());
                    }
                    getMvpView().onInitOrgs(userOrgs);
                }, Throwable::printStackTrace));


    }


}