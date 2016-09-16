# 封装的webView

```
compile 'com.baiwanlu.android:webview:1.0.2'
```

# 使用方法

```xml
<com.baiwanlu.android.webview.FWebView
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```
有需要的话调用setWebViewCallBack和addJavascriptInterface接口进行设置

没有额外需求的话，无需调用，默认已支持常用的功能

```
    /**
     * 设置webview回调接口，包括加载开始，加载结束等，有需要的话，重载相应的接口
     * @param callBack
     */
    public void setWebViewCallBack(SHWebViewCallBack callBack) 

    /**
     * 设置javascript接口，有需要的话继承SHJavascriptInterfaceImp，增加JavaScript函数
     * @param object
     */
    public void addJavascriptInterface(SHJavascriptInterfaceImp object)

    public void loadUrl(String url)
```

新增接口

```java
//设置interface name 
FWebViewManager.setJavascriptInterfaceName("your name");

//自定义useragent
FWebViewManager.setUserAgentString("your userAgent");

//设置是否显示log
FWebViewManager.setLogEnable(true);

 ```
