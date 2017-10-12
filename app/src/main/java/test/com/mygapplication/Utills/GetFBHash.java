package test.com.mygapplication.Utills;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jack on 19/12/2016.
 */

public class GetFBHash {
        @Nullable
        public static String printKeyHash(@NonNull Context context) {


            PackageInfo packageInfo;
            String key = null;
            try {
                //getting application package name, as defined in manifest
                String packageName = context.getApplicationContext().getPackageName();

                //Retriving package info
                packageInfo = context.getPackageManager().getPackageInfo(packageName,
             PackageManager.GET_SIGNATURES);

//            Log.e("Package Name=", context.getApplicationContext().getPackageName());

                for (Signature signature : packageInfo.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    key = new String(Base64.encode(md.digest(), 0));

                    // String key = new String(Base64.encodeBytes(md.digest()));
                    Log.e("Key Hash=========", key);
                }
            } catch (PackageManager.NameNotFoundException e1) {
                Log.e("Name not found", e1.toString());
            } catch (NoSuchAlgorithmException e) {
                Log.e("No such an algorithm", e.toString());
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }

            return key;
        }
}
