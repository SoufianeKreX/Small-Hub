package com.soufianekre.smallhub.ui.repos;


import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.RestProvider;
import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.helper.ActivityHelper;
import com.soufianekre.smallhub.helper.AppHelper;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.RxHelper;
import com.soufianekre.smallhub.ui.base.BasePresenter;
import com.soufianekre.smallhub.ui.repos.code.RepoCodePagerFragment;


public class RepoPagerPresenter<V extends RepoPagerMvp.View>
        extends BasePresenter<V> implements RepoPagerMvp.Presenter<V> {

    private static final String TAG = "RepoPagerPresenter";
    @com.evernote.android.state.State String login;
    @com.evernote.android.state.State String repoId;
    @com.evernote.android.state.State Repo repo;
    @com.evernote.android.state.State int navTyp;
    @com.evernote.android.state.State boolean isCollaborator;

    private void callApi(int navTyp) {
        if (InputHelper.isEmpty(login) || InputHelper.isEmpty(repoId)) return;

        getCompositeDisposable().add(RxHelper.getObservable(RestProvider.getRepoService().getRepo(login(),repoId()))
                .subscribe(repoModel -> {
                    repo = repoModel;
                    //updatePinned(repoModel);
                    getMvpView().onInitRepo();
                    getMvpView().onNavigationChanged(navTyp);
                    //onCheckStarring();
                    //onCheckWatching();
                    }, this::handleApiError));
    }

    @Override public void onError(@NonNull Throwable throwable) {
        handleApiError(throwable);
        super.onError(throwable);
    }

    @Override public void onUpdatePinnedEntry(@NonNull String repoId, @NonNull String login) {
        //manageDisposable(PinnedRepos.updateEntry(login + "/" + repoId));
    }

    @Override public void onActivityCreate(@NonNull String repoId, @NonNull String login, int navTyp) {
        this.login = login;
        this.repoId = repoId;
        this.navTyp = navTyp;
        if (getRepo() == null || !isApiCalled()) {
            callApi(navTyp);
        } else {
            getMvpView().onInitRepo();
        }
    }

    @NonNull @Override public String repoId() {
        return repoId;
    }

    @NonNull @Override public String login() {
        return login;
    }

    @Nullable
    @Override public Repo getRepo() {
        return repo;
    }




    @Override public void onModuleChanged(@NonNull FragmentManager fragmentManager, @RepoNavigationType int type) {
        Fragment currentVisible = ActivityHelper.getVisibleFragment(fragmentManager);


        RepoCodePagerFragment codePagerView = (RepoCodePagerFragment)
                AppHelper.getFragmentByTag(fragmentManager, RepoCodePagerFragment.TAG);
        /*
        RepoIssuesPagerFragment repoIssuesPagerView = (RepoIssuesPagerFragment)
                AppHelper.getFragmentByTag(fragmentManager, RepoIssuesPagerFragment.TAG);
        RepoPullRequestPagerFragment pullRequestPagerView = (RepoPullRequestPagerFragment)
                AppHelper.getFragmentByTag(fragmentManager, RepoPullRequestPagerFragment.TAG);
        RepoProjectsFragmentPager projectsFragmentPager = (RepoProjectsFragmentPager)
                AppHelper.getFragmentByTag(fragmentManager, RepoProjectsFragmentPager.Companion.getTAG());

         */


        if (getRepo() == null) {
            getMvpView().onFinishActivity();
            return;
        }
        if (currentVisible == null) return;


        switch (type) {
            case PROFILE:
                getMvpView().openUserProfile();
            case CODE:
                if (codePagerView == null) {
                    onAddAndHide(fragmentManager, RepoCodePagerFragment.newInstance(repoId(), login(),
                            getRepo().getHtmlUrl(), getRepo().getUrl(), getRepo().getDefaultBranch()),
                            currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, codePagerView, currentVisible);
                }
                break;
            case ISSUES:
                /*
                if ((!getRepo().isHasIssues())) {
                    sendToView(view -> view.showMessage(R.string.error, R.string.repo_issues_is_disabled));
                    break;
                }
                if (repoIssuesPagerView == null) {
                    onAddAndHide(fragmentManager, RepoIssuesPagerFragment.newInstance(repoId(), login()), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, repoIssuesPagerView, currentVisible);
                }

                 */
                break;
            case PULL_REQUEST:
                /*
                if (pullRequestPagerView == null) {
                    onAddAndHide(fragmentManager, RepoPullRequestPagerFragment.newInstance(repoId(), login()), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, pullRequestPagerView, currentVisible);
                }

                 */
                break;
            case PROJECTS:
                /*
                if (projectsFragmentPager == null) {
                    onAddAndHide(fragmentManager, RepoProjectsFragmentPager.Companion.newInstance(login(), repoId()), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, projectsFragmentPager, currentVisible);
                }

                 */
                break;
        }
    }

    @Override public void onShowHideFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment toShow, @NonNull Fragment toHide) {
        fragmentManager
                .beginTransaction()
                .hide(toHide)
                .show(toShow)
                .commit();
        toHide.onHiddenChanged(true);
        toShow.onHiddenChanged(false);
    }

    @Override public void onAddAndHide(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment toAdd, @NonNull Fragment toHide) {
        fragmentManager
                .beginTransaction()
                .hide(toHide)
                .add(R.id.container, toAdd, toAdd.getClass().getSimpleName())
                .commit();

        //noinspection ConstantConditions really android?
        if (toHide != null) toHide.onHiddenChanged(true);
        //noinspection ConstantConditions really android?
        if (toAdd != null) toAdd.onHiddenChanged(false);
    }


    @Override public void onMenuItemSelect(@IdRes int id, int position, boolean fromUser) {
        if (id == R.id.issues && (getRepo() != null && !getRepo().isHasIssues())) {
            //sendToView(RepoPagerMvp.View::disableIssueTab);
            return;
        }

        if (getMvpView() != null && isViewAttached() && fromUser) {
            getMvpView().onNavigationChanged(position);
        }
    }

    @Override public void onMenuItemReselect(@IdRes int id, int position, boolean fromUser) {}



}