
package com.pqiorg.multitracker.drive;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ammarptn.gdriverest.DriveServiceHelper;
import com.ammarptn.gdriverest.GoogleDriveFileHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pqiorg.multitracker.R;
import com.synapse.ProgressHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.ammarptn.gdriverest.DriveServiceHelper.getGoogleDriveService;

//import planet.info.skyline.R;
//import planet.info.skyline.old_activity.BaseActivity;

public class FullscreenImageViewDrive2 extends AppCompatActivity {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;


    private static final int REQUEST_CODE_SIGN_IN = 100;
    private static final String TAG = "MainActivity";
    Context context;
    ProgressHUD mProgressHUD;
    String parentFolderid = null;
    String fileId = null;
    private GoogleSignInClient mGoogleSignInClient;
    private DriveServiceHelper mDriveServiceHelper;

    static final int REQUEST_AUTHORIZATION = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_view);
        showprogressdialog();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("driveParentId") && bundle.containsKey("driveFileId")) {
            parentFolderid = bundle.getString("driveParentId");
            fileId = bundle.getString("driveFileId");
        }

        // initialize the view and the gesture detector
        mImageView = findViewById(R.id.imageView);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        setToolbar();


    }


    void viewImage(String imgURL) {

        Glide.with(this).load(imgURL).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                hideprogressdialog();
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                hideprogressdialog();
                return false;
            }
        })
                .into(mImageView);

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        final String name = getIntent().getStringExtra("name");
        // mTitle.setText(Utility.getLimitedCharFromString(name));
        mTitle.setText(name);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleGestureDetector.onTouchEvent(event);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        // when a scale gesture is detected, use it to resize the image
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mImageView.setScaleX(mScaleFactor);
            mImageView.setScaleY(mScaleFactor);
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account == null) {
            signIn();
        } else {
            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), account, getString(R.string.app_name)));
            viewFileFolder();
        }
    }

    private void signIn() {
        mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }


    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .requestEmail()
                        .build();
        return GoogleSignIn.getClient(getApplicationContext(), signInOptions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SIGN_IN) {
                if (resultCode == Activity.RESULT_OK && resultData != null) {
                    handleSignInResult(resultData);
                }
            } else if (requestCode == REQUEST_AUTHORIZATION) {
              //  onResume();
                viewFileFolder();
            }

        }
    }


    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        Log.d(TAG, "Signed in as " + googleSignInAccount.getEmail());
                        mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), googleSignInAccount, getString(R.string.app_name)));
                       // onResume();
                        viewFileFolder();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to sign in.", e);
                        Toast.makeText(getApplicationContext(), "Sign in Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void viewFileFolder() {

        if (mDriveServiceHelper == null) {
            return;
        }


        mDriveServiceHelper.queryFiles(parentFolderid)
                .addOnSuccessListener(new OnSuccessListener<List<GoogleDriveFileHolder>>() {
                    @Override
                    public void onSuccess(List<GoogleDriveFileHolder> googleDriveFileHolders) {
                        String fullImageLink = "";
                        Gson gson = new Gson();
                        String userJson = gson.toJson(googleDriveFileHolders);
                        Type userListType = new TypeToken<ArrayList<GoogleDriveFileHolder>>() {
                        }.getType();
                        ArrayList<GoogleDriveFileHolder> list = gson.fromJson(userJson, userListType);
                        for (GoogleDriveFileHolder googleDriveFileHolder : list) {
                            if (googleDriveFileHolder.getId().equals(fileId)) {
                                fullImageLink = googleDriveFileHolder.getThumbnailLink();
                                if (fullImageLink.contains("=")) {
                                    fullImageLink = fullImageLink.substring(0, fullImageLink.lastIndexOf("="));
                                }

                                break;
                            }
                        }
                      //  hideprogressdialog();
                        viewImage(fullImageLink);

                        if (list.isEmpty()) {
                            // txt_no_items.setVisibility(View.VISIBLE);
                        } else {
                            //  txt_no_items.setVisibility(View.GONE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideprogressdialog();
                        String ex = e.getMessage();
                        if (e instanceof UserRecoverableAuthIOException) {
                            startActivityForResult(
                                    ((UserRecoverableAuthIOException) e).getIntent(),
                                    REQUEST_AUTHORIZATION);
                        }
                    }
                });
    }


    public void showprogressdialog() {
        try {
            mProgressHUD = ProgressHUD.show(FullscreenImageViewDrive2.this, "", false);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void hideprogressdialog() {
        try {
            if (mProgressHUD.isShowing()) {
                mProgressHUD.dismiss();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
