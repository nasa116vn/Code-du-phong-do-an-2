package com.example.doantotnghiep.thongbao;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class ThongBaoKeHoachReciver extends BroadcastReceiver {
    String taikhoan,trangthai;
    SharedPreferences sharedPreferences,sharedPreferencesTT;
    private SQLiteDatabase data;
    private SimpleDateFormat simpleDateFormat;


    @Override
    public void onReceive(Context context, Intent intent) {
        //Nhan ten tai khoan
        sharedPreferences = context.getSharedPreferences("tendangnhapKH", Context.MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("taikhoancanchuyenkh","khong tim thay");
        //Kiem tra trang thai


        //Nhan trang thai
        sharedPreferencesTT = context.getSharedPreferences("tendangnhapTT", Context.MODE_PRIVATE);
        trangthai = sharedPreferencesTT.getString("taikhoancanchuyentt","khong tim thay"); //Trang thai tu ben staralarm truyen qua

        String title = "Bạn có một kế hoạch tiết kiệm đã kết thúc"; //trangthai
        String message = "Hãy đăng nhập tài khoản "+taikhoan+" để xem kế hoạch";
        NotificationHelper notificationHelper1 = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper1.getChanne12Notification(title,message);
        notificationHelper1.getManager().notify(2,nb.build());
    }


}
