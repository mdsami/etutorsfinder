package com.finalproject.app.findingtutors;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class SelectionDialogFragment extends DialogFragment {

    private String [] optionMenu = {"Teacher", "Student"};

    public SelectionDialogFragment() {
        // Required empty public constructor
    }

    public static SelectionDialogFragment newInstance(){
        SelectionDialogFragment fragment = new SelectionDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        builder.setTitle("SIGN IN");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.dialog_selection, null);
        final MaterialSpinner materialSpinner = rootView.findViewById(R.id.spinner);
        materialSpinner.setItems(optionMenu);
        materialSpinner.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == event.ACTION_DOWN){

                }
                return true;
            }
        });

        builder.setView(rootView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = materialSpinner.getItems().get(materialSpinner.getSelectedIndex()).toString();

                        DialogFragment dialogWindow = SignUpDialogFragment.newInstance();
                        Bundle args = new Bundle();
                        args.putString("type", item);
                        dialogWindow.setArguments(args);
                        showRegisterDialog(dialogWindow, "SignUpDialogFragment");
                    }
                });

        return builder.create();
    }

    private void showRegisterDialog(DialogFragment dialog, String TAG) {
        dialog.show(getFragmentManager(), TAG);
    }

    public interface optionItemSelectedInfo {
        void optionItem(String item);
    }
}
