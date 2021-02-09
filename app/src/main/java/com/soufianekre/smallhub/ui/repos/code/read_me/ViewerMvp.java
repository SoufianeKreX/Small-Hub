package com.soufianekre.smallhub.ui.repos.code.read_me;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.soufianekre.smallhub.ui.web_view.CustomizedWebView;
import com.soufianekre.smallhub.ui.base.BaseMvpPresenter;
import com.soufianekre.smallhub.ui.base.BaseMvpView;

import io.reactivex.annotations.NonNull;

public interface ViewerMvp {

    interface View extends BaseMvpView, CustomizedWebView.OnContentChangedListener {


        void onSetImageUrl(@NonNull String url, boolean isSvg);

        void onSetMdText(@NonNull String text, String baseUrl, boolean replace);

        void onSetCode(@NonNull String text);

        void onShowMdProgress();

        void openUrl(@NonNull String url);

        void onViewAsCode();

    }
    interface Presenter<V extends BaseMvpView> extends BaseMvpPresenter<V> {

        void onHandleIntent(@Nullable Bundle intent);

        void onLoadContentAsStream();

        String downloadedStream();

        boolean isMarkDown();

        void onWorkOnline();

        boolean isRepo();

        boolean isImage();

        @NonNull String url();

    }
}
