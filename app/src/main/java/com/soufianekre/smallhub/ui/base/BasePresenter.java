package com.soufianekre.smallhub.ui.base;

import android.util.Log;

import androidx.annotation.NonNull;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.models.GithubStatus;
import com.soufianekre.smallhub.data.network.RestProvider;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.ObjectsCompat;
import com.soufianekre.smallhub.helper.RxHelper;
import com.soufianekre.smallhub.helper.rx.AppSchedulerProvider;
import com.soufianekre.smallhub.helper.rx.SchedulerProvider;


import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

public class BasePresenter<V extends BaseMvpView> implements BaseMvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final SchedulerProvider mSchedulerProvider = new AppSchedulerProvider();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private V mvpView;
    private boolean apiCalled;

    public BasePresenter(){

    }


    @Override
    public void onAttach(V mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mvpView = null;
    }

    @Override
    public boolean isApiCalled() {
        return apiCalled;
    }

    @Override
    public void checkGithubStatus() {

    }

    @Override
    public <T> void makeRestCall(@NonNull Observable<T> observable, @NonNull Consumer<T> onNext) {
        makeRestCall(observable,onNext,false);
    }

    @Override
    public <T> void makeRestCall(@NonNull Observable<T> observable, @NonNull Consumer<T> onNext, boolean cancelable) {
        getCompositeDisposable().add(RxHelper.getObservable(observable)
                .doOnSubscribe(disposable -> onSubscribed(cancelable))
                .subscribe(onNext, this::onError, () -> apiCalled = true));
    }

    @Override public void onSubscribed(boolean cancelable) {
        getMvpView().showProgress(R.string.in_progress);
    }

    @Override
    public void onError(Throwable throwable) {
        apiCalled = true;
        throwable.printStackTrace();

    }

    @Override
    public void handleApiError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            switch (((HttpException) throwable).code()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                    getMvpView().showMessage("Unauthorised User ");
                    break;
                case HttpsURLConnection.HTTP_FORBIDDEN:
                    if (getMvpView().checkInternetConnection()){
                        getMvpView().showMessage("Internet may be unavailable");
                    }else{
                        getMvpView().showMessage(R.string.max_rate_request_error_msg);
                    }

                    break;
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                    getMvpView().showMessage("internal error");
                    break;
                case HttpsURLConnection.HTTP_BAD_REQUEST:
                    getMvpView().showMessage("Bad Request ");
                    break;
                default:
                    getMvpView().showMessage(throwable.getLocalizedMessage());

            }
        }
        Log.e("handle Api Error",throwable.getLocalizedMessage());
    }

    public void onCheckGitHubStatus() {
        getCompositeDisposable().add((Disposable) RestProvider.gitHubStatus()
                .filter(ObjectsCompat::nonNull)
                .doOnNext(gitHubStatusModel -> {
                    Log.e("check github Status : ",gitHubStatusModel.getStatus().getDescription());
                    GithubStatus status = gitHubStatusModel.getStatus();
                    String description = status != null ? status.getDescription() : null;
                    String indicatorStatus = status != null ? status.getIndicator() : null;
                    if (!InputHelper.isEmpty(description) && !"none".equalsIgnoreCase(indicatorStatus)) {
                        getMvpView().showMessage("Github Status:(" + indicatorStatus + ")\n" + description);
                    }
                })
                .doOnError(throwable -> {
                    onError(throwable);
                }).subscribe(gitHubStatusModel -> {
                    getMvpView().showMessage(gitHubStatusModel.getStatus().toString());

                },this::onError)
        );
    }

    public boolean isViewAttached() {
        return mvpView != null;
    }

    public V getMvpView() {
        return mvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}