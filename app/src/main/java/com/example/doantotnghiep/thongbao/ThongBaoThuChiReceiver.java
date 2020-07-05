package com.example.doantotnghiep.thongbao;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

import com.example.doantotnghiep.MainActivity;

public class ThongBaoThuChiReceiver extends BroadcastReceiver {
    String taikhoan;
    SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences("tendangnhapTB", Context.MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("taikhoancanchuyentb","khong tim thay");
        String title = "Thông báo thu chi";
        String message = "Hãy đăng nhập tài khoản " + taikhoan +" để xem thu chi";
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title,message);
        notificationHelper.getManager().notify(1,nb.build());
    }
}
