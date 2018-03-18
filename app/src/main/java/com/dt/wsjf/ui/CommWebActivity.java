package com.dt.wsjf.ui;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dt.wsjf.R;
import com.dt.wsjf.base.BaseActivity;

public class CommWebActivity extends BaseActivity {
    public static final String COMM_WEB_URL = "web_url";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_web);

        String url = getIntent().getStringExtra(COMM_WEB_URL);
        webView = (WebView) findViewById(R.id.comm_web);
        setWebSetting(webView.getSettings());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
        webView.loadUrl(url);
    }

    private void setWebSetting(WebSettings setting) {
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setAppCacheEnabled(false);
        setting.setBuiltInZoomControls(false);
        setting.setSupportZoom(false);
        setting.setDomStorageEnabled(true);
        setting.setUseWideViewPort(true);
        setting.setAllowFileAccess(true);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        setting.setLoadWithOverviewMode(true);
        setting.setSaveFormData(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }
}
