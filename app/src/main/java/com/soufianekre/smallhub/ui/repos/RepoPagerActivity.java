package com.soufianekre.smallhub.ui.repos;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import android.content.Intent;
import android.content.Context;
import android.text.format.Formatter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.evernote.android.state.State;
import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.model.LicenseModel;
import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.helper.AppBundler;
import com.soufianekre.smallhub.helper.AppHelper;
import com.soufianekre.smallhub.helper.BundleConstant;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.ParseDateFormat;
import com.soufianekre.smallhub.helper.PrefGetter;
import com.soufianekre.smallhub.helper.TypeFaceHelper;
import com.soufianekre.smallhub.helper.ViewHelper;
import com.soufianekre.smallhub.ui.base.BaseActivity;
import com.soufianekre.smallhub.ui.repos.code.RepoCodePagerFragment;
import com.soufianekre.smallhub.ui.widgets.AvatarLayout;
import com.soufianekre.smallhub.ui.widgets.ForegroundImageView;
import com.soufianekre.smallhub.ui.widgets.SpannableBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;

import static com.soufianekre.smallhub.ui.repos.RepoPagerMvp.Presenter.CODE;
import static com.soufianekre.smallhub.ui.repos.RepoPagerMvp.Presenter.ISSUES;
import static com.soufianekre.smallhub.ui.repos.RepoPagerMvp.Presenter.PROFILE;
import static com.soufianekre.smallhub.ui.repos.RepoPagerMvp.Presenter.PULL_REQUEST;

@SuppressLint("NonConstantResourceId")
public class RepoPagerActivity extends BaseActivity implements RepoPagerMvp.View {

    //@Inject
    RepoPagerPresenter<RepoPagerMvp.View> mPresenter;

    @BindView(R.id.toolbar)
    Toolbar repoPagerToolbar;
    @BindView(R.id.avatarLayout)
    AvatarLayout avatarLayout;
    @BindView(R.id.headerTitle)
    TextView title;
    @BindView(R.id.size)
    TextView size;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.forkRepo)
    TextView forkRepo;
    @BindView(R.id.starRepo) TextView starRepo;
    @BindView(R.id.watchRepo) TextView watchRepo;
    @BindView(R.id.license) TextView license;
    @BindView(R.id.bottomNavigation)
    BottomNavigation bottomNavigation;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.language) TextView language;
    @BindView(R.id.detailsIcon)
    android.view.View detailsIcon;
    @BindView(R.id.tagsIcon)
    android.view.View tagsIcon;
    @BindView(R.id.watchRepoImage)
    ForegroundImageView watchRepoImage;
    @BindView(R.id.starRepoImage) ForegroundImageView starRepoImage;
    @BindView(R.id.forkRepoImage) ForegroundImageView forkRepoImage;
    @BindView(R.id.licenseLayout)
    android.view.View licenseLayout;
    @BindView(R.id.watchRepoLayout)
    android.view.View watchRepoLayout;
    @BindView(R.id.starRepoLayout)
    android.view.View starRepoLayout;
    @BindView(R.id.forkRepoLayout)
    android.view.View forkRepoLayout;
    @BindView(R.id.pinImage) ForegroundImageView pinImage;
    @BindView(R.id.pinLayout)
    android.view.View pinLayout;
    @BindView(R.id.pinText) TextView pinText;

    @BindView(R.id.topicsList)
    RecyclerView topicsList;
    //@BindView(R.id.sortByUpdated) CheckBox sortByUpdated;
    @BindView(R.id.wikiLayout)
    android.view.View wikiLayout;
    @State
    @RepoPagerMvp.Presenter.RepoNavigationType
    int navType;
    @State String login;
    @State String repoId;
    @State int showWhich = -1;

    private NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private boolean userInteracted;
    private int accentColor;
    private int iconColor;


    public static Intent createIntent(@NonNull Context context, @NonNull String repoId, @NonNull String login) {
        return createIntent(context, repoId, login,CODE);
    }

    public static Intent createIntent(@NonNull Context context, @NonNull String repoId, @NonNull String login,
                                      @RepoPagerMvp.Presenter.RepoNavigationType int navType) {
        return createIntent(context, repoId, login, navType, -1);
    }

    public static Intent createIntent(@NonNull Context context, @NonNull String repoId, @NonNull String login,
                                      @RepoPagerMvp.Presenter.RepoNavigationType int navType, int showWhat) {
        Intent intent = new Intent(context, RepoPagerActivity.class);
        intent.putExtras(AppBundler.start()
                .put(BundleConstant.ID, repoId)
                .put(BundleConstant.EXTRA_TWO, login)
                .put(BundleConstant.EXTRA_TYPE, navType)
                .put(BundleConstant.EXTRA_THREE, showWhat)
                .end());
        return intent;
    }
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos_pager);
        mPresenter = new RepoPagerPresenter<>();
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(RepoPagerActivity.this);


        if (savedInstanceState == null) {
            if (getIntent() == null || getIntent().getExtras() == null) {
                finish();
                return;
            }
            final Bundle extras = getIntent().getExtras();
            repoId = extras.getString(BundleConstant.ID);
            login = extras.getString(BundleConstant.EXTRA_TWO);
            navType = extras.getInt(BundleConstant.EXTRA_TYPE);
            showWhich = extras.getInt(BundleConstant.EXTRA_THREE);
            mPresenter.onUpdatePinnedEntry(repoId, login);
        }

        mPresenter.onActivityCreate(repoId, login, navType);
        setTitle("");
        accentColor = ViewHelper.getAccentColor(this);
        iconColor = ViewHelper.getIconColor(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new DummyFragment(), "DummyFragment")
                    .commit();
        }
        Typeface myTypeface = TypeFaceHelper.getTypeface();
        bottomNavigation.setDefaultTypeface(myTypeface);

        bottomNavigation.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this
                ,R.color.material_light_white)));

        fab.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        showHideFab();
    }

    @Override
    public void onNavigationChanged(int navType) {
        if (navType == PROFILE) {
            mPresenter.onModuleChanged(getSupportFragmentManager(), navType);
            bottomNavigation.setSelectedIndex(this.navType, false);
            return;
        }
        this.navType = navType;
        //noinspection WrongConstant
        try {
            if (bottomNavigation.getSelectedIndex() != navType)
                bottomNavigation.setSelectedIndex(navType, false);

        } catch (Exception ignored) {}
        showHideFab();
        mPresenter.onModuleChanged(getSupportFragmentManager(), navType);
    }

    @Override
    public void onFinishActivity() {

    }

    @Override public void onInitRepo() {
        hideProgress();
        if (mPresenter.getRepo() == null) {
            return;
        }

        showWhich = -1;
        //setTaskName(getPresenter().getRepo().getFullName());
        Repo repoModel = mPresenter.getRepo();
        /*
        if (repoModel.isHasProjects()) {
            bottomNavigation.inflateMenu(R.menu.repo_with_project_bottom_nav_menu);
        }

         */
        bottomNavigation.setOnMenuItemClickListener(mPresenter);
        topicsList.setVisibility(android.view.View.GONE);
        /*
        if (repoModel.getTopics() != null && !repoModel.getTopics().isEmpty()) {
            tagsIcon.setVisibility(View.VISIBLE);
            topicsList.setAdapter(new TopicsAdapter(repoModel.getTopics()));
        } else {

        }
         */
        //onRepoPinned(AbstractPinnedRepos.isPinned(repoModel.getFullName()));


        wikiLayout.setVisibility(repoModel.isHasWiki() ? android.view.View.VISIBLE : android.view.View.GONE);
        pinText.setText(numberFormat.format(repoModel.getWatchers()));

        detailsIcon.setVisibility(InputHelper.isEmpty(repoModel.getDescription()) ? android.view.View.GONE : android.view.View.VISIBLE);
        language.setVisibility(InputHelper.isEmpty(repoModel.getLanguage()) ? android.view.View.GONE : android.view.View.VISIBLE);
        if (!InputHelper.isEmpty(repoModel.getLanguage())) {
            language.setText(repoModel.getLanguage());
            //language.setTextColor(ColorsProvider.getColorAsColor(repoModel.getLanguage(), language.getContext()));
        }
        forkRepo.setText(numberFormat.format(repoModel.getForksCount()));
        starRepo.setText(numberFormat.format(repoModel.getStargazersCount()));
        watchRepo.setText(numberFormat.format(repoModel.getWatchers()));
        if (repoModel.getOwner() != null) {
            avatarLayout.setUrl(repoModel.getOwner().getAvatarUrl(), repoModel.getOwner().getLogin(),
                    repoModel.getOwner().isOrganizationType(), false);
        }


        long repoSize = repoModel.getSize() > 0 ? (repoModel.getSize() * 1000) : repoModel.getSize();
        date.setText(SpannableBuilder.builder()
                .append(ParseDateFormat.getTimeAgo(repoModel.getPushedAt()))
                .append(" ,")
                .append(" ")
                .append(Formatter.formatFileSize(this, repoSize)));

        size.setVisibility(android.view.View.GONE);
        title.setText(repoModel.getFullName());
        TextViewCompat.setTextAppearance(title, R.style.TextAppearance_AppCompat_Medium);
        title.setTextColor(ViewHelper.getPrimaryTextColor(this));


        if (repoModel.getLicense() != null) {
            licenseLayout.setVisibility(android.view.View.VISIBLE);
            LicenseModel licenseModel = repoModel.getLicense();
            license.setText(!InputHelper.isEmpty(licenseModel.getSpdxId()) ? licenseModel.getSpdxId() : licenseModel.getName());
        }

        supportInvalidateOptionsMenu();
        //if (!PrefGetter.isRepoGuideShowed()) {}

    }


    @Override
    public void openUserProfile() {
        //String login = mPresenter.getRepo().getOwner().getLogin();
        //if (login != null) UserPagerActivity.startActivity(this,login, false, -1);
    }

    @Override
    public void onScrolled(boolean isUp) {

    }

    @Override public void onBackPressed() {
        if (navType == CODE) {
            RepoCodePagerFragment codePagerView =
                    (RepoCodePagerFragment) AppHelper.getFragmentByTag(getSupportFragmentManager(),
                    RepoCodePagerFragment.TAG);
            if (codePagerView != null) {
                if (codePagerView.canPressBack()) {
                    super.onBackPressed();
                } else {
                    codePagerView.onBackPressed();
                    return;
                }
            }

        }
    }


    private void showHideFab() {
        if (navType == ISSUES) {
            fab.setImageResource(R.drawable.ic_menu);
            fab.show();
            PrefGetter.isRepoFabHintShowed();
        } else if (navType == PULL_REQUEST) {
            fab.setImageResource(R.drawable.ic_search);
            fab.show();
        } else {
            fab.hide();
        }
    }


    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

}