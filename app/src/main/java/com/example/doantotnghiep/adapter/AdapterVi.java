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
import com.example.doantotnghiep.model.ArrayVi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterVi extends ArrayAdapter<ArrayVi> {
    private Activity context = null;
    private int mavi;
    private List<ArrayVi> arr = null;
    private TextView textView_TenVi, textView_MoTaVi ,textView_SoTien;

    public AdapterVi(@NonNull Context context, int resource, @NonNull List<ArrayVi> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.mavi = resource;
        this.arr = objects;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(mavi, null);
        if (arr.size() > 0 && position >= 0) {
            textView_TenVi = (TextView) view.findViewById(R.id.textView_TenVi);
            textView_SoTien = (TextView) view.findViewById(R.id.textView_SoTien);
            textView_MoTaVi = (TextView) view.findViewById(R.id.textView_MoTaVi);
            textView_TenVi.setText(arr.get(position).tenvi);
            textView_MoTaVi.setText(arr.get(position).motavi);
            textView_SoTien.setText(DoiSoSangTien(arr.get(position).sotienvi));
        }
        return view;
    }

    public static String DoiSoSangTien(double so) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        return decimalFormat.format((so)) + " đ";
    }
}
