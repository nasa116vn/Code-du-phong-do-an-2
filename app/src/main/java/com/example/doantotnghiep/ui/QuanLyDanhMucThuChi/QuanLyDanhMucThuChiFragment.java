package com.example.doantotnghiep.ui.QuanLyDanhMucThuChi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import com.example.doantotnghiep.R;
import com.example.doantotnghiep.adapter.AdapterDanhMucThuChi;
import com.example.doantotnghiep.model.ArrayDanhMucThuChi;
import com.example.doantotnghiep.model.ArrayVi;

import java.util.ArrayList;
import java.util.List;

public class QuanLyDanhMucThuChiFragment extends Fragment {
    private View myFragment;
    private String taikhoan;
    private Activity activity;
    private SQLiteDatabase data;
    private Animation animation;
    private String[] arrSpinner, arrSpinnerDialog,arrSpinnerDialogSua;
    private ArrayAdapter<String> adapterSpinner, adapterSpinnerDialog, adapterViUuTienDialog,adapterSpinnerDialogSua,adapterViUuTienDialogSua;
    private Spinner spinner_LoaiKhoan, spinner_LoaiKhoanDialog, spinner_ViUuTienChoDanhMuc, spinner_SuaViUuTienChoDanhMuc,spinner_SuaLoaiKhoanDialog;
    private ImageButton button_ThemDanhMuc;
    private EditText editText_TenDanhMucThuChi,editText_SuaTenDanhMucThuChi;
    private ArrayList<ArrayDanhMucThuChi> arrDanhMucThuChi;
    private AdapterDanhMucThuChi adapterDanhMucThuChi;
    private List<ArrayDanhMucThuChi> list = null;
    private List<ArrayVi> listViDialog = null,listViDialogSua = null;
    private ListView listView_DanhMucThuChi;
    private SharedPreferences sharedPreferences;
    private Button button_LuuThemDanhMucThuChi, button_HuyThemDanhMucThuChi,button_LuuSuaDanhMucThuChi,button_HuySuaDanhMucThuChi;
    private ArrayList<Integer> arrMaViDialog,arrMaViDialogSua;
    private ArrayList<String> arrTenViDialog,arrTenViDialogSua;
    private int vitri,mavidanhmucsua;
    Cursor cursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_quanlydanhmucthuchi, container, false);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        data = activity.openOrCreateDatabase("data.db", activity.MODE_PRIVATE, null);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);

        sharedPreferences = getActivity().getSharedPreferences("tendangnhap", Context.MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("taikhoancanchuyen","khong tim thay");


        AnhXa();
        LoadSpinner();
        LoadTatCaDanhMucThuChi();
        //XoaDanhMuc();
        ThemDanhMucThuChiDialog();
        listView_DanhMucThuChi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                vitri = position;
                return false;
            }
        });
        registerForContextMenu(listView_DanhMucThuChi);
    }

    //Menu
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        activity.getMenuInflater().inflate(R.menu.menu_danhmucthuchi, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_CapNhatDanhMuc: {
                SuaDanhMucDialog();
                return true;
            }
            case R.id.option_XoaDanhMuc: {
                if(KiemtratrungDanhmuc(vitri)){
                    XoaDanhMuc(vitri);
                }else {
                    Toast.makeText(activity,"Danh mục mặc định không thể xóa",Toast.LENGTH_SHORT).show();
                }

                return true;
            }

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void AnhXa() {
        listView_DanhMucThuChi = (ListView) myFragment.findViewById(R.id.listView_DanhMucThuChi);

        spinner_LoaiKhoan = (Spinner) myFragment.findViewById(R.id.spinner_LoaiKhoan);

        button_ThemDanhMuc = (ImageButton) myFragment.findViewById(R.id.button_ThemDanhMuc);

        editText_TenDanhMucThuChi = (EditText) myFragment.findViewById(R.id.editText_TenDanhMucThuChi);
    }

    public void LoadSpinner()  {
        //Spinner Loai thu chi
        arrSpinner = getResources().getStringArray(R.array.loaithuchi);
        adapterSpinner = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrSpinner);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_LoaiKhoan.setAdapter(adapterSpinner);
        spinner_LoaiKhoan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadTatCaDanhMucThuChi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void LoadSpinnerDialog() {
        //Spinner loai khoan dialog
        arrSpinnerDialog = getResources().getStringArray(R.array.loaithuchi);
        adapterSpinnerDialog = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrSpinnerDialog);
        adapterSpinnerDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_LoaiKhoanDialog.setAdapter(adapterSpinnerDialog);

        //Spinner vi dialog
        arrMaViDialog = new ArrayList<Integer>();
        arrTenViDialog = new ArrayList<String>();
        adapterViUuTienDialog = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrTenViDialog);
        adapterViUuTienDialog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ViUuTienChoDanhMuc.setAdapter(adapterViUuTienDialog);
    }

    public void LoadTatCaDanhMucThuChi() {
        String dieukien = "= '" + spinner_LoaiKhoan.getSelectedItem().toString() + "'";
        Cursor cursor = data.rawQuery("select madanhmuc, tendanhmuc, loaikhoan from tbldanhmucthuchi" +
                " where loaikhoan" + dieukien + " and tentaikhoan = '" + taikhoan + "'", null);
        cursor.moveToFirst();
        list = new ArrayList<ArrayDanhMucThuChi>();
        while (cursor.isAfterLast() == false) {
            ArrayDanhMucThuChi a = new ArrayDanhMucThuChi();
            a.setMadanhmuc(cursor.getInt(0));
            a.setTendanhmuc(cursor.getString(1));
            a.setLoaikhoan(cursor.getString(2));
            list.add(a);

            cursor.moveToNext();
        }
        cursor.close();

        adapterDanhMucThuChi = new AdapterDanhMucThuChi(getContext(), R.layout.adapter_quanlydanhmucthuchi_item, list);
        listView_DanhMucThuChi.setAdapter(adapterDanhMucThuChi);
    }

    public void LoadDanhSachViLenSpinnerDialog() {
        arrMaViDialog.clear();
        arrTenViDialog.clear();
        Cursor cursor = data.rawQuery("select * from tblvi where tentaikhoan = '" + taikhoan +"'", null);
        cursor.moveToFirst();
        listViDialog = new ArrayList<ArrayVi>();
        while (cursor.isAfterLast() == false) {
            arrMaViDialog.add(cursor.getInt(cursor.getColumnIndex("mavi")));
            arrTenViDialog.add(cursor.getString(cursor.getColumnIndex("tenvi")));
            cursor.moveToNext();
        }
        adapterViUuTienDialog.notifyDataSetChanged();
    }


    //Sua danh muc
    public void SuaDanhMucDialog(){

        final Dialog d = new Dialog(getContext());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_capnhatdanhmucthuchi);
        d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        d.show();

        //AnhXa
        editText_SuaTenDanhMucThuChi = d.findViewById(R.id.editText_SuaTenDanhMucThuChi);
        spinner_SuaLoaiKhoanDialog = d.findViewById(R.id.spinner_SuaLoaiKhoanDialog);
        spinner_SuaViUuTienChoDanhMuc = d.findViewById(R.id.spinner_SuaViUuTienChoDanhMuc);
        button_LuuSuaDanhMucThuChi = d.findViewById(R.id.button_LuuSuaDanhMucThuChi);
        button_HuySuaDanhMucThuChi = d.findViewById(R.id.button_HuySuaDanhMucThuChi);

        //Xuly
        LoadSpinnerDialogSua();
        LoadDanhSachViLenSpinnerDialogSua();
        //Lay thong tin dua len dialog
        if(!LayThongTinDanhMucSua()){
            editText_SuaTenDanhMucThuChi.setEnabled(false);
            spinner_SuaLoaiKhoanDialog.setEnabled(false);
        }

        //Xu ly nut
        button_LuuSuaDanhMucThuChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(CapNhatDanhMuc()){
                   d.dismiss();
                   LoadTatCaDanhMucThuChi();
               }
            }
        });

        button_HuySuaDanhMucThuChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

    }

    public boolean CapNhatDanhMuc(){
        String thongbao = "";
        boolean tendanhmuc = true;
        String tenviuutien = spinner_SuaViUuTienChoDanhMuc.getSelectedItem().toString();
        int maviuutien = 0;
        final int madanhmucht = list.get(vitri).madanhmuc;

        cursor = data.rawQuery("select* from tblvi where tenvi = '"+ tenviuutien +"'" + "and tentaikhoan ='" + taikhoan +"'",null);
        cursor.moveToFirst();
        maviuutien = cursor.getInt(cursor.getColumnIndex("mavi"));

        Cursor cursor = data.rawQuery("select tendanhmuc from tbldanhmucthuchi where tentaikhoan ='"+taikhoan+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals(editText_SuaTenDanhMucThuChi.getText().toString())) {
                tendanhmuc = false;
            }
            cursor.moveToNext();
        }
        cursor.close();
        if (tendanhmuc == false) {
            Toast.makeText(activity, "Tên danh mục này đã tồn tại", Toast.LENGTH_SHORT).show();
            editText_SuaTenDanhMucThuChi.startAnimation(animation);
        } else if (editText_SuaTenDanhMucThuChi.getText().toString().equals("")) {
            editText_SuaTenDanhMucThuChi.startAnimation(animation);
            Toast.makeText(activity,"Bạn chưa nhập tên danh mục",Toast.LENGTH_LONG).show();
        } else {
            ContentValues values = new ContentValues();
            values.put("tendanhmuc",editText_SuaTenDanhMucThuChi.getText().toString());
            values.put("loaikhoan",spinner_SuaLoaiKhoanDialog.getSelectedItem().toString());
            values.put("mavi",maviuutien);
            values.put("tenviuutien",tenviuutien);
            data.update("tbldanhmucthuchi",values,"madanhmuc = "+ madanhmucht,null);
            thongbao = "Cập nhật thành công";
            Toast.makeText(activity,thongbao,Toast.LENGTH_LONG).show();
            return true;
        }
        //thongbao = "Cập nhật không thành công";
        //Toast.makeText(activity,thongbao,Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean LayThongTinDanhMucSua(){
        final int madanhmucht = list.get(vitri).madanhmuc;
        cursor = data.rawQuery("select * from tbldanhmucthuchi" +  " where madanhmuc = "+ madanhmucht +" and tentaikhoan ='"+ taikhoan+  "'" , null);
        cursor.moveToFirst();
        String tendanhmucsua = cursor.getString(cursor.getColumnIndex("tendanhmuc"));
        String loaidanhmuc = cursor.getString(cursor.getColumnIndex("loaikhoan"));
        String tenviuutien = cursor.getString(cursor.getColumnIndex("tenviuutien"));


        if(loaidanhmuc.equals("Khoản thu"))
        {
            spinner_SuaLoaiKhoanDialog.setSelection(0);
        }else {
            spinner_SuaLoaiKhoanDialog.setSelection(1);
        }

        spinner_SuaViUuTienChoDanhMuc.setSelection(LayViTriUuTien(spinner_SuaViUuTienChoDanhMuc,tenviuutien));

        editText_SuaTenDanhMucThuChi.setText(tendanhmucsua);

        if(editText_SuaTenDanhMucThuChi.getText().toString().equals("Tiền lương")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Khoản thu khác")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Được cho")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Tiền thưởng")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Làm thêm")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Ăn uống")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Học tập")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Chi phí đi lại")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Khoản chi khác")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Tiền nhà trọ")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Tiền điện nước")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Dụng cụ sinh hoạt cá nhân")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Quần áo")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Giải trí")){
            return false;
        }else
        if (editText_SuaTenDanhMucThuChi.getText().toString().equals("Du lịch")){
            return false;
        }else {
            return true;
        }
    }

    //Lay vi tri vi uu tien trong sua danh muc
    public int LayViTriUuTien(Spinner spinnerVi,String tenvican){
        for (int i = 0;i < spinnerVi.getCount();i++){
            if(spinnerVi.getItemAtPosition(i).toString().equalsIgnoreCase(tenvican)){
                return i;
            }
        }
        return 0;
    }

    public void LoadSpinnerDialogSua() {
        //Spinner loai khoan dialog
        arrSpinnerDialogSua = getResources().getStringArray(R.array.loaithuchi);
        adapterSpinnerDialogSua = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrSpinnerDialogSua);
        adapterSpinnerDialogSua.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SuaLoaiKhoanDialog.setAdapter(adapterSpinnerDialogSua);

        //Spinner vi dialog
        arrMaViDialogSua = new ArrayList<Integer>();
        arrTenViDialogSua = new ArrayList<String>();
        adapterViUuTienDialogSua = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrTenViDialogSua);
        adapterViUuTienDialogSua.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SuaViUuTienChoDanhMuc.setAdapter(adapterViUuTienDialogSua);
    }

    public void LoadDanhSachViLenSpinnerDialogSua() {
        arrMaViDialogSua.clear();
        arrTenViDialogSua.clear();
        Cursor cursor = data.rawQuery("select * from tblvi where tentaikhoan = '" + taikhoan +"'", null);
        cursor.moveToFirst();
        listViDialogSua = new ArrayList<ArrayVi>();
        while (cursor.isAfterLast() == false) {
            arrMaViDialogSua.add(cursor.getInt(cursor.getColumnIndex("mavi")));
            arrTenViDialogSua.add(cursor.getString(cursor.getColumnIndex("tenvi")));
            cursor.moveToNext();
        }
        adapterViUuTienDialogSua.notifyDataSetChanged();
    }



    //Xoa danh muc
    public void XoaDanhMuc(int vitrixoa) {
        HamXoaDanhMuc(list.get(vitrixoa).tendanhmuc);
    }

    public void HamXoaDanhMuc(final String tendanhmuc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Thông báo !");
        builder.setMessage("Bạn có chắc chắn muốn xóa danh mục này ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.rawQuery("delete from tbldanhmucthuchi " +
                        "where tendanhmuc = '" + tendanhmuc + "'", null).moveToFirst();
                LoadTatCaDanhMucThuChi();
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

    public void ThemDanhMucThuChiDialog() {
        button_ThemDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_edittext);
                final Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.dialog_themdanhmucthuchi);
                d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                d.show();

                //Anh xa
                editText_TenDanhMucThuChi = (EditText) d.findViewById(R.id.editText_TenDanhMucThuChi);
                spinner_LoaiKhoanDialog = (Spinner) d.findViewById(R.id.spinner_LoaiKhoanDialog);
                spinner_ViUuTienChoDanhMuc = (Spinner) d.findViewById(R.id.spinner_ViUuTienChoDanhMuc);
                button_LuuThemDanhMucThuChi = (Button) d.findViewById(R.id.button_LuuThemDanhMucThuChi);
                button_HuyThemDanhMucThuChi = (Button) d.findViewById(R.id.button_HuyThemDanhMucThuChi);

                //Xu ly
                LoadSpinnerDialog();
                LoadDanhSachViLenSpinnerDialog();

                button_HuyThemDanhMucThuChi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                button_LuuThemDanhMucThuChi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String thongbao = "";

                        boolean tendanhmuc = true;
                        // chi lay ten danh muc cua tai khoan de kiem tra
                        Cursor cursor = data.rawQuery("select tendanhmuc from tbldanhmucthuchi where tentaikhoan ='"+taikhoan+"'", null);
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals(editText_TenDanhMucThuChi.getText().toString())) {
                                tendanhmuc = false;
                            }
                            cursor.moveToNext();
                        }
                        cursor.close();
                        if (tendanhmuc == false) {
                            Toast.makeText(activity, "Tên danh mục này đã tồn tại", Toast.LENGTH_SHORT).show();
                            editText_TenDanhMucThuChi.startAnimation(animation);
                        } else if (editText_TenDanhMucThuChi.getText().toString().equals("")) {
                            Toast.makeText(activity, "Bạn chưa nhập tên danh mục", Toast.LENGTH_SHORT).show();
                            editText_TenDanhMucThuChi.startAnimation(animation);
                        } else {
                            int madanhmuc = 1;
                            //Lay tat ca ma danh muc co trong bang
                            Cursor cursorMa = data.rawQuery("select madanhmuc from tbldanhmucthuchi", null);
                            if (cursorMa.moveToLast()) {
                                madanhmuc = cursorMa.getInt(cursorMa.getColumnIndex("madanhmuc")) + 1;
                            }


                            //Da sua luu ma vi sang int
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("madanhmuc", madanhmuc);
                            contentValues.put("tendanhmuc", editText_TenDanhMucThuChi.getText().toString());
                            contentValues.put("loaikhoan", spinner_LoaiKhoanDialog.getSelectedItem().toString());
                            contentValues.put("mavi", arrMaViDialog.get(spinner_ViUuTienChoDanhMuc.getSelectedItemPosition()));
                            contentValues.put("tenviuutien", spinner_ViUuTienChoDanhMuc.getSelectedItem().toString()); // moi them
                            contentValues.put("tentaikhoan", taikhoan);

                            if (data.insert("tbldanhmucthuchi", null, contentValues) != -1) {
                                thongbao = "Thêm danh mục thành công";
                                d.dismiss();
                                //load list view o day
                                LoadTatCaDanhMucThuChi();
                            }
                            Toast.makeText(getActivity(), thongbao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //Kiem tra
    public boolean KiemtratrungDanhmuc(int vitrixoa){
        String tendanhmucxoa = list.get(vitrixoa).tendanhmuc;
        if(tendanhmucxoa.equals("Tiền lương")){
            return false;
        }else
        if(tendanhmucxoa.equals("Khoản thu khác")){
            return false;
        }else
        if(tendanhmucxoa.equals("Được cho")){
            return false;
        }else
        if(tendanhmucxoa.equals("Tiền thưởng")){
            return false;
        }else
        if(tendanhmucxoa.equals("Làm thêm")){
            return false;
        }else
        if(tendanhmucxoa.equals("Ăn uống")){
            return false;
        }else
        if(tendanhmucxoa.equals("Học tập")){
            return false;
        }else
        if(tendanhmucxoa.equals("Chi phí đi lại")){
            return false;
        }else
        if(tendanhmucxoa.equals("Khoản chi khác")){
            return false;
        }else
        if(tendanhmucxoa.equals("Tiền nhà trọ")){
            return false;
        }else
        if(tendanhmucxoa.equals("Tiền điện nước")){
            return false;
        }else
        if(tendanhmucxoa.equals("Dụng cụ sinh hoạt các nhân")){
            return false;
        }else
        if(tendanhmucxoa.equals("Quần áo")){
            return false;
        }else
        if(tendanhmucxoa.equals("Giải trí")){
            return false;
        }else
        if(tendanhmucxoa.equals("Du lịch")){
            return false;
        }else {
            return true;
        }
    }
}
