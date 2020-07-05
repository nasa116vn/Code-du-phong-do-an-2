package com.example.doantotnghiep.thongbao;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import com.example.doantotnghiep.MainActivity;
import com.example.doantotnghiep.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channel1ID = "channel1ID";
    public static final String channel1Name = "Thông báo thu chi";
    public static final String channel2ID = "channel2ID";
    public static final String channel2Name = "Thông báo kế hoạch";
    private NotificationManager mManager;
    private PendingIntent pendingIntent;


    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }
    }

    //Thong bao thu chi
    public void createChannels(){
        NotificationChannel channel = new NotificationChannel(channel1ID,channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);

        NotificationChannel channel2 = new NotificationChannel(channel2ID,channel2Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLightColor(R.color.colorPrimary);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel2);
    }

    public NotificationManager getManager(){
        if(mManager == null){

            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(this, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title,String message){
        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    //Thong bao ke hoach

    public NotificationCompat.Builder getChanne12Notification(String title,String message){
        return new NotificationCompat.Builder(getApplicationContext(), channel2ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }




}



