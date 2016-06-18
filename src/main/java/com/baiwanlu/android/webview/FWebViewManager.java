package com.baiwanlu.android.webview;

import com.baiwanlu.android.webview.util.WebViewLog;

/**
 * webview 管理类
 * Created by lufei on 6/14/16.
 */
public class FWebViewManager {

    private static String javascriptInterfaceName = "jsObj";
    private static String userAgentString = "Android";

    //自定义useragent
    public static void setUserAgentString(String userAgent) {
        userAgentString = userAgent;
    }

    //自定义useragent
    public static String getUserAgentString() {
        return userAgentString;
    }

    //自定义javascriptInterfaceName
    public static void setJavascriptInterfaceName(String interfaceName) {
        javascriptInterfaceName = interfaceName;
    }

    //自定义javascriptInterfaceName
    public static String getJavascriptInterfaceName() {
        return javascriptInterfaceName;
    }

    //设置是否显示log
    public static void setLogEnable(boolean enable) {
        WebViewLog.LOG_DEBUG = enable;
    }

}
