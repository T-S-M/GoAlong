package com.tsm.way.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsm.way.R;
import com.tsm.way.firebase.FirebaseDBHelper;
import com.tsm.way.ui.MainActivity;


public class NotifyUserOfPendingTask {

    public static void syncDataThenNotify(final Context context) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbRef = FirebaseDBHelper.getFirebaseDatabaseInstance().getReference();
        DatabaseReference pendingRef = dbRef.child("pending").child(user.getUid());
        final DatabaseReference statRef = dbRef.child("stats").child(user.getUid()).child("pendingCount");

        pendingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    long count = dataSnapshot.getChildrenCount();
                    sendNotification(context, count);
                    statRef.setValue(count);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.v("Job", "executed");
    }

    private static void sendNotification(Context context, long count) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "default_channel");

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_schedule_black_24dp);
        mBuilder.setContentTitle("Pending Invites");
        String content = "You have " + count + " new invitation";
        if (count > 1) {
            content += "s";
        }
        mBuilder.setContentText(content);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }
}
