package com.example.doantotnghiep.ui.QuanLyVi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.doantotnghiep.R;
import com.example.doantotnghiep.adapter.AdapterLichSuChuyenTien;
import com.example.doantotnghiep.model.ArrayLichSuChuyenTien;
import com.example.doantotnghiep.model.ArrayVi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LichSuChuyenTienActivity extends AppCompatActivity {
    private String taikhoan;
    private SharedPreferences sharedPreferences;
    private ArrayList<ArrayLichSuChuyenTien> arrayLichSuChuyenTien;
    private AdapterLichSuChuyenTien adapterLichSuChuyenTien;
    private ImageButton imageButton_TroVeQuanLyVi;
    private Spinner spinner_LocLichSuChuyenTienTheoVi;
    private ListView listView_LichSuChuyenTien;
    private Date date;
    private SimpleDateFormat simpleDateFormatDialog;
    private SQLiteDatabase data;
    private ArrayList<Integer> arrMaVi;
    private ArrayList<String> arrTenVi;
    private ArrayAdapter adapterVi;
    private List<ArrayVi> listVi = null;
    private boolean danhsachchuyentien = false;
    private TextView textView_DanhSachChuyenTienTrong;
    private RadioButton radioButton_TatCaLichSuChuyenTien, radioButton_LocLichSuChuyenTienTheoNgay, radioButton_LocLichSuChuyenTienTheoVi;
    private TextView textView_ChonNgayLocChuyenTien;
    private RadioGroup radioGroup_LichSuChuyenTien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichsuchuyentien);
        data = openOrCreateDatabase("data.db", MODE_PRIVATE, null);
        simpleDateFormatDialog = new SimpleDateFormat("dd/MM/yyyy");

        sharedPreferences = getSharedPreferences("tendangnhap", Context.MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("taikhoancanchuyen","khong tim thay");

        AnhXa();
        TroVeQuanLyVi();
        ChonNgayLocLichSuChuyenTien();
        setListView();
        LoadTatCaLichSuChuyenTien();
        XuLyKhiDanhSachChuyenTienTrong(danhsachchuyentien);
    }

    public void AnhXa() {
        imageButton_TroVeQuanLyVi = (ImageButton) findViewById(R.id.imageButton_TroVeQuanLyVi);
        textView_ChonNgayLocChuyenTien = (TextView) findViewById(R.id.textView_ChonNgayLocChuyenTien);
        listView_LichSuChuyenTien = (ListView) findViewById(R.id.listView_LichSuChuyenTien);
        spinner_LocLichSuChuyenTienTheoVi = (Spinner) findViewById(R.id.spinner_LocLichSuChuyenTienTheoVi);
        textView_DanhSachChuyenTienTrong = (TextView) findViewById(R.id.textView_DanhSachChuyenTienTrong);
        radioButton_TatCaLichSuChuyenTien = (RadioButton) findViewById(R.id.radioButton_TatCaLichSuChuyenTien);
        radioButton_LocLichSuChuyenTienTheoVi = (RadioButton) findViewById(R.id.radioButton_LocLichSuChuyenTienTheoVi);
        radioButton_LocLichSuChuyenTienTheoNgay = (RadioButton) findViewById(R.id.radioButton_LocLichSuChuyenTienTheoNgay);
        radioGroup_LichSuChuyenTien = (RadioGroup) findViewById(R.id.radioGroup_LichSuChuyenTien);
        radioGroup_LichSuChuyenTien.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LocLichSuChuyenTienTheoRadioButton(group, checkedId);
            }
        });

        textView_ChonNgayLocChuyenTien.setVisibility(View.INVISIBLE);
        spinner_LocLichSuChuyenTienTheoVi.setVisibility(View.INVISIBLE);
    }

    public void TroVeQuanLyVi() {
        imageButton_TroVeQuanLyVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void ChonNgayLocLichSuChuyenTien() {
        textView_ChonNgayLocChuyenTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadLichDeChonNgayLocLichSuChuyenTien();
            }
        });
    }

    public void LocLichSuChuyenTienTheoRadioButton(RadioGroup radioGroup, int checkedID) {
        int checkedid = radioGroup.getCheckedRadioButtonId();

        textView_ChonNgayLocChuyenTien.setVisibility(View.INVISIBLE);
        spinner_LocLichSuChuyenTienTheoVi.setVisibility(View.INVISIBLE);
        if (checkedid == R.id.radioButton_TatCaLichSuChuyenTien) {
            LoadTatCaLichSuChuyenTien();
            textView_ChonNgayLocChuyenTien.setVisibility(View.INVISIBLE);
            spinner_LocLichSuChuyenTienTheoVi.setVisibility(View.INVISIBLE);

        } else if (checkedid == R.id.radioButton_LocLichSuChuyenTienTheoNgay) {
            LoadLichDeChonNgayLocLichSuChuyenTien();
            textView_ChonNgayLocChuyenTien.setVisibility(View.VISIBLE);
            spinner_LocLichSuChuyenTienTheoVi.setVisibility(View.INVISIBLE);

        } else if (checkedid == R.id.radioButton_LocLichSuChuyenTienTheoVi) {
            textView_ChonNgayLocChuyenTien.setVisibility(View.INVISIBLE);
            spinner_LocLichSuChuyenTienTheoVi.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoadTatCaVi();
                }
            }, 100);

            LoadDanhSachViLenSpinnerLocTheoVi();
        }
    }

    public void LoadLichDeChonNgayLocLichSuChuyenTien() {
        final Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chonngay);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        //Anh xa
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        Button button_ChonNgayXong = (Button) dialog.findViewById(R.id.button_ChonNgayXong);

        button_ChonNgayXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    date = simpleDateFormatDialog.parse(datePicker.getDayOfMonth() + "/" +(datePicker.getMonth() + 1) + "/" + datePicker.getYear());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                textView_ChonNgayLocChuyenTien.setText(simpleDateFormatDialog.format(date));
                dialog.cancel();
                LoadLichSuChuyenTienTheoNgay();
            }
        });
    }

    public void setListView() {
        arrayLichSuChuyenTien = new ArrayList<ArrayLichSuChuyenTien>();
        adapterLichSuChuyenTien = new AdapterLichSuChuyenTien(this, R.layout.adapter_lichsuchuyentien_item, arrayLichSuChuyenTien);
        listView_LichSuChuyenTien.setAdapter(adapterLichSuChuyenTien);
    }

    public void LoadLichSuChuyenTienTheoNgay() {
        arrayLichSuChuyenTien.clear();
        Cursor cursor = data.rawQuery("select malichsuchuyentien, tenvichuyen, tenvinhan, sotienchuyen, ngaythuchien" +
                " from tbllichsuchuyentien where tbllichsuchuyentien.tentaikhoan = '" + taikhoan + "' " +
                " and ngaythuchien = '" + simpleDateFormatDialog.format(date) + "' ", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            arrayLichSuChuyenTien.add(new ArrayLichSuChuyenTien(cursor.getString(cursor.getColumnIndex("ngaythuchien")), cursor.getString(cursor.getColumnIndex("tenvichuyen")), cursor.getString(cursor.getColumnIndex("tenvinhan")), cursor.getInt(cursor.getColumnIndex("malichsuchuyentien")), cursor.getInt(cursor.getColumnIndex("sotienchuyen"))));
            cursor.moveToNext();
        }
        cursor.close();
        adapterLichSuChuyenTien.notifyDataSetChanged();
    }

    public void LoadTatCaLichSuChuyenTien() {
        arrayLichSuChuyenTien.clear();
        Cursor cursor = data.rawQuery("select malichsuchuyentien, tenvichuyen, tenvinhan, sotienchuyen, ngaythuchien" +
                " from tbllichsuchuyentien where tbllichsuchuyentien.tentaikhoan = '" + taikhoan + "' ", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            arrayLichSuChuyenTien.add(new ArrayLichSuChuyenTien(cursor.getString(cursor.getColumnIndex("ngaythuchien")), cursor.getString(cursor.getColumnIndex("tenvichuyen")), cursor.getString(cursor.getColumnIndex("tenvinhan")), cursor.getInt(cursor.getColumnIndex("malichsuchuyentien")), cursor.getInt(cursor.getColumnIndex("sotienchuyen"))));
            cursor.moveToNext();
        }
        cursor.close();
        adapterLichSuChuyenTien.notifyDataSetChanged();
    }

    public void LoadDanhSachViLenSpinnerLocTheoVi() {
        arrMaVi = new ArrayList<Integer>();
        arrTenVi = new ArrayList<String>();
        adapterVi= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrTenVi);
        adapterVi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_LocLichSuChuyenTienTheoVi.setAdapter(adapterVi);
        spinner_LocLichSuChuyenTienTheoVi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadLichSuChuyenTienTheoSpinnerLocTheoVi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void LoadTatCaVi() {
        arrMaVi.clear();
        arrTenVi.clear();
        Cursor cursor = data.rawQuery("select * from tblvi where tentaikhoan = '" + taikhoan +"'", null);
        cursor.moveToFirst();
        listVi = new ArrayList<ArrayVi>();
        while (cursor.isAfterLast() == false) {
            arrMaVi.add(cursor.getInt(cursor.getColumnIndex("mavi")));
            arrTenVi.add(cursor.getString(cursor.getColumnIndex("tenvi")));
            cursor.moveToNext();
        }
        adapterVi.notifyDataSetChanged();
    }

    public void LoadLichSuChuyenTienTheoSpinnerLocTheoVi() {
        arrayLichSuChuyenTien.clear();
        Cursor cursor = data.rawQuery("select malichsuchuyentien, tenvichuyen, tenvinhan, sotienchuyen, ngaythuchien" +
                " from tbllichsuchuyentien where tbllichsuchuyentien.tentaikhoan = '" + taikhoan + "' " +
                " and tenvichuyen = '" + spinner_LocLichSuChuyenTienTheoVi.getSelectedItem().toString() + "' ", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            arrayLichSuChuyenTien.add(new ArrayLichSuChuyenTien(cursor.getString(cursor.getColumnIndex("ngaythuchien")), cursor.getString(cursor.getColumnIndex("tenvichuyen")), cursor.getString(cursor.getColumnIndex("tenvinhan")), cursor.getInt(cursor.getColumnIndex("malichsuchuyentien")), cursor.getInt(cursor.getColumnIndex("sotienchuyen"))));
            cursor.moveToNext();
        }
        cursor.close();
        adapterLichSuChuyenTien.notifyDataSetChanged();
    }

    public void XuLyKhiDanhSachChuyenTienTrong(boolean danhsachchuyentien) {
        this.danhsachchuyentien = danhsachchuyentien;
        if (danhsachchuyentien) {
            textView_DanhSachChuyenTienTrong.setVisibility(View.VISIBLE);
        } else {
            if (adapterLichSuChuyenTien.getCount() <= 0) {
                textView_DanhSachChuyenTienTrong.setVisibility(View.VISIBLE);
            } else {
                textView_DanhSachChuyenTienTrong.setVisibility(View.GONE);
            }
        }
    }
}
