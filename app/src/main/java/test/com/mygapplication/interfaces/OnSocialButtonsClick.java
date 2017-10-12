package test.com.mygapplication.interfaces;

/**
 *
 * Interface to notify when a facebook or Google login button is clicked
 * @see uses at {@link test.com.mygapplication.Logins.LoginRegisterActivitySimple} and implementation on {@link test.com.mygapplication.Logins.LoginFragment
 * }
 * and {@link test.com.mygapplication.Logins.RegisterFragment}
 * Created by Ashish Nautiyal on 19/09/2016.
 */
public interface OnSocialButtonsClick {
    //Called when Facebook LoginButton is clicked
    public void onFBClicked();
    //Called when Google LoginButton is clicked
    public void onGoogleClicked();
}
