package com.pqiorg.multitracker.spreadsheet.creater;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.GridData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.spreadsheet.viewer.SheetActivity;
//import com.spreadsheet.viewer.SheetActivity;
import com.synapse.Constants;
import com.synapse.ProgressHUD;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.adapter.DrivePathAdapter;
import com.synapse.adapter.DriveSheetsAdapter;
import com.synapse.adapter.GridSpacingItemDecoration;
import com.synapse.listener.DriveItemListener;
import com.synapse.listener.DrivePathClickListener;
import com.synapse.listener.DriveListener;
import com.synapse.model.PathDrive;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.ammarptn.gdriverest.DriveServiceHelper.EXPORT_TYPE_MS_EXCEL;
import static com.ammarptn.gdriverest.DriveServiceHelper.TYPE_GOOGLE_SHEETS;
import static com.ammarptn.gdriverest.DriveServiceHelper.getGoogleDriveService;


public class SpreadSheetListActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, DriveItemListener, DrivePathClickListener, DriveListener {
    //    Button mSignOut,mRevokeAccess;
    // SignInButton mSignIn;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    AlertDialog alertDialog;
    // String token;
    GoogleAccountCredential mCredential;
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS, "https://www.googleapis.com/auth/drive", "https://www.googleapis.com/auth/spreadsheets"};

    public static final int indexOfStatusColumn = 4;

    //   Drive.SCOPE_FILE,new Scope("https://www.googleapis.com/auth/drive"), new Scope("https://www.googleapis.com/auth/spreadsheets"
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Sheets API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    // private ProgressBar mProgressBar;
    public static final int RC_SIGN_IN = 1;
    ProgressHUD mProgressHUD;
    String spreadsheetTitle = "";
    public static final String SheetOne = "QR Sheet", SheetTwo = "Bluetooth Sheet";

    boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.recycler_sheets)
    RecyclerView recycler_sheets;


    @BindView(R.id.recycler_view_path)
    RecyclerView recycler_path;

    @BindView(R.id._default)
    TextView _default;


    //@BindView(R.id.floating_action_button)
    //FloatingActionButton floating_action_button;

    FloatingActionButton fab, fab_CretaeFolder, fab_createSpreadsheet;
    LinearLayout fabLayout1, fabLayout2;
    View fabBGLayout;
    boolean isFABOpen = false;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    private DriveServiceHelper mDriveServiceHelper;
    String parentFolderid = null;
    String spreadsheetId = "";
    // String SpreadsheetId = "";


    List<List<Object>> QRSheetData = new ArrayList<>();
    List<List<Object>> BluetoothSheetData = new ArrayList<>();
    List<Sheet> sheetList = new ArrayList<>();


    Context context;
    ImageView signOutButton;
    ImageView signinButton;
    TextView mail_id, txt_no_items;
    ArrayList<PathDrive> List_Path = new ArrayList<>();
    DrivePathAdapter drivePathAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spreadsheet_list);
        ButterKnife.bind(this);
        context = SpreadSheetListActivity.this;

        mCredential = GoogleAccountCredential.usingOAuth2(
                context, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());


        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE, new Scope("https://www.googleapis.com/auth/drive"), new Scope("https://www.googleapis.com/auth/spreadsheets"))
                        .requestEmail()
                        .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);


        initView();
        if (account == null) {
            Toast.makeText(context, "You Need To Sign In First", Toast.LENGTH_SHORT).show();
            txt_no_items.setVisibility(View.VISIBLE);
        }
        initGoogleLayout();
        setToolbar();
        initFAB();

    }

    void initGoogleLayout() {
        // If already signed in with the app it can be obtained here
        if (account == null) {
            Utility.ReportNonFatalError("0", "You Need To Sign In First");
            signinButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
            _default.setVisibility(View.GONE);
            signIn();
        } else {
            signinButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            _default.setVisibility(View.VISIBLE);

            if (account.getEmail() == null) {
                Utility.ReportNonFatalError("account.getEmail() == null", String.valueOf(account.getEmail()) + "- " + account.getDisplayName());
                getResultsFromApi();
            } else {
                Utility.ReportNonFatalError("account.getEmail() != null", account.getEmail() + account.getGivenName() + account.getFamilyName());
                setAccountName(account.getEmail());
                mail_id.setText(account.getEmail());

            }
            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(context, account, getString(R.string.app_name)));
        }


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_GoogleLogoutAlert();
               // signOut();
            }
        });

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
      //  ImageView imgBack = (ImageView) toolbar.findViewById(R.id.img_back);

        mTitle.setText("Spreadsheet");
        // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

     //   imgBack.setOnClickListener((View.OnClickListener) v -> onBackPressed());
        String defaultSheetName = SharedPreferencesUtil.getDefaultSheetName(this);
        if (!defaultSheetName.equals("")) {
            _default.setVisibility(View.VISIBLE);
            _default.setText("Default: " + defaultSheetName + ".xlsx");
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

    private void initView() {
        //context = SpreadSheetListActivity.this;
        signOutButton = findViewById(R.id.google_sign_out);
        signinButton = findViewById(R.id.google_sign_in);
        mail_id = findViewById(R.id.mail_id);
        txt_no_items = findViewById(R.id.txtvw_noItems);
        progressBar1 = findViewById(R.id.progressBar1);


        recycler_sheets.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_sheets.setItemAnimator(new DefaultItemAnimator());
        recycler_sheets.addItemDecoration(new GridSpacingItemDecoration(2, 10, true));


        recycler_path = (RecyclerView) findViewById(R.id.recycler_view_path);
        recycler_path.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycler_path.setItemAnimator(new DefaultItemAnimator());

        if (List_Path.isEmpty()) {
            List_Path.add(new PathDrive("Root", null));
        }
        drivePathAdapter = new DrivePathAdapter(List_Path, context, this);
        recycler_path.setAdapter(drivePathAdapter);


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
            }
        });
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


    @OnClick({
            // R.id.floating_action_button,

    })
    public void onClick(@NonNull View view) {

        switch (view.getId()) {
            // case R.id.floating_action_button:

            //    break;

        }
    }

    private void signIn() {
        Utility.ReportNonFatalError("signInIntent", "You Need To Sign In First");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            Utility.ReportNonFatalError("RC_SIGN_IN", account.getEmail() + account.getGivenName() + account.getFamilyName());

            // Signed in successfully, show authenticated UI.
            initGoogleLayout();
            if (account.getEmail() == null) {
                getResultsFromApi();
            } else {
                setAccountName(account.getEmail());
            }

            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(context, account, getString(R.string.app_name)));
            onResume();
        } catch (ApiException e) {
            Utility.ReportNonFatalError("RC_SIGN_IN ApiException", String.valueOf(e.getStatusCode() + "-" + e));
            Toast.makeText(context, "Sign In Failed.Try again Later", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Utility.ReportNonFatalError("RC_SIGN_IN Exception", "-" + e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (List_Path.isEmpty()) {
            List_Path.add(new PathDrive("Root", null));
        }
        drivePathAdapter.notifyDataSetChanged();

        String defaultFolderName = SharedPreferencesUtil.getDefaultSheetName(this);
        if (!defaultFolderName.equals("")) {
            _default.setVisibility(View.VISIBLE);
            _default.setText("Default: " + defaultFolderName);
        } else {
            _default.setVisibility(View.GONE);
        }

        EmptyTrash();
        viewFileFolder();
    }

    private void initFAB() {
        fabLayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) findViewById(R.id.fabLayout2);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_CretaeFolder = (FloatingActionButton) findViewById(R.id.fab1);
        fab_createSpreadsheet = (FloatingActionButton) findViewById(R.id.fab2);

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
        fab_CretaeFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }

                if (account == null || account.getEmail() == null || mDriveServiceHelper == null) {
                    Toast.makeText(context, "You Need To Sign In First", Toast.LENGTH_SHORT).show();
                    signIn();
                } else {
                    Dialog_CreateFolder();
                }


            }
        });
        fab_createSpreadsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
                if (account == null || account.getEmail() == null || mDriveServiceHelper == null) {
                    Toast.makeText(context, "You Need To Sign In First", Toast.LENGTH_SHORT).show();
                    signIn();
                } else {
                    dialogSpreadsheetName();
                    // Dialog_CreateSpreadsheet();
                }


            }
        });

    }

    public void Dialog_CreateSpreadsheet() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("New Spreadsheet");
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
                            Toast.makeText(context, "Please enter spreadsheet name", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            spreadsheetTitle = input.getText().toString().trim();
                            new CrateSpreadsheetTask(mCredential, context).execute();
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

    public void Dialog_DeleteSpreadsheet(String itemID) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("This is default spreadsheet for saving data.");
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

    protected void showDialogDefaultSpreadsheetPath() {
        String path = SharedPreferencesUtil.getDefaultSheetPath(this);
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                .setMessage(path)
                .setTitle("Default Spreadsheet Path")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();


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
                        Toast.makeText(context, "Successfully created", Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar1.setVisibility(View.INVISIBLE);
                        //  Log.d(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                });
    }

    private void showFABMenu() {
        isFABOpen = true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
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

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Signed Out", Toast.LENGTH_SHORT).show();
                        signinButton.setVisibility(View.VISIBLE);
                        signOutButton.setVisibility(View.GONE);
                        mDriveServiceHelper = null;
                        mail_id.setText("");
                        recycler_sheets.setAdapter(null);
                        List_Path.clear();
                        drivePathAdapter.notifyDataSetChanged();
                        txt_no_items.setVisibility(View.VISIBLE);
                        _default.setVisibility(View.GONE);
                        SharedPreferencesUtil.setDefaultSheetId(SpreadSheetListActivity.this, "");
                        SharedPreferencesUtil.setDefaultSheetName(SpreadSheetListActivity.this, "");
                        SharedPreferencesUtil.setDefaultSheetPath(SpreadSheetListActivity.this, "");
                        onResume();


                    }
                });
    }

    void dialogSpreadsheetName() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_spreadsheet_name, null);
        dialogBuilder.setView(dialogView);

        Button buttonSaveTemplate = (Button) dialogView.findViewById(R.id.buttonSaveTemplate);
        EditText et_templateName = (EditText) dialogView.findViewById(R.id.et_templateName);


        buttonSaveTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (et_templateName.getText().toString().trim().isEmpty()) {
                    Utility.showToast(context, "Enter Spreadsheet name!");
                } else {
                    spreadsheetTitle = et_templateName.getText().toString().trim();
                    uploadSampleSpreadsheetUsingDriveAPI(TYPE_GOOGLE_SHEETS, parentFolderid);
                    // new CrateSpreadsheetTask(mCredential, context).execute();
                }
            }
        });


        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        }
    }


    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = SharedPreferencesUtil.getAccountName(this);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                Utility.ReportNonFatalError("2", accountName);
            } else {
                Utility.ReportNonFatalError("3", "startActivityForResult");
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            Utility.ReportNonFatalError("3", "requestPermissions");
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }


    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Log.e(this.toString(), "This app requires Google Play Services. Please install " +
                            "Google Play Services on your device and relaunch this app.");
                    Utility.ReportNonFatalError("4", "this app requires Google Play Services. Please install Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    Utility.ReportNonFatalError("REQUEST_ACCOUNT_PICKER", data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    setAccountName(accountName);

                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    Utility.ReportNonFatalError("REQUEST_AUTHORIZATION", spreadsheetTitle);
                    if (spreadsheetTitle.equals("")) {
                        onResume();
                    } else {
                        //new CrateSpreadsheetTask(mCredential, context).execute();
                        uploadSampleSpreadsheetUsingDriveAPI(TYPE_GOOGLE_SHEETS, parentFolderid);
                    }

                }
                break;

            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);

                break;

        }
    }

    private void setAccountName(String accountName) {
        if (accountName != null) {
            SharedPreferencesUtil.setAccountName(context, accountName);
            mCredential.setSelectedAccountName(accountName);

        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                SpreadSheetListActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    public void Dialog_GoogleLogoutAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SpreadSheetListActivity.this);
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
    private class CrateSpreadsheetTask extends AsyncTask<Void, Void, Void> {
        private Sheets mService = null;
        private Exception mLastError = null;
        private Context mContext;

        CrateSpreadsheetTask(GoogleAccountCredential credential, Context context) {

            mContext = context;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {


                String spreadsheetId = createSpreadSheetForQRScanner();
                writeTitleToQRScannerSheet(spreadsheetId);
                writeTitleToBluetoothBeaconSheet(spreadsheetId);
                if (parentFolderid != null) {
                    moveSpreadsheetToDesiredFolder(spreadsheetId, parentFolderid);
                }

                spreadsheetTitle = "";
            } catch (Exception e) {
                mLastError = e;
                Utility.ReportNonFatalError("CrateSpreadsheetTask", mLastError.getMessage());
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {

                    startActivityForResult(((UserRecoverableAuthIOException) mLastError).getIntent(), REQUEST_AUTHORIZATION);

                } else {
                    Log.e(this.toString(), "The following error occurred:\n" + mLastError.getMessage());
                }
                Log.e(this.toString(), e + "");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //  mTextView.setText("Calling Api");
            // appendToSheetBtn.setVisibility(View.INVISIBLE);
            //mProgressBar.setVisibility(View.VISIBLE);
            showprogressdialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // mProgressBar.setVisibility(View.GONE);
            hideprogressdialog();
            //   mTextView.setText("Task Completed.");
            //refreshSpreadsheetList();
            viewFileFolder();
        }


        @Override
        protected void onCancelled() {

            hideprogressdialog();
            // mProgressBar.setVisibility(View.GONE);
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    // mTextView.setText("The following error occurred:\n"+ mLastError.getMessage());
                    Log.e("Error", mLastError.getMessage() + "");
                }
            } else {
                //mTextView.setText("Request cancelled.");
                Log.e("Request", "Request cancelled.");
            }
        }

        private String createSpreadSheetForQRScanner() throws IOException, GeneralSecurityException {
            // TODO: Assign values to desired fields of `requestBody`:
            //  Spreadsheet requestBody = new Spreadsheet();
            SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
            spreadsheetProperties.setTitle(spreadsheetTitle);

            SheetProperties sheetProperties = new SheetProperties();
            sheetProperties.setTitle(SheetOne);
            Sheet sheet = new Sheet();
            sheet.setProperties(sheetProperties);
            List<GridData> list = new ArrayList<>();
            sheet.setData(list);

            SheetProperties sheetProperties2 = new SheetProperties();
            sheetProperties2.setTitle(SheetTwo);
            Sheet sheet2 = new Sheet();
            sheet2.setProperties(sheetProperties2);
            sheet2.setData(list);

            List<Sheet> sheets = new ArrayList<>();
            sheets.add(sheet);
            sheets.add(sheet2);


            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.setProperties(spreadsheetProperties);
            spreadsheet.setSheets(sheets);






           /* Spreadsheet spreadsheet = new Spreadsheet()
                    .setProperties(new SpreadsheetProperties()
                            .setTitle(spreadsheetTitle)
                    );*/


            Spreadsheet response = mService.spreadsheets().create(spreadsheet)
                    .setFields("spreadsheetId")
                    .execute();

            String SpreadsheetId = response.getSpreadsheetId();

            if (SharedPreferencesUtil.getDefaultSheetId(context).equals("")) {
                SharedPreferencesUtil.setDefaultSheetId(context, response.getSpreadsheetId());
            }

            return SpreadsheetId;
        }

        void moveSpreadsheetToDesiredFolder(final String fileId, final String ParentFolderId) {
            if (mDriveServiceHelper == null) {
                return;
            }
            mDriveServiceHelper.moveFile(fileId, ParentFolderId)
                    .addOnSuccessListener(new OnSuccessListener<com.google.api.services.drive.model.File>() {
                        @Override
                        public void onSuccess(com.google.api.services.drive.model.File googleDriveFileHolders) {
                            Gson gson = new Gson();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Utility.ReportNonFatalError("moveSpreadsheetToDesiredFolder", e.getMessage());
                        }
                    });
        }


        private void createSpreadSheetForBeaconScanner(String SpreadSheetTitle) throws IOException, GeneralSecurityException {
            Spreadsheet requestBody = new Spreadsheet()
                    .setProperties(new SpreadsheetProperties()
                            .setTitle(SpreadSheetTitle));
            Spreadsheet response = mService.spreadsheets().create(requestBody)
                    .setFields("spreadsheetId")
                    .execute();

            // if (Utility.isSpreadsheetListEmpty(context)) {
            if (SharedPreferencesUtil.getDefaultBluetoothSheet(context).equals("")) {
                SharedPreferencesUtil.setDefaultBluetoothSheet(context, response.getSpreadsheetId());
            }

            writeTitleToBluetoothBeaconSheet(response.getSpreadsheetId());

        }


        private void writeTitleToQRScannerSheet(String spreadsheetId) throws IOException, GeneralSecurityException {
            // String spreadsheetId = SharedPreferencesUtil.getDefaultSheet(context);
            // String range = "Sheet1!A1:X1";
            String range = SheetOne;
            String valueInputOption = "USER_ENTERED";
            ValueRange requestBody = new ValueRange();
            List<Object> data1 = new ArrayList<>();

            data1.add("datetime");  //0
            data1.add("Timestamp");  //1
            data1.add("QRCODE");  //2
            data1.add("URL");//3
            data1.add("Status"); //4
            List<List<Object>> requestBodyData = new ArrayList<>();
            requestBodyData.add(data1);
            requestBody.setValues(requestBodyData);

            Sheets.Spreadsheets.Values.Append request =
                    mService.spreadsheets().values().append(spreadsheetId, range, requestBody);
            request.setValueInputOption(valueInputOption);

            AppendValuesResponse response = request.execute();
            Log.e(this.toString(), response.toString());
        }

        private void writeTitleToBluetoothBeaconSheet(String spreadsheetId) throws IOException, GeneralSecurityException {
            // String spreadsheetId = SharedPreferencesUtil.getDefaultBluetoothSheet(context);
            //  String spreadsheetId = SpreadsheetId;
            //  String range = "Sheet1!A1:X1";//
            String range = SheetTwo;
            String valueInputOption = "USER_ENTERED";
            ValueRange requestBody = new ValueRange();
            List<Object> data1 = new ArrayList<>();

            data1.add("datetime");
            data1.add("Name");
            data1.add("Timestamp");
            data1.add("RSSI");
            data1.add("UUID");
            data1.add("Major");
            data1.add("Minor");
            data1.add("Battery(%)");


            List<List<Object>> requestBodyData = new ArrayList<>();
            requestBodyData.add(data1);
            requestBody.setValues(requestBodyData);

            Sheets.Spreadsheets.Values.Append request =
                    mService.spreadsheets().values().append(spreadsheetId, range, requestBody);
            request.setValueInputOption(valueInputOption);

            AppendValuesResponse response = request.execute();
            Log.e(this.toString(), response.toString());
        }

    }

    void uploadSampleSpreadsheetUsingDriveAPI(final String mimeType, @Nullable final String folderId) {
        showprogressdialog();
        Utility.ReportNonFatalError("uploadSampleSpreadsheetUsingDriveAPI", "Start");
        InputStream inputStream = getResources().openRawResource(R.raw.sample);
        Utility.ReportNonFatalError("uploadSampleSpreadsheetUsingDriveAPI", "inputStream done");
        File localFile = Utility.writeStreamToFile(inputStream, Utility.getExcelOutputMediaFilePath());
        Utility.ReportNonFatalError("uploadSampleSpreadsheetUsingDriveAPI", "localFile done");
        if (mDriveServiceHelper == null) {
            return;
        }
        mDriveServiceHelper.uploadFile(localFile, mimeType, folderId)
                .addOnSuccessListener(googleDriveFileHolder -> {
                    Log.e("", "");
                    Utility.ReportNonFatalError("uploadSampleSpreadsheetUsingDriveAPI", "On Success");
                    RenameFileFolder(googleDriveFileHolder.getId(), spreadsheetTitle);

                }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
            hideprogressdialog();
            Utility.ReportNonFatalError("uploadSampleSpreadsheetUsingDriveAPI", e.getMessage());
        });
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
                        Utility.ReportNonFatalError("RenameFileFolder", "On Success");
                        Toast.makeText(context, "Successfully created!", Toast.LENGTH_SHORT).show();
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

    void createSpreadsheetUsingDriveAPI(final String SpreadsheetName, final String ParentFolderId) {
        if (mDriveServiceHelper == null) {
            return;
        }
        mDriveServiceHelper.createSpreadsheet(SpreadsheetName, ParentFolderId)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        spreadsheetId = s;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utility.ReportNonFatalError("createSpreadsheetUsingDriveAPI", e.getMessage());
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
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


                        Gson gson = new Gson();
                        String userJson = gson.toJson(googleDriveFileHolders);

                        Type userListType = new TypeToken<ArrayList<GoogleDriveFileHolder>>() {
                        }.getType();
                        ArrayList<GoogleDriveFileHolder> list = gson.fromJson(userJson, userListType);
                        ArrayList<GoogleDriveFileHolder> list_new = getOnlySpreadsheet(list);
                        //   Utility.checkBluetoothSheetAvailability(context, list);

                        DriveSheetsAdapter adapterDrive = new DriveSheetsAdapter(context, list_new, SpreadSheetListActivity.this, SpreadSheetListActivity.this);
                        recycler_sheets.setAdapter(adapterDrive);
                        if (list_new.isEmpty()) {
                            txt_no_items.setVisibility(View.VISIBLE);
                        } else {
                            txt_no_items.setVisibility(View.GONE);
                        }
                        progressBar1.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (pullToRefresh != null && pullToRefresh.isRefreshing())
                            pullToRefresh.setRefreshing(false);

                        progressBar1.setVisibility(View.INVISIBLE);
                        String ex = e.getMessage();
                        recycler_sheets.setAdapter(null);
                        txt_no_items.setVisibility(View.VISIBLE);

                        if (e instanceof UserRecoverableAuthIOException) {
                            startActivityForResult(
                                    ((UserRecoverableAuthIOException) e).getIntent(),
                                    REQUEST_AUTHORIZATION);
                        }
                    }
                });
    }

    public ArrayList<GoogleDriveFileHolder> getOnlySpreadsheet(ArrayList<GoogleDriveFileHolder> list) {
        ArrayList<GoogleDriveFileHolder> list_new = new ArrayList<>();
        ArrayList<GoogleDriveFileHolder> list_folders = new ArrayList<>();
        ArrayList<GoogleDriveFileHolder> list_sheets = new ArrayList<>();


        for (GoogleDriveFileHolder googleDriveFileHolder : list) {
           /* if (googleDriveFileHolder.getMimeType().equals(DriveFolder.MIME_TYPE) || googleDriveFileHolder.getMimeType().equals("application/vnd.google-apps.spreadsheet")) {
                list_new.add(googleDriveFileHolder);
            }*/
            if (googleDriveFileHolder.getMimeType().equals(DriveFolder.MIME_TYPE)) {
                list_folders.add(googleDriveFileHolder);
            } else if (googleDriveFileHolder.getMimeType().equals("application/vnd.google-apps.spreadsheet")) {
                list_sheets.add(googleDriveFileHolder);
            }

        }
        list_new.addAll(list_folders);
        list_new.addAll(list_sheets);


        return list_new;
    }

    @Override
    public void onDrivePathClick(String itemID, String itemName) {
        refreshList(itemID, itemName);
    }

    @Override
    public void onItemClick(String itemID, String itemName, String itemType) {
        if (itemType.equalsIgnoreCase("sheet")) {
            if (mCredential == null || account == null || mCredential.getSelectedAccountName() == null || mCredential.getSelectedAccountName().isEmpty()) {
                Toast.makeText(this, "Sign-in required!", Toast.LENGTH_SHORT).show();
                mGoogleSignInClient.signOut();
                signIn();
            } else {
                new ReadFromSheetsTask(mCredential, this, itemID).execute();
                // downloadFile(itemID);
            }

        } else if (itemType.equalsIgnoreCase("folder")) {
            refreshList(itemID, itemName);
        }

    }

    @Override
    public void onFavouriteClick(String ItemId, String ItemName) {
        String defaultSheetId = SharedPreferencesUtil.getDefaultSheetId(context);

        if (ItemId.equals(defaultSheetId)) {
            Utility.showToast(context, "Default for writing data.");
        } else {

            if (mCredential == null || account == null || mCredential.getSelectedAccountName() == null || mCredential.getSelectedAccountName().isEmpty()) {
                Toast.makeText(this, "Sign-in required!", Toast.LENGTH_SHORT).show();
                mGoogleSignInClient.signOut();
                signIn();
            } else {
                new CheckSpreadsheetTask(mCredential, context, ItemId, ItemName).execute();
            }


        }
    }

    void refreshList(String folderId, String folderName) {
        parentFolderid = folderId;
        List_Path.add(new PathDrive(folderName, folderId));
        drivePathAdapter.notifyDataSetChanged();
        onResume();
    }

    @Override
    public void onItemDelete(String itemName, String itemID, String itemType) {
        showBottomSheetDialog(itemName, itemID, itemType);
       /* String defaultSheetId = SharedPreferencesUtil.getDefaultSheetId(this);
        if (defaultSheetId.equals(itemID)) {
            Dialog_DeleteSpreadsheet(itemID);
        } else {
            deleteFileFolder(itemID);
        }*/

    }


    void CreateWorkbookInLocalStorage() {
        HSSFWorkbook workbook = new HSSFWorkbook();

        if (sheetList.size() > 0) {
            writeDataToWorkbook(workbook, QRSheetData, sheetList.get(0).getProperties().getTitle());
        }
        if (sheetList.size() > 1) {
            writeDataToWorkbook(workbook, BluetoothSheetData, sheetList.get(1).getProperties().getTitle());
        }

        FileOutputStream fos = null;
        try {
            File file = Utility.getPathWorkbook(this);
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void writeDataToWorkbook(HSSFWorkbook workbook, List<List<Object>> dataList, String sheetTitle) {
        HSSFSheet firstSheet = workbook.createSheet(sheetTitle);
        for (int i = 0; i < dataList.size(); i++) {
            HSSFRow rowA = firstSheet.createRow(i);
            rowA.setHeight((short) 500);
            List<Object> columns_list = dataList.get(i);
            for (int j = 0; j < columns_list.size(); j++) {
                HSSFCell cellA = rowA.createCell(j);
                cellA.setCellValue(new HSSFRichTextString(String.valueOf(columns_list.get(j))));
            }
        }
    }

    public void autoSizeColumns(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                Row row = sheet.getRow(sheet.getFirstRowNum());
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    //    sheet.autoSizeColumn(columnIndex);
                    int width = ((int) (100 * 1.14388)) * 256;
                    sheet.setColumnWidth(columnIndex, width);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        onResume();
    }


    private class ReadFromSheetsTask extends AsyncTask<Void, Void, Void> {
        private Sheets mService = null;

        private Exception mLastError = null;
        private Context mContext;
        private String sheetId;
       /* List<List<Object>> QRSheetData = new ArrayList<>();
        List<List<Object>> BluetoothSheetData = new ArrayList<>();
        List<Sheet> sheetList= new ArrayList<>();*/


        ReadFromSheetsTask(GoogleAccountCredential credential, Context context, String sheetId) {

            mContext = context;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(String.valueOf(R.string.app_name))
                    .build();
            this.sheetId = sheetId;
            QRSheetData.clear();
            BluetoothSheetData.clear();
            sheetList.clear();


        }

        /**
         * Background task to call Google Sheets API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected Void doInBackground(Void... params) {
            try {
                sheetList = getPagesFromSheet(sheetId);
                if (sheetList.size() > 0)
                    QRSheetData = ReadFromSheetUsingApi(sheetId, sheetList.get(0).getProperties().getTitle());
                if (sheetList.size() > 1)
                    BluetoothSheetData = ReadFromSheetUsingApi(sheetId, sheetList.get(1).getProperties().getTitle());

                CreateWorkbookInLocalStorage();

            } catch (Exception e) {
                mLastError = e;
                Utility.ReportNonFatalError("ReadFromSheetUsingApi", mLastError.getMessage());

                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {

                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    Log.e(this.toString(), "The following error occurred:\n" + mLastError.getMessage());
                }
                Log.e(this.toString(), e + "");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // mProgressBar.setVisibility(View.VISIBLE);
            showprogressdialog();
        }

        @Override
        protected void onPostExecute(Void output) {
            hideprogressdialog();

          /* List<String> sheetNames = new ArrayList<>();
            for (Sheet sheet : sheetList) {
                sheetNames.add(sheet.getProperties().getTitle());
            }*/

            Intent intent = new Intent(mContext, SheetActivity.class);
            mContext.startActivity(intent);


        }


        private List<List<Object>> ReadFromSheetUsingApi(String spreadsheetId, String range) throws IOException, GeneralSecurityException {

            // String spreadsheetId = sheetId;
            //String range = "Sheet1!A1:X20";// nameof sheet!range
            // String range = SheetOne;
            Sheets.Spreadsheets.Values.Get request =
                    mService.spreadsheets().values().get(spreadsheetId, range);

            ValueRange response = request.execute();
            Log.e(this.toString(), response.toPrettyString());


            List<List<Object>> values = response.getValues();
            List<List<Object>> val = new ArrayList<>();
            if (values != null) val.addAll(values);
            return val;

        }

        private List<Sheet> getPagesFromSheet(String spreadsheetId) throws IOException, GeneralSecurityException {

            Spreadsheet sheet_metadata = mService.spreadsheets().get(spreadsheetId).execute();
            List<Sheet> sheets = sheet_metadata.getSheets();

            return sheets;

        }


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void deleteFileFolder(String fileFolderId) {
        if (mDriveServiceHelper == null) {
            return;
        }
        progressBar1.setVisibility(View.VISIBLE);

        mDriveServiceHelper.deleteFolderFile(fileFolderId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar1.setVisibility(View.INVISIBLE);
                if (fileFolderId.equals(SharedPreferencesUtil.getDefaultSheetId(context))) {
                    SharedPreferencesUtil.setDefaultSheetId(context, "");
                }
                onResume();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar1.setVisibility(View.INVISIBLE);
                String ex = e.getMessage();
                onResume();
                if (e instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) e).getIntent(),
                            REQUEST_AUTHORIZATION);
                }
            }
        });

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

    private class CheckSpreadsheetTask extends AsyncTask<Void, Void, List<Sheet>> {
        private Sheets mService = null;
        private Exception mLastError = null;
        private Context mContext;
        private String sheetId, sheetName;


        CheckSpreadsheetTask(GoogleAccountCredential credential, Context context, String sheetId, String sheetName) {

            mContext = context;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(String.valueOf(R.string.app_name))
                    .build();
            this.sheetId = sheetId;
            this.sheetName = sheetName;
        }


        @Override
        protected List<Sheet> doInBackground(Void... params) {
            List<Sheet> sheetArrayList = new ArrayList<>();
            try {
                sheetArrayList = getPagesFromSheet(sheetId);
            } catch (Exception e) {
                mLastError = e;
                Utility.ReportNonFatalError("CheckSpreadsheetTask", mLastError.getMessage());
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {

                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    Log.e(this.toString(), "The following error occurred:\n" + mLastError.getMessage());
                }
                Log.e(this.toString(), e + "");
            }
            return sheetArrayList;
        }

        @Override
        protected void onPreExecute() {
            // mProgressBar.setVisibility(View.VISIBLE);
            showprogressdialog();
        }

        @Override
        protected void onPostExecute(List<Sheet> sheetArrayList) {
            hideprogressdialog();
            List<String> sheetNames = new ArrayList<>();
            for (Sheet sheet : sheetArrayList) {
                sheetNames.add(sheet.getProperties().getTitle());
            }


            if (sheetNames.size() > 1 && sheetNames.get(0).equals(Constants.SheetOne) && sheetNames.get(1).equals(Constants.SheetTwo)) {
                Utility.showToast(context, "Selected for writing data.");
                SharedPreferencesUtil.setDefaultSheetId(context, sheetId);
                SharedPreferencesUtil.setDefaultSheetName(context, sheetName);
                String path = drivePathAdapter.getPath() + sheetName + ".xlsx";
                SharedPreferencesUtil.setDefaultSheetPath(context, path);
                onRefresh();
                setToolbar();
            } else {
                Snackbar.make(recycler_sheets, "This Spreadsheet do not have 'QR Sheet' & 'Bluetooth Sheet'. Please choose another or create a new spreadsheet.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();

            }


        }


        private List<List<Object>> ReadFromSheetUsingApi(String spreadsheetId, String range) throws IOException, GeneralSecurityException {

            // String spreadsheetId = sheetId;
            //String range = "Sheet1!A1:X20";// nameof sheet!range
            // String range = SheetOne;

            Sheets.Spreadsheets.Values.Get request =
                    mService.spreadsheets().values().get(spreadsheetId, range);
            ValueRange response = request.execute();
            Log.e(this.toString(), response.toPrettyString());

            List<List<Object>> values = response.getValues();
            List<List<Object>> val = new ArrayList<>();
            if (values != null) val.addAll(values);
            return val;

        }

        private List<Sheet> getPagesFromSheet(String spreadsheetId) throws IOException, GeneralSecurityException {
            Spreadsheet sheet_metadata = mService.spreadsheets().get(spreadsheetId).execute();
            List<Sheet> sheets = sheet_metadata.getSheets();
            return sheets;
        }


    }

    public void downloadFile(String fileId) {
        if (mDriveServiceHelper == null) {
            return;
        }
        final String filenameWithExt = "Test.xlsx";
        final File fileSaveLocation = Utility.createDriveCacheFolder(filenameWithExt);
        progressBar1.setVisibility(View.VISIBLE);
        // showprogressdialog();

        mDriveServiceHelper.exportFile(fileSaveLocation, fileId, EXPORT_TYPE_MS_EXCEL)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar1.setVisibility(View.INVISIBLE);
                        hideprogressdialog();
                        Toast.makeText(context, "Ready", Toast.LENGTH_SHORT).show();
                        Uri fileURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", fileSaveLocation);

                        try {
                            // readXLSFile(fileURI);
                        } catch (Exception e) {
                            e.getMessage();
                        }
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


    /*    public void readXLSFile(Uri uri) throws IOException {
            InputStream ExcelFileToRead = getContentResolver().openInputStream(uri);
            //   Workbook workbook = WorkbookFactory.create(ExcelFileToRead);
            // InputStream ExcelFileToRead = new FileInputStream(path);
            //HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
            XSSFWorkbook workbook = null;
            try {
                workbook = new XSSFWorkbook(ExcelFileToRead);
            } catch (Exception e) {
                e.printStackTrace();
            }

            FileOutputStream fos = null;
            try {
                File file = Utility.getPathWorkbook(this);
                fos = new FileOutputStream(file);
                workbook.write(fos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


            //  HSSFSheet sheet = wb.getSheetAt(0);
            //   HSSFRow row;
            // HSSFCell cell;

            //     Iterator rows = sheet.rowIterator();

        *//*    while (rows.hasNext()) {
            row = (HSSFRow) rows.next();
            Iterator cells = row.cellIterator();

            while (cells.hasNext()) {
                cell = (HSSFCell) cells.next();

                *//**//*if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
                {
                    System.out.print(cell.getStringCellValue()+" ");
                }
                else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
                {
                    System.out.print(cell.getNumericCellValue()+" ");
                }*//**//*
     *//**//* else
                {
                    //U Can Handel Boolean, Formula, Errors
                }*//**//*
            }
            System.out.println();
        }*//*

    }*/
    public void showBottomSheetDialog(String itemName, String itemID, String itemType) {
        View view = getLayoutInflater().inflate(R.layout.menu_manage_drive, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        LinearLayout action_delete = view.findViewById(R.id.action_delete);
        TextView folder_name = view.findViewById(R.id.name);
        ImageView item_type_icon = view.findViewById(R.id.item_type_icon);
        LinearLayout action_rename = view.findViewById(R.id.action_rename);
        if (itemType.equals(DriveFolder.MIME_TYPE)) {
            item_type_icon.setImageResource(R.drawable.ic_baseline_folder_24);
        } else {
            item_type_icon.setImageResource(R.drawable.ic_baseline_list_alt_24);
        }


        folder_name.setText(itemName);
        action_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String defaultSheetId = SharedPreferencesUtil.getDefaultSheetId(SpreadSheetListActivity.this);
                if (defaultSheetId.equals(itemID)) {
                    Dialog_DeleteSpreadsheet(itemID);
                } else {
                    deleteFileFolder(itemID);
                }

            }
        });

        action_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Dialog_RenameFolder(itemID, itemName, itemType);
            }
        });


        dialog.setContentView(view);
        dialog.show();
    }

    public void Dialog_RenameFolder(String fileFolderId, String currentName, String itemType) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        if (itemType.equals(DriveFolder.MIME_TYPE)) {
            alertDialog.setMessage("Rename Folder");
        } else {
            alertDialog.setMessage("Rename Spreadsheet");
        }

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
                            Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show();
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
