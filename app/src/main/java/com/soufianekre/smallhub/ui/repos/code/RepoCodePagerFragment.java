package com.soufianekre.smallhub.ui.repos.code;

import android.os.Bundle;
//import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.annimon.stream.Objects;
import com.annimon.stream.Stream;
import com.evernote.android.state.State;
import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.models.FragmentPagerAdapterModel;
import com.soufianekre.smallhub.data.models.TabsCountStateModel;
import com.soufianekre.smallhub.helper.AppBundler;
import com.soufianekre.smallhub.helper.BundleConstant;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.ViewHelper;
import com.soufianekre.smallhub.ui.base.BaseFragment;
import com.soufianekre.smallhub.ui.widgets.SpannableBuilder;
import com.soufianekre.smallhub.ui.widgets.ViewPagerView;
import com.soufianekre.smallhub.ui.adapters.FragmentsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.HashSet;

public class RepoCodePagerFragment extends BaseFragment implements RepoCodeMvp.View {

    public static final String TAG = RepoCodePagerFragment.class.getSimpleName();


    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPagerView pager;

    @State
    HashSet<TabsCountStateModel> counts = new HashSet<>();



    private RepoCodePagerPresenter<RepoCodeMvp.View> mPresenter;


    public static RepoCodePagerFragment newInstance(@NonNull String repoId,@NonNull String login,@NonNull String htmlLink,
                                                    @NonNull String url, @NonNull String defaultBranch) {
        RepoCodePagerFragment fragment = new RepoCodePagerFragment();
        fragment.setArguments(AppBundler.start()
            .put(BundleConstant.ID,repoId)
            .put(BundleConstant.EXTRA,login)
            .put(BundleConstant.EXTRA_TWO, url)
            .put(BundleConstant.EXTRA_THREE,defaultBranch)
            .put(BundleConstant.EXTRA_FOUR,htmlLink)
            .end());

        return fragment;
    }
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View v = inflater.inflate(R.layout.fragment_repo_code_pager,container,false);
        mPresenter = new RepoCodePagerPresenter<>();

        setUnBinder(ButterKnife.bind(this,v));
        mPresenter.onAttach(RepoCodePagerFragment.this);

        return v;
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            String repoId = getArguments().getString(BundleConstant.ID);
            String login = getArguments().getString(BundleConstant.EXTRA);
            String url = getArguments().getString(BundleConstant.EXTRA_TWO);
            String defaultBranch = getArguments().getString(BundleConstant.EXTRA_THREE);
            String htmlUrl = getArguments().getString(BundleConstant.EXTRA_FOUR);

            if (InputHelper.isEmpty(repoId) || InputHelper.isEmpty(login) || InputHelper.isEmpty(url) || InputHelper.isEmpty(htmlUrl)) {
                return;
            }
            pager.setAdapter(new FragmentsPagerAdapter(getChildFragmentManager(),
                    FragmentPagerAdapterModel.buildForRepoCode(getContext(), repoId, login, url,
                            Objects.toString(defaultBranch, "master"), htmlUrl)));


            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabs.setupWithViewPager(pager);
        }

        if (savedInstanceState != null && !counts.isEmpty()) {
            Stream.of(counts).forEach(this::updateCount);
        }
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
            @Override public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
                onScrollTop(tab.getPosition());
            }

        });
    }



    @Override
    public void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override public boolean canPressBack() {
        if (pager.getCurrentItem() != 1) return true;

        //RepoFilePathFragment pathView = (RepoFilePathFragment) pager.getAdapter().instantiateItem(pager, 1);
        //return pathView == null || pathView.canPressBack();
        return false;
    }

    @Override public void onBackPressed() {
        if (pager != null && pager.getAdapter() != null) {
            /*
            RepoFilePathFragment pathView = (RepoFilePathFragment) pager.getAdapter().instantiateItem(pager, 1);
            if (pathView != null) {
                pathView.onBackPressed();
            }

             */
        }
    }

    @Override public void onSetBadge(int tabIndex, int count) {
        TabsCountStateModel model = new TabsCountStateModel();
        model.setTabIndex(tabIndex);
        model.setCount(count);
        counts.add(model);
        if (tabs != null) {
            updateCount(model);
        }
    }

    private void updateCount(@NonNull TabsCountStateModel model) {
        TextView tv = ViewHelper.getTabTextView(tabs, model.getTabIndex());
        tv.setText(SpannableBuilder.builder()
                .append(getString(R.string.commits))
                .append("   ")
                .append("(")
                .bold(String.valueOf(model.getCount()))
                .append(")"));
    }

    @Override
    public void showProgress(int ResId) {

    }

    @Override
    public void hideProgress() {

    }
    @Override
    public void onScrollTop(int position) {

    }
}
