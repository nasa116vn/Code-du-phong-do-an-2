package com.example.doantotnghiep.ui.QuanLyVi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aldoapps.autoformatedittext.AutoFormatEditText;
import com.example.doantotnghiep.Database;
import com.example.doantotnghiep.R;
import com.example.doantotnghiep.adapter.AdapterVi;
import com.example.doantotnghiep.model.ArrayVi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuanLyViFragment extends Fragment {
    private Activity activity;
    private ImageButton button_ThemVi, button_LichSuChuyenTien;
    private Button button_LuuCapNhatVi, button_HuyCapNhatVi,button_ChuyenTien,button_HuyChuyenTien;
    private ListView listView_Vi;
    private View myFragment;
    private ArrayList<ArrayVi> arrayVi;
    private AdapterVi adapterVi;
    private SQLiteDatabase data;
    private Database database;
    private ArrayAdapter arrayAdapter;
    private Animation animation;
    private List<ArrayVi> list = null;
    private String taikhoan,tenvichuyen,tenvichuyentoi;
    private SharedPreferences sharedPreferences;
    private EditText editText_NhapTenViCapNhat, editText_NhapMoTaViCapNhat,
            editText_TenVi,editText_MoTaVi,editText_ViChuyen,editText_SoTienChuyen, editText_SoTienVi;
    private Button button_LuuVi,button_ThoatVi;
    private TextView textView_NgayThucHienChuyenTien;
    private int vitri = 0;
    private Cursor cursor;
    private Date date;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormatDialog;
    private AutoFormatEditText editText_TienCuaViChuyen, editText_TienCuaViNhan, editText_NhapSoTienViCapNhat; //NhapSoTienViCapNhat

    //Chuyen tien
    private List<ArrayVi> listDialog = null;
    private ArrayList<Integer> arrMaViDialog;
    private ArrayList<String> arrTenViDialog;
    private ArrayAdapter<String> adapterSpinnerDialog, adapterViChuyenDialog;
    private Spinner spinner_ViNhanDialog;
    private Double sotienvitoi = 0.0, sotienvichon = 0.0, sotiencanchuyen = 0.0, sotienchuyen = 0.0, sotienvi = 0.0;
    private int mavi;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_quanlyvi, container, false);
        return myFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        data = activity.openOrCreateDatabase("data.db", activity.MODE_PRIVATE, null);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);
        simpleDateFormatDialog = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();

        AnhXa();
        ThemVi();
        LayTenTaiKhoan();
        LoadTatCaVi();
        LichSuChuyenTien();

        listView_Vi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                vitri = position;
                return false;
            }
        });
        registerForContextMenu(listView_Vi);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        activity.getMenuInflater().inflate(R.menu.menu_vi, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_CapNhat: {
                SuaVi();
                return true;
            }
            case R.id.option_Xoa: {
                if (!KiemtraXoa(vitri)){
                    Toast.makeText(activity,"Ví mặc định không thể xóa",Toast.LENGTH_SHORT).show();
                }else {

                    XoaVi(vitri);
                }

                return true;
            }
            case R.id.option_ChuyenTien: {
                ChuyenTien();
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void ChuyenTien() {
        final Dialog d = new Dialog(getContext());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_chuyentien);
        d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        d.show();


        //Anh Xa
        editText_ViChuyen = d.findViewById(R.id.edit_ViChuyen);
        editText_SoTienChuyen = d.findViewById(R.id.edit_SoTienChuyen);
        editText_TienCuaViChuyen = d.findViewById(R.id.edit_TienCuaViChuyen);
        editText_TienCuaViNhan = d.findViewById(R.id.edit_TienCuaViNhan);
        textView_NgayThucHienChuyenTien = d.findViewById(R.id.tetxView_NgayThucHienChuyenTien);
        spinner_ViNhanDialog = d.findViewById(R.id.spinner_ViNhan);
        button_ChuyenTien = d.findViewById(R.id.btnChuyen);
        button_HuyChuyenTien = d.findViewById(R.id.btnHuyChuyenTien);

        calendar = Calendar.getInstance();

        editText_SoTienChuyen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b && editText_SoTienChuyen.getText().toString() != null){
                    if (editText_SoTienChuyen.getText().toString().equals("")) {
                        Toast.makeText(activity, "Bạn chưa nhập số tiền chuyển", Toast.LENGTH_SHORT).show();
                        editText_SoTienChuyen.startAnimation(animation);
                    }else {
                        GioiHanSoTienChuyenKhiChuyen();
                    }

                }

            }
        });
        LoadSpinnerDialog();
        LoadDanhSachViLenSpinnerDialog();
        HienThiThoiGian();

        //Lay tien theo vi chon o spinner
        spinner_ViNhanDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                mavi = position+1;
                LayTienTuVi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //Xu Ly
        editText_TienCuaViChuyen.setEnabled(false);
        editText_TienCuaViNhan.setEnabled(false);

        button_ChuyenTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tenvichuyen.equals(tenvichuyentoi)) {
                    Toast.makeText(activity,"Tên ví chuyển tiền bị trùng",Toast.LENGTH_SHORT).show();
                    spinner_ViNhanDialog.startAnimation(animation);
                }else  if (editText_SoTienChuyen.getText().toString().equals("")){
                    Toast.makeText(activity,"Bạn chưa nhập số tiền chuyển",Toast.LENGTH_SHORT).show();
                    editText_SoTienChuyen.startAnimation(animation);

                } else if (!KiemTraChuyenTien()) {
                    Toast.makeText(activity,"Số tiền chuyển vượt quá số tiền có trong ví",Toast.LENGTH_SHORT).show();
                    editText_SoTienChuyen.startAnimation(animation);

                }else if(!GioiHanSoTienChuyen()){
                    editText_SoTienChuyen.startAnimation(animation);
                    //editText_SoTienChuyen.setText(String.valueOf(0));
                    Toast.makeText(activity,"Số tiền nhập quá lớn",Toast.LENGTH_SHORT).show();
                }
                else {

                    XuLyChuyenTien();
                    d.dismiss();
                    LoadTatCaVi();
                }
            }
        });

        button_HuyChuyenTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

    }

    public void LayTienTuVi(){
        tenvichuyen = list.get(vitri).tenvi;
        editText_ViChuyen.setText(tenvichuyen);
        editText_ViChuyen.setEnabled(false);
        tenvichuyentoi = spinner_ViNhanDialog.getSelectedItem().toString();
        cursor = data.rawQuery("select* from tblvi where tentaikhoan ='"+taikhoan+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("tenvi")).equals(tenvichuyen)) {
                sotienvichon = cursor.getDouble(cursor.getColumnIndex("sotienvi"));
                editText_TienCuaViChuyen.setText(DoiSoSangTien((sotienvichon)));
            }
            if (cursor.getInt(cursor.getColumnIndex("mavi"))== mavi) {
                sotienvitoi = cursor.getDouble(cursor.getColumnIndex("sotienvi"));
                editText_TienCuaViNhan.setText(DoiSoSangTien(sotienvitoi));
            }
            cursor.moveToNext();
        }
    }

    public static String DoiSoSangTien(Double so) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("##########");
        return decimalFormat.format((so));
    }

    public boolean KiemTraChuyenTien() {
        sotiencanchuyen = Double.parseDouble(editText_SoTienChuyen.getText().toString());
        if (sotiencanchuyen > sotienvichon) {
            editText_SoTienChuyen.setText(DoiSoSangTien(sotienvichon));
            Double sotiencanchuyenmin = Double.parseDouble(editText_SoTienChuyen.getText().toString());
            sotiencanchuyen =  sotiencanchuyenmin;
            return false;
        }
        return true;
    }

    public boolean XuLyChuyenTien(){
        Double sotiendatru = 0.0;
        Double sotiendacong = 0.0;
        int malichsuchuyentien = 1;

        //cong tien vao vi chuyen toi
        sotiendacong = sotienvitoi + sotiencanchuyen;
        sotiendatru = sotienvichon - sotiencanchuyen;
        //Gan lai
        //cap nhat lai so tien da tru o vi chon
        ContentValues values = new ContentValues();
        values.put("sotienvi",sotiendatru);
        data.update("tblvi",values,"tenvi like '"+ tenvichuyen + "'",null);

        //cap nhat lai so tien da cong them o vi duoc chuyen
        ContentValues values1 = new ContentValues();
        values1.put("sotienvi",sotiendacong);
        data.update("tblvi",values1,"tenvi like '"+ tenvichuyentoi + "'",null);

        //Luu lich su vao database
        cursor = data.rawQuery("select malichsuchuyentien from tbllichsuchuyentien", null);
        if (cursor.moveToLast()) {
            malichsuchuyentien = cursor.getInt(cursor.getColumnIndex("malichsuchuyentien")) + 1;
        }
        String thongbao = "";
        ContentValues values2 = new ContentValues();
        values2.put("malichsuchuyentien", malichsuchuyentien);
        values2.put("tenvichuyen", editText_ViChuyen.getText().toString());
        values2.put("tenvinhan", spinner_ViNhanDialog.getSelectedItem().toString());
        values2.put("sotienchuyen", editText_SoTienChuyen.getText().toString());
        values2.put("ngaythuchien", textView_NgayThucHienChuyenTien.getText().toString());
        values2.put("mavi", editText_ViChuyen.getText().toString());
        values2.put("tentaikhoan", taikhoan);

        if (data.insert("tbllichsuchuyentien", null, values2) == -1) {
            return false;
        }
        Toast.makeText(activity, "Chuyển tiền thành công", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void LoadSpinnerDialog() {
        //Spinner Vi
        arrMaViDialog = new ArrayList<Integer>();
        arrTenViDialog = new ArrayList<String>();
        adapterViChuyenDialog = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrTenViDialog);
        adapterViChuyenDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ViNhanDialog.setAdapter(adapterViChuyenDialog);

    }

    public void LoadDanhSachViLenSpinnerDialog() {
        arrMaViDialog.clear();
        arrTenViDialog.clear();
        //cursor = data.query("tblvi", null, null, null, null, null, null);
        Cursor cursor = data.rawQuery("select * from tblvi where tentaikhoan = '" + taikhoan +"'", null);
        cursor.moveToFirst();
        listDialog = new ArrayList<ArrayVi>();
        while (!cursor.isAfterLast()) {
            arrMaViDialog.add(cursor.getInt(cursor.getColumnIndex("mavi")));
            arrTenViDialog.add(cursor.getString(cursor.getColumnIndex("tenvi")));
            cursor.moveToNext();
        }
        adapterViChuyenDialog.notifyDataSetChanged();
    }

    public void LayTenTaiKhoan()
    {
        sharedPreferences = getActivity().getSharedPreferences("tendangnhap", Context.MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("taikhoancanchuyen","khong tim thay");
    }

    public void ThemVi() {
        button_ThemVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(getContext());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_themvi);
                d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                d.show();

                //AnhXa
                button_LuuVi =  d.findViewById(R.id.button_LuuVi);
                button_ThoatVi =  d.findViewById(R.id.button_ThoatVi);
                editText_TenVi =  d.findViewById(R.id.editText_TenVi);
                editText_MoTaVi = d.findViewById(R.id.editText_MoTaVi);
                editText_SoTienVi =  d.findViewById(R.id.editText_SoTienVi);

                editText_SoTienVi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(!b && editText_SoTienVi.getText().toString() != null){
                            if (editText_SoTienVi.getText().toString().equals("")) {
                                editText_SoTienVi.startAnimation(animation);
                                Toast.makeText(activity, "Bạn chưa nhập số tiền ban đầu cho ví", Toast.LENGTH_SHORT).show();
                            }else {
                                GioiHanSoTienThemKhiChuyen();
                            }
                        }

                    }
                });

                //Xuly
               button_LuuVi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean tenvi = true;
                        String thongbao = "";

                        cursor = data.rawQuery("select * from tblvi where tentaikhoan ='" + taikhoan+"'", null);
                        cursor.moveToFirst();
                        while (cursor.isAfterLast()==false) {
                            if (cursor.getString(cursor.getColumnIndex("tenvi")).equals(editText_TenVi.getText().toString())) {
                                tenvi = false;
                            }
                            cursor.moveToNext();
                        }
                        if (!tenvi) {
                            Toast.makeText(activity, "Tên ví này đã tồn tại", Toast.LENGTH_SHORT).show();
                            editText_TenVi.startAnimation(animation);
                        } else if (editText_TenVi.getText().toString().equals("")) {
                            editText_TenVi.startAnimation(animation);
                            Toast.makeText(activity, "Bạn chưa nhập tên ví", Toast.LENGTH_SHORT).show();

                        } else if (editText_SoTienVi.getText().toString().equals("")) {
                            editText_SoTienVi.startAnimation(animation);
                            Toast.makeText(activity, "Bạn chưa nhập số tiền ban đầu cho ví", Toast.LENGTH_SHORT).show();
                        } else if (editText_MoTaVi.getText().toString().equals("")) {
                            editText_MoTaVi.startAnimation(animation);
                            Toast.makeText(activity, "Bạn chưa nhập mô tả cho ví", Toast.LENGTH_SHORT).show();

                        }else if(!GioiHanSoTienThem()){
                            editText_SoTienVi.startAnimation(animation);
                            editText_SoTienVi.setText(String.valueOf(0));
                            Toast.makeText(activity,"Số tiền nhập quá lớn",Toast.LENGTH_SHORT).show();
                        }
                        else if (GioiHanSoVi()){
                            int mavi = 1;
                            cursor = data.rawQuery("select mavi from tblvi", null);
                            if (cursor.moveToLast()) {
                                mavi = cursor.getInt(cursor.getColumnIndex("mavi")) + 1;
                            }

                            ContentValues values = new ContentValues();
                            values.put("mavi", mavi);
                            values.put("tenvi", editText_TenVi.getText().toString());
                            values.put("motavi", editText_MoTaVi.getText().toString());
                            values.put("sotienvi", editText_SoTienVi.getText().toString());
                            values.put("tentaikhoan", taikhoan);

                            if (data.insert("tblvi", null, values) != -1) {
                                thongbao = "Thêm ví thành công";
                                d.dismiss();
                            } else {
                                thongbao = "Thêm ví không thành công";
                            }
                            Toast.makeText(activity, thongbao, Toast.LENGTH_LONG).show();
                        } else {
//                            editText_TenVi.startAnimation(animation);
//                            editText_MoTaVi.startAnimation(animation);
//                            editText_SoTienVi.startAnimation(animation);
                            thongbao="Số ví đã đạt tối đa";
                            Toast.makeText(activity, thongbao, Toast.LENGTH_LONG).show();
                            d.dismiss();
                        }
                        LoadTatCaVi();
                    }
                });
                button_ThoatVi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
            }
        });
    }


    public boolean GioiHanSoVi() {
        int count = 0;
        cursor = data.rawQuery("select * from tblvi where tentaikhoan = '"+ taikhoan +"'", null);
        while (cursor.isAfterLast() == false) {
            count++;

            cursor.moveToNext();
        }
        if (count < 6) {
            return true;
        }
        return false;
    }

    public void AnhXa() {
        button_ThemVi = (ImageButton) myFragment.findViewById(R.id.button_ThemVi);
        button_LichSuChuyenTien = (ImageButton) myFragment.findViewById(R.id.button_LichSuChuyenTien);
        listView_Vi = (ListView) myFragment.findViewById(R.id.listView_Vi);
    }

    public void LoadTatCaVi() {
        //Cursor cursor = data.query("tblvi", null, null, null, null, null, null);
        Cursor cursor = data.rawQuery("select * from tblvi where tentaikhoan = '" + taikhoan + "'", null);
        cursor.moveToFirst();
        list = new ArrayList<ArrayVi>();
        while (!cursor.isAfterLast()) {
            ArrayVi a = new ArrayVi();
            a.setMavi(cursor.getInt(0));
            a.setTenvi(cursor.getString(1));
            a.setMotavi(cursor.getString(2));
            a.setSotienvi(cursor.getInt(3));
            list.add(a);

            cursor.moveToNext();
        }
        cursor.close();
        adapterVi = new AdapterVi(getContext(), R.layout.adapter_quanlyvi_item, list);
        listView_Vi.setAdapter(adapterVi);
    }

    public void SuaVi() {
                 // lay ten vi de cap nhat
                final Dialog d = new Dialog(getContext());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_capnhatvi);
                d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                d.show();
                //AnhXa
                editText_NhapTenViCapNhat =  d.findViewById(R.id.editText_NhapTenViCapNhat);
                editText_NhapMoTaViCapNhat =  d.findViewById(R.id.editText_NhapMoTaViCapNhat);
                editText_NhapSoTienViCapNhat =  d.findViewById(R.id.editText_NhapSoTienCapNhat);
                editText_NhapSoTienViCapNhat.setEnabled(false);
                button_LuuCapNhatVi = d.findViewById(R.id.button_LuuCapNhatVi);
                button_HuyCapNhatVi =  d.findViewById(R.id.button_HuyCapNhatVi);
                //LayThongTinViLenDialog
                if (!LayThongTinLenDialogSua()){
                    editText_NhapTenViCapNhat.setEnabled(false);
                }

                //XuLy
                button_LuuCapNhatVi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(CapNhatVi())
                        {
                            d.dismiss();
                            LoadTatCaVi();
                        }
                    }
                });
                button_HuyCapNhatVi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
            }

    public boolean CapNhatVi() {
        String thongbao = "";
        boolean tenviht = true;
        final int maviht = list.get(vitri).mavi;
        cursor = data.rawQuery("select * from tblvi where tentaikhoan ='" + taikhoan+"'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false) {
            if (cursor.getString(cursor.getColumnIndex("tenvi")).equals(editText_NhapTenViCapNhat.getText().toString())) {
                tenviht = false;
            }
            cursor.moveToNext();
        }
        if (tenviht == false) {
            Toast.makeText(activity, "Tên ví này đã tồn tại", Toast.LENGTH_SHORT).show();
            editText_NhapTenViCapNhat.startAnimation(animation);
        } else if (editText_NhapTenViCapNhat.getText().toString().equals("")) {
            editText_NhapTenViCapNhat.startAnimation(animation);
            Toast.makeText(activity,"Bạn chưa nhập tên ví",Toast.LENGTH_LONG).show();
        } else if (editText_NhapMoTaViCapNhat.getText().toString().equals("")) {
            editText_NhapMoTaViCapNhat.startAnimation(animation);
            Toast.makeText(activity,"Bạn chưa nhập mô tả ví",Toast.LENGTH_LONG).show();
        } else {
            ContentValues values = new ContentValues();
            values.put("tenvi",editText_NhapTenViCapNhat.getText().toString());
            values.put("motavi",editText_NhapMoTaViCapNhat.getText().toString());
            data.update("tblvi",values,"mavi = "+ maviht ,null);

            //Cap nhat ten vi vao ten vi uu tien ben danh muc thu chi
            ContentValues values1 = new ContentValues();
            values1.put("tenviuutien",editText_NhapTenViCapNhat.getText().toString());
            data.update("tbldanhmucthuchi",values1,"mavi = "+ maviht,null);
            thongbao = "Cập nhật thành công";
            Toast.makeText(activity,thongbao,Toast.LENGTH_LONG).show();
            return true;
        }
        thongbao = "Cập nhật không thành công";
        Toast.makeText(activity,thongbao,Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean LayThongTinLenDialogSua(){
        final String tenviht = list.get(vitri).tenvi;
        Cursor cursor = data.rawQuery("select * from tblvi where tenvi like '"+ tenviht +"'" + " and tentaikhoan ='" + taikhoan+"'" , null);
        cursor.moveToFirst();
        String tenvi1 = cursor.getString(1);
        String motavi1 = cursor.getString(2);
        String sotienvi1 = String.valueOf(cursor.getInt(3));

        editText_NhapTenViCapNhat.setText(tenvi1);
        editText_NhapMoTaViCapNhat.setText(motavi1);
        editText_NhapSoTienViCapNhat.setText(sotienvi1.replace(".0", ""));

        if(editText_NhapTenViCapNhat.getText().toString().equals("Cá nhân")){
            return false;
        }else if(editText_NhapTenViCapNhat.getText().toString().equals("Gia đình")){
            return false;
        }else if (editText_NhapTenViCapNhat.getText().toString().equals("Tiết kiệm")){
            return false;
        }else {
            return true;
        }

    }

    public void XoaVi(int vitri1) {

        HamXoaVi(list.get(vitri1).tenvi);
    }

    public void HamXoaVi(final String tenvi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Thông báo !");
        builder.setMessage("Bạn có chắc chắn muốn xóa ví này ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.rawQuery("delete from tblvi where tenvi = '" + tenvi + "'", null).moveToFirst();
                LoadTatCaVi();
                Toast.makeText(activity, "Xóa thành công", Toast.LENGTH_SHORT).show();
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

    public void HienThiThoiGian() {
        date = calendar.getTime();
        textView_NgayThucHienChuyenTien.setText(simpleDateFormatDialog.format(date));
    }

    public void LichSuChuyenTien() {
        button_LichSuChuyenTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LichSuChuyenTienActivity.class);
                startActivity(i);
            }
        });
    }

    //Kiem tra
    public boolean KiemtraXoa(int vitrivi){
        String tenvichon = list.get(vitrivi).tenvi;
        if (tenvichon.equals("Cá nhân")){
            return false;
        }else
        if (tenvichon.equals("Gia đình")){
            return false;
        }else
        if (tenvichon.equals("Tiết kiệm")){
            return false;
        }else {
            return true;
        }

    }
    //Gioi han tien
    public boolean GioiHanSoTienThem(){
        sotienvi = Double.parseDouble(editText_SoTienVi.getText().toString());
        if(sotienvi > 100000000){

            return false;
        }
        return true;
    }
    public void GioiHanSoTienThemKhiChuyen(){
        sotienvi = Double.parseDouble(editText_SoTienVi.getText().toString());
        if(sotienvi > 100000000){
            editText_SoTienVi.startAnimation(animation);
            editText_SoTienVi.setText(String.valueOf(0));
            Toast.makeText(activity,"Số tiền nhập quá lớn",Toast.LENGTH_SHORT).show();

        }

    }

    public void GioiHanSoTienChuyenKhiChuyen(){
        sotienchuyen = Double.parseDouble(editText_SoTienChuyen.getText().toString());
        if(sotienchuyen > 100000000){
            editText_SoTienChuyen.startAnimation(animation);
            editText_SoTienChuyen.setText(String.valueOf(0));
            Toast.makeText(activity,"Số tiền nhập quá lớn",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean GioiHanSoTienChuyen(){
        sotienchuyen = Double.parseDouble(editText_SoTienChuyen.getText().toString());
        if(sotienchuyen > 100000000){
            return false;
        }
        return true;
    }
}