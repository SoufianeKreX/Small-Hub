package com.soufianekre.smallhub.ui.base;

import androidx.annotation.NonNull;



import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public interface BaseMvpPresenter<V extends BaseMvpView> {
    void onAttach(V mvpView);
    void onDetach();
    boolean isApiCalled();
    void checkGithubStatus();

    <T> void makeRestCall(@NonNull Observable<T> observable, @NonNull Consumer<T> onNext);
    <T> void makeRestCall(@NonNull Observable<T> observable, @NonNull Consumer<T> onNext, boolean cancelable);

    void onSubscribed(boolean cancelable);

    void onError(Throwable throwable);
    void handleApiError(Throwable throwable);
}
