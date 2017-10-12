/**
 * File created on 26/06/14 at 20:54
 * Copyright Vyacheslav Krylov, 2014
 */
package test.com.mygapplication.Utills;

import android.content.res.Resources;

import test.com.mygapplication.application.AppController;


public class Screen {
    private static float density;
    private static float scaledDensity;

    public static int dp(float dp) {
        if (density == 0f)
            density = AppController.getAppContext().getResources().getDisplayMetrics().density;

        return (int) (dp * density );
    }

    public static int sp(float sp) {
        if (scaledDensity == 0f)
            scaledDensity = AppController.getAppContext().getResources().getDisplayMetrics().scaledDensity;

        return (int) (sp * scaledDensity + .5f);
    }

    public static int getWidth() {
        return AppController.getAppContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeight() {
        return AppController.getAppContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight() {

        int result = 0;
        int resourceId = AppController.getAppContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = AppController.getAppContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavbarHeight() {
        if (hasNavigationBar()) {
            int resourceId = AppController.getAppContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return AppController.getAppContext().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public static boolean hasNavigationBar() {
        Resources resources = AppController.getAppContext().getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return  (id > 0) && resources.getBoolean(id);
    }
}