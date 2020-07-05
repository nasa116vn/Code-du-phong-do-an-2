package com.example.doantotnghiep.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.doantotnghiep.R;
import com.example.doantotnghiep.model.ArrayLichSuChuyenTien;
import com.example.doantotnghiep.model.ArrayThuChi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterLichSuChuyenTien extends ArrayAdapter<ArrayLichSuChuyenTien> {
    private Activity a;
    private int id;
    private ArrayList<ArrayLichSuChuyenTien> arr;
    private TextView ngay, tien, vichuyen, vinhan;

    public AdapterLichSuChuyenTien(Activity context, int resource, ArrayList<ArrayLichSuChuyenTien> objects) {
        super(context, resource, objects);
        this.a = context;
        this.id = resource;
        this.arr = objects;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater in = a.getLayoutInflater();
        view = in.inflate(id, null);
        if (arr.size()>0 && position>=0) {
            ngay = (TextView) view.findViewById(R.id.txtNgayChuyenTien);
            tien = (TextView) view.findViewById(R.id.txtTienChuyen);
            vichuyen = (TextView) view.findViewById(R.id.txtViChuyen);
            vinhan = (TextView) view.findViewById(R.id.txtViNhan);

            ngay.setText(""+ arr.get(position).thoigian);
            tien.setText(DoiSoSangTien(arr.get(position).tien));
            vichuyen.setText(arr.get(position).vichuyen);
            vinhan.setText(arr.get(position).vinhan);
        }
        return view;
    }

    public static String DoiSoSangTien(double so) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        return decimalFormat.format((so)) + " đ";
    }
}
