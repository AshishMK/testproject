package test.com.mygapplication.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Singleton Preferences class to save edit and update the application Preferences
 * Created by Ashish Nautiyal on 08/08/2016.
 */
public class Preferences {
    public static final String PREFIX="myApp_";
    static final String SHARED_PREFS_NAME = PREFIX+"preferences";
    public static final String USER_ID = PREFIX+"user_id";
    public static final String USER_NAME = PREFIX+"user_name";
    //public static final String USER_ID_SOCIAL =PREFIX+ "social_user_id";
    public static final String USER_ID_FB =PREFIX+ "social_user_id_fb";
    public static final String USER_ID_G =PREFIX+ "social_user_id_google";
    public static final String USER_MAIl = PREFIX+"user_mail";
    public static final String USER_NUMBER = PREFIX+"user_number";
    public static final String USER_IMG_URL = PREFIX+"user_url";
    public static final String USER_LOGIN_WITH =PREFIX+ "user_login_with";


    /**
     * User Login Type
     **/
    public static final int TYPE_R = 0;
    public static final int TYPE_FB = 1;
    public static final int TYPE_G = 2;


    @Nullable
    public static SharedPreferences prefs = null;

    @Nullable
    public static SharedPreferences getSharedPreferences(@NonNull Context context) {
        if (prefs != null) return prefs;
        return prefs = context.getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
    }

    public static boolean isUserLoggedIn(@NonNull Context ctx) {
        Preferences.getSharedPreferences(ctx);
        boolean isLoggedIn = false;
        isLoggedIn = (!TextUtils.isEmpty(prefs.getString(Preferences.USER_ID, null)) && !TextUtils.isEmpty(prefs.getString(Preferences.USER_NAME, null)));
        return isLoggedIn;
    }

    public static boolean saveUser(String response, @NonNull Context ctx, int loginType, String img) {
        boolean saved = false;
        System.out.println("xxxxx "+response);
        try {
            JSONObject job = new JSONObject(response);
            if (job.getInt("status") == 1) {

                JSONObject jmm = job.getJSONObject("user");
                Preferences.getSharedPreferences(ctx).edit().putInt(Preferences.USER_LOGIN_WITH, loginType).commit();
                Preferences.getSharedPreferences(ctx).edit().putString(Preferences.USER_NAME, jmm.getString("fname")+" "+ jmm.getString("lname")).commit();
                Preferences.getSharedPreferences(ctx).edit().putString(Preferences.USER_MAIl, jmm.getString("email")).commit();
                Preferences.getSharedPreferences(ctx).edit().putString(Preferences.USER_ID, jmm.getString("id")).commit();
                //Preferences.getSharedPreferences(ctx).edit().putString(Preferences.USER_ID_SOCIAL, jmm.has("social_id") ? jmm.getString("social_id") : null).commit();
                Preferences.getSharedPreferences(ctx).edit().putString(Preferences.USER_ID_FB, jmm.has("facebook_id") ? jmm.getString("facebook_id") : null).commit();
                Preferences.getSharedPreferences(ctx).edit().putString(Preferences.USER_ID_FB, jmm.has("google_id") ? jmm.getString("google_id") : null).commit();

                Preferences.getSharedPreferences(ctx).edit().putString(Preferences.USER_NUMBER, jmm.getString("phone")).commit();

                Preferences.getSharedPreferences(ctx).edit().putString(Preferences.USER_IMG_URL, img).commit();

                saved = true;
            } else {
                showMessage(job.getString("message"), ctx);
            }

        } catch (JSONException e) {
            System.out.println("xxxxx "+e.getLocalizedMessage());
            e.printStackTrace();
        }
        return saved;
    }


    static public void showMessage(final String msg, final Context ctx) {

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message message) {
                // This is where you do your work in the UI thread.
                // Your worker tells you in the message what to do.
                Toast.makeText(ctx, "" + msg, Toast.LENGTH_SHORT).show();
            }
        };

        android.os.Message message = mHandler.obtainMessage(0, msg);
        message.sendToTarget();


    }

    public static void cleanAllpreferences(@NonNull Context ctx) {
        Preferences.getSharedPreferences(ctx);

        prefs.edit().clear().commit();

    }

}
