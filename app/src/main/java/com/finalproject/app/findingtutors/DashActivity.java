package com.finalproject.app.findingtutors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.finalproject.app.findingtutors.utils.Config.PREFS_NAME;

public class DashActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener{

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private Toolbar toolbar;
    private SharedPreferences myPref;
    private TextView toolbarTitleView;

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
        setContentView(R.layout.activity_dash);

        toolbar = findViewById(R.id.mToolbar);
        toolbarTitleView = toolbar.findViewById(R.id.title_bar);
        toolbarTitleView.setText("Profile");
        setSupportActionBar(toolbar);

        myPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //Init Firebase
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addValueEventListener(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new ProfileFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment mFragment;
            switch (item.getItemId()) {

                case R.id.profile:
                    mFragment = new ProfileFragment();
                    loadFragment(mFragment);
                    toolbarTitleView.setText("Profile");
                    return true;

                case R.id.teacher:
                    if (myPref.getBoolean("type", false)){
                        toolbarTitleView.setText("Teacher's List");
                        mFragment = new TeacherListFragment();
                        loadFragment(mFragment);
                    } else {
                        toolbarTitleView.setText("Student's List");
                        mFragment = new StudentListFragment();
                        loadFragment(mFragment);
                    }
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment mFragment){
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.frame_container, mFragment);
        mTransaction.addToBackStack(null);
        mTransaction.commit();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
            if (snapshot.getKey().compareTo("Student")==0){
                for (DataSnapshot data : snapshot.getChildren()){
                    if (data.getKey().compareTo(firebaseUser.getUid())==0){
                        Toast.makeText(DashActivity.this, "" + snapshot.getKey(), Toast.LENGTH_SHORT).show();
                        onInsertSharedPreference(true);
                    }
                }
            } else {
                for (DataSnapshot data : snapshot.getChildren()){
                    if (data.getKey().compareTo(firebaseUser.getUid())==0){
                        onInsertSharedPreference(false);
                    }
                }
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void onInsertSharedPreference(boolean type){
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("type", type);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
