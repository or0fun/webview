package com.baiwanlu.android.webview;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;

import com.baiwanlu.android.webview.util.WebViewLog;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.List;


/**
 * 底层webview
 * Created by lufei on 4/6/16.
 */
public class FWebView extends WebView {

    public static final int FILE_CHOOSER = 9998;

    ProgressBar progressbar;
    boolean hasProgressBar = true;
    FWebViewCallBack shWebViewCallBack;

    public FWebView(Context context) {
        super(context);
        init();
    }

    public FWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        if (isInEditMode()) {
            return;
        }

        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        String ua = webSetting.getUserAgentString();
        //尚妆是ShowJoyAndroid
        webSetting.setUserAgentString(webSetting.getUserAgentString() + FWebViewManager.getUserAgentString());

        progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,3));
        progressbar.setProgressDrawable(new ColorDrawable(Color.parseColor("#976DF7")));
        addView(progressbar);

        setWebChromeClient(new FWebChromeClient());
        setWebViewClient(new FWebViewClient());

        Activity activity = getActivity();

        if (null != activity) {
            setWebViewCallBack(new FWebViewCallBack(activity));
        }
    }

    /**
     * 设置webview回调接口，包括加载开始，加载结束等
     * @param callBack
     */
    public void setWebViewCallBack(FWebViewCallBack callBack) {
        shWebViewCallBack = callBack;
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }

    @Override
    public void addJavascriptInterface(Object object, String name) {
        super.addJavascriptInterface(object, name);
    }

    /**
     * 设置javascript接口
     * @param object
     */
    public void addJavascriptInterface(Object object) {
        super.addJavascriptInterface(object, FWebViewManager.getJavascriptInterfaceName());
    }

    public void setProgressbarDrawable(Drawable drawable) {
        progressbar.setProgressDrawable(drawable);
    }

    /**
     * 设置进度条是否显示
     * @param isVisible
     */
    public void setProgressbarVisible(boolean isVisible) {
        if (isVisible) {
            if (!hasProgressBar) {
                addView(progressbar);
            }
        } else {
            if (hasProgressBar) {
                removeView(progressbar);
            }
        }
        hasProgressBar = isVisible;
    }

    @Override
    public void reload() {
        super.reload();
    }

    @Override
    public boolean canGoBack() {
        return super.canGoBack();
    }

    @Override
    public void goBack() {
        super.goBack();
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    class FWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (hasProgressBar && null != progressbar ) {
                if (newProgress == 100) {
                    progressbar.setVisibility(View.GONE);
                } else {
                    if (progressbar.getVisibility() == View.GONE)
                        progressbar.setVisibility(View.VISIBLE);
                    progressbar.setProgress(newProgress);
                }
            }
            if (null != shWebViewCallBack) {
                shWebViewCallBack.onProgressChanged((FWebView) view, newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (null != shWebViewCallBack) {
                shWebViewCallBack.onReceivedTitle((FWebView)view, title);
            }
        }

        @Override
        public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> uploadFile, String acceptType, String captureType) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            ((Activity) (FWebView.this.getContext())).startActivityForResult(Intent.createChooser(i, "choose files"),
                    FWebView.FILE_CHOOSER);
            super.openFileChooser(uploadFile, acceptType, captureType);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, com.tencent.smtt.export.external.interfaces.JsResult result) {
            if (null != shWebViewCallBack) {
                return shWebViewCallBack.onJsAlert((FWebView)view, url, message, result);
            }
            return super.onJsAlert(view, url, message, result);
        }

    }

    class FWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            WebViewLog.d("shouldOverrideUrlLoading", url);
            if (null != shWebViewCallBack) {
                return shWebViewCallBack.shouldOverrideUrlLoading((FWebView)view, url);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            WebViewLog.d("onPageStarted", url);
            if (null != shWebViewCallBack) {
                shWebViewCallBack.onPageStarted((FWebView)view, url, favicon);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WebViewLog.d("onPageFinished", url);
            if (null != shWebViewCallBack) {
                shWebViewCallBack.onPageFinished((FWebView)view, url);
            }
        }
    }

    public int getWebScrollY() {
        return super.getWebScrollY();
    }


    public static void setWebViewCookie(Context context, String url, List<String> cookies) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        if (null != cookies) {
            for (String cookie : cookies) {
                cookieManager.setCookie(url, cookie);
            }
        }
        CookieSyncManager.getInstance().sync();
    }

    public static void removeWebViewCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }
}