package com.soufianekre.smallhub.ui.repos.code.read_me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.evernote.android.state.State;
import com.google.android.material.appbar.AppBarLayout;
import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.helper.ActivityHelper;
import com.soufianekre.smallhub.helper.AppBundler;
import com.soufianekre.smallhub.helper.AppHelper;
import com.soufianekre.smallhub.helper.BundleConstant;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.PrefGetter;
import com.soufianekre.smallhub.ui.base.BaseFragment;
import com.soufianekre.smallhub.ui.web_view.CustomizedWebView;
import com.soufianekre.smallhub.ui.widgets.StateLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class ViewerFragment extends BaseFragment implements ViewerMvp.View,
        AppBarLayout.OnOffsetChangedListener {

    public static final String TAG = ViewerFragment.class.getSimpleName();
    @BindView(R.id.readmeLoader)
    ProgressBar loader;
    @BindView(R.id.webView)
    CustomizedWebView webView;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @State
    boolean isWrap = PrefGetter.isWrapCode();
    private ViewerPresenter<ViewerMvp.View> mPresenter;
    private AppBarLayout appBarLayout;
    private BottomNavigation bottomNavigation;
    private boolean isAppBarMoving;
    private boolean isAppBarExpanded = true;
    private boolean isAppBarListener;

    public static ViewerFragment newInstance(@NonNull String url, @Nullable String htmlUrl) {
        return newInstance(url, htmlUrl, false);
    }

    public static ViewerFragment newInstance(@NonNull String url, boolean isRepo) {
        return newInstance(url, null, isRepo);
    }

    public static ViewerFragment newInstance(@NonNull String url, @Nullable String htmlUrl, boolean isRepo) {
        return newInstance(AppBundler.start()
                .put(BundleConstant.ITEM, url)
                .put(BundleConstant.EXTRA_TWO, htmlUrl)
                .put(BundleConstant.EXTRA, isRepo)
                .end());
    }

    private static ViewerFragment newInstance(@NonNull Bundle bundle) {
        ViewerFragment fragmentView = new ViewerFragment();
        fragmentView.setArguments(bundle);
        return fragmentView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_code_viewer_readme, container, false);
        mPresenter = new ViewerPresenter<>();
        setUnBinder(ButterKnife.bind(this, v));
        mPresenter.onAttach(this);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //stateLayout.setVisibility(View.GONE);
        stateLayout.setEmptyText(R.string.no_data);
        stateLayout.setOnReloadListener(view1 -> {
                    mPresenter.onHandleIntent(getArguments());
                }
        );
        if (savedInstanceState == null) {
            stateLayout.showReload(0);

        }

        if (InputHelper.isEmpty(mPresenter.downloadedStream())) {
            mPresenter.onHandleIntent(getArguments());
            Log.e("Viewer", "handle intent");
        } else {
            if (mPresenter.isMarkDown()) {
                onSetMdText(mPresenter.downloadedStream(), mPresenter.url(), false);
            } else {
                onSetCode(mPresenter.downloadedStream());
            }
            Log.e("Viewer", "Not handle intent");
        }

        if (getActivity() != null) getActivity().invalidateOptionsMenu();





        if (mPresenter.isRepo()) {
            appBarLayout = getActivity().findViewById(R.id.appbar);
            bottomNavigation = getActivity().findViewById(R.id.bottomNavigation);

            if (appBarLayout != null && !isAppBarListener) {
                appBarLayout.addOnOffsetChangedListener(this);
                isAppBarListener = true;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (AppHelper.isDeviceAnimationEnabled(getContext())) {
            if (appBarLayout != null && !isAppBarListener) {
                appBarLayout.addOnOffsetChangedListener(this);
                isAppBarListener = true;
            }
        }
    }

    @Override
    public void onStop() {
        if (AppHelper.isDeviceAnimationEnabled(getContext())) {
            if (appBarLayout != null && isAppBarListener) {
                appBarLayout.removeOnOffsetChangedListener(this);
                isAppBarListener = false;
            }
        }
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        /*
        inflater.inflate(R.menu.wrap_menu_option, menu);
        menu.findItem(R.id.wrap).setVisible(false);

         */
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //MenuItem menuItem = menu.findItem(R.id.wrap);
        /*
        if (menuItem != null) {
            if (getPresenter().isMarkDown() || getPresenter().isRepo() || getPresenter().isImage()) {
                menuItem.setVisible(false);
            } else {
                menuItem.setVisible(true).setCheckable(true).setChecked(isWrap);
            }
        }

         */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        if (item.getItemId() == R.id.wrap) {
            item.setChecked(!item.isChecked());
            isWrap = item.isChecked();
            showProgress(0);
            onSetCode(mPresenter.downloadedStream());
        }

         */
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onContentChanged(int progress) {
        if (loader != null) {
            loader.setProgress(progress);
            if (progress == 100) {
                hideProgress();
                if (!mPresenter.isMarkDown() && !mPresenter.isImage()) {
                    webView.scrollToLine(mPresenter.url());
                }
            }
        }
    }

    @Override
    public void onScrollChanged(boolean reachedTop, int scroll) {
        if (AppHelper.isDeviceAnimationEnabled(getActivity())) {
            if (mPresenter.isRepo() && appBarLayout != null && bottomNavigation != null && webView != null) {
                boolean shouldExpand = webView.getScrollY() == 0;
                if (!isAppBarMoving && shouldExpand != isAppBarExpanded) {
                    isAppBarMoving = true;
                    isAppBarExpanded = shouldExpand;
                    bottomNavigation.setExpanded(shouldExpand, true);
                    appBarLayout.setExpanded(shouldExpand, true);
                    webView.setNestedScrollingEnabled(shouldExpand);
                    if (shouldExpand)
                        webView.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0));
                }
            }
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && appBarLayout != null) {
            appBarLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        verticalOffset = Math.abs(verticalOffset);
        if (verticalOffset == 0 || verticalOffset == appBarLayout.getTotalScrollRange())
            isAppBarMoving = false;
    }


    @Override
    public void onSetImageUrl(@NonNull String url, boolean isSvg) {
        webView.loadImage(url, isSvg);
        webView.setOnContentChangedListener(this);
        webView.setVisibility(View.VISIBLE);
        if (getActivity() != null) getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onSetMdText(@NonNull String text, String baseUrl, boolean replace) {
        webView.setVisibility(View.VISIBLE);
        loader.setIndeterminate(false);
        webView.setGithubContentWithReplace(text, baseUrl, replace);
        webView.setOnContentChangedListener(this);
        if (getActivity() != null) getActivity().invalidateOptionsMenu();
        stateLayout.hideProgress();
    }

    @Override
    public void onSetCode(@NonNull String text) {
        webView.setVisibility(View.VISIBLE);
        loader.setIndeterminate(false);
        webView.setSource(text, isWrap);
        webView.setOnContentChangedListener(this);
        if (getActivity() != null) getActivity().invalidateOptionsMenu();
    }


    @Override
    public void onShowMdProgress() {
        loader.setIndeterminate(true);
        loader.setVisibility(View.VISIBLE);
        stateLayout.showProgress();
    }

    @Override
    public void showProgress(@StringRes int resId) {
        onShowMdProgress();
    }

    @Override
    public void hideProgress() {
        loader.setVisibility(View.GONE);
        stateLayout.hideProgress();
        if (!mPresenter.isImage())
            stateLayout.showReload(mPresenter.downloadedStream() == null ? 0 : 1);
    }


    @Override
    public void openUrl(@NonNull String url) {
        ActivityHelper.startCustomTab(getActivity(), url);
    }

    @Override
    public void onViewAsCode() {
        mPresenter.onLoadContentAsStream();
    }


    @Override
    public void showMessage(int msgRes) {
        hideProgress();
        super.showMessage(msgRes);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
