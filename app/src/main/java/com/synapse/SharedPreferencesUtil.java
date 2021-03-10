package com.synapse;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {
    private static final String PREF_ACCOUNT_NAME = "accountName";
    public static void setAuthToken(Context context, String token) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("token", token);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String getAuthToken(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("token", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void setAsanaEmail(Context context, String asana_email) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("asana_email", asana_email);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String getAsanaEmail(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("asana_email", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setCodeVerifier(Context context, String CodeVerifier) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("CodeVerifier", CodeVerifier);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String getCodeVerifier(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("CodeVerifier", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void setRefreshToken(Context context, String RefreshToken) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("RefreshToken", RefreshToken);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String getRefreshToken(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("RefreshToken", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setDefaultSheetId(Context context, String id) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("default_sheet_id", id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDefaultSheetId(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("default_sheet_id", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void setDefaultSheetName(Context context, String name) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("default_sheet_name", name);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDefaultSheetName(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("default_sheet_name", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void setDefaultSheetPath(Context context, String path) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("default_sheet_path", path);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDefaultSheetPath(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("default_sheet_path", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setDefaultDriveFolderPath(Context context, String path) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("default_folder_path", path);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDefaultDriveFolderPath(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("default_folder_path", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setAppInForeground(Context context, boolean isAppInForeground) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.APP_IN_FOREGROUND, isAppInForeground);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getAppInForeground(Context context) {
        boolean val = false;
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getBoolean(Constants.APP_IN_FOREGROUND, false);
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }


    public static void setDefaultBluetoothSheet(Context context, String id) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("bluetooth_sheet_id", id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDefaultBluetoothSheet(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("bluetooth_sheet_id", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setDefaultDriveFolderId(Context context, String id) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("default_folder_id", id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getDefaultDriveFolderId(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("default_folder_id", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void setDefaultDriveFolderName(Context context, String id) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("default_folder_name", id);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getDefaultDriveFolderName(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("default_folder_name", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void setAccountName(Context context, String email) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREF_ACCOUNT_NAME, email);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getAccountName(Context context) {
        String val = null;
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString(PREF_ACCOUNT_NAME, null);
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setUserId(Context context, String id) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_id",id);
            //SharedPreferences.Editor editor = preferences.edit();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUserId(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("user_id", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setPassword(Context context, String password) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("password", password);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPassword(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("password", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean clearPrefernce(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        boolean res = editor.clear().commit();


        return res;
    }

    public static boolean clearCartItems(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        boolean res = editor.remove("CartItems").commit();
        return res;
    }

    public static void setSpreadsheetList(Context context, String jsonSheets) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Sheets", jsonSheets);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSpreadsheetList(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("Sheets", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    public static void setAuthCode(Context context, String authCode) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("AuthCode", authCode);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getAuthCode(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("AuthCode", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
/*

    public static void setAsanaUserId(Context context, String user_gid) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_gid", user_gid);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getAsanaUserId(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("user_gid", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setAsanaWorkspaceId(Context context, String workspace_gid) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("workspace_gid", workspace_gid);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getAsanaWorkspaceId(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("workspace_gid", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
*/
    public static void setCurrentLoggedInUserWorkspaceId(Context context, String workspace_gid) {
    try {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_logged_in_user_workspace_gid", workspace_gid);
        editor.apply();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public static String getCurrentLoggedInUserWorkspaceId(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("current_logged_in_user_workspace_gid", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    public static void setCurrentLoggedInUserWorkspaceName(Context context, String workspace_gid) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("current_logged_in_user_workspace_name", workspace_gid);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getCurrentLoggedInUserWorkspaceName(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("current_logged_in_user_workspace_name", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setTeamIdForCreatingNewProject(Context context, String workspace_gid) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("team_id_for_creating_new_project", workspace_gid);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getTeamIdForCreatingNewProject(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("team_id_for_creating_new_project", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setNumberOfProjectsCreated(Context context, String json) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("NumberOfProjectsCreated", json);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getNumberOfProjectsCreated(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("NumberOfProjectsCreated", "");
            return val;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setTeamNameForCreatingNewProject(Context context, String workspace_gid) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("team_name_for_creating_new_project", workspace_gid);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getTeamNameForCreatingNewProject(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("team_name_for_creating_new_project", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setLevyLabWorkspaceId(Context context, String workspace_gid) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("levy_lab_workspace_gid", workspace_gid);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getLevyLabWorkspaceId(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("levy_lab_workspace_gid", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setLevyLabProjectId(Context context, String workspace_gid) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("levy_lab_project_gid", workspace_gid);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getLevyLabProjectId(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("levy_lab_project_gid", "");
            return val;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setBlacklistBeacon(Context context, String authCode) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("BlacklistedBeacons", authCode);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getBlacklistBeacon(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("BlacklistedBeacons", "");
            return val;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
