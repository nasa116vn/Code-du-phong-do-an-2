package com.example.doantotnghiep;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.doantotnghiep.model.ArrayVi;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context) {
        super(context, "data", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        try {
//            //Table Tai khoan
//            db.execSQL("create table if not exists tbltaikhoan(tentaikhoan text primary key, masobimat text, matkhau text, " +
//                    "hovaten text, diachi text, sodienthoai int, email text, ngaysinh numeric);");
//
//            //Table Sinh vien
//            //data.execSQL("create table if not exists tblsinhvien()");
//
//            //Table Vi
//            db.execSQL("create table if not exists tblvi(mavi int primary key, tenvi text, motavi text, sotienvi real, sodu real, " +
//                    "tentaikhoan text constraint tentaikhoan references tbltaikhoan(tentaikhoan) on delete cascade);");
//
//            //Table Danh muc thu chi
//            db.execSQL("create table if not exists tbldanhmucthuchi(madanhmuc int primary key, tendanhmuc text, loaikhoan text, " +
//                    "tentaikhoan text constraint tentaikhoan references tbltaikhoan(tentaikhoan) on delete cascade)");
//
//            //Table Thu chi
//            db.execSQL("create table if not exists tblthuchi(mathuchi int primary key, loaithuchi text, sotienthuchi real, mota text, " +
//                    "ngaythuchien numeric, mavi int constraint mavi references tblvi(mavi) on delete cascade, " +
//                    "tentaikhoan text constraint tentaikhoan references tbltaikhoan(tentaikhoan) on delete cascade, " +
//                    "madanhmuc int constraint madanhmuc references tbldanhmucthuchi(madanhmuc) on delete cascade)");
//
//            //Table Ke hoach
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists tblvi");
        db.execSQL("drop table if exists tbltaikhoan");
        onCreate(db);
    }
}
