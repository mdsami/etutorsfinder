package com.finalproject.app.findingtutors;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.finalproject.app.findingtutors.adapter.ProfileAdapter;
import com.finalproject.app.findingtutors.model.Student;
import com.finalproject.app.findingtutors.model.Teacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.finalproject.app.findingtutors.utils.Config.PREFS_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements ValueEventListener {

    private static final int RESULT_LOAD_IMG = 1889;
    private RecyclerView profileView;
    private boolean type;
    private Context context;
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference database;
    private String phone;
    private String classN;
    private String institute, add, sub;
    private Student student;
    private Teacher teacher;
    private ProfileAdapter adapter;
    private FirebaseUser auth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        type = prefs.getBoolean("type", false);

        refreshLayout = rootView.findViewById(R.id.swipeRefresh);

        profileView = rootView.findViewById(R.id.profileView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        profileView.setLayoutManager(layoutManager);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        if (type){
            database = FirebaseDatabase.getInstance().getReference().child("users").child("Student").child(auth.getUid());
        } else {
            database = FirebaseDatabase.getInstance().getReference().child("users").child("Teacher").child(auth.getUid());
        }
        refreshLayout.setRefreshing(true);
        database.addValueEventListener(this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (type){
                    //profileView.setAdapter(new ProfileAdapter(student, true));
                } else {
                    //profileView.setAdapter(new ProfileAdapter(teacher, false));
                }
                refreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (type){
            student = dataSnapshot.getValue(Student.class);
            assert student != null;
            adapter = new ProfileAdapter(student, true);
            adapter.setOnClickListener(handler);
            profileView.setAdapter(adapter);
        } else {
            teacher = dataSnapshot.getValue(Teacher.class);
            assert teacher != null;
            adapter = new ProfileAdapter(teacher, false);
            adapter.setOnClickListener(handler);
            profileView.setAdapter(adapter);
        }
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit:
                //do something
                onProfileManager();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onProfileManager() {
        MaterialDialog.Builder builder  = new MaterialDialog.Builder(context)
                .title("Edit Profile")
                .customView(R.layout.layout_dialog_edit, true)
                .backgroundColorRes(android.R.color.white)
                .titleColor(Color.parseColor("#DF4F5F"))
                .positiveText("Update");

        MaterialDialog dialog = builder.build();
        final EditText editPhone = (EditText) dialog.findViewById(R.id.edit_phone);
        final EditText editClass = (EditText) dialog.findViewById(R.id.edit_class);
        final EditText editInst = (EditText) dialog.findViewById(R.id.edit_institute);
        final EditText editAdd = (EditText) dialog.findViewById(R.id.edit_address);
        final EditText editSub = (EditText) dialog.findViewById(R.id.edit_interested);
        TextInputLayout inputLayoutClass = (TextInputLayout) dialog.findViewById(R.id.til_class);
        TextInputLayout inputLayoutInterested = (TextInputLayout) dialog.findViewById(R.id.til_interested);

        if (!type){
            editAdd.setText(teacher.getAddress());
            editInst.setText(teacher.getInstitution());
            editPhone.setText(teacher.getPhone());
            editSub.setText(teacher.getInstitution());
            inputLayoutInterested.setVisibility(View.VISIBLE);
        } else {
            editAdd.setText(student.getAddress());
            editInst.setText(student.getInstitution());
            editPhone.setText(student.getPhone());
            editClass.setText(student.getInstitution());
            inputLayoutClass.setVisibility(View.VISIBLE);
        }



        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                phone = editPhone.getText().toString();
                classN = editClass.getText().toString();
                institute = editInst.getText().toString();
                add = editAdd.getText().toString();
                sub = editSub.getText().toString();
                updateProfile();
            }
        });

        dialog.show();
    }

    private void updateProfile() {
        database.child("phone").setValue(phone);
        database.child("institution").setValue(institute);
        database.child("address").setValue(add);

        //Check user is Student or Teacher
        if (type){
            database.child("classname").setValue(classN);
        } else {
            database.child("interested").setValue(sub);
        }
        Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show();
    }

    private ProfileAdapter.ClickListenerHandler handler = new ProfileAdapter.ClickListenerHandler() {
        @Override
        public void onClick(View view, int position) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        }
    };


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
                final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encodedImg = Base64.encodeToString(b, Base64.DEFAULT);
                database.child("thumbnail").setValue(encodedImg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(context, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
