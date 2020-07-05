package com.example.doantotnghiep.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.doantotnghiep.R;
import com.example.doantotnghiep.model.ArrayThongKe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterThongKe extends BaseExpandableListAdapter {
    private Context C;
    private String[] arrtong;
    private ArrayList<ArrayThongKe> arrthu, arrchi;
    private TextView txtTenNhom, txtSoLuong, txtTenNhomNho, txtTien;

    public AdapterThongKe(Context C, String[] arrtong, ArrayList<ArrayThongKe> arrthu, ArrayList<ArrayThongKe> arrchi) {
        this.C = C;
        this.arrtong = arrtong;
        this.arrthu = arrthu;
        this.arrchi = arrchi;
    }
    @Override
    public int getGroupCount() {
        return arrtong.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0) {
            return arrthu.size();
        }
        return arrchi.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(C).inflate(R.layout.adapter_thongke_tong_item, parent, false);
        }
        txtTenNhom = (TextView) view.findViewById(R.id.txtTenNhom);
        txtSoLuong = (TextView) view.findViewById(R.id.txtSoLuong);
        txtTenNhom.setText(arrtong[groupPosition]);
        if (groupPosition == 0) {
            txtSoLuong.setText("" + arrthu.size());
        } else {
            txtSoLuong.setText("" + arrchi.size());
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(C).inflate(R.layout.adapter_thongke_item, parent, false);
        }
        txtTenNhomNho = (TextView) view.findViewById(R.id.txtTenNhomNho);
        txtTien = (TextView) view.findViewById(R.id.txtTien);
        if (groupPosition == 0) {
            txtTenNhomNho.setText(arrthu.get(childPosition).TenThongKe);
            txtTien.setText(DoiSoSangTien(arrthu.get(childPosition).TienThongKe));
            txtTien.setTextColor(Color.parseColor("#2FB400"));
            view.setBackgroundColor(Color.parseColor("#EEEEEE"));
        } else {
            txtTenNhomNho.setText(arrchi.get(childPosition).TenThongKe);
            txtTien.setText(DoiSoSangTien(arrchi.get(childPosition).TienThongKe));
            txtTien.setTextColor(Color.RED);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public static String DoiSoSangTien(Double so) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        return decimalFormat.format((so)) + " đ";
    }
}
