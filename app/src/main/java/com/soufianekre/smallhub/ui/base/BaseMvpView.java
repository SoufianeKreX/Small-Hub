package com.soufianekre.smallhub.ui.base;

import androidx.annotation.StringRes;

public interface BaseMvpView {
    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    void hideKeyboard();

    void showProgress(@StringRes int ResId);

    void hideProgress();


    boolean checkInternetConnection();
}
