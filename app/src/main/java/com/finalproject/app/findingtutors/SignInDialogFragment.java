package com.finalproject.app.findingtutors;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;



public class SignInDialogFragment extends DialogFragment {
    private EditText emailText, passText;
    private SharedPreferences myPref;
    private SharedPreferences.Editor edit;
    private CheckBox checkBox;

    public SignInDialogFragment() {
        // Required empty public constructor
    }

    public static SignInDialogFragment newInstance(){
        SignInDialogFragment fragment = new SignInDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPref = getActivity().getSharedPreferences("SavedLogin", Context.MODE_PRIVATE);
        edit = myPref.edit();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);

        builder.setTitle("SIGN IN");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.dialog_signin, null);
        emailText = rootView.findViewById(R.id.emailText);
        passText = rootView.findViewById(R.id.passText);
        checkBox = rootView.findViewById(R.id.check_remember);

        if (myPref.getBoolean("state", false)){
            checkBox.setChecked(true);
            emailText.setText(myPref.getString("useremail",""));
            passText.setText(myPref.getString("userpass",""));
        }

        passText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSignInProcess();
                return true;
            }
        });

        builder.setView(rootView)
                .setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onSignInProcess();
                    }
                });

        return builder.create();
    }

    private void onSignInProcess() {
        if (checkBox.isChecked()){
            edit.putBoolean("state", true);
            edit.putString("useremail", emailText.getText().toString());
            edit.putString("userpass", passText.getText().toString());
            edit.commit();
        } else {
            edit.clear();
            edit.commit();
        }
        UserSignInInfo activity = (UserSignInInfo) getActivity();
        activity.getSignInInfo(emailText.getText().toString(),
                passText.getText().toString());
    }

    public interface UserSignInInfo{
        void getSignInInfo(String email, String password);
    }
}
