package com.baiwanlu.android.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

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
    public void onProgressChanged(WebView view, int newProgress) {
    }

    /**
     * 页面标题
     * @param view
     * @param title
     */
    public void onReceivedTitle(WebView view, String title) {

    }

    public void onPageFinished(WebView webview, String url) {
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return false;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

    }

    /**
     * 选择文件
     * @param webView
     * @param filePathCallback
     * @param fileChooserParams
     */
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return true;
    }


    /**
     * 选择文件
     * @param filePathCallback
     * @param acceptType
     * @param capture
     */
    public void openFileChooser(ValueCallback filePathCallback,  String acceptType, String capture) {
    }
}
