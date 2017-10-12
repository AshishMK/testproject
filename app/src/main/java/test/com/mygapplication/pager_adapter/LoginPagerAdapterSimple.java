package test.com.mygapplication.pager_adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import test.com.mygapplication.Logins.LoginFragment;
import test.com.mygapplication.Logins.RegisterFragment;
import test.com.mygapplication.R;
import test.com.mygapplication.interfaces.OnSocialButtonsClick;
import test.com.mygapplication.interfaces.OnSocialClickfromAdapter;


/**
 * Pager Adapter class to inflate {@link LoginFragment} and {@link RegisterFragment} into a ViewPager
 *
 * @see {@link test.com.mygapplication.Logins.LoginRegisterActivitySimple#mPager}
 * Created by Ashish Nautiyal on 13/09/2016.
 */
public class LoginPagerAdapterSimple extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    /**
     * {@link LoginFragment} object holds the reference of {@link LoginFragment} inside Adapter
     */
    LoginFragment mLoginFragment;

    /**
     * {@link RegisterFragment} object holds the reference of {@link RegisterFragment} inside Adapter
     */
    RegisterFragment mRegisterFragment;


    /**
     * Activity class which holds this adapter
     * BaseSocialActivity reference is needed to invoke google and facebook sign in methods
     */
    AppCompatActivity mBaseSocialActivity;


    /**
     * Interface use to notify when Facebook or Google Login button clicked
     */
    OnSocialClickfromAdapter mSocialClickfromAdapter;

    /**
     * Method use to setListener on this Fragment
     *
     * @param mSocialLoginListener
     * @see app.medician.com.medician.pager_adapter.LoginPagerAdapter
     */
    public void setOnSocialLoginClickListener(OnSocialClickfromAdapter mSocialClickfromAdapter) {
        this.mSocialClickfromAdapter = mSocialClickfromAdapter;
    }

    //Constructor to the class
    public LoginPagerAdapterSimple(AppCompatActivity mBaseSocialActivity, FragmentManager fm, int tabCount) {
        super(fm);
        this.mBaseSocialActivity = mBaseSocialActivity;
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            //LoginFragment instantiation with OnSocialLoginClick implementation
            case 0:
                mLoginFragment = new LoginFragment();
                mLoginFragment.setOnSocialButtonsClickListener(new OnSocialButtonsClick() {
                    @Override
                    public void onFBClicked() {
                        if(mSocialClickfromAdapter!=null)
                        mSocialClickfromAdapter.onFBClicked();
                    }

                    @Override
                    public void onGoogleClicked() {
                        if(mSocialClickfromAdapter!=null)
                        mSocialClickfromAdapter.onGoogleClicked();
                    }
                });
                return mLoginFragment;

            //RegisterFragment instantiation with OnSocialLoginClick implementation
            default:
                mRegisterFragment = new RegisterFragment();
                mRegisterFragment.setOnSocialButtonsClickListener(new OnSocialButtonsClick() {
                    @Override
                    public void onFBClicked() {
                        if(mSocialClickfromAdapter!=null)
                        mSocialClickfromAdapter.onFBClicked();
                    }

                    @Override
                    public void onGoogleClicked() {
                        if(mSocialClickfromAdapter!=null)
                        mSocialClickfromAdapter.onGoogleClicked();
                    }
                });
                return mRegisterFragment;
        }
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mBaseSocialActivity.getString(R.string.tab_login);
            default:
                return mBaseSocialActivity.getString(R.string.tab_register);

        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}