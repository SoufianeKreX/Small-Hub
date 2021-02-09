package com.soufianekre.smallhub.ui.search;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.models.FragmentPagerAdapterModel;
import com.soufianekre.smallhub.data.models.TabsCountStateModel;
import com.soufianekre.smallhub.data.network.model.SearchHistory;
import com.soufianekre.smallhub.helper.AnimHelper;
import com.soufianekre.smallhub.helper.ViewHelper;
import com.soufianekre.smallhub.ui.base.BaseActivity;
import com.soufianekre.smallhub.ui.widgets.ForegroundImageView;
import com.soufianekre.smallhub.ui.widgets.ViewPagerView;
import com.soufianekre.smallhub.ui.adapters.FragmentsPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.LinkedHashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class SearchActivity extends BaseActivity implements SearchMvp.View {

    SearchPresenter<SearchMvp.View> mPresenter;

    private ArrayAdapter<SearchHistory> searchAdapter;


    //widgets
    @BindView(R.id.search_toolbar)
    Toolbar searchToolbar;
    @BindView(R.id.searchEditText)
    AutoCompleteTextView searchEditText;
    @BindView(R.id.clear)
    ForegroundImageView clear;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.pager)
    ViewPagerView pager;

    HashSet<TabsCountStateModel> tabsCountSet = new LinkedHashSet<>();
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();


    public static Intent getStartIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mPresenter = new SearchPresenter<>();
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(SearchActivity.this);
        // toolbar

        setSupportActionBar(searchToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        // widgets
        pager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapterModel.buildForSearch(this)));

        tabs.setupWithViewPager(pager);
        searchEditText.setAdapter(getSearchHistoryAdapter());
        searchEditText.setOnItemClickListener((parent, view, position, id)
                -> mPresenter.onSearchClicked(pager, searchEditText));

        if (!tabsCountSet.isEmpty()) {
            setupTab();
        }

        if (savedInstanceState == null && getIntent() != null) {
            if (getIntent().hasExtra("search")) {
                searchEditText.setText(getIntent().getStringExtra("search"));
                mPresenter.onSearchClicked(pager, searchEditText);
            }
        }
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
            @Override public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
                //onScrollTop(tab.getPosition());
            }
        });
    }

    @OnTextChanged(value = R.id.searchEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onTextChange(Editable s) {
        String text = s.toString();
        if (text.length() == 0) {
            AnimHelper.animateVisibility(clear, false);
        } else {
            AnimHelper.animateVisibility(clear, true);
        }
    }

    @OnClick(R.id.search_basic_btn)
    void onSearchClicked() {
        mPresenter.onSearchClicked(pager, searchEditText);
    }

    @OnEditorAction(R.id.searchEditText) boolean onEditor() {
        onSearchClicked();
        return true;
    }

    @OnClick(value = {R.id.clear}) void onClear(android.view.View view) {
        if (view.getId() == R.id.clear) {
            searchEditText.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @OnClick(R.id.search_basic_btn) void onSearch(){
        mPresenter.onSearchClicked(pager, searchEditText);
    }
    @Override
    public void onNotifyAdapter(@Nullable SearchHistory query) {
        if (query == null)
            getSearchHistoryAdapter().notifyDataSetChanged();
        else getSearchHistoryAdapter().add(query);
    }

    @Override
    public void onSetCount(int count, int index) {

    }

    private ArrayAdapter<SearchHistory> getSearchHistoryAdapter() {
        if (searchAdapter == null)
            searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                    mPresenter.getHints());
        return searchAdapter;
    }
    private void setupTab() {
        for (TabsCountStateModel model : tabsCountSet) {
            int index = model.getTabIndex();
            int count = model.getCount();
            TextView textView = ViewHelper.getTabTextView(tabs, index);
            if (index == 0) {
                textView.setText(String.format("%s(%s)", getString(R.string.repos), numberFormat.format(count)));
            } else if (index == 1) {
                textView.setText(String.format("%s(%s)", getString(R.string.users), numberFormat.format(count)));
            }
        }
    }

}