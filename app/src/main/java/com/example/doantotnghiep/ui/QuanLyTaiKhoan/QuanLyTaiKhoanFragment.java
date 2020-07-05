package com.example.doantotnghiep.ui.QuanLyTaiKhoan;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.doantotnghiep.MainActivity;
import com.example.doantotnghiep.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class QuanLyTaiKhoanFragment extends Fragment {
    private View myFragment;
    private Activity activity;
    private Animation animation;
    private SQLiteDatabase data;
    private String tentaikhoan;
    private Button button_CapNhatTaiKhoan, button_DangXuat, button_DoiMatKhauTaiKhoan,thaydoi,huy,btnLuu,btnHuy;
    private TextView txt_TenTaiKhoanFrag, txt_TenNguoiDung, txt_DiaChi, txt_Email, txt_SDT,txt_TenTaiKhoan,txt_TongSoDu;
    private EditText maso, matkhau1, matkhau2, editText_HoTen, editText_DiaChi,editText_Email,editText_SoDienThoai;
    private String taikhoan;
    private SharedPreferences sharedPreferences;

//code Phat

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_quanlytaikhoan, container, false);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        data = activity.openOrCreateDatabase("data.db", activity.MODE_PRIVATE, null);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);

        AnhXa();
        CapNhatTaiKhoan();
        DangXuat();
        DoiMatKhauTaiKhoan();
        LayTenTaiKhoan();
        LayTaiKhoan();
        LayTongSoDu();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        activity.getMenuInflater().inflate(R.menu.menu_taikhoan, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_CapNhatTaiKhoan: {
                CapNhatTaiKhoan();
                return true;
            }
            case R.id.option_DoiMatKhau: {
                DoiMatKhau();
                return true;
            }
            case R.id.option_DangXuat: {
                DangXuat();
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void LayTenTaiKhoan()
    {
        sharedPreferences = getActivity().getSharedPreferences("tendangnhap", Context.MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("taikhoancanchuyen","khong tim thay");
        txt_TenTaiKhoanFrag.setText(taikhoan);
    }

    public void CapNhatTaiKhoan() {
        button_CapNhatTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dialog
                animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);
                final Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_capnhatthongtintaikhoan);
                d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                d.show();
                //Anh xa
                txt_TenTaiKhoan = (TextView)d.findViewById(R.id.txtTenTaiKhoanCapNhat);
                editText_HoTen = d.findViewById(R.id.TextHoTen);
                editText_DiaChi = d.findViewById(R.id.TextDiaChi);
                editText_Email = d.findViewById(R.id.TextEmail);
                editText_SoDienThoai = d.findViewById(R.id.TextSDT);
                btnLuu = d.findViewById(R.id.btnLuuCapNhatTaiKhoan);
                btnHuy = d.findViewById(R.id.btnHuyCapNhatTaiKhoan);
                //Load thong tin
                LayThongTinLenDialog();
                //Xu ly
                txt_TenTaiKhoan.setText(taikhoan);
                btnLuu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(CapNhat())
                        {
                            d.dismiss();
                            LayTaiKhoan();
                        }
                    }
                });
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
            }
        });
    }

    public boolean CapNhat() {
        String thongbao = "";
        Cursor cursor = data.rawQuery("select tentaikhoan from tbltaikhoan", null); // moi
        cursor.moveToFirst();
        while (cursor.moveToLast()==false)
        {
            if (cursor.getString(cursor.getColumnIndex("tentaikhoan")).equals(taikhoan))
            {
                tentaikhoan = cursor.getString(cursor.getColumnIndex("tentaikhoan"));
            }
            cursor.moveToNext();
        }
        if (editText_HoTen.getText().toString().equals("")) {
            editText_HoTen.startAnimation(animation);
            Toast.makeText(activity, "Bạn chưa nhập họ tên", Toast.LENGTH_SHORT).show();
        } else if (editText_DiaChi.getText().toString().equals("")) {
            editText_DiaChi.startAnimation(animation);
            Toast.makeText(activity, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
        } else if (editText_Email.getText().toString().equals("")) {
            editText_Email.startAnimation(animation);
            Toast.makeText(activity, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
        } else if (editText_SoDienThoai.getText().toString().equals("")) {
            editText_SoDienThoai.startAnimation(animation);
            Toast.makeText(activity, "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
        } else {

            ContentValues contentValues = new ContentValues();
            String sdt = editText_SoDienThoai.getText().toString();
            contentValues.put("hovaten", editText_HoTen.getText().toString());
            contentValues.put("diachi", editText_DiaChi.getText().toString());
            contentValues.put("sodienthoai", sdt);
            contentValues.put("email", editText_Email.getText().toString());
            data.update("tbltaikhoan", contentValues, "tentaikhoan like '" + taikhoan + "'", null);
            thongbao = "Cập nhật thành công";
            Toast.makeText(activity, thongbao, Toast.LENGTH_SHORT).show();
            return true;
        }
        thongbao = "Cập nhật không thành công";
        Toast.makeText(activity, thongbao, Toast.LENGTH_SHORT).show();
        return false;
    }

    public void AnhXa()
    {
        button_CapNhatTaiKhoan =  myFragment.findViewById(R.id.btnCapNhatTaiKhoan);
        txt_TenTaiKhoanFrag =  myFragment.findViewById(R.id.txtTenTaiKhoan);
        txt_TenNguoiDung =  myFragment.findViewById(R.id.txtTenNguoiDung);
        txt_DiaChi =  myFragment.findViewById(R.id.txtDiaChi);
        txt_Email =  myFragment.findViewById(R.id.txtEmail);
        txt_SDT =  myFragment.findViewById(R.id.txtSoDienThoai);
        txt_TongSoDu=  myFragment.findViewById(R.id.txtTongSoDu);
        button_DangXuat =  myFragment.findViewById(R.id.btnDangXuat);
        button_DoiMatKhauTaiKhoan =  myFragment.findViewById(R.id.btnDoiMatKhauTaiKhoan);
    }

    public void DoiMatKhauTaiKhoan()
    {
        button_DoiMatKhauTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);
                //XoaTrang();
                final Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_quenmatkhau);
                d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                d.show();

                maso =  d.findViewById(R.id.txtNhapmasoquen);
                matkhau1 =  d.findViewById(R.id.txtNhapmatkhauquen);
                matkhau2 =  d.findViewById(R.id.txtNhapmatkhau2quen);
                thaydoi =  d.findViewById(R.id.btnThaydoiquen);
                huy =  d.findViewById((R.id.btnHuyQuenMatKhau));
                thaydoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DoiMatKhau()) {
                            d.dismiss();
                        }
                    }
                });
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

            }
        });
    }

    public boolean DoiMatKhau() {
        String thongbao = "";
        String matkhauht = "";
        boolean msbm = false;
        Cursor c = data.rawQuery("select * from tbltaikhoan", null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            if(c.getString(c.getColumnIndex("tentaikhoan")).equals(taikhoan))
            {
                if (c.getString(c.getColumnIndex("masobimat")).equals(maso.getText().toString())) {
                    msbm = true;
                    matkhauht = c.getString(c.getColumnIndex("matkhau"));
                }
            }
            c.moveToNext();
        }
        if (maso.getText().toString().equals("")) {
            thongbao = "Bạn chưa nhập mã số bí mật";
            maso.startAnimation(animation);
        } else if (matkhau1.getText().toString().equals("")) {
            thongbao = "Bạn chưa nhập mật khẩu";
            matkhau1.startAnimation(animation);
        } else if (matkhau2.getText().toString().equals("")) {
            thongbao = "Bạn chưa nhập mật khẩu mới";
            matkhau2.startAnimation(animation);
        } else if (msbm == false) {
            thongbao = "Mã số bí mật sai";
            maso.startAnimation(animation);
        } else if (matkhau1.getText().toString().equals(matkhau2.getText().toString()) == false) {
            thongbao = "Mật khẩu không khớp";
            matkhau1.startAnimation(animation);
            matkhau2.startAnimation(animation);
        } else if(matkhau1.getText().toString().equals(matkhauht)){
            thongbao = "Mật khẩu này hiện đang được sử dụng";
            matkhau1.startAnimation(animation);
            matkhau1.setText("");
            matkhau2.setText("");
        }else {
            ContentValues values = new ContentValues();
            values.put("matkhau", matkhau1.getText().toString());
            data.update("tbltaikhoan", values, "tentaikhoan like '"+ taikhoan+"'", null);
            thongbao = "Lưu thành công";
            Toast.makeText(activity, thongbao, Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(activity, thongbao, Toast.LENGTH_SHORT).show();
        return false;
    }

    public void DangXuat()
    {
        button_DangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                activity.finish();
            }
        });
    }

    public void LayTaiKhoan()
    {
        Cursor cursor = data.rawQuery("select * from tbltaikhoan where tentaikhoan = '"+ taikhoan + "'",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
        if(cursor.getString(cursor.getColumnIndex("tentaikhoan")).equals(taikhoan))
        {
            txt_TenNguoiDung.setText(cursor.getString(3));
            txt_DiaChi.setText(cursor.getString(4));
            txt_SDT.setText(cursor.getString(5));
            txt_Email.setText(cursor.getString(6));
        }
         cursor.moveToNext();
        }
        cursor.close();
    }

    public void LayThongTinLenDialog()
    {
        Cursor cursor = data.rawQuery("select * from tbltaikhoan where tentaikhoan = '"+ taikhoan + "'",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            if(cursor.getString(cursor.getColumnIndex("tentaikhoan")).equals(taikhoan))
            {
                editText_HoTen.setText(cursor.getString(3));
                editText_DiaChi.setText(cursor.getString(4));
                editText_SoDienThoai.setText(cursor.getString(5));
                editText_Email.setText(cursor.getString(6));
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void LayTongSoDu(){
        int tongsodu = 0;
        Cursor c = data.rawQuery("select sotienvi from tblvi where tentaikhoan = '"+ taikhoan +"'",null);
        c.moveToFirst();
        while (c.isAfterLast()==false){
            tongsodu = tongsodu + c.getInt(c.getColumnIndex("sotienvi"));
            c.moveToNext();
        }
        txt_TongSoDu.setText(DoiSoSangTien(Double.parseDouble(String.valueOf(tongsodu))));
        txt_TongSoDu.setTextColor(Color.BLUE);
        c.close();
    }

    public static String DoiSoSangTien(Double so) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        return decimalFormat.format((so)) + " đ";
    }
}