package es.molestudio.photochop.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppEditText;


/**
 * Created by Chus on 27/11/14.
 */
public class SignInFragment extends Fragment {

    public static final Integer RC_USER_CREATION = 1001;
    public static final String USER_MAIL = "USER_EMAIL";


    private AppEditText mEtUserNickName;


    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
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

        mEtUserNickName = (AppEditText) root.findViewById(R.id.et_email);
        final AppEditText etUserPassword = (AppEditText) root.findViewById(R.id.et_password);
        RelativeLayout btnSignIn = (RelativeLayout) root.findViewById(R.id.btn_login);



        //final UserManager mUserManager = new UserManager();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                mUserManager.validateUser(getActivity(), mEtUserNickName.getText().toString(),
                        etUserPassword.getText().toString(),
                        new UserManager.UserListener() {
                            @Override
                            public void onDone(ArrayList<User> users, Exception err) {

                                if (users != null && users.get(0) != null) {
                                    Intent mainActivity = new Intent(getActivity(), MainActivity.class);
                                    getActivity().startActivity(mainActivity);
                                    getActivity().finish();
                                }
                            }
                        });*/
            }

        });



        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_USER_CREATION && data != null) {
                /*String email = data.getStringExtra(UpdateableFragment.UPDATED_DATA);
                if (email != null) {
                    mEtUserNickName.setText(email);
                }*/

            }
        }
    }

}
