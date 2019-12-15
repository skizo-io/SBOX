package com.smackjeeves.ui.webview

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import com.smackjeeves.R
import com.smackjeeves.ui.base.BaseFragment
import com.smackjeeves.utils.Config
import com.smackjeeves.utils.bundleOf
import kotlinx.android.synthetic.main.fragment_webview.*
import kotlinx.android.synthetic.main.item_appbar.*
import skizo.library.extensions.trace

class WebViewFragment : BaseFragment() {


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            WebViewFragment().apply {
                arguments = bundle
            }

        @JvmStatic
        fun getBundle(url: String, title: String = "", isFullscreen: Boolean = false) = bundleOf(
            "URL".to(url),
            "APPBAR_TITLE".to(title),
            "FullScreen".to(isFullscreen)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            webview.loadUrl(it.getString("URL"))
            appbar_title.text = it.getString("APPBAR_TITLE")
        }

        appbar.setVisible(close = true)


    }


}


@Suppress("DEPRECATION")
class SJWebView : WebView {

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    init {

    }

    var currentUrl: String? = ""
    var extraHeaders: Map<String, String>? = null

    var mChormeClient: SJWebChormeClient? = null
    var mViewClient: SJWebViewClient? = null

    override fun loadUrl(url: String?) {
        currentUrl = url
        super.loadUrl(url)
    }

    override fun loadUrl(url: String?, additionalHttpHeaders: Map<String, String>) {
        currentUrl = url
        extraHeaders = additionalHttpHeaders
        super.loadUrl(url, additionalHttpHeaders)
    }

    override fun destroy() {
        super.destroy()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } else {
            var cookieSyncMngr: CookieSyncManager = CookieSyncManager.createInstance(context)
            cookieSyncMngr.startSync()
            CookieManager.getInstance().apply {
                removeAllCookie()
                removeSessionCookie()
            }
            cookieSyncMngr.stopSync()
            cookieSyncMngr.sync()
        }


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        /*
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canGoBack()) {
                activeGoBack = true;
                goBack();
                return true;
            } else {
                if(mChormeClient != null)
                    mChormeClient.onHideCustomView();
            }
        }
        */
        return super.onKeyDown(keyCode, event)
    }


    private fun initialize(context: Context, attrs: AttributeSet?) {

        setWebContentsDebuggingEnabled(Config.isDebugMode)

        //ScrollBar見えるのを設定
        var visibleScrollBar = true
        isVerticalScrollBarEnabled = visibleScrollBar
        isHorizontalScrollBarEnabled = visibleScrollBar
        isScrollbarFadingEnabled = visibleScrollBar
        if (visibleScrollBar)
            scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        else
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY


        settings.apply {
            javaScriptEnabled = true //このfunctionは危険性があります。(XSSの攻撃)

            var enableZoom = true //Zoom機能を使う
            setSupportZoom(enableZoom) //拡大、縮小機能を使います。
            builtInZoomControls = enableZoom //基本のZoomのアイコンを使います。
            displayZoomControls = !enableZoom

            var enableMultipleWindows = true //多重窓を使う
            setSupportMultipleWindows(enableMultipleWindows) //多重窓は使わないように
            javaScriptCanOpenWindowsAutomatically = enableMultipleWindows //JavaScriptが自動的に窓を開かないように

            var enableCache = true //HTML5のApplicationCacheを使う
            allowFileAccess = enableCache //WebViewの中にApplicationファイルを使う
            domStorageEnabled = enableCache //DOM storage APIを使う (HTML5にローカルの保存所を使うなら）
            cacheMode = WebSettings.LOAD_DEFAULT
            setAppCacheEnabled(enableCache) //App Cache使う
            setAppCachePath("/data/data/$context.packageName/cache") //App Cacheの位置を設定

//            userAgentString = userAgentString + " " + str

            loadWithOverviewMode = true //コンテンツがWebViewより大きかったら大きさを調整させます。
            useWideViewPort = true //wide viewportを使います。
            loadsImagesAutomatically = true //WebViewがアプリに登録させたイメージを自動的でロード機能を使います。
            defaultTextEncodingName = "UTF-8" //基本文字Encoderを設定
            setNeedInitialFocus(false) //初期のfocusを設定しないように
        }

        mViewClient = SJWebViewClient()
        mChormeClient = SJWebChormeClient()
        setWebViewClient(mViewClient);
        setWebChromeClient(mChormeClient);


    }


}


class SJWebViewClient : WebViewClient() {
    var startUrl: String? = ""

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        startUrl = url
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

        // webviewのみのスキーム
        var url: String = request?.url.toString()
        var uri: Uri = Uri.parse(url)

        trace(
            "@@@@@@ shouldOverrideUrlLoading ",
            url,
            uri
        )

        if (url.startsWith("mailto:")) {
//            ActivityUtil.startEmail(getContext(), url);
        } else if (url.startsWith("comico://close")) {
//            ((Activity)getContext()).finish();
        } else if (url.startsWith("comico://webbrowserChangeChannelAuth")) {
//            String channelNoStr = parseUri . getQueryParameter ("channelno");
//            String mediaNoStr = parseUri . getQueryParameter ("mediano");
//
//            if (TextUtils.isEmpty(channelNoStr) || TextUtils.isEmpty(mediaNoStr)) {
//                return true;
//            }
//            Map<String, Integer> map = new HashMap<>();
//            map.put("channelno", Integer.valueOf(channelNoStr));
//            map.put("mediano", Integer.valueOf(mediaNoStr));
//            EventManager.instance.dispatcher(EventType.VOD_BUY_PACK, map);
        } else if ("true".equals(uri.getQueryParameter("outbrowser"))
            || "true".equals(uri.getQueryParameter("shoplogin"))
        ) {
//            ActivityUtil.startUrlScheme(getContext(), url);
//            if (TextUtils.equals(parseUri.getQueryParameter("close"), "Y")) {
//                ((Activity)getContext()).finish();
//            }
        } else if (url.equals("https://play.google.com/store/apps/details?id=sj..")) {
//            ActivityUtil.startActivityMarket(getContext());
        } else {

        }


        return super.shouldOverrideUrlLoading(view, request)
    }

}


class SJWebChormeClient : WebChromeClient() {

}

/*

public class MyWebChormeClient extends WebChromeClient {
    private View mCustomView;
    private Activity mActivity;
    private MyWebChormeClient.FullscreenHolder mFullscreenContainer;
    private CustomViewCallback mCustomViewCollback;

    public MyWebChormeClient(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if(mListner != null) {
            if(mListner.onConsoleMessage(consoleMessage)) {
                return true;
            }
        }
        return super.onConsoleMessage(consoleMessage);
    }

    //WebViewで出るポップアップの処理
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        //すぐ終了させます。
        result.confirm();
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if(mListner != null) {
            mListner.onProgressChanged(view, newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }

    //全体画面モードで始まる時、呼びます。
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (mCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }

        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        mFullscreenContainer = new MyWebChormeClient.FullscreenHolder(mActivity);
        mFullscreenContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT);
        decor.addView(mFullscreenContainer, ViewGroup.LayoutParams.MATCH_PARENT);
        mCustomView = view;
        mCustomViewCollback = callback;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    //全体画面モード終了する時、呼びます。
    @Override
    public void onHideCustomView() {
        if (mCustomView == null) {
            return;
        }

        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        decor.removeView(mFullscreenContainer);
        mFullscreenContainer = null;
        mCustomView = null;
        mCustomViewCollback.onCustomViewHidden();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }




    public void setRequestedOrientation(int orientation) {

        mActivity.setRequestedOrientation(orientation);

        int uiOptions = mActivity.getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);

        newUiOptions = 0;

        boolean isFullscreen = true;

        switch (orientation) {
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
            isFullscreen = false;
            break;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
            break;
        }

        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= (!isFullscreen ? 0 : View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= (!isFullscreen ? 0 : View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= (!isFullscreen ? 0 : View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }


        if (!isFullscreen) {
            if(ComicoState.isLowSDK == false) mActivity.getWindow().getDecorView().setSystemUiVisibility(0);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            if(ComicoState.isLowSDK == false) mActivity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }

    }

    public void destory() {
        mActivity = null;
        mCustomView = null;
        if(mFullscreenContainer != null) {
            mFullscreenContainer.removeAllViews();
            mFullscreenContainer = null;
        }
        mCustomViewCollback = null;
    }

    class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }
}
*/


/*
    public interface OnListener {
        void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);
        void onPageFinished(WebView view, String url);
        void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon);

        //falseをreturnしたら処理しない。
        boolean onUrlLoading(WebView view, String url);

        void onProgressChanged(WebView view, int newProgress);
        void onPageError();

        boolean onConsoleMessage(ConsoleMessage consoleMessage);
    }
}
 */