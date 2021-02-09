package com.soufianekre.smallhub.ui.search;

import android.widget.AutoCompleteTextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.soufianekre.smallhub.data.network.model.SearchHistory;
import com.soufianekre.smallhub.ui.base.BaseMvpPresenter;
import com.soufianekre.smallhub.ui.base.BaseMvpView;

import java.util.ArrayList;

interface SearchMvp {

    interface Presenter<V extends View> extends BaseMvpPresenter<V> {


        @NonNull
        ArrayList<SearchHistory> getHints();

        void onSearchClicked(@NonNull ViewPager viewPager, @NonNull AutoCompleteTextView editText);

    }

    interface View extends BaseMvpView {

        void onNotifyAdapter(@Nullable SearchHistory query);

        void onSetCount(int count, @IntRange(from = 0, to = 3) int index);
    }
}
