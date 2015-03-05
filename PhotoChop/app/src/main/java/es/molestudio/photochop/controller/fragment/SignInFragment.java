package es.molestudio.photochop.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppEditText;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.controller.LoginManager;
import es.molestudio.photochop.controller.LoginManagerWrap;
import es.molestudio.photochop.model.User;


/**
 * Created by Chus on 27/11/14.
 */
public class SignInFragment extends Fragment implements LoginManager.LoginActionListener,
        View.OnClickListener{

    public static final String LOGIN_OK = "es.molestudio.photochop.controller.fragment.LOGIN_OK";


    private AppEditText mEtEmail;
    private AppEditText mEtPassword;
    private ProgressBar mProgressBar;


    private static SignInFragment fragment;

    public static SignInFragment newInstance() {

        if (fragment == null) {
            fragment = new SignInFragment();
        }

        return fragment;
    }


    public SignInFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mEtEmail = (AppEditText) root.findViewById(R.id.et_email);
        mEtPassword = (AppEditText) root.findViewById(R.id.et_password);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        RelativeLayout btnSignIn = (RelativeLayout) root.findViewById(R.id.btn_login);
        RelativeLayout btnSignInWithFacebook = (RelativeLayout) root.findViewById(R.id.btn_facebook_login);
        AppTextView btnTvForgotPass = (AppTextView) root.findViewById(R.id.tv_forgot_pass);

        btnSignIn.setOnClickListener(this);
        btnSignInWithFacebook.setOnClickListener(this);
        btnTvForgotPass.setOnClickListener(this);

        final AppTextView btnShowPass = (AppTextView)root.findViewById(R.id.tv_show_password);
        mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        btnShowPass.setTag(false);

        btnShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean show = (Boolean) btnShowPass.getTag();

                if (show) {
                    mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    btnShowPass.setText(getString(R.string.txt_hide_pass));
                } else {
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    btnShowPass.setText(getString(R.string.txt_show));
                }

                btnShowPass.setTag(!show);

            }
        });

        return root;
    }


    ////////////////////////////////////////////////////////////////////
    // INTERFACE IMPLEMENTATION
    ////////////////////////////////////////////////////////////////////

    // ILoginManager.LoginActionListener implementation
    @Override
    public void onDone(User user, Exception error) {

        mProgressBar.setVisibility(View.GONE);

        if (error == null && user != null) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(LOGIN_OK, true);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), getString(R.string.sign_in_error), Toast.LENGTH_LONG).show();
        }
    }

    // View.OnClickListener implementation
    @Override
    public void onClick(View v) {

        mProgressBar.setVisibility(View.VISIBLE);

        switch (v.getId()) {
            case R.id.btn_login:
                loginWithEmail();
                break;

            case R.id.btn_facebook_login:
                loginWithFacebook();
                break;

            case R.id.tv_forgot_pass:
                forgotPass();
                break;
        }
    }

    private void loginWithEmail() {
        User user = new User();
        user.setUserEmail(mEtEmail.getText().toString());
        user.setUserPassword(mEtPassword.getText().toString());
        LoginManagerWrap.getLoginManager(getActivity()).signInWithEmail(user, this);
    }


    private void loginWithFacebook(){
        LoginManagerWrap.getLoginManager(getActivity()).signInWithFacebook(this);
    }


    private void forgotPass() {
        User user = new User();
        user.setUserEmail(mEtEmail.getText().toString());
        LoginManagerWrap.getLoginManager(getActivity()).resetPassword(user, this);
    }


    public void setUserData(User user) {
        mEtEmail.setText(user.getUserEmail());
        mEtPassword.setText(user.getUserPassword());
    }


}
