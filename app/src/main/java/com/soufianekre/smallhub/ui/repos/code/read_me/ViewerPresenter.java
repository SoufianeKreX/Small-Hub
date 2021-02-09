package com.soufianekre.smallhub.ui.repos.code.read_me;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.models.MarkdownModel;
import com.soufianekre.smallhub.data.models.ViewerFile;
import com.soufianekre.smallhub.data.network.RestProvider;
import com.soufianekre.smallhub.helper.BundleConstant;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.RxHelper;
import com.soufianekre.smallhub.helper.markdown.MarkDownProvider;
import com.soufianekre.smallhub.ui.base.BasePresenter;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

public class ViewerPresenter<V extends ViewerMvp.View> extends BasePresenter<V>
        implements ViewerMvp.Presenter<V>  {


    private String downloadedStream;
    @com.evernote.android.state.State boolean isMarkdown;
    @com.evernote.android.state.State boolean isRepo;
    @com.evernote.android.state.State boolean isImage;
    @com.evernote.android.state.State String url;
    @com.evernote.android.state.State String htmlUrl;

    @Override public void onError(@NonNull Throwable throwable) {
        throwable.printStackTrace();
        handleApiError(throwable);
        super.onError(throwable);
    }

    @Override public void onHandleIntent(@Nullable Bundle intent) {
        if (intent == null){
            getMvpView().showMessage("handling Intent is Impossibe because of  null");
            return;
        }
        isRepo = intent.getBoolean(BundleConstant.EXTRA);
        url = intent.getString(BundleConstant.ITEM);
        htmlUrl = intent.getString(BundleConstant.EXTRA_TWO);
        Log.e("Viewer Presenter","htmlUrl  : = " + htmlUrl);
        if (!InputHelper.isEmpty(url)) {
            if (MarkDownProvider.isArchive(url)) {
                getMvpView().onError(R.string.archive_file_detected_error);
                return;
            }
            if (isRepo) {
                url = url.endsWith("/") ? (url + "readme") : (url + "/readme");
            }
            onWorkOnline();
        }
    }

    @Override public void onWorkOnline() {
        isImage = MarkDownProvider.isImage(url);
        if (isImage) {
            if ("svg".equalsIgnoreCase(MimeTypeMap.getFileExtensionFromUrl(url))) {
                makeRestCall(RestProvider.getRepoService().getFileAsStream(url),
                        s -> getMvpView().onSetImageUrl(s,true));
                return;
            }
            getMvpView().onSetImageUrl(url,false);
            return;
        }
        Observable<String> streamObservable = MarkDownProvider.isMarkdown(url)
                ? RestProvider.getRepoService().getFileAsHtmlStream(url)
                : RestProvider.getRepoService().getFileAsStream(url);

        Observable<String> observable = isRepo ? RestProvider.getRepoService()
                .getReadmeHtml(url) : streamObservable;

        getCompositeDisposable().add(
                RxHelper.getObservable(observable)
                .subscribe(content ->{
                    downloadedStream = content;
                    ViewerFile fileModel = new ViewerFile();
                    fileModel.setContent(downloadedStream);
                    fileModel.setFullUrl(url);
                    fileModel.setRepo(isRepo);
                    if (isRepo) {
                        fileModel.setMarkdown(true);
                        isMarkdown = true;
                        isRepo = true;
                        if (getMvpView() != null)
                            getMvpView().onSetMdText(
                                    downloadedStream, htmlUrl == null ? url : htmlUrl, false);
                    } else {
                        isMarkdown = MarkDownProvider.isMarkdown(url);
                        if (isMarkdown) {
                            MarkdownModel model = new MarkdownModel();
                            model.setText(downloadedStream);
                            Uri uri = Uri.parse(url);
                            StringBuilder baseUrl = new StringBuilder();
                            for (String s : uri.getPathSegments()) {
                                if (!s.equalsIgnoreCase(uri.getLastPathSegment())) {
                                    baseUrl.append("/").append(s);
                                }
                            }
                            model.setContext(baseUrl.toString());
                            makeRestCall(RestProvider.getRepoService().convertReadmeToHtml(model), string -> {
                                isMarkdown = true;
                                downloadedStream = string;
                                fileModel.setMarkdown(true);
                                fileModel.setContent(downloadedStream);
                                getMvpView().onSetMdText(downloadedStream, htmlUrl == null ? url : htmlUrl, true);
                            });
                            return;
                        }
                        fileModel.setMarkdown(false);
                        getMvpView().onSetCode(downloadedStream);
                    }
                },throwable ->{
                    Log.e("ViewerPres:WorkOnline",throwable.getLocalizedMessage());
                }));

    }

    @Override public void onLoadContentAsStream() {
        boolean isImage = MarkDownProvider.isImage(url) &&
                !"svg".equalsIgnoreCase(MimeTypeMap.getFileExtensionFromUrl(url));
        if (isImage || MarkDownProvider.isArchive(url)) {
            return;
        }
        makeRestCall(RestProvider.getRepoService().getFileAsStream(url),
                body -> {
                    downloadedStream = body;
                    getMvpView().onSetCode(body);
                });
    }

    @Override public String downloadedStream() {
        return downloadedStream;
    }

    @Override public boolean isMarkDown() {
        return isMarkdown;
    }

    @Override public boolean isRepo() {
        return isRepo;
    }

    @Override public boolean isImage() {
        return isImage;
    }

    @NonNull @Override public String url() {
        return url;
    }
}
