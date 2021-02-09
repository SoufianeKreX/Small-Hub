package com.soufianekre.smallhub.ui.user;

import android.app.Application;
import android.app.Service;
import android.os.Bundle;


import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.annimon.stream.Stream;

import com.evernote.android.state.State;
import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.models.FragmentPagerAdapterModel;
import com.soufianekre.smallhub.data.models.TabsCountStateModel;
import com.soufianekre.smallhub.helper.AppBundler;
import com.soufianekre.smallhub.helper.BundleConstant;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.ViewHelper;
import com.soufianekre.smallhub.ui.base.BaseActivity;
import com.soufianekre.smallhub.ui.user.UserPagerMvp.View;
import com.soufianekre.smallhub.ui.widgets.SpannableBuilder;
import com.soufianekre.smallhub.ui.widgets.ViewPagerView;
import com.soufianekre.smallhub.ui.adapters.FragmentsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.HashSet;

public class UserPagerActivity extends BaseActivity implements View {

    //@Inject
    UserPagerPresenter<View> mPresenter;
    //widget
    @BindView(R.id.user_pager_toolbar)
    Toolbar userPagerToolbar;
    @BindView(R.id.user_pager_tabs)
    TabLayout tabs;
    @BindView(R.id.user_pager_tabbedPager)
    ViewPagerView pager;
    @BindView(R.id.user_pager_fab)
    FloatingActionButton fab;
    @State int index;
    @State String login;
    @State boolean isOrg;
    @State
    HashSet<TabsCountStateModel> counts = new HashSet<>();

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, UserPagerActivity.class);
        return intent;
    }
    public static void startActivity(@NonNull Context context, @NonNull String login, boolean isOrg, int index) {
        context.startActivity(createIntent(context, login, isOrg, index));
    }

    public static Intent createIntent(@NonNull Context context, @NonNull String login) {
        return createIntent(context, login, false);
    }

    public static Intent createIntent(@NonNull Context context, @NonNull String login, boolean isOrg) {
        return createIntent(context, login, isOrg,-1);
    }

    public static Intent createIntent(@NonNull Context context, @NonNull String login, boolean isOrg, int index) {
        Intent intent = new Intent(context, UserPagerActivity.class);
        intent.putExtras(AppBundler.start()
                .put(BundleConstant.EXTRA, login)
                .put(BundleConstant.EXTRA_TYPE, isOrg)
                .put(BundleConstant.EXTRA_TWO, index)
                .end());
        if (context instanceof Service || context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pager);
        mPresenter = new UserPagerPresenter<>();
        //getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(UserPagerActivity.this);

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getExtras() != null) {
                login = getIntent().getExtras().getString(BundleConstant.EXTRA);
                isOrg = getIntent().getExtras().getBoolean(BundleConstant.EXTRA_TYPE);
                index = getIntent().getExtras().getInt(BundleConstant.EXTRA_TWO, -1);
                if (!InputHelper.isEmpty(login)) {
                    if (isOrg) {
                        //mPresenter.checkOrgMembership(login);
                    }
                }
            }
        }


        if (InputHelper.isEmpty(login)) {
            finish();
            return;
        }
        setSupportActionBar(userPagerToolbar);
        userPagerToolbar.setTitle(login);
        userPagerToolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        //setTaskName(login);
        setTitle(login);

        if (!isOrg) {
            FragmentsPagerAdapter adapter = new FragmentsPagerAdapter(getSupportFragmentManager(),
                    FragmentPagerAdapterModel.buildForProfile(this, login));
            pager.setAdapter(adapter);
            tabs.setTabGravity(TabLayout.GRAVITY_FILL);
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabs.setupWithViewPager(pager);
            if (savedInstanceState == null) {
                if (index != -1) {
                    pager.setCurrentItem(index);
                }
            }
        } else {
            /*
            if (mPresenter.getIsMember() == -1) {
                mPresenter.checkOrgMembership(login);
            } else {
                onInitOrg(mPresenter.isMember == 1);
            }

             */
        }
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
            @Override public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
                //onScrollTop(tab.getPosition());
            }
        });
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override public void onPageSelected(int position) {
                super.onPageSelected(position);
                hideShowFab(position);
            }
        });
        if (!isOrg) {
            if (savedInstanceState != null && !counts.isEmpty()) {
                Stream.of(counts).forEach(this::updateCount);
            }
        }


        hideShowFab(pager.getCurrentItem());
    }


    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onInitOrg(boolean isMember) {

    }

    private void updateCount(@NonNull TabsCountStateModel model) {
        TextView tv = ViewHelper.getTabTextView(tabs, model.getTabIndex());
        tv.setText(SpannableBuilder.builder()
                .append(getString(R.string.starred))
                .append("   ")
                .append("(")
                .bold(String.valueOf(model.getCount()))
                .append(")"));
    }

    private void hideShowFab(int position) {
        if (isOrg) {
            /*
            if (mPresenter.getIsMember() == 1) {
                if (position == 2) {
                    fab.show();
                } else {
                    fab.hide();
                }
            } else {
                if (position == 1) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        } else {
            if (position == 2) {
                fab.show();
            } else {
                fab.hide();
            }
             */
        }

    }


}