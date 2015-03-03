package es.molestudio.photochop.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppEditText;


public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";


    private Boolean mIsNickNameEmpty = null;
    private AppEditText mEtEmail;

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
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
        final AppEditText etNickName = (AppEditText) root.findViewById(R.id.et_nick_name);
        final AppEditText etPassword = (AppEditText) root.findViewById(R.id.et_password);

        RelativeLayout btnSignUp = (RelativeLayout) root.findViewById(R.id.btn_signup);
        btnSignUp.setEnabled(false);

        mEtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (mIsNickNameEmpty == null) {
                    mIsNickNameEmpty = etNickName.getText().toString().equals("");
                }

                String[] text = s.toString().split("@");


                if (mIsNickNameEmpty) {
                    etNickName.setText(text[0]);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                UserManager userManager = new UserManager();
                userManager.createUser(etNickName.getText().toString(),

                        mEtEmail.getText().toString(),
                        etPassword.getText().toString(), new UserManager.UserListener() {
                    @Override
                    public void onDone(ArrayList<User> users, Exception err) {
                        if (users != null && users.get(0) != null) {
                            Bundle savedData = new Bundle();
                            savedData.putString(UPDATED_DATA, mEtEmail.getText().toString());
                            mListener.onFragmentInteraction(savedData);
                        } else {
                            Toast.makeText(getActivity(), "No se ha podido crear el usuario :(", Toast.LENGTH_LONG).show();
                        }
                    }
                });*/

            }
        });

        return root;
    }



}
