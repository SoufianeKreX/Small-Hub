package com.soufianekre.smallhub.ui.web_view.callback;

import android.webkit.JavascriptInterface;

import com.soufianekre.smallhub.ui.web_view.CustomizedWebView;

public class MarkDownInterceptorInterface {
    private CustomizedWebView customizedWebView;
    private boolean toggleNestScrolling;

    public MarkDownInterceptorInterface(CustomizedWebView customizedWebView) {
        this(customizedWebView, false);
    }

    public MarkDownInterceptorInterface(CustomizedWebView customizedWebView, boolean toggleNestScrolling) {
        this.customizedWebView = customizedWebView;
        this.toggleNestScrolling = toggleNestScrolling;
    }

    @JavascriptInterface
    public void startIntercept() {
        if (customizedWebView != null) {
            customizedWebView.setInterceptTouch(true);
            if (toggleNestScrolling) customizedWebView.setEnableNestedScrolling(false);
        }
    }

    @JavascriptInterface public void stopIntercept() {
        if (customizedWebView != null) {
            customizedWebView.setInterceptTouch(false);
            if (toggleNestScrolling) customizedWebView.setEnableNestedScrolling(true);
        }
    }
}
