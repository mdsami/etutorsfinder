package com.finalproject.app.findingtutors;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.jaredrummler.materialspinner.MaterialSpinner;



public class SignUpDialogFragment extends DialogFragment implements SelectionDialogFragment.optionItemSelectedInfo {
    private EditText emailText, passText, nameText, addText, phoneText, instText;
    private EditText guardianText, guardianPhone;
    private String optionSelectedItem;
    private MaterialSpinner genderText, classText, deptText, profText;

    private String[] genderItems = {"MALE", "FEMALE"};
    private String[] classItems = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    private String[] deptItems = {"None", "Science", "Commerce", "Arts"};
    private String[] profItems = {"Student", "Teacher"};

    public SignUpDialogFragment() {
        // Required empty public constructor
    }

    public static SignUpDialogFragment newInstance(){
        SignUpDialogFragment fragment = new SignUpDialogFragment();
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
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        optionSelectedItem = getArguments().getString("type");
        builder.setTitle(optionSelectedItem + " REGISTER");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView;

        if (optionSelectedItem.compareTo("Student")==0){
            rootView = inflater.inflate(R.layout.dialog_student, null);
            onCreateLayoutInflateItem(rootView, builder, true);
        } else {
            rootView = inflater.inflate(R.layout.dialog_teacher, null);
            onCreateLayoutInflateItem(rootView, builder, false);
        }

        return builder.create();
    }

    private void onCreateLayoutInflateItem(final View rootView, AlertDialog.Builder builder, final boolean isTrue) {
        emailText = rootView.findViewById(R.id.emailText);
        nameText = rootView.findViewById(R.id.nameText);
        passText = rootView.findViewById(R.id.passText);
        addText = rootView.findViewById(R.id.addText);
        genderText = rootView.findViewById(R.id.genderText);
        phoneText = rootView.findViewById(R.id.phoneText);
        instText = rootView.findViewById(R.id.institutionText);

        genderText.setItems(genderItems);

        if (isTrue){
            classText = rootView.findViewById(R.id.classText);
            deptText = rootView.findViewById(R.id.departmentSpinner);
            guardianText = rootView.findViewById(R.id.guardianText);
            guardianPhone = rootView.findViewById(R.id.guardianPhone);

            classText.setItems(classItems);
            deptText.setItems(deptItems);
        } else {
            profText = rootView.findViewById(R.id.profText);
            profText.setItems(profItems);
        }

        builder.setView(rootView)
                .setPositiveButton("CREATE ACCOUNT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isTrue){
                            StudentInfo activity = (StudentInfo) getActivity();
                            activity.getInfo(emailText.getText().toString(),
                                    nameText.getText().toString(),
                                    passText.getText().toString(),
                                    addText.getText().toString(),
                                    genderText.getItems().get(genderText.getSelectedIndex()).toString(),
                                    phoneText.getText().toString(),
                                    instText.getText().toString(),
                                    classText.getItems().get(classText.getSelectedIndex()).toString(),
                                    deptText.getItems().get(deptText.getSelectedIndex()).toString(),
                                    guardianText.getText().toString(),
                                    guardianPhone.getText().toString(),
                                    optionSelectedItem);
                        } else {
                            TeacherInfo activity = (TeacherInfo) getActivity();
                            activity.getInfo(emailText.getText().toString(),
                                    nameText.getText().toString(),
                                    passText.getText().toString(),
                                    addText.getText().toString(),
                                    genderText.getItems().get(genderText.getSelectedIndex()).toString(),
                                    phoneText.getText().toString(),
                                    instText.getText().toString(),
                                    profText.getItems().get(profText.getSelectedIndex()).toString(),
                                    optionSelectedItem);
                        }
                    }
                });
    }

    @Override
    public void optionItem(String item) {
        optionSelectedItem = item;
    }

    public interface StudentInfo {
        void getInfo(String email, String name, String password, String address, String gender,
                     String phone, String institution, String classname, String deptname, String gname, String gphone, String as);
    }

    public interface TeacherInfo {
        void getInfo(String email, String name, String password, String address,
                     String gender, String phone, String institution, String prof, String as);
    }
}
