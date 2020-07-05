package com.example.doantotnghiep.ui.KeHoachTietKiem;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aldoapps.autoformatedittext.AutoFormatEditText;
import com.example.doantotnghiep.R;
import com.example.doantotnghiep.adapter.AdapterKeHoachTietKiem;
import com.example.doantotnghiep.adapter.AdapterThuChiChoKeHoachTietKiem;
import com.example.doantotnghiep.model.ArrayKeHoachTietKiem;
import com.example.doantotnghiep.model.ArrayThuChiChoKeHoachTietKiem;
import com.example.doantotnghiep.thongbao.ThongBaoKeHoachReciver;
import com.example.doantotnghiep.thongbao.ThongBaoThuChiReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KeHoachTietKiemFragment extends Fragment {
    private int vitri = 0;
    private View myFragment;
    private SQLiteDatabase data;
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private String taikhoan;
    private Animation animation;
    private ImageButton imageButton_ThemKeHoachTietKiem;
    private Button button_LuuKeHoachTietKiem, button_HuyKeHoachTietKiem, button_NgayBatDauKeHoachTietKiem,
            button_NgayKetThucKeHoachTietKiem;
    private EditText editText_TenKeHoachTietKiem;
    private ListView listView_KeHoachTietKiem, listView_ThuChiChoKeHoachTietKiem;
    private Calendar calendar;
    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private ArrayList<ArrayKeHoachTietKiem> arrayKeHoachTietKiem;
    private List<ArrayKeHoachTietKiem> list = null;
    private AdapterKeHoachTietKiem adapterKeHoachTietKiem;
    private boolean danhsachkehoachtietkiem = false, danhsachthuchichokehoachtietkiem = false;
    private TextView textView_DanhSachKeHoachTietKiemTrong, textView_DanhSachThuChiChoKeHoachTietKiemTrong;
    private EditText editText_MoTaThuChiChoKeHoach;
    private Spinner spinner_LoaiThuChiChoKeHoach;
    private TextView textView_NgayThucHienThuChiChoKeHoach, textView_Toolbar;
    private Button button_LuuThuChiChoKeHoach, button_HuyThuChiChoKeHoach, button_HuyThuChiChoKeHoachTietKiem;
    private String[] arrSpinner, arrTrangThaiKeHoachTietKiem;
    private ArrayAdapter<String> adapterSpinner, adapterTrangThaiKeHoachTietKiem;
    private Double sotienthuchichokehoach, sotiendatietkiemchokehoachtietkiem, sotienkehoachtietkiem;
    private ArrayList<ArrayThuChiChoKeHoachTietKiem> arrayThuChiChoKeHoachTietKiem;
    private AdapterThuChiChoKeHoachTietKiem adapterThuChiChoKeHoachTietKiem;
    private EditText editText_TenKeHoachTietKiem_ChiTiet;
    private Button button_NgayBatDauKeHoachTietKiem_ChiTiet, button_NgayKetThucKeHoachTietKiem_ChiTiet, button_HuyKeHoachTietKiem_ChiTiet;
    private Spinner spinner_TrangThaiKeHoachTietKiem;
    private RadioGroup radioGroup_KeHoachTietKiem;
    private EditText editText_SoTienThuChiChoKeHoach, editText_SoTienKeHoachTietKiem;
    private AutoFormatEditText editText_SoTienKeHoachTietKiem_ChiTiet;
    private Double sotienkehoach,sotienthuchikehoach;
    private CheckBox checkBox_KeHoach;

    public static KeHoachTietKiemFragment newInstance() {
        return new KeHoachTietKiemFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_kehoachtietkiem, container, false);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);
        activity = getActivity();
        data = activity.openOrCreateDatabase("data.db", activity.MODE_PRIVATE, null);
        sharedPreferences = getActivity().getSharedPreferences("tendangnhap", Context.MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("taikhoancanchuyen","khong tim thay");
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();

        AnhXa();
        ThemKHTK();
        setListview();
        LoadTatCaKeHoachTietKiem();
        XuLyKhiDanhSachKeHoachTietKiemTrong(danhsachkehoachtietkiem);
        KiemTraTrangThaiCuaKeHoachTietKiem();

        listView_KeHoachTietKiem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                vitri = position;
                return false;
            }
        });
        registerForContextMenu(listView_KeHoachTietKiem);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        activity.getMenuInflater().inflate(R.menu.menu_kehoachtietkiem, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_ThuChoKeHoachTietKiem: {
                if (KiemTraTrangThaiKHTK(vitri)) {
                    ThuChiChoKeHoachTietKiem();
                } else {
                    Toast.makeText(activity,"Kế hoạch đã kết thúc",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            case R.id.option_XoaKeHoachTietKiem: {
                XoaKeHoachTietKiem(vitri);
                return true;
            }
            case R.id.option_XemLishSuThuChiChoKeHoachTietKiem: {
                XemLichSuThuChiChoKeHoachTietKiem();
                return true;
            }
            case R.id.option_XemChiTietKeHoachTietKiem: {
                XemChiTietKeHoachKiem();
                return true;
            }
            case R.id.option_TatThongBao: {
                if(KiemTraTatThongBao(vitri)){
                    TatThongBao();
                    CapNhatThongBao(vitri);
                    LoadTatCaKeHoachTietKiem();
                }
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    //Kiem tra trang thai ke hoach de thuc hien thu chi
    public boolean KiemTraTrangThaiKHTK(int vitrichon){
        int makehoach = arrayKeHoachTietKiem.get(vitrichon).makehoachtietkiem;
        Cursor cursor = data.rawQuery("Select* from tblkehoachtietkiem where tentaikhoan ='"+ taikhoan +"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getInt(cursor.getColumnIndex("makehoachtietkiem")) == makehoach){
                if(cursor.getString(cursor.getColumnIndex("trangthai")).equals("Đang thực hiện")){
                    return true;
                }
            }
            cursor.moveToNext();
        }
        return false;
    }

    public void AnhXa() {
        listView_KeHoachTietKiem = (ListView) myFragment.findViewById(R.id.listView_KeHoachTietKiem);
        imageButton_ThemKeHoachTietKiem = (ImageButton) myFragment.findViewById(R.id.imageButton_ThemKeHoachTietKiem);
        textView_DanhSachKeHoachTietKiemTrong = (TextView) myFragment.findViewById(R.id.textView_DanhSachKeHoachTietKiemTrong);
        spinner_TrangThaiKeHoachTietKiem = (Spinner) myFragment.findViewById(R.id.spinner_TrangThaiKeHoachTietKiem);
        radioGroup_KeHoachTietKiem = (RadioGroup) myFragment.findViewById(R.id.radioGroup_KeHoachTietKiem);
        radioGroup_KeHoachTietKiem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LoadKeHoachTietKiemTheoRadioButton(group, checkedId);
            }
        });

        spinner_TrangThaiKeHoachTietKiem.setVisibility(View.INVISIBLE);
    }

    public void ThemKHTK() {
        imageButton_ThemKeHoachTietKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(getContext());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_themkehoachtietkiem);
                d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                d.show();

                //Anh xa
                button_LuuKeHoachTietKiem = d.findViewById(R.id.button_LuuKeHoachTietKiem);
                button_HuyKeHoachTietKiem = d.findViewById(R.id.button_HuyKeHoachTietKiem);
                editText_TenKeHoachTietKiem = d.findViewById(R.id.editText_TenKeHoachTietKiem);
                button_NgayBatDauKeHoachTietKiem = d.findViewById(R.id.button_NgayBatDauKeHoachTietKiem);
                button_NgayKetThucKeHoachTietKiem = d.findViewById(R.id.button_NgayKetThucKeHoachTietKiem);
                editText_SoTienKeHoachTietKiem = d.findViewById(R.id.editText_SoTienKeHoachTietKiem);
                checkBox_KeHoach = d.findViewById(R.id.checkBox_KeHoach);

                calendar = Calendar.getInstance();

                editText_SoTienKeHoachTietKiem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b && editText_SoTienKeHoachTietKiem.getText().toString() != null) {
                            if (editText_SoTienKeHoachTietKiem.getText().toString().equals("")) {
                                Toast.makeText(activity, "Bạn chưa nhập số tiền cho kế hoạch tiết kiệm", Toast.LENGTH_SHORT).show();
                                editText_SoTienKeHoachTietKiem.startAnimation(animation);
                            } else {
                                GioiHanSoTienKeHoachKhiChuyen();
                            }
                        }
                    }
                });

                //Xu Ly
                HienThiThoiGianThemKeHoachTietKiem();

                button_HuyKeHoachTietKiem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                button_LuuKeHoachTietKiem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean tenkehoachtietkiem = true;
                        String ngaybatdaukehoach, ngayketthuckehoach;
                        calendar = Calendar.getInstance();
                        date = calendar.getTime();

                        Cursor cursor = data.rawQuery("select * from tblkehoachtietkiem", null);
                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {
                            if (cursor.getString(cursor.getColumnIndex("tenkehoachtietkiem")).equals(editText_TenKeHoachTietKiem.getText().toString())) {
                                tenkehoachtietkiem = false;
                            }
                            cursor.moveToNext();
                        }

                        try {
                            ngayketthuckehoach = button_NgayKetThucKeHoachTietKiem.getText().toString();
                            ngaybatdaukehoach = simpleDateFormat.format(date);
                            Date dateketthuc = simpleDateFormat.parse(ngayketthuckehoach);
                            Date datebatdau = simpleDateFormat.parse(ngaybatdaukehoach);
                            if (tenkehoachtietkiem == false) {
                                Toast.makeText(activity, "Tên kế hoạch tiết kiệm này đã tồn tại", Toast.LENGTH_SHORT).show();
                                editText_TenKeHoachTietKiem.startAnimation(animation);
                            } else if (editText_TenKeHoachTietKiem.getText().toString().equals("")) {
                                Toast.makeText(activity, "Bạn chưa nhập tên kế hoạch tiết kiệm", Toast.LENGTH_SHORT).show();
                                editText_TenKeHoachTietKiem.startAnimation(animation);
                            } else if (button_NgayKetThucKeHoachTietKiem.getText().toString().equals("CHỌN NGÀY")) {
                                Toast.makeText(activity, "Bạn chưa chọn ngày kết thúc cho kế hoạch tiết kiệm", Toast.LENGTH_SHORT).show();
                                button_NgayKetThucKeHoachTietKiem.startAnimation(animation);
                            } else if (editText_SoTienKeHoachTietKiem.getText().toString().equals("")) {
                                Toast.makeText(activity, "Bạn chưa nhập số tiền cho kế hoạch tiết kiệm", Toast.LENGTH_SHORT).show();
                                editText_SoTienKeHoachTietKiem.startAnimation(animation);
                            } else if (dateketthuc.before(datebatdau)) {
                                Toast.makeText(activity, "Ngày kết thúc phải sau ngày bắt đầu", Toast.LENGTH_SHORT).show();
                            } else if(!GioiHanSoTienKeHoach()){
                                editText_SoTienKeHoachTietKiem.startAnimation(animation);
                                editText_SoTienKeHoachTietKiem.setText(String.valueOf(0));
                                Toast.makeText(activity,"Số tiền nhập quá lớn",Toast.LENGTH_SHORT).show();
                            }else if(checkBox_KeHoach.isChecked() && !KiemTraThongBaoKeHoach()){
                                checkBox_KeHoach.startAnimation(animation);
                                checkBox_KeHoach.setChecked(false);
                                Toast.makeText(activity,"Đã có một kế hoạch đang chờ thông báo",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                int makehoachtietkiem = 1;
                                Cursor cursor1 = data.rawQuery("select makehoachtietkiem from tblkehoachtietkiem", null);
                                if (cursor1.moveToLast() == true) {
                                    makehoachtietkiem = cursor1.getInt(cursor1.getColumnIndex("makehoachtietkiem")) + 1;
                                }

                                //Luu vao co so du lieu
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("makehoachtietkiem", makehoachtietkiem);
                                contentValues.put("tenkehoachtietkiem", editText_TenKeHoachTietKiem.getText().toString());
                                contentValues.put("ngaybatdaukehoachtietkiem", button_NgayBatDauKeHoachTietKiem.getText().toString());
                                contentValues.put("ngayketthuckehoachtietkiem", button_NgayKetThucKeHoachTietKiem.getText().toString());
                                contentValues.put("sotienkehoachtietkiem", editText_SoTienKeHoachTietKiem.getText().toString().replaceAll(",", ""));
                                contentValues.put("sotiendatietkiem", 0);
                                if(checkBox_KeHoach.isChecked()){
                                    contentValues.put("nhanthongbao",1);
                                }else {
                                    contentValues.put("nhanthongbao",0);
                                }
                                contentValues.put("trangthai", "Đang thực hiện");
                                contentValues.put("tentaikhoan", taikhoan);

                                if (data.insert("tblkehoachtietkiem", null, contentValues) != -1) {
                                    startAlarm();
                                    Toast.makeText(activity, "Thêm kế hoạch tiết kiệm thành công", Toast.LENGTH_SHORT).show();
                                    d.dismiss();

                                    //Load tat ca danh sach ke hoach tiet kiem

                                    LoadTatCaKeHoachTietKiem();

                                    XuLyKhiDanhSachKeHoachTietKiemTrong(danhsachkehoachtietkiem);

                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                button_NgayKetThucKeHoachTietKiem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChonNgayKetThucKeHoachTietKiem();
                    }
                });
            }
        });
    }

    public void ChonNgayKetThucKeHoachTietKiem() {
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
                    date = simpleDateFormat.parse(datePicker.getDayOfMonth() + "/" +(datePicker.getMonth() + 1) + "/" + datePicker.getYear());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                button_NgayKetThucKeHoachTietKiem.setText(simpleDateFormat.format(date));
                dialog.cancel();
            }
        });
    }

    public void HienThiThoiGianThemKeHoachTietKiem() {
        date = calendar.getTime();
        button_NgayBatDauKeHoachTietKiem.setText(simpleDateFormat.format(date));
    }

    public void HienThiThoiGianThemThuChiChoKeHoachTietKiem() {
        date = calendar.getTime();
        textView_NgayThucHienThuChiChoKeHoach.setText(simpleDateFormat.format(date));
    }

    public void setListview() {
        arrayKeHoachTietKiem = new ArrayList<ArrayKeHoachTietKiem>();
        adapterKeHoachTietKiem = new AdapterKeHoachTietKiem(getActivity(), R.layout.adapter_kehoachtietkiem_item, arrayKeHoachTietKiem);
        listView_KeHoachTietKiem.setAdapter(adapterKeHoachTietKiem);
    }

    public void setListView_ThuChiChoKeHoachTietKiem() {
        arrayThuChiChoKeHoachTietKiem = new ArrayList<ArrayThuChiChoKeHoachTietKiem>();
        adapterThuChiChoKeHoachTietKiem = new AdapterThuChiChoKeHoachTietKiem(getActivity(), R.layout.adapter_thuchichokehoachtietkiem_item, arrayThuChiChoKeHoachTietKiem);
        listView_ThuChiChoKeHoachTietKiem.setAdapter(adapterThuChiChoKeHoachTietKiem);
    }

    public void LoadTatCaKeHoachTietKiem() {
        arrayKeHoachTietKiem.clear();
        String thongbao;
        Cursor cursor = data.rawQuery("select * from tblkehoachtietkiem where tentaikhoan = '" + taikhoan + "' ", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if(cursor.getInt(cursor.getColumnIndex("nhanthongbao"))==1){
                thongbao = "Đang đặt thông báo";
            }else {
                thongbao = "";
            }
            arrayKeHoachTietKiem.add(new ArrayKeHoachTietKiem(cursor.getString(cursor.getColumnIndex("tenkehoachtietkiem")), cursor.getString(cursor.getColumnIndex("ngaybatdaukehoachtietkiem")), cursor.getString(cursor.getColumnIndex("ngayketthuckehoachtietkiem")), cursor.getInt(cursor.getColumnIndex("sotienkehoachtietkiem")), cursor.getInt(cursor.getColumnIndex("sotiendatietkiem")), cursor.getInt(cursor.getColumnIndex("makehoachtietkiem")), cursor.getString(cursor.getColumnIndex("trangthai")),thongbao));
            cursor.moveToNext();
        }
        cursor.close();
        adapterKeHoachTietKiem.notifyDataSetChanged();
    }

    public void XoaKeHoachTietKiem(int vitri1) {
        HamXoaKeHoachTietKiem(arrayKeHoachTietKiem.get(vitri1).makehoachtietkiem);
    }

    public void HamXoaKeHoachTietKiem(final int makehoachtietkiem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Thông báo !");
        builder.setMessage("Bạn có chắc chắn muốn xóa kế hoạch tiết kiệm này ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.rawQuery("delete from tblkehoachtietkiem where makehoachtietkiem = '" + makehoachtietkiem + "' and tentaikhoan = '" + taikhoan + "'", null).moveToFirst();
                data.rawQuery("delete from tblthuchichokehoachtietkiem where makehoachtietkiem = '" + makehoachtietkiem + "' and tentaikhoan = '" + taikhoan + "'", null).moveToNext();
                Toast.makeText(activity, "Xóa thành công", Toast.LENGTH_SHORT).show();

                //Delay ham xuat du lieu de tranh bi crash app
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoadTatCaKeHoachTietKiem();
                        XuLyKhiDanhSachKeHoachTietKiemTrong(danhsachkehoachtietkiem);
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

    public void XuLyKhiDanhSachKeHoachTietKiemTrong(boolean danhsachkehoachtietkiem) {
        this.danhsachkehoachtietkiem = danhsachkehoachtietkiem;
        if (danhsachkehoachtietkiem) {
            textView_DanhSachKeHoachTietKiemTrong.setVisibility(View.VISIBLE);
        } else {
            if (adapterKeHoachTietKiem.getCount() <= 0) {
                textView_DanhSachKeHoachTietKiemTrong.setVisibility(View.VISIBLE);
            } else {
                textView_DanhSachKeHoachTietKiemTrong.setVisibility(View.INVISIBLE);
            }
        }
    }

    //Spinner loai thu chi cho ke hoach tiet kiem
    public void LoadSpinnerThuChiChoKeHoachTietKiem() {
        arrSpinner = getResources().getStringArray(R.array.loaithuchichokehoachtietkiem);
        adapterSpinner = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrSpinner);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_LoaiThuChiChoKeHoach.setAdapter(adapterSpinner);
    }

    //Spinner loc ke hoach tiet kiem theo trang thai
    public void LoadSpinnerTrangThaiCuaKeHoachTietKiem() {
        arrTrangThaiKeHoachTietKiem = getResources().getStringArray(R.array.trangthaicuakehoachtietkiem);
        adapterTrangThaiKeHoachTietKiem = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrTrangThaiKeHoachTietKiem);
        adapterTrangThaiKeHoachTietKiem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_TrangThaiKeHoachTietKiem.setAdapter(adapterTrangThaiKeHoachTietKiem);
        spinner_TrangThaiKeHoachTietKiem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadKeHoachTietKiemTheoTrangThai();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void ThuChiChoKeHoachTietKiem() {
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);
        final Dialog d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_thuchichokehoachtietkiem);
        d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        d.show();

        //Anh xa
        editText_SoTienThuChiChoKeHoach = (EditText) d.findViewById(R.id.editText_SoTienThuChiChoKeHoach);
        editText_MoTaThuChiChoKeHoach = (EditText) d.findViewById(R.id.editText_MoTaThuChiChoKeHoachTietKiem);
        textView_NgayThucHienThuChiChoKeHoach = (TextView) d.findViewById(R.id.textView_NgayThucHienThuChiChoKeHoachTietKiem);
        button_LuuThuChiChoKeHoach = (Button) d.findViewById(R.id.button_LuuThuChiChoKeHoach);
        button_HuyThuChiChoKeHoachTietKiem = (Button) d.findViewById(R.id.button_HuyThuChiChoKeHoachTietKiem);
        spinner_LoaiThuChiChoKeHoach = (Spinner) d.findViewById(R.id.spinner_LoaiThuChiChoKeHoach);

        calendar = Calendar.getInstance();

        editText_SoTienThuChiChoKeHoach.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && editText_SoTienThuChiChoKeHoach.getText().toString()!= null) {
                    if (editText_SoTienThuChiChoKeHoach.getText().toString().equals("")) {
                        editText_SoTienThuChiChoKeHoach.startAnimation(animation);
                        Toast.makeText(activity, "Bạn chưa nhập số tiền", Toast.LENGTH_SHORT).show();
                    } else {
                        GioiHanSoTienThuChiKhiChuyen();
                    }

                }
            }
        });

        //Xu ly
        HienThiThoiGianThemThuChiChoKeHoachTietKiem();
        LoadSpinnerThuChiChoKeHoachTietKiem();

        button_LuuThuChiChoKeHoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_SoTienThuChiChoKeHoach.getText().toString().equals("")) {
                    editText_SoTienThuChiChoKeHoach.startAnimation(animation);
                    Toast.makeText(activity, "Bạn chưa nhập số tiền", Toast.LENGTH_SHORT).show();
                } else if (editText_MoTaThuChiChoKeHoach.getText().toString().equals("")) {
                    editText_MoTaThuChiChoKeHoach.startAnimation(animation);
                    Toast.makeText(activity, "Bạn chưa nhập mô tả", Toast.LENGTH_SHORT).show();
                } else if (!GioiHanSoTienThuChi()) {
                    editText_SoTienThuChiChoKeHoach.startAnimation(animation);
                    editText_SoTienThuChiChoKeHoach.setText(String.valueOf(0));
                    Toast.makeText(activity,"Số tiền nhập quá lớn",Toast.LENGTH_SHORT).show();
                } else {
                    int mathuchichokehoachtietkiem = 1;
                    Cursor cursor = data.rawQuery("select mathuchichokehoachtietkiem from tblthuchichokehoachtietkiem", null);
                    if (cursor.moveToLast() == true) {
                        mathuchichokehoachtietkiem = cursor.getInt(cursor.getColumnIndex("mathuchichokehoachtietkiem")) + 1;
                    }

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("mathuchichokehoachtietkiem", mathuchichokehoachtietkiem);
                    contentValues.put("loaithuchichokehoachtietkiem", spinner_LoaiThuChiChoKeHoach.getSelectedItem().toString());
                    contentValues.put("sotienthuchichokehoachtietkiem", editText_SoTienThuChiChoKeHoach.getText().toString().replaceAll(",", ""));
                    contentValues.put("motathuchichokehoachtietkiem", editText_MoTaThuChiChoKeHoach.getText().toString());
                    contentValues.put("ngaythuchienthuchichokehoachtietkiem", textView_NgayThucHienThuChiChoKeHoach.getText().toString());
                    contentValues.put("makehoachtietkiem", arrayKeHoachTietKiem.get(vitri).makehoachtietkiem);
                    contentValues.put("tentaikhoan", taikhoan);

                    if (data.insert("tblthuchichokehoachtietkiem", null, contentValues) != -1) {
                        Toast.makeText(activity, "Thêm thành công", Toast.LENGTH_SHORT).show();

                        LayTienTuKeHoachTietKiem();
                        XuLyChuyenTienChoKeHoach();

                        d.dismiss();

                        LoadTatCaKeHoachTietKiem();
                    } else {
                        Toast.makeText(activity, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        button_HuyThuChiChoKeHoachTietKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    public void LayTienTuKeHoachTietKiem() {
        Cursor cursor = data.rawQuery("select * from tblkehoachtietkiem", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (cursor.getInt(cursor.getColumnIndex("makehoachtietkiem")) == arrayKeHoachTietKiem.get(vitri).makehoachtietkiem) {
                sotiendatietkiemchokehoachtietkiem = cursor.getDouble(cursor.getColumnIndex("sotiendatietkiem"));
            }
            cursor.moveToNext();
        }
    }


    public void XuLyChuyenTienChoKeHoach() {
        Double sotienthu = 0.0;
        Double sotienchi = 0.0;
        sotienthuchichokehoach = Double.parseDouble(editText_SoTienThuChiChoKeHoach.getText().toString().replaceAll(",", ""));

        sotienthu = sotiendatietkiemchokehoachtietkiem + sotienthuchichokehoach;
        sotienchi = sotiendatietkiemchokehoachtietkiem - sotienthuchichokehoach;

        if (spinner_LoaiThuChiChoKeHoach.getSelectedItem().toString().equals("Thu vào")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("sotiendatietkiem", sotienthu);
            data.update("tblkehoachtietkiem", contentValues, "makehoachtietkiem = '" + arrayKeHoachTietKiem.get(vitri).makehoachtietkiem + "'", null);
        } else if (spinner_LoaiThuChiChoKeHoach.getSelectedItem().toString().equals("Chi ra")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("sotiendatietkiem", sotienchi);
            data.update("tblkehoachtietkiem", contentValues, "makehoachtietkiem = '" + arrayKeHoachTietKiem.get(vitri).makehoachtietkiem + "'", null);
        }
    }

    public void KiemTraTrangThaiCuaKeHoachTietKiem() {
        String ngayketthuc, ngayhientai;
        int makehoach = 0;
        calendar = Calendar.getInstance();
        date = calendar.getTime();

        Cursor cursor = data.rawQuery("select * from tblkehoachtietkiem where tentaikhoan = '" + taikhoan + "'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            ngayketthuc = cursor.getString(cursor.getColumnIndex("ngayketthuckehoachtietkiem"));
            //ngayhientai = simpleDateFormat.format(date);
            ngayhientai = simpleDateFormat.format(date);
            makehoach = cursor.getInt(cursor.getColumnIndex("makehoachtietkiem"));
            sotiendatietkiemchokehoachtietkiem = cursor.getDouble(cursor.getColumnIndex("sotiendatietkiem"));
            sotienkehoachtietkiem = cursor.getDouble(cursor.getColumnIndex("sotienkehoachtietkiem"));

           // try {
//                Date dateketthuc = simpleDateFormat.parse(ngayketthuc);
//                Date datehientai = simpleDateFormat.parse(ngayhientai);

               // if (datehientai.after(dateketthuc) && sotiendatietkiemchokehoachtietkiem >= sotienkehoachtietkiem) {
            if(ngayhientai.equals(ngayketthuc) && sotiendatietkiemchokehoachtietkiem >= sotienkehoachtietkiem){
                ContentValues contentValues = new ContentValues();
                contentValues.put("trangthai", "Đã kết thúc - Kế hoạch thành công");
                data.update("tblkehoachtietkiem", contentValues, "makehoachtietkiem = '" + makehoach + "' and tentaikhoan = '" + taikhoan + "'", null);

                    //Ham load danh sach ke hoach tiet kiem
                LoadTatCaKeHoachTietKiem();
            } else if (ngayhientai.equals(ngayketthuc) && sotiendatietkiemchokehoachtietkiem < sotienkehoachtietkiem) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("trangthai", "Đã kết thúc - Kế hoạch thất bại");
                data.update("tblkehoachtietkiem", contentValues, "makehoachtietkiem = '" + makehoach + "' and tentaikhoan = '" + taikhoan + "'", null);

                    //Ham load danh sach ke hoach tiet kiem
                LoadTatCaKeHoachTietKiem();
            }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            cursor.moveToNext();
        }
    }

    //Chuyen trang thai qua ben thong bao
    public String KiemTraTrangThaiCuaKeHoachTietKiemTB() {
        String ngayketthuc, ngayhientai;
        int makehoach = 0;
        calendar = Calendar.getInstance();
        date = calendar.getTime();

        Cursor cursor = data.rawQuery("select * from tblkehoachtietkiem where tentaikhoan = '" + taikhoan + "'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            ngayketthuc = cursor.getString(cursor.getColumnIndex("ngayketthuckehoachtietkiem"));
            //ngayhientai = simpleDateFormat.format(date);
            ngayhientai = simpleDateFormat.format(date);
            makehoach = cursor.getInt(cursor.getColumnIndex("makehoachtietkiem"));
            sotiendatietkiemchokehoachtietkiem = cursor.getDouble(cursor.getColumnIndex("sotiendatietkiem"));
            sotienkehoachtietkiem = cursor.getDouble(cursor.getColumnIndex("sotienkehoachtietkiem"));

            // try {
//                Date dateketthuc = simpleDateFormat.parse(ngayketthuc);
//                Date datehientai = simpleDateFormat.parse(ngayhientai);

            // if (datehientai.after(dateketthuc) && sotiendatietkiemchokehoachtietkiem >= sotienkehoachtietkiem) {
            if(ngayhientai.equals(ngayketthuc) && sotiendatietkiemchokehoachtietkiem >= sotienkehoachtietkiem){
                ContentValues contentValues = new ContentValues();
                contentValues.put("trangthai", "Đã kết thúc - Kế hoạch thành công");
                data.update("tblkehoachtietkiem", contentValues, "makehoachtietkiem = '" + makehoach + "' and tentaikhoan = '" + taikhoan + "'", null);
                return "Đã kết thúc - Kế hoạch thành công";
                //Ham load danh sach ke hoach tiet kiem
                //LoadTatCaKeHoachTietKiem();
            } else if (ngayhientai.equals(ngayketthuc) && sotiendatietkiemchokehoachtietkiem < sotienkehoachtietkiem) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("trangthai", "Đã kết thúc - Kế hoạch thất bại");
                data.update("tblkehoachtietkiem", contentValues, "makehoachtietkiem = '" + makehoach + "' and tentaikhoan = '" + taikhoan + "'", null);
                return "Đã kết thúc - Kế hoạch thất bại";
                //Ham load danh sach ke hoach tiet kiem
                //LoadTatCaKeHoachTietKiem();
            }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            cursor.moveToNext();
        }
        return "Đang thực hiện tb";
    }

    public void LoadLichSuThuChiChoKeHoachTietKiem() {
        arrayThuChiChoKeHoachTietKiem.clear();
        Cursor cursor = data.rawQuery("select * from tblthuchichokehoachtietkiem where tentaikhoan = '" + taikhoan + "' " +
                "and makehoachtietkiem = '" + arrayKeHoachTietKiem.get(vitri).makehoachtietkiem + "'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            arrayThuChiChoKeHoachTietKiem.add(new ArrayThuChiChoKeHoachTietKiem(cursor.getInt(cursor.getColumnIndex("mathuchichokehoachtietkiem")), cursor.getInt(cursor.getColumnIndex("sotienthuchichokehoachtietkiem")), cursor.getString(cursor.getColumnIndex("loaithuchichokehoachtietkiem")), cursor.getString(cursor.getColumnIndex("motathuchichokehoachtietkiem")), cursor.getString(cursor.getColumnIndex("ngaythuchienthuchichokehoachtietkiem"))));
            cursor.moveToNext();
        }
        cursor.close();
        adapterThuChiChoKeHoachTietKiem.notifyDataSetChanged();
    }

    public void XemLichSuThuChiChoKeHoachTietKiem() {
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);
        final Dialog d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_lichsuthuchichokehoachtietkiem);
        d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        d.show();

        //Anh xa
        button_HuyThuChiChoKeHoach = (Button) d.findViewById(R.id.button_HuyThuChiChoKeHoach);
        listView_ThuChiChoKeHoachTietKiem = (ListView) d.findViewById(R.id.listView_ThuChiChoKeHoachTietKiem);
        textView_DanhSachThuChiChoKeHoachTietKiemTrong = (TextView) d.findViewById(R.id.textView_DanhSachThuChiChoKeHoachTietKiemTrong);

        //Xu ly
        setListView_ThuChiChoKeHoachTietKiem();
        LoadLichSuThuChiChoKeHoachTietKiem();
        XuLyKhiDanhSachThuChiChoKeHoachTietKiemTrong(danhsachthuchichokehoachtietkiem);

        button_HuyThuChiChoKeHoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    public void XuLyKhiDanhSachThuChiChoKeHoachTietKiemTrong(boolean danhsachthuchichokehoachtietkiem) {
        this.danhsachthuchichokehoachtietkiem = danhsachthuchichokehoachtietkiem;
        if (danhsachthuchichokehoachtietkiem) {
            textView_DanhSachThuChiChoKeHoachTietKiemTrong.setVisibility(View.VISIBLE);
        } else {
            if (adapterThuChiChoKeHoachTietKiem.getCount() <= 0) {
                textView_DanhSachThuChiChoKeHoachTietKiemTrong.setVisibility(View.VISIBLE);
            } else {
                textView_DanhSachThuChiChoKeHoachTietKiemTrong.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void XemChiTietKeHoachKiem() {
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);
        final Dialog d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_xemchitietkehoachtietkiem);
        d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        d.show();

        //Anh xa
        editText_TenKeHoachTietKiem_ChiTiet = (EditText) d.findViewById(R.id.editText_TenKeHoachTietKiem_ChiTiet);
        editText_SoTienKeHoachTietKiem_ChiTiet = (AutoFormatEditText) d.findViewById(R.id.editText_SoTienKeHoachTietKiem_ChiTiet);
        button_NgayBatDauKeHoachTietKiem_ChiTiet = (Button) d.findViewById(R.id.button_NgayBatDauKeHoachTietKiem_ChiTiet);
        button_NgayKetThucKeHoachTietKiem_ChiTiet = (Button) d.findViewById(R.id.button_NgayKetThucKeHoachTietKiem_ChiTiet);
        button_HuyKeHoachTietKiem_ChiTiet = (Button) d.findViewById(R.id.button_HuyKeHoachTietKiem_ChiTiet);

        //Xu ly
        final int makehoachtietkiem = arrayKeHoachTietKiem.get(vitri).makehoachtietkiem;
        Cursor cursor = data.rawQuery("select * from tblkehoachtietkiem where makehoachtietkiem = '" + makehoachtietkiem + "' and tentaikhoan = '" + taikhoan + "'", null);
        cursor.moveToFirst();

        String tenkehoach = cursor.getString(1);
        String ngaybatdau = cursor.getString(2);
        String ngayketthuc = cursor.getString(3);
        String sotien = String.valueOf(cursor.getInt(4));

        editText_TenKeHoachTietKiem_ChiTiet.setText(tenkehoach);
        button_NgayBatDauKeHoachTietKiem_ChiTiet.setText(ngaybatdau);
        button_NgayKetThucKeHoachTietKiem_ChiTiet.setText(ngayketthuc);

        editText_TenKeHoachTietKiem_ChiTiet.setEnabled(false);
        editText_SoTienKeHoachTietKiem_ChiTiet.setEnabled(false);

        editText_SoTienKeHoachTietKiem_ChiTiet.setText(sotien.replace(".0", ""));

        button_HuyKeHoachTietKiem_ChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    public void LoadKeHoachTietKiemTheoTrangThai() {
        arrayKeHoachTietKiem.clear();
        String thongbao;
        Cursor cursor = data.rawQuery("select * from tblkehoachtietkiem where tentaikhoan = '" + taikhoan + "' " +
                "and trangthai = '" + spinner_TrangThaiKeHoachTietKiem.getSelectedItem().toString() + "'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if(cursor.getInt(cursor.getColumnIndex("nhanthongbao"))==1){
                thongbao = "Đang đặt thông báo";
            }else {
                thongbao = "";
            }
            arrayKeHoachTietKiem.add(new ArrayKeHoachTietKiem(cursor.getString(cursor.getColumnIndex("tenkehoachtietkiem")), cursor.getString(cursor.getColumnIndex("ngaybatdaukehoachtietkiem")), cursor.getString(cursor.getColumnIndex("ngayketthuckehoachtietkiem")), cursor.getInt(cursor.getColumnIndex("sotienkehoachtietkiem")), cursor.getInt(cursor.getColumnIndex("sotiendatietkiem")), cursor.getInt(cursor.getColumnIndex("makehoachtietkiem")), cursor.getString(cursor.getColumnIndex("trangthai")), thongbao));
            cursor.moveToNext();
        }
        cursor.close();
        adapterKeHoachTietKiem.notifyDataSetChanged();
    }

    public void LoadKeHoachTietKiemTheoRadioButton(RadioGroup radioGroup, int checkedID) {
        int checkedid = radioGroup.getCheckedRadioButtonId();

        spinner_TrangThaiKeHoachTietKiem.setVisibility(View.INVISIBLE);
        if (checkedid == R.id.radioButton_TatCaKeHoachTietKiem) {
            spinner_TrangThaiKeHoachTietKiem.setVisibility(View.INVISIBLE);
            LoadTatCaKeHoachTietKiem();
        } else if (checkedid == R.id.radioButton_LocKeHoachTietKiemTheoTrangThai) {
            LoadSpinnerTrangThaiCuaKeHoachTietKiem();
            spinner_TrangThaiKeHoachTietKiem.setVisibility(View.VISIBLE);
        }
    }

    //Gioi han tien
    public void GioiHanSoTienKeHoachKhiChuyen() {
        sotienkehoach = Double.parseDouble(editText_SoTienKeHoachTietKiem.getText().toString());
        if (sotienkehoach > 100000000) {
            editText_SoTienKeHoachTietKiem.startAnimation(animation);
            editText_SoTienKeHoachTietKiem.setText(String.valueOf(0));
            Toast.makeText(activity,"Số tiền nhập quá lớn",Toast.LENGTH_SHORT).show();
        }
    }
    public boolean GioiHanSoTienKeHoach() {
        sotienkehoach = Double.parseDouble(editText_SoTienKeHoachTietKiem.getText().toString());
        if (sotienkehoach > 100000000) {
            return false;
        }
        return true;
    }

    public void GioiHanSoTienThuChiKhiChuyen() {
        sotienthuchikehoach = Double.parseDouble(editText_SoTienThuChiChoKeHoach.getText().toString());
        if (sotienthuchikehoach > 100000000) {
            editText_SoTienThuChiChoKeHoach.startAnimation(animation);
            editText_SoTienThuChiChoKeHoach.setText(String.valueOf(0));
            Toast.makeText(activity,"Số tiền nhập quá lớn",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean GioiHanSoTienThuChi() {
        sotienthuchikehoach = Double.parseDouble(editText_SoTienThuChiChoKeHoach.getText().toString());
        if (sotienthuchikehoach > 100000000) {
            return false;
        }
        return true;
    }

    //Thong bao ke hoach

    public void startAlarm(){

        String taikhoanthongbao;
        String ngaythongbao, trangthai;

        Cursor cursor = data.rawQuery("select * from tblkehoachtietkiem",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getInt(cursor.getColumnIndex("nhanthongbao"))==1){
                //Chuyen ten tai khoan qua thong bao
                taikhoanthongbao = cursor.getString(cursor.getColumnIndex("tentaikhoan"));
                SharedPreferences sharedPreferences = activity.getSharedPreferences("tendangnhapKH",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString("taikhoancanchuyenkh",taikhoanthongbao);
                editor.commit();

                //Chuyen trang thai qua thong bao
                trangthai = KiemTraTrangThaiCuaKeHoachTietKiemTB();
                SharedPreferences sharedPreferencesTT = activity.getSharedPreferences("tendangnhapTT",Context.MODE_PRIVATE);
                SharedPreferences.Editor editorTT= sharedPreferencesTT.edit();
                editorTT.putString("taikhoancanchuyentt",trangthai);
                editorTT.commit();

                //Lay ngay ket thuc de thong bao
                ngaythongbao = cursor.getString(cursor.getColumnIndex("ngayketthuckehoachtietkiem"));
//                int giothuchien = cursor.getInt(cursor.getColumnIndex("giothuchi"));
//                int phutthuchien = cursor.getInt(cursor.getColumnIndex("phutthuchi"));
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
                calendarTB.set(Calendar.HOUR_OF_DAY,0);
                calendarTB.set(Calendar.MINUTE,0);
                calendarTB.set(Calendar.SECOND,0);

                AlarmManager alarmManagerKH = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(activity, ThongBaoKeHoachReciver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 1, intent, 0);
                alarmManagerKH.setExact(AlarmManager.RTC, calendarTB.getTimeInMillis(), pendingIntent);
            }


            cursor.moveToNext();
        }

    }

    //Tat thong bao bo trong ham CapNhatThuChi
    public void CapNhatThongBao(int vitri){
        int makehoach = arrayKeHoachTietKiem.get(vitri).makehoachtietkiem;
        Cursor cursor = data.rawQuery("select nhanthongbao from tblkehoachtietkiem where makehoachtietkiem ="+ makehoach,null);
        cursor.moveToFirst();
        int thongbao = cursor.getInt(cursor.getColumnIndex("nhanthongbao"));
        if (thongbao == 1){
            ContentValues values = new ContentValues();
            values.put("nhanthongbao",0);
            data.update("tblkehoachtietkiem", values, "makehoachtietkiem =" + makehoach, null);
        }
        cursor.close();
    }

    public void TatThongBao(){
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, ThongBaoKeHoachReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 2, intent, 0);

        alarmManager.cancel(pendingIntent);
    }


    //Kiem tra gioi han nhan thong bao
    public boolean KiemTraThongBaoKeHoach(){
        int count = 0;
        Cursor cursor = data.rawQuery("select nhanthongbao from tblkehoachtietkiem",null);
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

    //Kiem tra tat thong bao
    public boolean KiemTraTatThongBao(int vitri){
        int makehoach = arrayKeHoachTietKiem.get(vitri).makehoachtietkiem;
        int thongbao;
        Cursor cursor = data.rawQuery("select nhanthongbao from tblkehoachtietkiem where makehoachtietkiem = "+ makehoach,null);
        cursor.moveToFirst();
        thongbao = cursor.getInt(cursor.getColumnIndex("nhanthongbao"));
        if(thongbao == 1){
            return true;
        }
        Toast.makeText(activity,"Kế hoạch không có thông báo",Toast.LENGTH_SHORT).show();
        return false;
    }

}
