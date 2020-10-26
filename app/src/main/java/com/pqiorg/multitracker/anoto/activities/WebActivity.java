package com.pqiorg.multitracker.anoto.activities;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Bundle;
//import android.support.p003v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
//import com.anoto.adna.C0524R;
//import com.anoto.adna.ServerApi.api.object.DataVo;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.appevents.AppEventsConstants;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataVo;

import java.util.HashMap;

public class WebActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "WebActivity";
    private String mProdLink;
    /* access modifiers changed from: private */
    public ProgressBar progressBar;
    private WebView webView;

    private void init() {
        WebView webView2;
        this.progressBar = (ProgressBar) findViewById(R.id.pb_progress);
        this.webView = (WebView) findViewById(R.id.web_view);
        int i = 1;
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
                return super.onJsAlert(webView, str, str2, jsResult);
            }
        });
        if (VERSION.SDK_INT >= 19) {
            webView2 = this.webView;
            i = 2;
        } else {
            webView2 = this.webView;
        }
        webView2.setLayerType(i, null);
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView webView, String str) {
                Log.e(WebActivity.this.TAG, "onPageFinished: ");
                if (WebActivity.this.progressBar.getVisibility() == View.VISIBLE) {
                    WebActivity.this.progressBar.setVisibility(View.GONE);
                }
            }

            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                String str = "SSL Certificate error.";
                switch (sslError.getPrimaryError()) {
                    case 0:
                        str = "The certificate is not yet valid.";
                        break;
                    case 1:
                        str = "The certificate has expired.";
                        break;
                    case 2:
                        str = "The certificate Hostname mismatch.";
                        break;
                    case 3:
                        str = "The certificate authority is not trusted.";
                        break;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" Do you want to continue anyway?");
                String sb2 = sb.toString();
                Builder builder = new Builder(WebActivity.this);
                builder.setTitle("SSL Certificate Error");
                builder.setMessage(sb2);
                builder.setPositiveButton("OK", new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                    }
                });
                builder.setNegativeButton("Cancel", new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                    }
                });
                builder.create().show();
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                Log.e(WebActivity.this.TAG, "shouldOverrideUrlLoading: ");
                webView.loadUrl(str);
                return true;
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getExtras() != null) {
            TextUtils.isEmpty(getIntent().getStringExtra("from"));
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_webview);
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("url");
        DataVo dataVo = (DataVo) intent.getSerializableExtra("data_info");
        if (!TextUtils.isEmpty(stringExtra)) {
            this.mProdLink = stringExtra;
        } else if (dataVo == null || TextUtils.isEmpty(dataVo.getCurl())) {
            Toast.makeText(getApplicationContext(), R.string.txt_no_link_data,  Toast.LENGTH_SHORT).show();
            finish();
        } else {
            this.mProdLink = dataVo.getCurl();
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mProdLink: ");
        sb.append(this.mProdLink);
        Log.e(str, sb.toString());
        init();
        HashMap hashMap = new HashMap();
        hashMap.put("1313687a5086c30c8c0c49c304028cc0", AppEventsConstants.EVENT_PARAM_VALUE_NO);
        this.webView.loadUrl(this.mProdLink, hashMap);
    }
}
