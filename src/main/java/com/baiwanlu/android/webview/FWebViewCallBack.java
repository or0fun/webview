package com.baiwanlu.android.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;

/**
 * webView 回调
 * Created by lufei on 4/7/16.
 */
public class FWebViewCallBack {

    protected Activity activity;

    public FWebViewCallBack(Activity activity) {
        this.activity = activity;
    }
    /**
     * 进度变化
     * @param view
     * @param newProgress
     */
    public void onProgressChanged(FWebView view, int newProgress) {
    }

    /**
     * 页面标题
     * @param view
     * @param title
     */
    public void onReceivedTitle(FWebView view, String title) {

    }

    public void onPageFinished(FWebView webview, String url) {
    }

    public void onPageStarted(FWebView view, String url, Bitmap favicon) {

    }

    public boolean shouldOverrideKeyEvent(FWebView view, KeyEvent event) {
        return false;
    }

    public boolean shouldOverrideUrlLoading(FWebView view, String url) {
        return false;
    }

    public void onReceivedSslError(FWebView view, SslErrorHandler handler, SslError error) {

    }

    public boolean onJsAlert(FWebView view, String url, String message, com.tencent.smtt.export.external.interfaces.JsResult result) {
        return false;
    }
}
