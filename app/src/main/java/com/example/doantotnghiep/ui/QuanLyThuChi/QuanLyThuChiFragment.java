package com.example.doantotnghiep.ui.QuanLyThuChi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aldoapps.autoformatedittext.AutoFormatEditText;
import com.example.doantotnghiep.R;
import com.example.doantotnghiep.adapter.AdapterThongKe;
import com.example.doantotnghiep.adapter.AdapterThuChi;
import com.example.doantotnghiep.model.ArrayThongKe;
import com.example.doantotnghiep.model.ArrayThuChi;
import com.example.doantotnghiep.model.ArrayVi;
import com.example.doantotnghiep.thongbao.ThongBaoThuChiReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QuanLyThuChiFragment extends Fragment{
    View myFragment;
    private Activity activity;
    private SQLiteDatabase data;
    private Animation animation;
    private ImageButton imageButton_ThemThuChi;
    private Button button_ThoatThuChiDialog, button_LuuThuChiDialog, button_NgayThuChiDialog,
            button_ThoiGianHienTaiDialog, button_GioThuChiDialog,
            button_ThoatThuChiDialogSua,button_LuuThuChiDialogSua,button_NgayThuChiDialogSua,button_GioThuChiDialogSua,button_ThoiGianHienTaiDialogSua;
    private String taikhoan;
    private Spinner spinner_LoaiThuChiDialog, spinner_ViDialog, spinner_DanhMucDialog,
                    spinner_LoaiThuChiDialogSua,spinner_ViDialogSua,spinner_DanhMucDialogSua;
    private String ngaythang;
    private Calendar today, calendar;
    private int thang, nam, vitrils = 0;
    private ArrayList<ArrayThongKe> arrthu, arrchi;
    private String[] arrGroup;
    private ListView listView_LichSuThuChi;
    private AdapterThongKe adapterThongKe;
    private ArrayList<ArrayThuChi> arr;
    private AdapterThuChi adapterThuChi;
    private SharedPreferences sharedPreferences;
    private List<ArrayThuChi> list;
    private ArrayList<String> arrSpinner;
    private ArrayAdapter<String> adapterSpinner;
    private boolean danhsachthuchi = false;
    private TextView textView_DanhSachThuChiTrong, textView_ChonNgayLocThuChi;
    private RadioGroup radioGroup_ThuChi;
    private RadioButton radioButton_TatCaThuChi;

    //DialogThem
    private EditText editText_MoTaThuChiDialog,editText_MoTaThuChiDialogSua;
    private SimpleDateFormat simpleDateFormatDialog;
    private Date date;
    private ArrayList<Integer> arrMaViDialog, arrMaDanhMucDialog;
    private ArrayList<String> arrTenViDialog, arrTenDanhMucDialog;
    private ArrayAdapter<String> adapterSpinnerDialog, adapterViDialog, adapterDanhMucDialog;
    private String[] arrSpinnerDialog;
    private List<ArrayVi> listDialog = null;
    private String gioDialog, tenVi, tenviuutien ;
    private Cursor cursor;
    private int  vitri, maviuutien, giothongbao, phutthongbao, nhanthongbao;
    private EditText editText_SoTienThuChiDialog,editText_SoTienThuChiDialogSua;
    private Double sotientuvi,sotienthuchi,sotienchi;
    //Dung cho thong bao
    //private Time time;
    private SimpleDateFormat simpleTimeFormat;
    //private DateFormat dateFormat;
    private int gio, phut, gioSua, phutSua, madanhmucht;
    private CheckBox check_thongbao, check_thongbaoSua, check_hangngay;
    private Double sotiennhapthem;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_quanlythuchi, container, false);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        data = activity.openOrCreateDatabase("data.db", activity.MODE_PRIVATE, null);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);
        simpleDateFormatDialog = new SimpleDateFormat("dd/MM/yyyy");
        simpleTimeFormat = new SimpleDateFormat("HH/mm");

        date = new Date();

        sharedPreferences = getActivity().getSharedPreferences("tendangnhap", Context.MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("taikhoancanchuyen","khong tim thay");

        AnhXa();
        ThemThuChi();
        setListview();
        LoadTatCaThuChi();
        XuLyKhiDanhSachThuChiTrong(danhsachthuchi);
        ChonNgayLocThuChi();
        //KiemTraThoiGianThongBao();

        listView_LichSuThuChi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                vitrils = position;
                return false;
            }
        });
        registerForContextMenu(listView_LichSuThuChi);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        activity.getMenuInflater().inflate(R.menu.menu_thuchi, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_CapNhatThuChi: {
                ShowDialogCapNhat();
                return true;
            }
            case R.id.option_XoaThuChi: {
                XoaThuChi(vitrils);
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }

    }



    public void AnhXa() {
        imageButton_ThemThuChi =  myFragment.findViewById(R.id.imageButton_ThemThuChi);
        listView_LichSuThuChi =  myFragment.findViewById(R.id.listView_LichSuThuChi);
        textView_ChonNgayLocThuChi =  myFragment.findViewById(R.id.textView_ChonNgayLocThuChi);
        textView_DanhSachThuChiTrong =  myFragment.findViewById(R.id.textView_DanhSachThuChiTrong);
        radioButton_TatCaThuChi = myFragment.findViewById(R.id.radioButton_TatCaThuChi);
        radioGroup_ThuChi =  myFragment.findViewById(R.id.radioGroup_ThuChi);
        radioGroup_ThuChi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LocThuChiTheoRadioButton(group, checkedId);
            }
        });
        textView_ChonNgayLocThuChi.setVisibility(View.INVISIBLE);
    }

    //Ham hien thi them thu chi dialog
    public void ThemThuChi() {
        imageButton_ThemThuChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);
                final Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_themthuchi);
                d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                d.show();

                //AnhXa
                button_ThoatThuChiDialog = (Button) d.findViewById(R.id.button_ThoatThuChi);
                button_LuuThuChiDialog = (Button) d.findViewById(R.id.button_LuuThuChi);
                button_NgayThuChiDialog = (Button) d.findViewById(R.id.button_NgayThuChi);
                button_GioThuChiDialog = (Button) d.findViewById(R.id.button_GioThuChi);
                button_ThoiGianHienTaiDialog = (Button) d.findViewById(R.id.button_ThoiGianHienTai);
                check_thongbao = d.findViewById(R.id.checkThongBao);
                check_hangngay =d.findViewById(R.id.checkBox_HangNgay);
                editText_SoTienThuChiDialog = (EditText) d.findViewById(R.id.editText_SoTienThuChi);
                editText_MoTaThuChiDialog = (EditText) d.findViewById(R.id.editText_MoTaThuChi);

                spinner_LoaiThuChiDialog = (Spinner) d.findViewById(R.id.spinner_LoaiThuChi);
                spinner_ViDialog = (Spinner) d.findViewById(R.id.spinner_Vi);
                spinner_DanhMucDialog = (Spinner) d.findViewById(R.id.spinner_DanhMuc);

                calendar = Calendar.getInstance();

                //Xu ly
                HienThiThoiGian();
                HienThiGio();
                LoadSpinnerDialog();
                LoadDanhSachViLenSpinnerDialog();

                //KiemTraChiTieuMax();
                editText_SoTienThuChiDialog.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b && editText_SoTienThuChiDialog.getText().toString()!= null) {

                            if(editText_SoTienThuChiDialog.getText().toString().equals("")){
                              editText_SoTienThuChiDialog.startAnimation(animation);
                              Toast.makeText(activity, "Bạn chưa nhập số tiền", Toast.LENGTH_SHORT).show();
                            }else {
                                KiemTraChiTieuMax();
                                GioiHanSoTienKhiChuyen();
                            }

                          //sotiennhapthem = Double.parseDouble(editText_SoTienThuChiDialog.getText().toString());
                        }
                    }
                });

                //Chon vi uu tien
                spinner_DanhMucDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        vitri = position ;
                        LayViUuTienTheoDanhMuc();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                button_ThoatThuChiDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                //Hien thi Date picker
                button_NgayThuChiDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChonNgayThemThuChi();
                    }
                });
                //Hien thi Time picker
                button_GioThuChiDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChonGio();
                    }
                });

                button_ThoiGianHienTaiDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HienThiThoiGian();

                    }
                });
                // Alert button
                button_LuuThuChiDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (editText_SoTienThuChiDialog.getText().toString().equals("")) {
                            editText_SoTienThuChiDialog.startAnimation(animation);
                            Toast.makeText(activity, "Bạn chưa nhập số tiền", Toast.LENGTH_SHORT).show();
                        } else if (editText_MoTaThuChiDialog.getText().toString().equals("")) {
                            editText_MoTaThuChiDialog.startAnimation(animation);
                            Toast.makeText(activity, "Bạn chưa nhập mô tả", Toast.LENGTH_SHORT).show();
                        } else if(check_thongbao.isChecked() && !KiemTraThongBao()){

                            check_thongbao.startAnimation(animation);
                            check_thongbao.setChecked(false);
                            Toast.makeText(activity, "Đã có một thu chi đang chờ thông báo", Toast.LENGTH_SHORT).show();
                        }else if(!GioiHanSoTien()){
                            editText_SoTienThuChiDialog.startAnimation(animation);
                            editText_SoTienThuChiDialog.setText(String.valueOf(0));
                            Toast.makeText(activity,"Số tiền nhập quá lớn",Toast.LENGTH_SHORT).show();
                        }else if(!KiemTraChiTieuMaxLuu()){
                            editText_SoTienThuChiDialog.setText(String.valueOf(sotientuvi).replace(".0", ""));
                            editText_SoTienThuChiDialog.startAnimation(animation);
                            Toast.makeText(activity,"Số tiền chi vượt quá số tiền ví",Toast.LENGTH_SHORT).show();
                        }else if(check_hangngay.isChecked() && check_thongbao.isChecked()){ //moi
                            check_thongbao.startAnimation(animation);
                            check_thongbao.setChecked(false);
                            check_hangngay.startAnimation(animation);
                            check_hangngay.setChecked(false);
                            Toast.makeText(activity,"Không thể chọn cả hai",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            ThemThuChiDialog();
                            startAlarm();
                            //LayThongTinThongBao();
                            LoadTatCaThuChi();
                            radioButton_TatCaThuChi.setChecked(true);

                            d.dismiss();
                        }
                    }
                });
            }
        });
    }


    //Xu ly chon vi uu tien
    public void LayViUuTienTheoDanhMuc() {
        String tendanhmuc = arrTenDanhMucDialog.get(vitri);
        cursor = data.rawQuery("select * from tbldanhmucthuchi where tentaikhoan ='"+taikhoan+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals(tendanhmuc)) {
               tenviuutien = cursor.getString(cursor.getColumnIndex("tenviuutien"));
            }
            cursor.moveToNext();
        }
        spinner_ViDialog.setSelection(LayViTriUuTien(spinner_ViDialog,tenviuutien));

    }

    public int LayViTriUuTien(Spinner spinnerVi,String tenvican){
        for (int i = 0;i < spinnerVi.getCount();i++){
            if(spinnerVi.getItemAtPosition(i).toString().equalsIgnoreCase(tenvican)){
                return i;
            }
        }
        return 0;
    }

    //Dialog Them Thu Chi
    public void LoadSpinnerDialog() {
        //Spinner Danh muc
        arrMaDanhMucDialog = new ArrayList<Integer>();
        arrTenDanhMucDialog = new ArrayList<String>();
        adapterDanhMucDialog = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrTenDanhMucDialog);
        adapterDanhMucDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_DanhMucDialog.setAdapter(adapterDanhMucDialog);

        //Spinner Loai thu chi
        arrSpinnerDialog = getResources().getStringArray(R.array.loaithuchi);
        adapterSpinnerDialog = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrSpinnerDialog);
        adapterSpinnerDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_LoaiThuChiDialog.setAdapter(adapterSpinnerDialog);
        spinner_LoaiThuChiDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadDanhSachDanhMucLenSpinnerDialog();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner Vi
        arrMaViDialog = new ArrayList<Integer>();
        arrTenViDialog = new ArrayList<String>();
        adapterViDialog = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrTenViDialog);
        adapterViDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ViDialog.setAdapter(adapterViDialog);
    }

    public void LoadDanhSachDanhMucLenSpinnerDialog() {
        arrMaDanhMucDialog.clear();
        arrTenDanhMucDialog.clear();
        //Cursor cursor = data.rawQuery("select madanhmuc, tendanhmuc from tbldanhmucthuchi where loaikhoan = '" + spinner_LoaiThuChiDialog.getSelectedItem().toString() + "'"+ "and tentaikhoan = '"+ taikhoan + "'", null);
        Cursor cursor = data.rawQuery("select * from tbldanhmucthuchi where tentaikhoan = '" + taikhoan + "' and loaikhoan = '" + spinner_LoaiThuChiDialog.getSelectedItem().toString() + "' ", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            arrMaDanhMucDialog.add(cursor.getInt(cursor.getColumnIndex("madanhmuc")));
            arrTenDanhMucDialog.add(cursor.getString(cursor.getColumnIndex("tendanhmuc")));
            cursor.moveToNext();
        }
        adapterDanhMucDialog.notifyDataSetChanged();
    }

    public void LoadDanhSachViLenSpinnerDialog() {
        arrMaViDialog.clear();
        arrTenViDialog.clear();
        Cursor cursor = data.rawQuery("select * from tblvi where tentaikhoan = '" + taikhoan +"'", null);
        cursor.moveToFirst();
        listDialog = new ArrayList<ArrayVi>();
        while (!cursor.isAfterLast()) {
            arrMaViDialog.add(cursor.getInt(cursor.getColumnIndex("mavi")));
            arrTenViDialog.add(cursor.getString(cursor.getColumnIndex("tenvi")));
            cursor.moveToNext();
        }
        adapterViDialog.notifyDataSetChanged();
        cursor.close();
    }

    //Date Picker
    public void ChonNgayThemThuChi() {
        final Dialog dialog = new Dialog(activity, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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
                button_NgayThuChiDialog.setText(simpleDateFormatDialog.format(date));
                dialog.cancel();
            }
        });
    }

    public void LoadLichDeChonNgayLocThuChi() {
        final Dialog dialog = new Dialog(activity, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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
                textView_ChonNgayLocThuChi.setText(simpleDateFormatDialog.format(date));
                dialog.cancel();
                LoadThuChiTheoNgay();
            }
        });
    }

    // Lay thoi gian hien tai
    public void HienThiThoiGian() {
        int thang = calendar.get(Calendar.MONTH) - 1;
        gioDialog = calendar.get(Calendar.HOUR_OF_DAY) + ":"+ calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        date = calendar.getTime();
        button_NgayThuChiDialog.setText(simpleDateFormatDialog.format(date));
        HienThiGio();
    }

    //Time Picker
    public void ChonGio(){
        final Calendar calendarGio = Calendar.getInstance();
        int gio1 = calendarGio.get(Calendar.HOUR_OF_DAY);
        int phut1 = calendarGio.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(activity,R.style.Theme_AppCompat_DayNight_Dialog_Alert,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                        Calendar laygio = Calendar.getInstance();
//                        laygio.set(Calendar.HOUR_OF_DAY,i);
//                        laygio.set(Calendar.MINUTE,i1);
//                        laygio.set(Calendar.SECOND,0);
//                        startAlarm(laygio);
                        gio = i;
                        phut = i1;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        calendarGio.set(0,0,0,i,i1); //i la gio, i1 la phut
                        button_GioThuChiDialog.setText(simpleDateFormat.format(calendarGio.getTime()));

                    }
                }, gio1, phut1, true);
        timePickerDialog.show();

    }

    //Hien thi thoi gian hien tai tao thu chi khi chua click time picker
    public void HienThiGio(){
        final Calendar calendarGio = Calendar.getInstance();
        gio = calendarGio.get(Calendar.HOUR_OF_DAY);
        phut = calendarGio.get(Calendar.MINUTE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        calendarGio.set(0,0,0,gio, phut); //i la gio, i1 la phut
        button_GioThuChiDialog.setText(simpleDateFormat.format(calendarGio.getTime()));
    }

    //Ham xu ly chinh them thu chi xuong database
    public boolean ThemThuChiDialog() {
        int mathuchi = 1;
        //int sotienthuchi = 0;
        cursor = data.rawQuery("select mathuchi from tblthuchi", null);
        if (cursor.moveToLast() == true) {
            mathuchi = cursor.getInt(cursor.getColumnIndex("mathuchi")) + 1;
        }
        String thongbao = "";
        //Them vao bang thu chi
        ContentValues values = new ContentValues();
        values.put("mathuchi", mathuchi);
        values.put("mota", editText_MoTaThuChiDialog.getText().toString());
        values.put("loaithuchi", spinner_LoaiThuChiDialog.getSelectedItem().toString());
        values.put("sotienthuchi", editText_SoTienThuChiDialog.getText().toString());
        values.put("mavi", arrMaViDialog.get(spinner_ViDialog.getSelectedItemPosition()));
        values.put("ngaythuchien", simpleDateFormatDialog.format(date));
        values.put("giothuchi", gio);
        values.put("phutthuchi", phut);
        if (check_thongbao.isChecked()){
            values.put("nhanthongbao",1);
        }else {
            values.put("nhanthongbao",0);
        }
        if (check_hangngay.isChecked()){ //moi
            values.put("hangngay",1);
        }else {
            values.put("hangngay",0);
        }
        values.put("madanhmuc", arrMaDanhMucDialog.get(spinner_DanhMucDialog.getSelectedItemPosition()));
        values.put("tentaikhoan", taikhoan);

        if (data.insert("tblthuchi", null, values) == -1) {
            return false;
        }

        //startAlarm(date);
        LoadTatCaThuChi();
        TinhSoDu();
        thongbao = "Lưu thành công";
        Toast.makeText(activity, thongbao, Toast.LENGTH_SHORT).show();

        textView_ChonNgayLocThuChi.setText("Chọn ngày");

        return true;
    }

    //Kiem tra gioi han nhan thong bao
    public boolean KiemTraThongBao(){
        int count = 0;
        cursor = data.rawQuery("select nhanthongbao from tblthuchi",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getInt(cursor.getColumnIndex("nhanthongbao"))==1){
               count++;
            }
            cursor.moveToNext();
        }
        if (count == 1){
            return false;
        }
        return true;
    }

    //Kiem tra xem chi co vuot qua so tien co trong vi hay khong
    public void KiemTraChiTieuMax() {
        int mavithem = arrMaViDialog.get(spinner_ViDialog.getSelectedItemPosition());
        Double sotientuvikt =0.0;
        Cursor cursor =  data.rawQuery("select * from tblvi where tentaikhoan ='"+taikhoan+"'" ,null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (cursor.getInt(cursor.getColumnIndex("mavi")) == mavithem) {
                sotientuvikt = cursor.getDouble(cursor.getColumnIndex("sotienvi"));
            }
            cursor.moveToNext();
        }
        if (spinner_LoaiThuChiDialog.getSelectedItem().toString().equals("Khoản chi")) {
            Double sotienchi = Double.parseDouble(editText_SoTienThuChiDialog.getText().toString());
            if (sotientuvikt < sotienchi) {
                editText_SoTienThuChiDialog.setText(String.valueOf(sotientuvikt).replace(".0", ""));
                editText_SoTienThuChiDialog.startAnimation(animation);
                Toast.makeText(activity,"Số tiền chi vượt quá số tiền ví",Toast.LENGTH_SHORT).show();

            }
        }


    }

    public boolean KiemTraChiTieuMaxLuu() {
        int mavithem = arrMaViDialog.get(spinner_ViDialog.getSelectedItemPosition());
       // Double sotientuvikt =0.0;
        Cursor cursor =  data.rawQuery("select * from tblvi where tentaikhoan ='"+taikhoan+"'" ,null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (cursor.getInt(cursor.getColumnIndex("mavi")) == mavithem) {
                sotientuvi = cursor.getDouble(cursor.getColumnIndex("sotienvi"));
            }
            cursor.moveToNext();
        }
        if (spinner_LoaiThuChiDialog.getSelectedItem().toString().equals("Khoản chi")) {
            Double sotienchi = Double.parseDouble(editText_SoTienThuChiDialog.getText().toString());
            if (sotientuvi < sotienchi) {
                return false;
            }
        }
        return true;

    }


    public void TinhSoDu() {
        Double sodu = 0.0;
        int mavithem = arrMaViDialog.get(spinner_ViDialog.getSelectedItemPosition());
        Double sotientuvitinh =0.0;
        //Tinh
        Double sotienthuchikt;
        Cursor cursor =  data.rawQuery("select * from tblvi where tentaikhoan ='"+taikhoan+"'" ,null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (cursor.getInt(cursor.getColumnIndex("mavi")) == mavithem) {
                sotientuvitinh = cursor.getDouble(cursor.getColumnIndex("sotienvi"));
            }
            cursor.moveToNext();
        }
        if (spinner_LoaiThuChiDialog.getSelectedItem().toString().equals("Khoản thu")) {
            sotienthuchikt = Double.parseDouble(editText_SoTienThuChiDialog.getText().toString());
        }
        else {
            sotienthuchikt = -Double.parseDouble(editText_SoTienThuChiDialog.getText().toString());
        }
        sodu = sotientuvitinh + sotienthuchikt;

        // gan lai
        ContentValues values1 = new ContentValues();
        values1.put("sotienvi",sodu);
        data.update("tblvi",values1,"mavi = " + mavithem,null);
    }


    //Thu Chi Fragment
    public void setListview() {
        arr = new ArrayList<ArrayThuChi>();
        adapterThuChi = new AdapterThuChi(getActivity(), R.layout.adapter_thuchi_item, arr);
        listView_LichSuThuChi.setAdapter(adapterThuChi);
    }

    // Load thu chi theo ngay hien tai
    public void LoadThuChiTheoNgay() { //moi
        arr.clear();
        String thongbao;
        Cursor cursor = data.rawQuery("select tblthuchi.mathuchi, tblthuchi.ngaythuchien, tblthuchi.sotienthuchi, tbldanhmucthuchi.tendanhmuc, tblvi.tenvi, loaikhoan, mota, tblthuchi.nhanthongbao " +
                " from tblthuchi inner join tbldanhmucthuchi on tblthuchi.madanhmuc = tbldanhmucthuchi.madanhmuc " +
                " inner join tblvi on tbldanhmucthuchi.tentaikhoan = tblvi.tentaikhoan " +
                " where tblthuchi.tentaikhoan = '" + taikhoan + "' and tblvi.mavi = tblthuchi.mavi and tblthuchi.ngaythuchien = '" + simpleDateFormatDialog.format(date) + "' ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(cursor.getInt(cursor.getColumnIndex("nhanthongbao"))==1){
                thongbao = "Đang đặt thông báo";
            }else {
                thongbao = "";
            }
            arr.add(new ArrayThuChi(cursor.getString(cursor.getColumnIndex("ngaythuchien")), cursor.getString(cursor.getColumnIndex("tendanhmuc")),
                    cursor.getString(cursor.getColumnIndex("tenvi")), cursor.getInt(cursor.getColumnIndex("sotienthuchi")), cursor.getInt(cursor.getColumnIndex("mathuchi")), thongbao, cursor.getString(cursor.getColumnIndex("loaikhoan")), cursor.getString(cursor.getColumnIndex("mota"))));
            cursor.moveToNext();
        }
        cursor.close();
        adapterThuChi.notifyDataSetChanged();
    }

    //Load thong tin thu chi len list view theo chon tat ca
    public void LoadTatCaThuChi() { // moi
        arr.clear();
        String thongbao;
        Cursor cursor = data.rawQuery("select tblthuchi.mathuchi, tblthuchi.ngaythuchien, tblthuchi.sotienthuchi, tbldanhmucthuchi.tendanhmuc, tblvi.tenvi, loaikhoan, mota, tblthuchi.nhanthongbao " +
                " from tblthuchi inner join tbldanhmucthuchi on tblthuchi.madanhmuc = tbldanhmucthuchi.madanhmuc " +
                " inner join tblvi on tbldanhmucthuchi.tentaikhoan = tblvi.tentaikhoan " +
                " where tblthuchi.tentaikhoan = '" + taikhoan + "' and tblvi.mavi = tblthuchi.mavi", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(cursor.getInt(cursor.getColumnIndex("nhanthongbao"))==1){
                thongbao = "Đang đặt thông báo";
            }else {
                thongbao = "";
            }
            arr.add(new ArrayThuChi(cursor.getString(cursor.getColumnIndex("ngaythuchien")), cursor.getString(cursor.getColumnIndex("tendanhmuc")), cursor.getString(cursor.getColumnIndex("tenvi")), cursor.getInt(cursor.getColumnIndex("sotienthuchi")), cursor.getInt(cursor.getColumnIndex("mathuchi")),thongbao, cursor.getString(cursor.getColumnIndex("loaikhoan")), cursor.getString(cursor.getColumnIndex("mota"))));
            cursor.moveToNext();
        }
        cursor.close();
        adapterThuChi.notifyDataSetChanged();
    }

    //Chuc nang xoa thu chi
    public void XoaThuChi(int vitri) {
         HamXoaThuChi(vitri);
    }

    public void HamXoaThuChi(final int mathuchi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Thông báo !");
        builder.setMessage("Bạn có chắc chắn muốn xóa khoản này ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.rawQuery("delete from tblthuchi where mathuchi = '" + arr.get(mathuchi).ma + "'", null).moveToFirst();
                Toast.makeText(activity, "Xóa thành công", Toast.LENGTH_SHORT).show();

                //Delay ham xuat du lieu de tranh bi crash app
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //LoadThuChiTheoNgay();
                        LoadTatCaThuChi();
                    }
                }, 100 );
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //Xu ly khi khong co thu chi de hien thi
    public void XuLyKhiDanhSachThuChiTrong(boolean danhsachthuchi) {
        this.danhsachthuchi = danhsachthuchi;
        if (danhsachthuchi) {
            textView_DanhSachThuChiTrong.setVisibility(View.VISIBLE);
        } else {
            if (adapterThuChi.getCount() <= 0) {
                textView_DanhSachThuChiTrong.setVisibility(View.VISIBLE);
            } else {
                textView_DanhSachThuChiTrong.setVisibility(View.GONE);
            }
        }
    }

    //Loc thu chi
    public void LocThuChiTheoRadioButton(RadioGroup radioGroup, int checkedID) {
        int checkedid = radioGroup.getCheckedRadioButtonId();

        textView_ChonNgayLocThuChi.setVisibility(View.INVISIBLE);
        if (checkedid == R.id.radioButton_TatCaThuChi) {
            LoadTatCaThuChi();
            textView_ChonNgayLocThuChi.setVisibility(View.INVISIBLE);
        } else if (checkedid == R.id.radioButton_LocThuChiTheoNgay) {
            LoadLichDeChonNgayLocThuChi();
            textView_ChonNgayLocThuChi.setVisibility(View.VISIBLE);
        }
    }

    public void ChonNgayLocThuChi() {
        textView_ChonNgayLocThuChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadLichDeChonNgayLocThuChi();
            }
        });
    }

    //Dung de thong bao
    public void startAlarm(){

        String taikhoanthongbao;
        String ngaythongbao;

        cursor = data.rawQuery("select * from tblthuchi",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getInt(cursor.getColumnIndex("nhanthongbao"))==1){
                //Chuyen ten tai khoan qua thong bao
                taikhoanthongbao = cursor.getString(cursor.getColumnIndex("tentaikhoan"));
                SharedPreferences sharedPreferences = activity.getSharedPreferences("tendangnhapTB",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString("taikhoancanchuyentb",taikhoanthongbao);
                editor.commit();

                ngaythongbao = cursor.getString(cursor.getColumnIndex("ngaythuchien"));
                int giothuchien = cursor.getInt(cursor.getColumnIndex("giothuchi"));
                int phutthuchien = cursor.getInt(cursor.getColumnIndex("phutthuchi"));
                String [] dateParts = ngaythongbao.split("/");
                String ngay = dateParts[0];
                String thang = dateParts[1];
                String nam = dateParts[2];

                int songay = Integer.parseInt(ngay);
                int sothang = Integer.parseInt(thang);
                int sonam = Integer.parseInt(nam);

                Calendar calendarTB = Calendar.getInstance();
                calendarTB.set(Calendar.DAY_OF_MONTH,songay);
                calendarTB.set(Calendar.MONTH,sothang - 1);
                calendarTB.set(Calendar.YEAR,sonam);
                calendarTB.set(Calendar.HOUR_OF_DAY,giothuchien);
                calendarTB.set(Calendar.MINUTE,phutthuchien);
                calendarTB.set(Calendar.SECOND,0);


                AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(activity, ThongBaoThuChiReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 1, intent, 0);
                alarmManager.setExact(AlarmManager.RTC, calendarTB.getTimeInMillis(), pendingIntent);
            }


            cursor.moveToNext();
        }

    }


    //Chuc nang sua thu chi
    //Show Dialog cap nhat thu chi
    public void ShowDialogCapNhat(){
        final Dialog d = new Dialog(getContext());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_capnhatthuchi);
        d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        d.show();

        //Anh xa trong dialog
        button_ThoatThuChiDialogSua =  d.findViewById(R.id.button_ThoatThuChiSua);
        button_LuuThuChiDialogSua =  d.findViewById(R.id.button_LuuThuChiSua);
        button_NgayThuChiDialogSua =  d.findViewById(R.id.button_NgayThuChiSua);
        button_GioThuChiDialogSua = d.findViewById(R.id.button_GioThuChiSua);
        button_ThoiGianHienTaiDialogSua =  d.findViewById(R.id.button_ThoiGianHienTaiSua);
        check_thongbaoSua = d.findViewById(R.id.checkBox_ThongBaoSua);
        editText_SoTienThuChiDialogSua =  d.findViewById(R.id.editText_SoTienThuChiSua);
        editText_MoTaThuChiDialogSua =  d.findViewById(R.id.editText_MoTaThuChiSua);

        spinner_LoaiThuChiDialogSua =  d.findViewById(R.id.spinner_LoaiThuChiSua);
        spinner_ViDialogSua =  d.findViewById(R.id.spinner_ViThuChiSua);
        spinner_DanhMucDialogSua = d.findViewById(R.id.spinner_DanhMucSua);


        LoadSpinnerDialogSua();
        LoadDanhSachViLenSpinnerDialog();
        //LoadDanhSachDanhMucLenSpinnerDialogSua();
        //Xu ly
        if(LoadThucChi()){
            //Xu ly hien thi
            spinner_LoaiThuChiDialogSua.setEnabled(false);
            spinner_ViDialogSua.setEnabled(false);
            editText_SoTienThuChiDialogSua.setEnabled(false);

        }
        spinner_DanhMucDialogSua.setSelection(3);
//        String tendanhmucsua = LayTenDanhMucChon(madanhmucht);
//        spinner_DanhMucDialogSua.setSelection(LayViTriChuoiDanhMuc(spinner_DanhMucDialogSua,tendanhmucsua));
        //spinner_DanhMucDialogSua.setSelection(4);
        //HienThiThoiGianSua();
        //HienThiGioSua();


        //Xu ly nut
        button_GioThuChiDialogSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonGioSua();
            }
        });
        button_ThoiGianHienTaiDialogSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HienThiThoiGianSua();
            }
        });

        button_NgayThuChiDialogSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgayThemThuChiSua();
            }
        });

        button_ThoatThuChiDialogSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        button_LuuThuChiDialogSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_MoTaThuChiDialogSua.getText().toString().equals("")) {
                    editText_MoTaThuChiDialogSua.startAnimation(animation);
                    Toast.makeText(activity, "Bạn chưa nhập mô tả", Toast.LENGTH_SHORT).show();
                }else if(check_thongbaoSua.isChecked() && !KiemTraThongBaoSua()) {
                    check_thongbaoSua.startAnimation(animation);
                    check_thongbaoSua.setChecked(false);
                    Toast.makeText(activity, "Đã có thu chi đang chờ thông báo",Toast.LENGTH_SHORT).show();

                }else {
                    if(nhanthongbao == 1 && !check_thongbaoSua.isChecked()){
                        // chạy ham tat thong
                        TatThongBao();

                    }
                    //Ham cap nhat
                    CapNhatThuChi();
                    startAlarm();//chay lai theo thoi gian moi
                    d.dismiss();
                    LoadTatCaThuChi();
                }
            }
        });

    }
    //
    public void HienThiThoiGianSua() {
        Calendar calendar = Calendar.getInstance();
        int thang = calendar.get(Calendar.MONTH) - 1;
        String gioDialogSua = calendar.get(Calendar.HOUR_OF_DAY) + ":"+ calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        Date date = calendar.getTime();
        button_NgayThuChiDialogSua.setText(simpleDateFormatDialog.format(date));
        HienThiGioSua();
    }

    public void HienThiGioSua(){
        final Calendar calendarGio = Calendar.getInstance();
        gioSua = calendarGio.get(Calendar.HOUR_OF_DAY);
        phutSua = calendarGio.get(Calendar.MINUTE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        calendarGio.set(0,0,0,gioSua, phutSua); //i la gio, i1 la phut
        button_GioThuChiDialogSua.setText(simpleDateFormat.format(calendarGio.getTime()));
    }

    //Ham cap nhat
    public void CapNhatThuChi(){
        String thongbao ="";
        final int mathuchiht = arr.get(vitrils).ma;
        if (editText_MoTaThuChiDialogSua.getText().toString().equals("")) {
            editText_MoTaThuChiDialogSua.startAnimation(animation);
            Toast.makeText(activity,"Bạn chưa nhập mô tả thu chi",Toast.LENGTH_LONG).show();
        }else {
            ContentValues values = new ContentValues();
            values.put("madanhmuc",arrMaDanhMucDialog.get(spinner_DanhMucDialogSua.getSelectedItemPosition()));
            values.put("mota",editText_MoTaThuChiDialogSua.getText().toString());
            if(check_thongbaoSua.isChecked()){
                values.put("nhanthongbao",1);
            }else
            {
                values.put("nhanthongbao",0);
            }
            values.put("ngaythuchien",button_NgayThuChiDialogSua.getText().toString());
            values.put("giothuchi",gioSua);
            values.put("phutthuchi",phutSua);
            data.update("tblthuchi",values,"mathuchi = "+ mathuchiht,null);
            thongbao = "Cập nhật thành công";
            Toast.makeText(activity,thongbao,Toast.LENGTH_SHORT).show();
        }

    }

    //Tat thong bao bo trong ham CapNhatThuChi
    public void TatThongBao(){
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, ThongBaoThuChiReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
    //Dung de kiem tra khi check vao thong bao cua dialog cap nhat
    public boolean KiemTraThongBaoSua(){
        int count = 0;
        if(nhanthongbao == 1){
            return true;
        }
        cursor = data.rawQuery("select * from tblthuchi",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getInt(cursor.getColumnIndex("nhanthongbao"))==1){
                count++;
            }
            cursor.moveToNext();
        }
        if(count == 1){
            return false;
        }
        return true;
    }



   //Load du lieu cua thu chi
    public boolean LoadThucChi() {
       final int mathuchiht = arr.get(vitrils).ma;
       cursor = data.rawQuery("select* from tblthuchi where tentaikhoan ='"+taikhoan+"' and mathuchi ="+ mathuchiht,null);
       cursor.moveToFirst();
       String loaikhoanht = cursor.getString(cursor.getColumnIndex("loaithuchi"));
       if(loaikhoanht.equals("Khoản thu")){
           spinner_LoaiThuChiDialogSua.setSelection(0);
       }else {
           spinner_LoaiThuChiDialogSua.setSelection(1);
       }

       int maviht = cursor.getInt(cursor.getColumnIndex("mavi"));
       int madanhmucht= cursor.getInt(cursor.getColumnIndex("madanhmuc"));
       int sotienthuchi = cursor.getInt(cursor.getColumnIndex("sotienthuchi"));
       String motaht = cursor.getString(cursor.getColumnIndex("mota"));
       nhanthongbao = cursor.getInt(cursor.getColumnIndex("nhanthongbao"));
       String ngaythuchien = cursor.getString(cursor.getColumnIndex("ngaythuchien"));
       gioSua = cursor.getInt(cursor.getColumnIndex("giothuchi"));
       phutSua = cursor.getInt(cursor.getColumnIndex("phutthuchi"));

       String tenvisua = LayTenViChon(maviht);
       String tendanhmucsua = LayTenDanhMucChon(madanhmucht);
       String thoigianthuchi = gioSua + ":" + phutSua;
//       Date time = null;
//        try {
//            time = simpleTimeFormat.parse(thoigianthuchi);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //Set len dialog
       spinner_ViDialogSua.setSelection(LayViTriChuoi(spinner_ViDialogSua,tenvisua));
     //  spinner_DanhMucDialogSua.setSelection(LayViTriChuoi(spinner_DanhMucDialogSua,tendanhmucsua));
        //spinner_DanhMucDialogSua.setSelection(4);
       editText_SoTienThuChiDialogSua.setText(String.valueOf(sotienthuchi));
       editText_MoTaThuChiDialogSua.setText(motaht);
       button_NgayThuChiDialogSua.setText(ngaythuchien);
       button_GioThuChiDialogSua.setText(thoigianthuchi);

       if(nhanthongbao == 1){
           check_thongbaoSua.setChecked(true);
       }else {
           check_thongbaoSua.setChecked(false);
       }
       return true;

    }

    //lay vi tri spinner tu 1 chuoi
    public int LayViTriChuoi(Spinner spinner, String ten){
        int i;
        for (i = 0; i < spinner.getCount();i++){
            if(spinner.getItemAtPosition(i).toString().equals(ten)){
                return i;
            }
        }
        return 0;
    }

    //Lay ten vi chon de dua len spinner sua
    public String LayTenViChon(int maviht){
        cursor = data.rawQuery("select tenvi from tblvi where tentaikhoan='"+taikhoan+"' and mavi="+ maviht,null);
        cursor.moveToFirst();
        String tenviht = cursor.getString(cursor.getColumnIndex("tenvi"));
        return  tenviht;
    }

    //Lay ten danh muc de dua len spinner sua
    public String LayTenDanhMucChon(int madanhmucht){
        cursor = data.rawQuery("select tendanhmuc from tbldanhmucthuchi where tentaikhoan='"+taikhoan+"' and madanhmuc ="+ madanhmucht,null);
        cursor.moveToFirst();
        String tendanhmucht = cursor.getString(cursor.getColumnIndex("tendanhmuc"));
        return tendanhmucht;
    }


    //Dialog Load data len spinner
    public void LoadSpinnerDialogSua() {
        //Spinner Danh muc
        arrMaDanhMucDialog = new ArrayList<Integer>();
        arrTenDanhMucDialog = new ArrayList<String>();
        adapterDanhMucDialog = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrTenDanhMucDialog);
        adapterDanhMucDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_DanhMucDialogSua.setAdapter(adapterDanhMucDialog);

        //Spinner Loai thu chi
        arrSpinnerDialog = getResources().getStringArray(R.array.loaithuchi);
        adapterSpinnerDialog = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrSpinnerDialog);
        adapterSpinnerDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_LoaiThuChiDialogSua.setAdapter(adapterSpinnerDialog);
        spinner_LoaiThuChiDialogSua.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadDanhSachDanhMucLenSpinnerDialogSua();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner Vi
        arrMaViDialog = new ArrayList<Integer>();
        arrTenViDialog = new ArrayList<String>();
        adapterViDialog = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrTenViDialog);
        adapterViDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ViDialogSua.setAdapter(adapterViDialog);
    }
    //TimePickerSua
    public void ChonGioSua(){
        final Calendar calendarGio = Calendar.getInstance();
        int gio1 = calendarGio.get(Calendar.HOUR_OF_DAY);
        int phut1 = calendarGio.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(activity,R.style.Theme_AppCompat_DayNight_Dialog_Alert,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                        Calendar laygio = Calendar.getInstance();
//                        laygio.set(Calendar.HOUR_OF_DAY,i);
//                        laygio.set(Calendar.MINUTE,i1);
//                        laygio.set(Calendar.SECOND,0);
//                        startAlarm(laygio);
                        gioSua = i;
                        phutSua = i1;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        calendarGio.set(0,0,0,i,i1); //i la gio, i1 la phut
                        button_GioThuChiDialogSua.setText(simpleDateFormat.format(calendarGio.getTime()));

                    }
                }, gio1, phut1, true);
        timePickerDialog.show();

    }
    //Date Picker sua
    public void ChonNgayThemThuChiSua() {
        final Dialog dialog = new Dialog(activity, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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
                button_NgayThuChiDialogSua.setText(simpleDateFormatDialog.format(date));
                dialog.cancel();
            }
        });
    }

    public void LoadDanhSachDanhMucLenSpinnerDialogSua() {
        arrMaDanhMucDialog.clear();
        arrTenDanhMucDialog.clear();
        //Cursor cursor = data.rawQuery("select madanhmuc, tendanhmuc from tbldanhmucthuchi where loaikhoan = '" + spinner_LoaiThuChiDialog.getSelectedItem().toString() + "'"+ "and tentaikhoan = '"+ taikhoan + "'", null);
        Cursor cursor = data.rawQuery("select * from tbldanhmucthuchi where tentaikhoan = '" + taikhoan + "' and loaikhoan = '" + spinner_LoaiThuChiDialogSua.getSelectedItem().toString() + "' ", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            arrMaDanhMucDialog.add(cursor.getInt(cursor.getColumnIndex("madanhmuc")));
            arrTenDanhMucDialog.add(cursor.getString(cursor.getColumnIndex("tendanhmuc")));
            cursor.moveToNext();
        }
        adapterDanhMucDialog.notifyDataSetChanged();
    }
    //Ham gioi han so tien nhap
    public void GioiHanSoTienKhiChuyen(){
        Double sotiennhapthem;
        sotiennhapthem = Double.parseDouble(editText_SoTienThuChiDialog.getText().toString());
        if(sotiennhapthem > 100000000){
            editText_SoTienThuChiDialog.startAnimation(animation);
            editText_SoTienThuChiDialog.setText(String.valueOf(0));
            Toast.makeText(activity,"Số tiền nhập quá lớn",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean GioiHanSoTien(){
        Double sotiennhapthem;
        sotiennhapthem = Double.parseDouble(editText_SoTienThuChiDialog.getText().toString());
        if(sotiennhapthem > 100000000){
            return false;
        }
        return true;
    }
}