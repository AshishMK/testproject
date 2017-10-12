package test.com.mygapplication.Logins;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import test.com.mygapplication.R;
import test.com.mygapplication.interfaces.OnSocialButtonsClick;

/**
 * Fragment use to Register user
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    //Registration Button
    AppCompatButton register_btn;

    //EditText(s) to enter email,mobile no, name, password
    EditText input_email, input_phone, input_name, input_pass;

    //RootView for this Fragment
    View root;

    //String to hold current email, password, phone and Name fetch from edit_mail and edit_pass EditText
    String stMail, stPass, stPhone, stName;

    //Weather to show password or no not
    boolean show_password = false;

    //Permission Request code for get account Manifest.permission.GET_ACCOUNTS
    final static int PERMISSIONS_REQUEST_ACCOUNT = 101;

    public RegisterFragment() {
        // Required empty public constructor
    }


    /**
     * Interface use to notify when Facebook or Google Login button clicked
     */
    OnSocialButtonsClick mSocialLoginListener;

    /**
     * Method use to setListener on this Fragment
     * @see  test.com.mygapplication.pager_adapter.LoginPagerAdapterSimple
     * @param mSocialLoginListener
     */
    public void setOnSocialButtonsClickListener(OnSocialButtonsClick mSocialLoginListener) {
        this.mSocialLoginListener = mSocialLoginListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_register, container, false);

        //Initialize views for Current Fragment's layout
        initViews();

        //Setup listeners on clickable views
        setListeners();

        return root;

    }


    /**
     * Method that implement permission model for Manifest.permission.GET_ACCOUNTS
     */
    void implementPermissionModel() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{Manifest.permission.GET_ACCOUNTS},
                    PERMISSIONS_REQUEST_ACCOUNT);
        } else {
            setEmailAccount();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(getActivity(), ""+requestCode , Toast.LENGTH_SHORT).show();
        if (requestCode == PERMISSIONS_REQUEST_ACCOUNT) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setEmailAccount();
            }
        }
    }

    /**
     * Method use to initialize vies for current fragment's layout
     */
    void initViews() {
        root.findViewById(R.id.txt_fb).setOnClickListener(this);
        root.findViewById(R.id.txt_g).setOnClickListener(this);
        input_email = (EditText) root.findViewById(R.id.input_email);
        input_pass = (EditText) root.findViewById(R.id.input_password);
        input_name = (EditText) root.findViewById(R.id.input_name);
        input_phone = (EditText) root.findViewById(R.id.input_phone);

        //register button
        register_btn = (AppCompatButton) root.findViewById(R.id.btn_register);
        ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{ContextCompat.getColor(getActivity(), R.color.colorPrimary)});
        // register_btn.setSupportBackgroundTintList(csl);
    }

    /**
     * Method use to set Listeners on Clickable views
     */
    void setListeners() {
        root.findViewById(R.id.toggle_vis).setOnClickListener(this);

        register_btn.setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {

            case R.id.btn_register:
                //Perform Registration
                if (validate()) {
                    //Call Registration web service
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)

                            .addFormDataPart("email", stMail)
                            .addFormDataPart("phone", stPhone)
                            .addFormDataPart("full_name", stName)
                            .addFormDataPart("password", stPass)
                            .addFormDataPart("device_type", "1")
                            .build();
                    Toast.makeText(getActivity(), "Signout url RegisterFragment 178", Toast.LENGTH_SHORT).show();
             /*       new ExecuteServices().executePost(CommonFields.webserver_url + "register", new ExecuteServices.OnServiceExecute() {
                        @Override
                        public void onServiceExecutedResponse(final String response) {
                            //pd.dismiss();
                            if (!Preferences.saveUser(response, getActivity(), Preferences.TYPE_R, null)) {
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "" + response, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                                    getActivity().finish();
                                }
                            });
                        }

                        @Override
                        public void onServiceExecutedFailed(final String messsage) {
                            pd.dismiss();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), messsage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, requestBody);*/


                }
                break;

            case R.id.txt_fb:
                //Notify OnSocialLoginClick listener for fb login button clicked event
                if (mSocialLoginListener != null)
                    mSocialLoginListener.onFBClicked();
                break;

            case R.id.txt_g:
                //Notify OnSocialLoginClick listener for google login button clicked event
                if (mSocialLoginListener != null)
                    mSocialLoginListener.onGoogleClicked();
                break;

            case R.id.toggle_vis:
                //Toggle between show password or hide password action
                show_password = !show_password;
                VectorDrawableCompat vc = VectorDrawableCompat.create(getResources(), show_password ? R.drawable.ic_visibility_off_black_24dp : R.drawable.ic_visibility_black_24dp, getActivity().getTheme());
                ((AppCompatImageView) root.findViewById(R.id.toggle_vis)).setImageDrawable(vc);
                input_pass.setInputType(show_password ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input_pass.setSelection(input_pass.getText().toString().length());
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        implementPermissionModel();
    }

    /**
     * method to set email address on email input EditText
     */
    void setEmailAccount() {
        String gmail = null;

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                gmail = account.name;
                input_email.setText(gmail);
                input_email.setSelection(gmail.length());
                break;

            }
        }

    }

    /**
     * Validate all input fields before performing Registration
     * @return return weather all input fields are fill with correct values or not
     */
    boolean validate() {
        //get input mail
        stMail = input_email.getText().toString().trim();
        //get input password
        stPass = input_pass.getText().toString().trim();
        //get input name
        stName = input_name.getText().toString().trim();
        //get input phone number
        stPhone = input_phone.getText().toString().trim();

        //validate name field
        if (TextUtils.isEmpty(stName)) {
            input_name.requestFocus();
            input_name.setError(getString(R.string.name_msg));
            return false;
        }

        //validate phone number field
        if (TextUtils.isEmpty(stPhone) || !Pattern.matches(Patterns.PHONE.pattern(), stPhone)) {
            input_phone.requestFocus();
            input_phone.setError(getString(R.string.phone_msg));
            return false;
        }

        //validate email field
        if (TextUtils.isEmpty(stMail) || !Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), stMail)) {
            input_email.requestFocus();
            input_email.setError(getString(R.string.mail_msg));
            return false;
        }

        //validate password field
        if (TextUtils.isEmpty(stPass) || stPass.length() < 6) {
            input_pass.requestFocus();
            input_pass.setError(getString(R.string.pass_msg1));
            return false;
        }

        return true;
    }
}
