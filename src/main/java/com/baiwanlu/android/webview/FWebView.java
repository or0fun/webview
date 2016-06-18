package com.baiwanlu.android.webview;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.baiwanlu.android.webview.util.WebViewLog;


/**
 * 底层webview
 * Created by lufei on 4/6/16.
 */
public class FWebView extends WebView {

    ProgressBar progressbar;
    boolean hasProgressBar = true;
    FWebViewCallBack fWebViewCallBack;

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

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");

        /***打开本地缓存提供JS调用**/
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        String ua = settings.getUserAgentString();
        //尚妆是ShowJoyAndroid
        settings.setUserAgentString(ua.replace("Android", FWebViewManager.getUserAgentString()));

        progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,3, 0, 0));
        progressbar.setProgressDrawable(new ColorDrawable(Color.parseColor("#976DF7")));
        addView(progressbar);

        setWebChromeClient(new SHWebChromeClient());
        setWebViewClient(new SHWebViewClient());

        Activity activity = getActivity();

        if (null != activity) {
            setWebViewCallBack(new FWebViewCallBack(activity));
            addJavascriptInterface(new FJavascriptInterfaceImp(activity), FWebViewManager.getJavascriptInterfaceName());
        }
    }

    /**
     * 设置webview回调接口，包括加载开始，加载结束等
     * @param callBack
     */
    public void setWebViewCallBack(FWebViewCallBack callBack) {
        fWebViewCallBack = callBack;
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
    public void addJavascriptInterface(FJavascriptInterfaceImp object) {
        super.addJavascriptInterface(object, FWebViewManager.getJavascriptInterfaceName());
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

    class SHWebChromeClient extends WebChromeClient {
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
            if (null != fWebViewCallBack) {
                fWebViewCallBack.onProgressChanged(view, newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (null != fWebViewCallBack) {
                fWebViewCallBack.onReceivedTitle(view, title);
            }
        }

        //Android 5.0+
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (null != fWebViewCallBack) {
                return fWebViewCallBack.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
            return true;
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            if (null != fWebViewCallBack) {
                fWebViewCallBack.openFileChooser(uploadMsg, null, null);
            }
        }

        public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
            if (null != fWebViewCallBack) {
                fWebViewCallBack.openFileChooser(uploadMsg, acceptType, null);
            }

        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
            if (null != fWebViewCallBack) {
                fWebViewCallBack.openFileChooser(uploadMsg, acceptType, capture);
            }

        }
    }

    class SHWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            WebViewLog.d("shouldOverrideUrlLoading", url);
            if (null != fWebViewCallBack) {
                return fWebViewCallBack.shouldOverrideUrlLoading(view, url);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            WebViewLog.d("onPageStarted", url);
            if (null != fWebViewCallBack) {
                fWebViewCallBack.onPageStarted(view, url, favicon);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WebViewLog.d("onPageFinished", url);
            if (null != fWebViewCallBack) {
                fWebViewCallBack.onPageFinished(view, url);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            if (null != fWebViewCallBack) {
                fWebViewCallBack.onReceivedSslError(view, handler, error);
            }
        }
    }
}
