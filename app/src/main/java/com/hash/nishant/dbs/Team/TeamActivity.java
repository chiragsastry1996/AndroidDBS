package com.hash.nishant.dbs.Team;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.hash.nishant.dbs.Home.Home;
import com.hash.nishant.dbs.R;

public class TeamActivity extends AppCompatActivity {

    WebView webView;
    ImageView back;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamactivity);

        back = (ImageView)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

//        webView = (WebView)findViewById(R.id.mybrowser);
//
//        WebSettings settings = webView.getSettings();
//
//        settings.setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
//
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//
//
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(TeamActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        webView.loadUrl("https://www.google.com/");

        }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(TeamActivity.this, Home.class);
        startActivity(intent);
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

}
