package com.neurotech.findfriends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hbb20.CountryCodePicker;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String openURL=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.web_view);

        openURL = getIntent().getStringExtra("openURL");

        Log.d(Constants.DEBUG_TAG,openURL);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        if (openURL == null)
            webView.loadUrl("https://google.com");
        else
            webView.loadUrl(openURL);

    }


}
