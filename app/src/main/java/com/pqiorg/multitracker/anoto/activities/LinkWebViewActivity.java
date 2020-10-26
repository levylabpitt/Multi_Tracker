package com.pqiorg.multitracker.anoto.activities;

import android.os.Bundle;
//import android.support.p003v7.app.AppCompatActivity;
import android.webkit.WebView;
//import com.anoto.adna.C0524R;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.appevents.AppEventsConstants;
import com.pqiorg.multitracker.R;

import java.util.HashMap;

public class LinkWebViewActivity extends AppCompatActivity {

    /* renamed from: m */
    WebView f2880m;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_link_web_view);
        this.f2880m = (WebView) findViewById(R.id.webview);
        String stringExtra = getIntent().getStringExtra("LINK_URL");
        new HashMap().put("1313687a5086c30c8c0c49c304028cc0", AppEventsConstants.EVENT_PARAM_VALUE_NO);
        this.f2880m.loadUrl(stringExtra);
        finish();
    }
}
