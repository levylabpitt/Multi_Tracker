package com.pqiorg.multitracker.anoto.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.fragments.TutorialFragment;


public class AboutActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public ImageView ivDot1;
    /* access modifiers changed from: private */
    public ImageView ivDot2;
    /* access modifiers changed from: private */
    public ImageView ivDot3;
    /* access modifiers changed from: private */
    public Fragment mCurFragment = new TutorialFragment();
    private String mFromActivity;
    /* access modifiers changed from: private */
    public int mPosition = 0;
    /* access modifiers changed from: private */
    public ViewPager viewPager;

    private class adapter extends FragmentPagerAdapter {
        public adapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public int getCount() {
            return 3;
        }

        public Fragment getItem(int i) {
            Bundle bundle;
            int i2;
            String str;
            if (i < 0 || 3 <= i) {
                return null;
            }
            new Bundle();
            switch (i) {
                case 0:
                    bundle = new Bundle();
                    AboutActivity.this.mCurFragment = new TutorialFragment();
                    str = "data";
                    i2 = 0;
                    break;
                case 1:
                    bundle = new Bundle();
                    AboutActivity.this.mCurFragment = new TutorialFragment();
                    str = "data";
                    i2 = 1;
                    break;
                case 2:
                    bundle = new Bundle();
                    AboutActivity.this.mCurFragment = new TutorialFragment();
                    str = "data";
                    i2 = 2;
                    break;
                default:
                    return AboutActivity.this.mCurFragment;
            }
            bundle.putInt(str, i2);
            AboutActivity.this.mCurFragment.setArguments(bundle);
            return AboutActivity.this.mCurFragment;
        }
    }

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
        setContentView((int) R.layout.activity_about);
        this.mFromActivity = getIntent().getStringExtra("from");
        this.ivDot1 = (ImageView) findViewById(R.id.iv_dot1);
        this.ivDot2 = (ImageView) findViewById(R.id.iv_dot2);
        this.ivDot3 = (ImageView) findViewById(R.id.iv_dot3);
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.viewPager.setAdapter(new adapter(getSupportFragmentManager()));
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                StringBuilder sb = new StringBuilder();
                sb.append("onPageSelected : ");
                sb.append(i);
                Log.d("ITPANGPANG", sb.toString());
                AboutActivity.this.mPosition = i;
                if (i == 0) {
                    AboutActivity.this.ivDot1.setImageResource(R.drawable.ic_dot_fill);
                    AboutActivity.this.ivDot2.setImageResource(R.drawable.ic_dot_none);
                } else if (i == 1) {
                    AboutActivity.this.ivDot1.setImageResource(R.drawable.ic_dot_none);
                    AboutActivity.this.ivDot2.setImageResource(R.drawable.ic_dot_fill);
                } else {
                    if (i == 2) {
                        AboutActivity.this.ivDot1.setImageResource(R.drawable.ic_dot_none);
                        AboutActivity.this.ivDot2.setImageResource(R.drawable.ic_dot_none);
                        AboutActivity.this.ivDot3.setImageResource(R.drawable.ic_dot_fill);
                    }
                    return;
                }
                AboutActivity.this.ivDot3.setImageResource(R.drawable.ic_dot_none);
            }
        });
        this.viewPager.setCurrentItem(this.mPosition);
        View findViewById = findViewById(R.id.bt_left);
        View findViewById2 = findViewById(R.id.bt_right);
        findViewById.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AboutActivity aboutActivity;
                int d;
                if (AboutActivity.this.mPosition == 0) {
                    aboutActivity = AboutActivity.this;
                    d = 0;
                } else {
                    aboutActivity = AboutActivity.this;
                    d = AboutActivity.this.mPosition - 1;
                }
                aboutActivity.mPosition = d;
                AboutActivity.this.viewPager.setCurrentItem(AboutActivity.this.mPosition);
            }
        });
        findViewById2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AboutActivity aboutActivity;
                int i = 2;
                if (AboutActivity.this.mPosition == 2) {
                    aboutActivity = AboutActivity.this;
                } else {
                    aboutActivity = AboutActivity.this;
                    i = AboutActivity.this.mPosition + 1;
                }
                aboutActivity.mPosition = i;
                AboutActivity.this.viewPager.setCurrentItem(AboutActivity.this.mPosition);
            }
        });
        final CheckBox checkBox = (CheckBox) findViewById(R.id.cb_dont_show_again);
        checkBox.setVisibility((TextUtils.isEmpty(this.mFromActivity) || !this.mFromActivity.equals(SplashActivity.class.getSimpleName())) ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.bt_proceed).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(AboutActivity.this.getApplicationContext());
                if (checkBox.getVisibility() == View.VISIBLE && checkBox.isChecked()) {
                    Editor edit = defaultSharedPreferences.edit();
                    edit.putBoolean("pref_about_dont_show", true);
                    edit.apply();
                }
                AboutActivity.this.onBackPressed();
            }
        });
    }
}
