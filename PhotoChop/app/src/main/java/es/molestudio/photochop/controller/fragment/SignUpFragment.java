package es.molestudio.photochop.controller.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppEditText;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.controller.LoginManager;
import es.molestudio.photochop.controller.LoginManagerWrap;
import es.molestudio.photochop.model.User;


public class SignUpFragment extends Fragment implements View.OnClickListener,
        LoginManager.LoginActionListener {

    public interface FinishSignUpListener {
        public void onFinishSingUp(User user);
    }

    private AppEditText mEtEmail;
    private AppEditText mEtNickName;
    private AppEditText mEtPassword;
    private ProgressBar mProgressBar;
    private FinishSignUpListener mFinishSignUpListener;

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    public void setSignUpListener(FinishSignUpListener listener) {
        mFinishSignUpListener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mEtEmail = (AppEditText) root.findViewById(R.id.et_email);
        mEtNickName = (AppEditText) root.findViewById(R.id.et_nick_name);
        mEtPassword = (AppEditText) root.findViewById(R.id.et_password);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        RelativeLayout btnSignUp = (RelativeLayout) root.findViewById(R.id.btn_signup);
        RelativeLayout btnSignInFacebook = (RelativeLayout) root.findViewById(R.id.btn_facebook_login);

        final AppTextView btnShowPass = (AppTextView) root.findViewById(R.id.tv_show_password);
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

        btnSignUp.setOnClickListener(this);
        btnSignInFacebook.setOnClickListener(this);


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
            if (ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
                getActivity().finish();
            } else {
                if (mFinishSignUpListener != null) {
                    mFinishSignUpListener.onFinishSingUp(user);
                }
            }

        } else {
            Toast.makeText(getActivity(), getString(R.string.sign_in_error), Toast.LENGTH_LONG).show();
        }
    }

    // View.OnClickListener implementation
    @Override
    public void onClick(View v) {

        mProgressBar.setVisibility(View.VISIBLE);

        switch (v.getId()) {
            case R.id.btn_signup:
                signUpWithEmail();
                break;

            case R.id.btn_facebook_login:
                loginWithFacebook();
                break;
        }
    }


    private void signUpWithEmail() {

        User user = new User();
        user.setUserEmail(mEtEmail.getText().toString());
        user.setUserNickName(mEtNickName.getText().toString());
        user.setUserPassword(mEtPassword.getText().toString());

        LoginManagerWrap.getLoginManager(getActivity()).signUpWithEmail(user, this);

    }

    private void loginWithFacebook(){
        LoginManagerWrap.getLoginManager(getActivity()).signInWithFacebook(this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFinishSignUpListener = (FinishSignUpListener) activity;
        } catch (ClassCastException e) {}
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFinishSignUpListener = null;
    }




}
