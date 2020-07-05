package com.example.doantotnghiep.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.doantotnghiep.R;
import com.example.doantotnghiep.model.ArrayDanhMucThuChi;
import com.example.doantotnghiep.model.ArrayVi;

import java.util.ArrayList;
import java.util.List;

public class AdapterDanhMucThuChi extends ArrayAdapter<ArrayDanhMucThuChi> {
    private Activity context;
    private int madanhmuc;
    private List<ArrayDanhMucThuChi> arr = null;
    private TextView textView_TenDanhMuc, textView_LoaiKhoan;

    public AdapterDanhMucThuChi(Context context, int resource, @NonNull List<ArrayDanhMucThuChi> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.madanhmuc = resource;
        this.arr = objects;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        view = layoutInflater.inflate(madanhmuc, null);
        if (arr.size() > 0 && position >= 0) {
            textView_TenDanhMuc = (TextView) view.findViewById(R.id.textView_TenDanhMuc);
            textView_LoaiKhoan = (TextView) view.findViewById(R.id.textView_LoaiKhoan);
            textView_TenDanhMuc.setText(arr.get(position).tendanhmuc);
            textView_LoaiKhoan.setText(arr.get(position).loaikhoan);
        }
        return view;
    }
}
