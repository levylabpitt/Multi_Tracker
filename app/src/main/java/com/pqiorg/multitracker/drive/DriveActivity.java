package com.pqiorg.multitracker.drive;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ammarptn.gdriverest.DriveServiceHelper;
import com.ammarptn.gdriverest.GoogleDriveFileHolder;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pqiorg.multitracker.R;
import com.synapse.ProgressHUD;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.adapter.AdapterDrive;
import com.synapse.adapter.DrivePathAdapter;
import com.synapse.listener.DriveItemListener;
import com.synapse.listener.DriveListener;
import com.synapse.listener.DrivePathClickListener;
import com.synapse.model.PathDrive;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ammarptn.gdriverest.DriveServiceHelper.getGoogleDriveService;

public class DriveActivity extends AppCompatActivity implements DriveItemListener, DrivePathClickListener, DriveListener {

    // public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int REQUEST_CODE_SIGN_IN = 100;
    private static final String TAG = "MainActivity";
    // private static String imageStoragePath;
    ImageView signOutButton;
    ImageView signinButton;
    TextView mail_id, txt_no_items;
    /*fab*/
    FloatingActionButton fab, fab_CreateFolder, fab_UploadFile;
    LinearLayout fabLayout1, fabLayout2;
    View fabBGLayout;
    boolean isFABOpen = false;
    /**/
    Context context;
    ProgressHUD mProgressHUD;
    /*fab*/
    String parentFolderid = null;
    ArrayList<PathDrive> List_Path = new ArrayList<>();
    DrivePathAdapter drivePathAdapter;
    ProgressBar progressBar1;
    boolean doubleBackToExitPressedOnce = false;
    private GoogleSignInClient mGoogleSignInClient;
    private DriveServiceHelper mDriveServiceHelper;
    /**/
    private RecyclerView recyclerView, path_recyclerView;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    static final int REQUEST_AUTHORIZATION = 1001;
    // private static final String PREF_ACCOUNT_NAME = "accountName";

    @BindView(R.id._default)
    TextView _default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        ButterKnife.bind(this);
        setToolbar();
        initView();
        initFAB();

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //  ImageView imgBack = (ImageView) toolbar.findViewById(R.id.img_back);
        // imgBack.setVisibility(View.GONE);
        mTitle.setText("Drive");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        // imgBack.setOnClickListener(v -> onBackPressed());


        String defaultFolderName = SharedPreferencesUtil.getDefaultDriveFolderName(this);
        if (!defaultFolderName.equals("")) {
            _default.setVisibility(View.VISIBLE);
            _default.setText("Default: " + defaultFolderName);
        } else {
            _default.setVisibility(View.GONE);
        }
        _default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDefaultSpreadsheetPath();
            }
        });
    }

    protected void showDialogDefaultSpreadsheetPath() {
        String path = SharedPreferencesUtil.getDefaultDriveFolderPath(this);
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                .setMessage(path)
                .setTitle("Default Folder Path")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        // Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 500);


        if (isFABOpen) {
            closeFABMenu();
        } else {
            // super.onBackPressed();
            if (List_Path.size() > 1) {
                List_Path.remove(List_Path.size() - 1);
                drivePathAdapter.notifyDataSetChanged();
                parentFolderid = List_Path.get(List_Path.size() - 1).getMasterId();
                onResume();
            } else {
                // root
                super.onBackPressed();

            }

            // finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account == null) {
            signinButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
            mail_id.setVisibility(View.GONE);
            _default.setVisibility(View.GONE);
            Toast.makeText(context, "You Need To Sign In First", Toast.LENGTH_SHORT).show();
            txt_no_items.setVisibility(View.VISIBLE);
            signIn();
        } else {
            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), account, getString(R.string.app_name)));
            _default.setVisibility(View.VISIBLE);
            mail_id.setText(account.getEmail());
            signinButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            mail_id.setVisibility(View.VISIBLE);
        }
    }

    private void signIn() {
        mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient = buildGoogleSignInClient();
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(getApplicationContext(), "Sign Out Success!", Toast.LENGTH_SHORT).show();
                    signinButton.setVisibility(View.VISIBLE);
                    signOutButton.setVisibility(View.GONE);
                    mDriveServiceHelper = null;
                    mail_id.setText("");
                    recyclerView.setAdapter(null);
                    List_Path.clear();
                    drivePathAdapter.notifyDataSetChanged();
                    txt_no_items.setVisibility(View.VISIBLE);
                    _default.setVisibility(View.GONE);
                    SharedPreferencesUtil.setDefaultDriveFolderId(DriveActivity.this, "");
                    SharedPreferencesUtil.setDefaultDriveFolderName(DriveActivity.this, "");
                    SharedPreferencesUtil.setDefaultDriveFolderPath(DriveActivity.this, "");

                    onResume();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "mGoogleSignInClient is null!", Toast.LENGTH_SHORT).show();
        }
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE, new Scope("https://www.googleapis.com/auth/drive"), new Scope("https://www.googleapis.com/auth/spreadsheets"))
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
                onResume();
            }

        }
    }


    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        Log.d(TAG, "Signed in as " + googleSignInAccount.getEmail());
                        // email.setText(googleSignInAccount.getEmail());
                        mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), googleSignInAccount, getString(R.string.app_name)));
                        Log.d(TAG, "handleSignInResult: " + mDriveServiceHelper);

                        signinButton.setVisibility(View.GONE);
                        signOutButton.setVisibility(View.VISIBLE);
                        _default.setVisibility(View.VISIBLE);
                        mail_id.setVisibility(View.VISIBLE);
                        mail_id.setText(googleSignInAccount.getEmail());
                        if (googleSignInAccount.getEmail() != null) {
                            setAccountName(googleSignInAccount.getEmail());
                        }
                        Toast.makeText(getApplicationContext(), "Sign in Success!", Toast.LENGTH_SHORT).show();
                        onResume();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to sign in.", e);
                        signinButton.setVisibility(View.VISIBLE);
                        signOutButton.setVisibility(View.GONE);
                        mail_id.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Sign in Failed!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void setAccountName(String accountName) {
        if (accountName != null) {
          /*  SharedPreferences settings =
                    getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PREF_ACCOUNT_NAME, accountName);
            editor.commit();*/
            SharedPreferencesUtil.setAccountName(DriveActivity.this, accountName);
        }
    }

    private void initView() {
        context = DriveActivity.this;
        signOutButton = findViewById(R.id.google_sign_out);
        signinButton = findViewById(R.id.google_sign_in);
        mail_id = findViewById(R.id.mail_id);
        txt_no_items = findViewById(R.id.txt_no_items);
        progressBar1 = findViewById(R.id.progressBar1);

        recyclerView = findViewById(R.id.recycler_view);
        path_recyclerView = (RecyclerView) findViewById(R.id.recycler_view_path);
        path_recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        path_recyclerView.setItemAnimator(new DefaultItemAnimator());
        //   path_recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, getResources().getInteger(R.integer.list_divider_margin_left)));
        if (List_Path.isEmpty()) {
            List_Path.add(new PathDrive("Root", null));
        }
        drivePathAdapter = new DrivePathAdapter(List_Path, context, this);
        path_recyclerView.setAdapter(drivePathAdapter);


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  signOut();
                Dialog_GoogleLogoutAlert();
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
            }
        });
    }

    private void initFAB() {
        fabLayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) findViewById(R.id.fabLayout2);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_CreateFolder = (FloatingActionButton) findViewById(R.id.fab1);
        fab_UploadFile = (FloatingActionButton) findViewById(R.id.fab2);

        fabBGLayout = findViewById(R.id.fabBGLayout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });
        fab_CreateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }

                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if (account == null || account.getEmail() == null || mDriveServiceHelper == null) {
                    Toast.makeText(context, "You Need To Sign In First", Toast.LENGTH_SHORT).show();
                    signIn();
                } else {
                    Dialog_CreateFolder();
                }

            }
        });
        fab_UploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
                // Dialog_ChooseFile();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (List_Path.isEmpty()) {
            List_Path.add(new PathDrive("Root", null));
        }
        drivePathAdapter.notifyDataSetChanged();
        String defaultFolderName = SharedPreferencesUtil.getDefaultDriveFolderName(this);
        if (!defaultFolderName.equals("")) {
            _default.setVisibility(View.VISIBLE);
            _default.setText("Default: " + defaultFolderName);
        } else {
            _default.setVisibility(View.GONE);
        }

        EmptyTrash();
        viewFileFolder();
    }

    private void showFABMenu() {
        isFABOpen = true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.GONE);
        //fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);
        fab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        //fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotation(0);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        // fabLayout3.animate().translationY(0);
        fabLayout1.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    //fabLayout3.setVisibility(View.GONE);
                }
                if (fab.getRotation() != -180) {
                    fab.setRotation(-180);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void viewFileFolder() {
        if (mDriveServiceHelper == null) {
            return;
        }
        progressBar1.setVisibility(View.VISIBLE);

        mDriveServiceHelper.queryFiles(parentFolderid)
                .addOnSuccessListener(new OnSuccessListener<List<GoogleDriveFileHolder>>() {
                    @Override
                    public void onSuccess(List<GoogleDriveFileHolder> googleDriveFileHolders) {
                        if (pullToRefresh != null && pullToRefresh.isRefreshing())
                            pullToRefresh.setRefreshing(false);
                        progressBar1.setVisibility(View.INVISIBLE);
                        Gson gson = new Gson();
                        String userJson = gson.toJson(googleDriveFileHolders);
                        Type userListType = new TypeToken<ArrayList<GoogleDriveFileHolder>>() {
                        }.getType();
                        ArrayList<GoogleDriveFileHolder> list = gson.fromJson(userJson, userListType);
                        ArrayList<GoogleDriveFileHolder> list_new = getOnlyFolderAndImages(list);
                        // Utility.checkBluetoothSheetAvailability(DriveActivity.this, list);
                        AdapterDrive adapterDrive = new AdapterDrive(context, list_new, DriveActivity.this, DriveActivity.this);
                        recyclerView.setAdapter(adapterDrive);
                        if (list_new.isEmpty()) {
                            txt_no_items.setVisibility(View.VISIBLE);
                        } else {
                            txt_no_items.setVisibility(View.GONE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //    Toast.makeText(DriveActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        Utility.ReportNonFatalError("viewFileFolder", e.getMessage());
                        Log.e("Drive Error", Objects.requireNonNull(e.getMessage()));
                        if (pullToRefresh != null && pullToRefresh.isRefreshing())
                            pullToRefresh.setRefreshing(false);
                        progressBar1.setVisibility(View.INVISIBLE);
                        String ex = e.getMessage();
                        recyclerView.setAdapter(null);
                        txt_no_items.setVisibility(View.VISIBLE);
                        if (e instanceof UserRecoverableAuthIOException) {
                            startActivityForResult(
                                    ((UserRecoverableAuthIOException) e).getIntent(),
                                    REQUEST_AUTHORIZATION);
                        }
                    }
                });
    }


    public ArrayList<GoogleDriveFileHolder> getOnlyFolderAndImages(ArrayList<GoogleDriveFileHolder> list) {
        ArrayList<GoogleDriveFileHolder> list_new = new ArrayList<>();
        ArrayList<GoogleDriveFileHolder> list_folders = new ArrayList<>();
        ArrayList<GoogleDriveFileHolder> list_images = new ArrayList<>();
        for (GoogleDriveFileHolder googleDriveFileHolder : list) {
            final String file_Folder_Name = googleDriveFileHolder.getName();
            String fileExt = "";
            if (file_Folder_Name.contains(".")) {
                fileExt = file_Folder_Name.substring(file_Folder_Name.lastIndexOf("."));
            }
           /* if (googleDriveFileHolder.getMimeType().equals(DriveFolder.MIME_TYPE) || Arrays.asList(Utility.imgExt).contains(fileExt)) {
                list_new.add(googleDriveFileHolder);
            }*/

            if (googleDriveFileHolder.getMimeType().equals(DriveFolder.MIME_TYPE)) {
                list_folders.add(googleDriveFileHolder);
            } else if (Arrays.asList(Utility.imgExt).contains(fileExt)) {
                list_images.add(googleDriveFileHolder);
            }
        }

        list_new.addAll(list_folders);
        list_new.addAll(list_images);


        return list_new;
    }

    public void refreshList(String file_folder_id, String file_folder_name) {

        parentFolderid = file_folder_id;
        List_Path.add(new PathDrive(file_folder_name, file_folder_id));
        drivePathAdapter.notifyDataSetChanged();
        onResume();

    }

    @Override
    public void onDrivePathClick(String itemID, String itemName) {
        refreshList(itemID, itemName);
    }


    private void createFolder(String folderName) {
        if (mDriveServiceHelper == null) {
            return;
        }
        progressBar1.setVisibility(View.VISIBLE);
        // you can provide  folder id in case you want to save this file inside some folder.
        // if folder id is null, it will save file to the root
        mDriveServiceHelper.createFolder(folderName, parentFolderid)
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        progressBar1.setVisibility(View.INVISIBLE);
                        Gson gson = new Gson();
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolder));
                        Toast.makeText(context, "Successfully created", Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utility.ReportNonFatalError("createFolder", e.getMessage());
                        // Toast.makeText(DriveActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        progressBar1.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                });
    }

    public void Dialog_RenameFileFolder(final String fileId, final String name, final String content, final String mimeType) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String title = name;
        if (name.contains(".")) {
            // title=title.substring(0,title.indexOf(".")-1);
        }
        alertDialog.setMessage("");


        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(30, 10, 30, 10);
        // input.setPadding(5,5,5,5);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        input.setText(title);
        alertDialog.setPositiveButton("Rename",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (input.getText().length() < 1) {
                            Toast.makeText(context, "Please enter folder name", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            RenameFileFolder(fileId, input.getText().toString().trim(), content, mimeType);
                        }

                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }

    private void RenameFileFolder(final String fileId, final String name, final String content, final String mimeType) {
        if (mDriveServiceHelper == null) {
            return;
        }
        progressBar1.setVisibility(View.VISIBLE);
        mDriveServiceHelper.saveFile(fileId, name, content, mimeType)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar1.setVisibility(View.INVISIBLE);
                        onResume();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar1.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteFileFolder(final String fileFolderId) {
        if (mDriveServiceHelper == null) {
            return;
        }
        progressBar1.setVisibility(View.VISIBLE);
        mDriveServiceHelper.deleteFolderFile(fileFolderId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar1.setVisibility(View.INVISIBLE);
                        if (fileFolderId.equalsIgnoreCase(SharedPreferencesUtil.getDefaultDriveFolderId(DriveActivity.this))) {
                            SharedPreferencesUtil.setDefaultDriveFolderId(DriveActivity.this, "");
                        }
                        onResume();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar1.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void downloadFile(final String filenameWithExt, String fileId, final String mimeType) {
        if (mDriveServiceHelper == null) {
            return;
        }

        final File fileCache = Utility.createDriveCacheFolder(filenameWithExt);
        progressBar1.setVisibility(View.VISIBLE);
        showprogressdialog();

        mDriveServiceHelper.downloadFile(fileCache, fileId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar1.setVisibility(View.INVISIBLE);
                        hideprogressdialog();
                        Toast.makeText(context, "Ready", Toast.LENGTH_SHORT).show();
                        Uri fileURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", fileCache);

                        Intent newIntent = new Intent(getApplicationContext(), FullscreenImageViewDrive.class);
                        newIntent.putExtra("uri", fileURI.toString());
                        newIntent.putExtra("name", filenameWithExt);
                        startActivity(newIntent);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar1.setVisibility(View.INVISIBLE);
                        hideprogressdialog();
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void Dialog_CreateFolder() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("New Folder");
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(40, 10, 40, 10);

        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (input.getText().length() < 1) {
                            Toast.makeText(context, "Please enter folder name", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            createFolder(input.getText().toString().trim());

                        }

                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }
    public void Dialog_GoogleLogoutAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Do you want to sign out from current google account.");
        alertDialog.setPositiveButton("Yes",
                (dialog, which) -> {
                    dialog.cancel();
                    signOut();
                });
        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }
    public void Dialog_ChooseAction(final String fileId, final String name, final String content, final String mimeType) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String[] colors = {"Rename", "Delete"};
        alertDialog.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                if (which == 0) {
                    Dialog_RenameFileFolder(fileId, name, content, mimeType);

                } else if (which == 1) {
                    deleteFileFolder(fileId);
                }
            }
        });


        alertDialog.show();


    }

    public void showprogressdialog() {
        try {
            mProgressHUD = ProgressHUD.show(context, "", false);
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
 /*   public void Dialog_ChooseFile() {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.openattachmentdilog_new);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        LinearLayout cameralayout = dialog
                .findViewById(R.id.cameralayout);
        LinearLayout gallarylayout = dialog
                .findViewById(R.id.gallarylayout);
        LinearLayout filelayout = dialog
                .findViewById(R.id.filelayout);
        ImageView crosse = dialog
                .findViewById(R.id.close);
        crosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               *//* if(userAdapter.getItemCount()==0){
                    finishs();
                }*//*
            }
        });
        cameralayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (CameraUtils.checkPermissions(context)) {
                    captureImage();
                } else {
                    Toast.makeText(context, "Camera & Write External Storage permission are disabled!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gallarylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumSelectActivity.class);
                intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 50);
                startActivityForResult(intent, AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(context, file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }*/

    private void uploadFile(String filePath) {
        if (mDriveServiceHelper == null) {
            return;
        }
        progressBar1.setVisibility(View.VISIBLE);
        String fileExt = "";
        if (filePath.contains(".")) {
            fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);
        }
        File file = new File(filePath);
        String mime_type = MimeUtils.guessMimeTypeFromExtension(fileExt);

        mDriveServiceHelper.uploadFile(file, mime_type, parentFolderid)
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson = new Gson();
                        Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolder));
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.INVISIBLE);
                        onResume();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.INVISIBLE);

                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //   Utility.deleteLocalFilesfromDrive();
    }

    @Override
    public void onItemClick(String itemID, String itemName, String itemType) {
        refreshList(itemID, itemName);
    }

    @Override
    public void onItemDelete(String itemName, String itemID, String itemType) {
        showBottomSheetDialog(itemName, itemID, itemType);
      /*
        String defaultSheetId = SharedPreferencesUtil.getDefaultDriveFolderId(this);
        if (defaultSheetId.equals(itemID)) {
            Dialog_DeleteFolder(itemID);
        } else {
            deleteFileFolder(itemID);
        }*/


    }

    @Override
    public void onRefresh() {
        onResume();
    }


    public void Dialog_DeleteFolder(String itemID) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("This is default google drive bfolder for saving QR photos.");
        alertDialog.setMessage("Do you really want to delete it?");


        alertDialog.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        deleteFileFolder(itemID);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }


    private void EmptyTrash() {
        if (mDriveServiceHelper == null) {
            return;
        }
        progressBar1.setVisibility(View.VISIBLE);
        mDriveServiceHelper.EmptyTrash().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar1.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar1.setVisibility(View.INVISIBLE);
                if (e instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) e).getIntent(),
                            REQUEST_AUTHORIZATION);
                }
            }
        });

    }

    @Override
    public void onFavouriteClick(String ItemId, String ItemName) {
        String defaultDriveId = SharedPreferencesUtil.getDefaultDriveFolderId(context);

        if (ItemId.equals(defaultDriveId)) {
            Utility.showToast(context, "Default for saving images.");
        } else {

            Utility.showToast(context, "Selected for saving images.");
            SharedPreferencesUtil.setDefaultDriveFolderId(context, ItemId);
            SharedPreferencesUtil.setDefaultDriveFolderName(context, ItemName);
            String path = drivePathAdapter.getPath();
            SharedPreferencesUtil.setDefaultDriveFolderPath(context, path + ItemName);

            setToolbar();
            onRefresh();
        }

    }

    public void showBottomSheetDialog(String itemName, String itemID, String itemType) {
        View view = getLayoutInflater().inflate(R.layout.menu_manage_drive, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        LinearLayout action_delete = view.findViewById(R.id.action_delete);
        LinearLayout action_rename = view.findViewById(R.id.action_rename);

        TextView folder_name = view.findViewById(R.id.name);
        folder_name.setText(itemName);
        action_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String defaultSheetId = SharedPreferencesUtil.getDefaultDriveFolderId(DriveActivity.this);
                if (defaultSheetId.equals(itemID)) {
                    Dialog_DeleteFolder(itemID);
                } else {
                    deleteFileFolder(itemID);
                }

            }
        });

        action_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Dialog_RenameFolder(itemID, itemName);
            }
        });


        dialog.setContentView(view);
        dialog.show();
    }

    private void RenameFileFolder(final String fileId, final String name) {
        if (mDriveServiceHelper == null) {
            return;
        }
        Utility.ReportNonFatalError("RenameFileFolder", "Started");
        progressBar1.setVisibility(View.VISIBLE);
        mDriveServiceHelper.updateFile(fileId, name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar1.setVisibility(View.INVISIBLE);
                        hideprogressdialog();
                        viewFileFolder();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideprogressdialog();
                        progressBar1.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        Utility.ReportNonFatalError("RenameFileFolder", "Failed-->\n" + e.getMessage());
                    }
                });
    }

    public void Dialog_RenameFolder(String fileFolderId, String currentName) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Rename Folder");
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(40, 10, 40, 10);

        input.setLayoutParams(lp);
        alertDialog.setView(input);
        input.setText(currentName);
        alertDialog.setPositiveButton("Rename",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (input.getText().length() < 1) {
                            Toast.makeText(context, "Please enter folder name", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            RenameFileFolder(fileFolderId, input.getText().toString().trim());

                        }

                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }
}
