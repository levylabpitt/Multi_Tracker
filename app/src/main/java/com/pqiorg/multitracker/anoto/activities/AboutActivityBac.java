package com.pqiorg.multitracker.anoto.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.pqiorg.multitracker.R;


public class AboutActivityBac extends AppCompatActivity {
    private String mFromActivity;

    private void goMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onBackPressed() {
        if (TextUtils.isEmpty(this.mFromActivity) || !this.mFromActivity.equals(SplashActivity.class.getSimpleName())) {
            super.onBackPressed();
        } else {
            goMainActivity();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_about_bac);
        this.mFromActivity = getIntent().getStringExtra("from");
        final CheckBox checkBox = (CheckBox) findViewById(R.id.cb_dont_show_again);
        checkBox.setVisibility((TextUtils.isEmpty(this.mFromActivity) || !this.mFromActivity.equals(SplashActivity.class.getSimpleName())) ? View.GONE : View.VISIBLE);
        findViewById(R.id.bt_proceed).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(AboutActivityBac.this.getApplicationContext());
                if (checkBox.getVisibility() == View.VISIBLE && checkBox.isChecked()) {
                    Editor edit = defaultSharedPreferences.edit();
                    edit.putBoolean("pref_about_dont_show", true);
                    edit.apply();
                }
                AboutActivityBac.this.onBackPressed();
            }
        });
    }
}
