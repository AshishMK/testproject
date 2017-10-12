package test.com.mygapplication.application;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Application class use to initialize singleton instance and app settings
 */
public class AppController extends Application {
    //Tag for logging
	public static final String TAG = AppController.class.getSimpleName();

    //Instance of Application class
    private static AppController mInstance;

    //Context for App
    private static Context mContext;

    //OkHttpClient object for Singleton class pattern
    public static OkHttpClient httpClient;
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mContext = this.getApplicationContext();

        //init OkHttpClient object
        httpClient = new OkHttpClient.Builder()
				.connectTimeout(600, TimeUnit.SECONDS)
				.writeTimeout(600, TimeUnit.SECONDS)
				.readTimeout(600, TimeUnit.SECONDS)
				.build();
	}

    /**
     * Method use to return Context object
     * @return Context object
     */
    public static Context getAppContext(){
		return mContext;
	}


    /**
     * Method that return Application class object
     * @return Application object
     */
    public static synchronized AppController getInstance() {
		return mInstance;
	}


}
