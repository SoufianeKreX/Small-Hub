package com.soufianekre.smallhub.ui.main;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.evernote.android.state.State;
import com.google.android.material.navigation.NavigationView;
import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.models.TrendingModel;
import com.soufianekre.smallhub.ui.base.BaseActivity;

import com.soufianekre.smallhub.ui.main.trending.TrendingRepoAdapter;
import com.soufianekre.smallhub.ui.repos.RepoPagerActivity;
import com.soufianekre.smallhub.ui.search.SearchActivity;
import com.soufianekre.smallhub.ui.widgets.StateLayout;
import com.soufianekre.smallhub.ui.widgets.recycler_view.DynamicRecyclerView;
import com.soufianekre.smallhub.ui.widgets.recycler_view.scroll.RecyclerViewFastScroller;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
Show Trending Repos By Language
 */

public class MainActivity extends BaseActivity implements MainMvp.View {

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_nav)
    NavigationView mainNavigationView;

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;

    @BindView(R.id.trending_repo_recycler_view)
    DynamicRecyclerView trendingRepoRecyclerView;
    @BindView(R.id.trending_swipe_refresh_layout)
    SwipeRefreshLayout trendingSwipeRefresh;

    @BindView(R.id.trending_empty_layout)
    StateLayout trendingEmptyView;
    @BindView(R.id.fastScroller)
    RecyclerViewFastScroller fastScroller;

    @State String selectedTitle = "All";
    @State String lang = "";
    @State String since ="";

    private TrendingRepoAdapter trendingRepoAdapter;
    MainPresenter<MainMvp.View> mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new MainPresenter<>();
        mPresenter.onAttach(this);
        setupUi();
        mPresenter.onFilterLanguages("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_search:{
                openSearchActivity();
                break;
            }
            case R.id.menu_main_filter:{
                drawerLayout.openDrawer(GravityCompat.END);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupUi(){
        // toolbar
        mainToolbar.setTitle("Trending Repos ");
        setSupportActionBar(mainToolbar);
        //nav + drawer
        mainNavigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.END);
            onNavMenuItemClicked(item);
            return true;
        });
        // recycler_view

        trendingRepoAdapter = new TrendingRepoAdapter(this,mPresenter.getTrendingRepos());
        trendingRepoAdapter.setListener(mPresenter);
        LinearLayoutManager llm = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);

        trendingRepoRecyclerView.setEmptyView(trendingEmptyView,trendingSwipeRefresh);
        trendingRepoRecyclerView.setLayoutManager(llm);
        trendingRepoRecyclerView.setAdapter(trendingRepoAdapter);

        fastScroller.attachRecyclerView(trendingRepoRecyclerView);
        trendingEmptyView.setEmptyText("No Trending");

        trendingEmptyView.setOnReloadListener(v -> {
            mPresenter.onCallApi(lang,since);
        });

        trendingSwipeRefresh.setOnRefreshListener(() ->{
            mPresenter.onCallApi(lang,since);
        });

        mPresenter.onCallApi(lang,since);
    }


    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void onNotifyAdapter(List<TrendingModel> trendingModels) {
        Log.e("Main Activity","trending List Size" + trendingModels.size());
        trendingRepoAdapter.insertItems(trendingModels);

    }


    @Override
    public void onAppendNavMenuItem(String title, int color) {
        mainNavigationView.getMenu()
                .add(R.id.programmingLanguageGroup, title.hashCode(), Menu.NONE, title)
                .setCheckable(true)
                .setIcon(createOvalShapeDrawable(color))
                .setChecked(title.equals(selectedTitle));
    }

    @Override
    public void showProgress() {
        trendingSwipeRefresh.setRefreshing(true);
        trendingEmptyView.showProgress();
    }

    @Override
    public void hideProgress() {
        trendingSwipeRefresh.setRefreshing(false);
        trendingEmptyView.hideProgress();
    }


    @Override
    public void clearNavMenu() {
        mainNavigationView.getMenu().clear();
    }

    @Override
    public void clearAdapter() {
        trendingRepoAdapter.clear();
    }

    @Override
    public void showTrendingRepo(int position, TrendingModel item) {
        String[] nameSplit = item.getName().trim().split("/");
        startActivity(RepoPagerActivity.createIntent(this,
                nameSplit[1].trim(), nameSplit[0].trim()));
    }

    private void onNavMenuItemClicked(MenuItem item){
        if (item.getTitle().toString().equals("All")){
            selectedTitle ="";
        }else{
            selectedTitle = item.getTitle().toString();
        }
        setTrendingLangValues(selectedTitle);
    }
    private void setTrendingLangValues(String lang){
        this.lang = lang;
        // call api
        mPresenter.onCallApi(lang,since);
    }

    private Drawable createOvalShapeDrawable(int color){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setSize(24, 24);
        drawable.setColor(color);
        return drawable;
    }

    void openSearchActivity(){
        startActivity(SearchActivity.getStartIntent(this));
    }
}
