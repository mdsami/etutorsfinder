package com.finalproject.app.findingtutors;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.finalproject.app.findingtutors.model.Student;
import com.finalproject.app.findingtutors.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements SignUpDialogFragment.StudentInfo, SignUpDialogFragment.TeacherInfo ,
        SignInDialogFragment.UserSignInInfo {

    private Button btnSignIn, btnResister;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;

    private RelativeLayout rootLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);

        //Init Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        //Check already session , if ok-> DashBoard
        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, DashActivity.class));
            finish();
        }

        //Init View
        btnSignIn = findViewById(R.id.btnSignIn);
        btnResister = findViewById(R.id.btnRegister);
        rootLayout = findViewById(R.id.rootLayout);


        //Event
        btnResister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });
    }

    private void showRegisterDialog() {
        DialogFragment dialog = SelectionDialogFragment.newInstance();
        dialog.show(MainActivity.this.getFragmentManager(), "SelectionDialogFragment");
    }

    private void showLoginDialog() {
        DialogFragment dialog = SignInDialogFragment.newInstance();
        dialog.show(MainActivity.this.getFragmentManager(), "SignInDialogFragment");
    }

    private void onCreateUserDatabase(String as, Object user) {
        assert auth.getUid()!=null;
        users.child(as).child(auth.getUid())
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(rootLayout, "Registration successfully done!", Snackbar.LENGTH_SHORT)
                                .show();
                        startActivity(new Intent(MainActivity.this, DashActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootLayout, "Registration failed due to "
                                + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void getSignInInfo(String email, String password) {
        //Sign in user
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(MainActivity.this, DashActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(rootLayout, "Failed due to " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getInfo(final String email, final String name, final String password, final String address, final String gender, final String phone, final String institution, final String classname, final String deptname, final String gname, final String gphone, final String as) {
        //Check validation
        if (TextUtils.isEmpty(name)){
            Snackbar.make(rootLayout, "Please enter your name", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)){
            Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6){
            Snackbar.make(rootLayout, "Password is too short !!!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(address)){
            Snackbar.make(rootLayout, "Please enter your address", Snackbar.LENGTH_SHORT).show();
            return;
        }

        //Register new user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Save user to db
                        Student user = new Student();
                        user.setEmail(email);
                        user.setName(name);
                        user.setPassword(password);
                        user.setId(auth.getUid());
                        user.setType(as);
                        user.setAddress(address);
                        user.setGender(gender);
                        user.setClassname(classname);
                        user.setDeptname(deptname);
                        user.setGuardianname(gname);
                        user.setGuardianphone(gphone);
                        user.setInstitution(institution);
                        user.setPhone(phone);

                        onCreateUserDatabase("Student", user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootLayout, "Registration failed due to "
                                + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void getInfo(final String email, final String name, final String password, final String address, final String gender, final String phone, final String institution, final String prof, final String as) {
        //Check validation
        if (TextUtils.isEmpty(name)){
            Snackbar.make(rootLayout, "Please enter your name", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)){
            Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6){
            Snackbar.make(rootLayout, "Password is too short !!!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(address)){
            Snackbar.make(rootLayout, "Please enter your address", Snackbar.LENGTH_SHORT).show();
            return;
        }

        //Register new user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Save user to db
                        Teacher user = new Teacher();
                        user.setEmail(email);
                        user.setName(name);
                        user.setPassword(password);
                        user.setId(auth.getUid());
                        user.setType(as);
                        user.setAddress(address);
                        user.setGender(gender);
                        user.setPhone(phone);
                        user.setInstitution(institution);
                        user.setProfession(prof);

                        onCreateUserDatabase("Teacher", user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootLayout, "Registration failed due to "
                                + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
