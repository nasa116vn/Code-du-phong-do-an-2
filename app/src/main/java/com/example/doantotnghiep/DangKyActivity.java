package com.example.doantotnghiep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

public class DangKyActivity extends AppCompatActivity {
    private SQLiteDatabase data;
    private EditText editText_TenTaiKhoan, editText_MatKhau, editText_MaSoBiMat;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        data = openOrCreateDatabase("data.db", MODE_PRIVATE, null);
        animation = AnimationUtils.loadAnimation(this, R.anim.animation_edittext);

        AnhXa();

    }

    public void AnhXa() {
        editText_MaSoBiMat = (EditText)findViewById(R.id.editText_MaSoBiMat);
        editText_TenTaiKhoan = (EditText)findViewById(R.id.editText_TenTaiKhoan);
        editText_MatKhau = (EditText)findViewById(R.id.editText_MatKhau);
    }

    public void XoaTrang(View v) {
        editText_TenTaiKhoan.setText(null);
        editText_MatKhau.setText(null);
        editText_MaSoBiMat.setText(null);
    }

    public void ThoatDangKy(View v){
        finish();
    }

    public void DangKy(View v) {
        boolean mk = true, tk = true;
        String thongbao = "";
        Cursor c = data.rawQuery("select * from tbltaikhoan", null);
        c.moveToFirst();
        while (c.isAfterLast()==false) {
            if (c.getString(c.getColumnIndex("masobimat")).equals(editText_MaSoBiMat.getText().toString())) {
                mk = false;
            } else if (c.getString(c.getColumnIndex("tentaikhoan")).equals(editText_TenTaiKhoan.getText().toString())) {
                tk = false;
            }
            c.moveToNext();
        }
        if (editText_TenTaiKhoan.getText().toString().equals("")) {
            thongbao = "Bạn chưa nhập tên tài khoản";
            editText_TenTaiKhoan.startAnimation(animation);
        } else if (editText_MatKhau.getText().toString().equals("")) {
            thongbao = "Bạn chưa nhập mật khẩu";
            editText_MatKhau.startAnimation(animation);
        } else if (editText_MaSoBiMat.getText().toString().equals("")) {
            thongbao = "Bạn chưa nhập mã số bí mật";
            editText_MaSoBiMat.startAnimation(animation);
        } else if (mk==false) {
            thongbao = "Hãy chọn mã số bí mật khác";
            editText_MaSoBiMat.startAnimation(animation);
        } else if (tk==false) {
            thongbao = "Tài khoản này đã tồn tại";
            editText_TenTaiKhoan.startAnimation(animation);
        } else {
            ContentValues values = new ContentValues();
            values.put("masobimat", editText_MaSoBiMat.getText().toString());
            values.put("tentaikhoan", editText_TenTaiKhoan.getText().toString());
            values.put("matkhau", editText_MatKhau.getText().toString());
            if (data.insert("tbltaikhoan", null, values)!=-1) {
                thongbao = "Tạo tài khoản thành công";
                finish();
            } else {
                thongbao = "Tạo tài khoản thất bại";
            }
        }
        Toast.makeText(getApplicationContext(), thongbao, Toast.LENGTH_SHORT).show();
    }


}
