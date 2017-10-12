package test.com.mygapplication.Logins;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import java.net.URLEncoder;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import test.com.mygapplication.R;
import test.com.mygapplication.interfaces.OnSocialButtonsClick;

/**
 * Fragment use to Login user
 *
 * @see {@link LoginRegisterActivitySimple}
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    //login Button
    AppCompatButton login_btn;

    //EditText(s) to enter email, password
    EditText edit_mail, edit_pass;

    //String to hold current email and password fetch from edit_mail and edit_pass EditText
    String mail, password;


    View root,    //Root View of this Fragment
         fb_btn, //Facebook button for facebook login
         g_btn;  //Google button to login with google

    //Weather to show password or no not
    boolean show_password = false;


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Interface use to notify when Facebook or Google Login button clicked
     */
    OnSocialButtonsClick mSocialButtonsClick;

    /**
     * Method use to setListener on this Fragment
     * @see  test.com.mygapplication.pager_adapter.LoginPagerAdapterSimple
     * @param mSocialButtonsClick
     */
    public void setOnSocialButtonsClickListener(OnSocialButtonsClick mSocialButtonsClick) {
        this.mSocialButtonsClick = mSocialButtonsClick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_login, container, false);

        //Initialize views for Current Fragment's layout
        initViews();

        //Setup listeners on clickable views
        setListener();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Method use to initialize vies for current fragment's layout
     */
    void initViews() {
        edit_mail = (EditText) root.findViewById(R.id.input_contact);
        edit_pass = (EditText) root.findViewById(R.id.input_password);


        //fb ang gplus init setup
        fb_btn = root.findViewById(R.id.txt_fb);
        g_btn = root.findViewById(R.id.txt_g);

        //login button
        login_btn = (AppCompatButton) root.findViewById(R.id.btn_login);
        ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{ContextCompat.getColor(getActivity(), R.color.colorPrimary)});
        //login_btn.setSupportBackgroundTintList(csl);


    }

    /**
     * Method use to set Listeners on Clickable views
     */
    void setListener() {
        login_btn.setOnClickListener(this);
        fb_btn.setOnClickListener(this);
        g_btn.setOnClickListener(this);
        root.findViewById(R.id.toggle_vis).setOnClickListener(this);
     root.findViewById(R.id.link_forgot).setOnClickListener(this);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

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

            //Perform Login
            case R.id.btn_login:
                if (validate()) {
                    //Call Login web service
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)

                            .addFormDataPart("email", mail)
                            .addFormDataPart("password", password)
                            .build();
                    Toast.makeText(getActivity(), "add login url here LoginFragment 176", Toast.LENGTH_SHORT).show();
                  /*  new ExecuteServices().executePost(CommonFields.webserver_url + "login", new ExecuteServices.OnServiceExecute() {
                        @Override
                        public void onServiceExecutedResponse(final String response) {
                            pd.dismiss();
                            if (!Preferences.saveUser(response, getActivity(), Preferences.TYPE_R, null)) {
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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
              if (mSocialButtonsClick != null)
                  mSocialButtonsClick.onFBClicked();
                break;

          case R.id.link_forgot:
                //Show forget password dialog when user forget the password
                showForgetDialog();
                break;


            case R.id.txt_g:
                //Notify OnSocialLoginClick listener for google login button clicked event
                if (mSocialButtonsClick != null)
                    mSocialButtonsClick.onGoogleClicked();
                break;

            case R.id.toggle_vis:
                //Toggle between show password or hide password action
                show_password = !show_password;
                VectorDrawableCompat vc = VectorDrawableCompat.create(getResources(), show_password ? R.drawable.ic_visibility_off_black_24dp : R.drawable.ic_visibility_black_24dp, getActivity().getTheme());
                ((AppCompatImageView) root.findViewById(R.id.toggle_vis)).setImageDrawable(vc);
                edit_pass.setInputType(show_password ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edit_pass.setSelection(edit_pass.getText().toString().length());
                break;

        }
    }

    /**
     * Method use to show forget password dialog
     */
    void showForgetDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.forget_password_alert, null);
        final TextInputLayout contLay, mailLay;
        final EditText cont, mail;
        contLay = (TextInputLayout) dialoglayout.findViewById(R.id.input_mob_lay);
        mailLay = (TextInputLayout) dialoglayout.findViewById(R.id.input_mail_lay);
        cont = (EditText) dialoglayout.findViewById(R.id.mob);
        mail = (EditText) dialoglayout.findViewById(R.id.mail);

        final AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = alertDialogBuilder3.setView(dialoglayout).create();
        dialog.setCancelable(false);
        dialog.show();

        dialoglayout.findViewById(R.id.No).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialoglayout.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View focus = cont;
                boolean valid = true;
                // View focus= contactinput;
                String cont_ = cont.getText().toString();
                String mail_ = mail.getText().toString();
                contLay.setError(null);
                mailLay.setError(null);
              /*  if (cont_.isEmpty() || cont_.length() != 10) {
                    focus = cont;
                    contLay.setError(getString(R.string.phone_msg));
                    valid = false;
                } else if (mail_.isEmpty() || !Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), mail_)) {
                    focus = mail;
                    mailLay.setError(getString(R.string.mail_msg));
                    valid = false;
                }*/

                if (!valid) {
                    focus.requestFocus();
                } else {
                    try {
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("user_id", "10")//URLEncoder.encode(Preferences.getSharedPreferences(getActivity()).getString(Preferences.USER_ID,"10"), "UTF-8"))
                                .addFormDataPart("old_password", "123456")
                                .addFormDataPart("new_password", URLEncoder.encode(mail_, "UTF-8"))
                                //    .addFormDataPart("email", URLEncoder.encode(object.getString("email"), "UTF-8"))
                                .build();
                        Toast.makeText(getActivity(), "Please add forgot password url here @LoginFragment 285", Toast.LENGTH_SHORT).show();
                /*        new ExecuteServices().executePost(CommonFields.webserver_url + "change-password", new ExecuteServices.OnServiceExecute() {
                            @Override
                            public void onServiceExecutedResponse(final String response) {
                                pd.dismiss();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "" + response, Toast.LENGTH_SHORT).show();
                                        //start new activity here on successful login register via FB account
                                        // startActivity(new Intent(getActivity(),HomeActivity.class));
                                    }
                                });
                            }

                            @Override
                            public void onServiceExecutedFailed(final String message) {
                                pd.dismiss();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }, requestBody);*/
                    }
                    catch (Exception e){

                    }
                }
                dialog.dismiss();


                //  dialog.dismiss();
            }
        });
    }

    /**
     * Validate all input fields before performing login
     * @return return weather all input fields are fill with correct values or not
     */
    boolean validate() {
        //get input mail
        mail = edit_mail.getText().toString().trim();
        //get input password
        password = edit_pass.getText().toString().trim();

        //Validating email field
        if (TextUtils.isEmpty(mail) || (!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), mail) && !Pattern.matches(Patterns.PHONE.pattern(), mail))) {
            edit_mail.requestFocus();
            edit_mail.setError(getString(R.string.mail_msg));
            return false;
        }

        //Validating password field
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            edit_pass.requestFocus();
            edit_pass.setError(getString(R.string.pass_msg1));
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


}
