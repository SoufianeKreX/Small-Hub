package com.soufianekre.smallhub.ui.search;


import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.annimon.stream.Stream;
import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.model.SearchHistory;
import com.soufianekre.smallhub.helper.AppHelper;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.ui.base.BasePresenter;
import com.soufianekre.smallhub.ui.search.repos.SearchReposFragment;
import com.soufianekre.smallhub.ui.search.users.SearchUsersFragment;

import java.util.ArrayList;


public class SearchPresenter<V extends SearchMvp.View> extends BasePresenter<V>
        implements SearchMvp.Presenter<V> {

    private static final String TAG = "SearchPresenter";
    private ArrayList<SearchHistory> hints = new ArrayList<>();

    @NonNull
    @Override
    public ArrayList<SearchHistory> getHints() {
        return hints;
    }

    @Override public void onSearchClicked(@NonNull ViewPager viewPager, @NonNull AutoCompleteTextView editText) {
        /*
        getCompositeDisposable().add(
                RxSearchObservable.fromView(editText)
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String text) {
                                if (text.isEmpty()) {
                                    editText.setText("");
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        })
                        .distinctUntilChanged()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String result) {
                            }
                        }));

         */

        boolean isEmpty = InputHelper.isEmpty(editText) || InputHelper.toString(editText).length() < 2;
        editText.setError(isEmpty ? editText.getResources().getString(R.string.minimum_three_chars) : null);
        if (!isEmpty) {
            editText.dismissDropDown();
            AppHelper.hideKeyboard(editText);
            String query = InputHelper.toString(editText);
            SearchReposFragment repos = (SearchReposFragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
            SearchUsersFragment users = (SearchUsersFragment) viewPager.getAdapter().instantiateItem(viewPager, 1);

            repos.onQueueSearch(query);
            users.onQueueSearch(query);
            boolean noneMatch = Stream.of(hints).noneMatch(value -> value.getText().equalsIgnoreCase(query));
            if (noneMatch) {
                //SearchHistory searchHistory = new SearchHistory(query);
                //manageObservable(searchHistory.save(searchHistory).toObservable());
                //sendToView(view -> view.onNotifyAdapter(new SearchHistory(query)));
            }
        }
    }

}