package com.example.doantotnghiep.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.doantotnghiep.R;
import com.example.doantotnghiep.model.ArrayKeHoachTietKiem;
import com.example.doantotnghiep.model.ArrayThuChi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterKeHoachTietKiem extends ArrayAdapter<ArrayKeHoachTietKiem> {
    private Activity a;
    private int id;
    private ArrayList<ArrayKeHoachTietKiem> arr;
    private TextView tenkehoachtietkiem, ngaybatdaukehoachtietkiem, ngayketthuckehoachtietkiem, sotienkehoachtietkiem, trangthai, sotiendatietkiem
            ,nhanthongbao; //moi

    public AdapterKeHoachTietKiem(Activity context, int resource, ArrayList<ArrayKeHoachTietKiem> objects) {
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
            tenkehoachtietkiem =  view.findViewById(R.id.txtTenKeHoachTietKiem);
            sotienkehoachtietkiem =  view.findViewById(R.id.txtSoTienKeHoachTietKiem);
            trangthai =  view.findViewById(R.id.txtTrangThaiKeHoachTietKiem);
            sotiendatietkiem = view.findViewById(R.id.txtSoTienDaTietKiem);
            nhanthongbao = view.findViewById(R.id.txtThongBaoKeHoach); //moi

            tenkehoachtietkiem.setText(arr.get(position).tenkehoachtietkiem);
            sotienkehoachtietkiem.setText(DoiSoSangTien(arr.get(position).sotienkehoachtietkiem));
            trangthai.setText(arr.get(position).trangthai);
            nhanthongbao.setText(arr.get(position).nhanthongbao); //moi
            if (trangthai.getText().toString().equals("Đã kết thúc - Kế hoạch thành công")) {
                trangthai.setTextColor(Color.parseColor("#2FB400"));
            } else if (trangthai.getText().toString().equals("Đã kết thúc - Kế hoạch thất bại")) {
                trangthai.setTextColor(Color.RED);
            } else if (trangthai.getText().toString().equals("Đang thực hiện")) {
                trangthai.setTextColor(Color.BLUE);
            }
            sotiendatietkiem.setText(DoiSoSangTien(arr.get(position).sotiendatietkiem));
        }
        return view;
    }

    public static String DoiSoSangTien(double so) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        return decimalFormat.format((so)) + " đ";
    }
}
