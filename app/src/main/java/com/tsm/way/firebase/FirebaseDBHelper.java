package com.tsm.way.firebase;

import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseDBHelper {

    public static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getFirebaseDatabaseInstance() {

        if (mDatabase == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance();
        }

        return mDatabase;
    }
}
