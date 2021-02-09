package com.soufianekre.smallhub.ui.user.overview;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.evernote.android.state.State;
import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.helper.AppBundler;
import com.soufianekre.smallhub.helper.BundleConstant;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.ParseDateFormat;
import com.soufianekre.smallhub.ui.base.BaseFragment;
import com.soufianekre.smallhub.ui.widgets.AvatarLayout;
import com.soufianekre.smallhub.ui.widgets.SpannableBuilder;
import com.soufianekre.smallhub.ui.adapters.ProfileOrgsAdapter;
import com.soufianekre.smallhub.ui.widgets.contributions.GitHubContributionsView;
import com.soufianekre.smallhub.ui.widgets.recycler_view.DynamicRecyclerView;
import com.soufianekre.smallhub.ui.widgets.recycler_view.layout_manager.GridManager;

//import github.GetPinnedReposQuery;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UserOverviewFragment extends BaseFragment implements UserOverviewMvp.View {

    //@Inject

    UserOverviewPresenter<UserOverviewMvp.View> mPresenter;

    //widgets

    @BindView(R.id.contributionsCaption) TextView contributionsCaption;
    @BindView(R.id.organizationsCaption) TextView organizationsCaption;
    @BindView(R.id.userInformation) LinearLayout userInformation;
    @BindView(R.id.username) TextView username;
    @BindView(R.id.fullname) TextView fullName;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindView(R.id.organization) TextView organization;
    @BindView(R.id.location) TextView location;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.link) TextView link;
    @BindView(R.id.joined) TextView joined;
    @BindView(R.id.following) Button following;
    @BindView(R.id.followers) Button followers;
    @BindView(R.id.progress)
    android.view.View progress;

    @BindView(R.id.orgsList)
    DynamicRecyclerView orgsList;


    @BindView(R.id.orgsCard)
    CardView orgsCard;
    @BindView(R.id.parentView)
    NestedScrollView parentView;
    @BindView(R.id.contributionView)
    GitHubContributionsView contributionView;
    @BindView(R.id.contributionCard) CardView contributionCard;


    @BindView(R.id.pinnedReposTextView) TextView pinnedReposTextView;
    @BindView(R.id.pinnedList) DynamicRecyclerView pinnedList;
    @BindView(R.id.pinnedReposCard) CardView pinnedReposCard;
    @State
    User userModel;
    //private ProfilePagerMvp.View profileCallback;


    public static UserOverviewFragment newInstance(@NonNull String username) {
        UserOverviewFragment fragment = new UserOverviewFragment();
        fragment.setArguments(AppBundler.start().put(BundleConstant.EXTRA,username).end());
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserOverviewPresenter<>();
        //getActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View v = inflater.inflate(R.layout.fragment_user_overview,container,false);
        setUnBinder(ButterKnife.bind(this,v));
        mPresenter.onAttach(UserOverviewFragment.this);
        return v;
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //onInitPinnedRepos(mPresenter.getNodes());

        if (savedInstanceState == null) {
            mPresenter.onFragmentCreated(getArguments());
        } else {
            if (userModel != null) {
                onInitViews(userModel);
            } else {
                mPresenter.onFragmentCreated(getArguments());
            }
        }
        onInitOrgs(mPresenter.getOrgs());


    }


    @SuppressLint("ClickableViewAccessibility")
    @Override public void onInitViews(@Nullable User userModel) {
        progress.setVisibility(GONE);
        if (userModel == null) return;
        //if (profileCallback != null) profileCallback.onCheckType(userModel.isOrganizationType());
        if (getView() != null) {
            if (this.userModel == null) {
                //animation
                TransitionManager.beginDelayedTransition((ViewGroup) getView(),
                        new AutoTransition().addListener(new Transition.TransitionListener() {

                            @Override public void onTransitionStart(@NonNull Transition transition) {

                            }

                            @Override public void onTransitionEnd(@NonNull Transition transition) {
                                if (contributionView != null){
                                    mPresenter.onLoadContributionWidget(contributionView);
                                }

                            }

                            @Override public void onTransitionCancel(@NonNull Transition transition) {

                            }

                            @Override public void onTransitionPause(@NonNull Transition transition) {

                            }

                            @Override public void onTransitionResume(@NonNull Transition transition) {

                            }
                        }));
            } else {
                mPresenter.onLoadContributionWidget(contributionView);
            }
        }
        this.userModel = userModel;
        username.setText(userModel.getLogin());
        fullName.setText(userModel.getName());
        if (userModel.getBio() != null) {
            description.setText(userModel.getBio());
        } else {
            description.setVisibility(GONE);
        }

        avatarLayout.setUrl(userModel.getAvatarUrl(), null, false, true);

        avatarLayout.findViewById(R.id.avatar).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //ActivityHelper.startCustomTab(getActivity(), userModel.getAvatarUrl());
                return true;
            }
            return false;
        });

        organization.setText(userModel.getCompany());
        location.setText(userModel.getLocation());
        email.setText(userModel.getEmail());
        link.setText(userModel.getBlog());
        joined.setText(ParseDateFormat.getTimeAgo(userModel.getCreatedAt()));
        if (InputHelper.isEmpty(userModel.getCompany())) {
            organization.setVisibility(GONE);
        }
        if (InputHelper.isEmpty(userModel.getLocation())) {
            location.setVisibility(GONE);
        }
        if (InputHelper.isEmpty(userModel.getEmail())) {
            email.setVisibility(GONE);
        }
        if (InputHelper.isEmpty(userModel.getBlog())) {
            link.setVisibility(GONE);
        }
        if (InputHelper.isEmpty(userModel.getCreatedAt())) {
            joined.setVisibility(GONE);
        }
        followers.setText(SpannableBuilder.builder()
                .append(getString(R.string.followers))
                .append(" (")
                .bold(String.valueOf(userModel.getFollowers()))
                .append(")"));

        following.setText(SpannableBuilder.builder()
                .append(getString(R.string.following))
                .append(" (")
                .bold(String.valueOf(userModel.getFollowing()))
                .append(")"));
    }


    @Override public void onInitContributions(boolean show) {
        if (contributionView == null) return;
        if (show) {
            contributionView.onResponse();
        }
        contributionCard.setVisibility(show ? VISIBLE : GONE);
        contributionsCaption.setVisibility(show ? VISIBLE : GONE);
    }

    @Override public void onInitOrgs(@Nullable List<User> orgs) {

        if (orgs != null && !orgs.isEmpty()) {
            orgsList.setNestedScrollingEnabled(false);
            ProfileOrgsAdapter adapter = new ProfileOrgsAdapter();
            adapter.addItems(orgs);
            orgsList.setAdapter(adapter);
            orgsCard.setVisibility(VISIBLE);
            organizationsCaption.setVisibility(VISIBLE);
            ((GridManager) orgsList.getLayoutManager())
                    .setIconSize(getResources().getDimensionPixelSize(R.dimen.header_icon_zie) + getResources()
                    .getDimensionPixelSize(R.dimen.spacing_xs_large));
            hideProgress();
        } else {
            organizationsCaption.setVisibility(GONE);
            orgsCard.setVisibility(GONE);
            hideProgress();
        }


    }

    @Override public void onUserNotFound() {
        showMessage(R.string.no_user_found);
    }

    /*
    @Override public void onInitPinnedRepos(@NonNull List<GetPinnedReposQuery.Node> nodes) {
        if (pinnedReposTextView == null) return;
        if (!nodes.isEmpty()) {
            pinnedReposTextView.setVisibility(VISIBLE);
            pinnedReposCard.setVisibility(VISIBLE);

            ProfileOrgsAdapter adapter = new ProfileOrgsAdapter(nodes);
            adapter.setListener(new BaseViewHolder.OnItemClickListener<GetPinnedReposQuery.Node>() {
                @Override public void onItemClick(int position, View v, GetPinnedReposQuery.Node item) {
                    //SchemeParser.launchUri(getContext(), item.url().toString());
                }

                @Override public void onItemLongClick(int position, View v, GetPinnedReposQuery.Node item) {}
            });
            pinnedList.addDivider();
            pinnedList.setAdapter(adapter);
        } else {
            pinnedReposTextView.setVisibility(GONE);
            pinnedReposCard.setVisibility(GONE);
        }


    }

     */




    public void showProgress(@StringRes int resId) {
        progress.setVisibility(VISIBLE);
    }

    public void hideProgress() {
        progress.setVisibility(GONE);
    }


    @Override public void showMessage( int msgRes) {
        onHideProgress();
        super.showMessage(msgRes);
    }
    /*
    @Override public void onScrollTop(int index) {
        super.onScrollTop(index);
    }

     */

    private void onHideProgress() {
        hideProgress();
    }


}