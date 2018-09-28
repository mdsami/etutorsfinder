package com.finalproject.app.findingtutors.app;


import android.support.multidex.MultiDexApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class TutorApplication extends MultiDexApplication {
    private static TutorApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("users");
        scoresRef.keepSynced(true);

    }
}
