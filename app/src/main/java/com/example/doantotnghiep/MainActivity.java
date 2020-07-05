package com.example.doantotnghiep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.doantotnghiep.model.ThuChiLap;
import com.example.doantotnghiep.thongbao.ThongBaoKeHoachReciver;
import com.example.doantotnghiep.thongbao.ThongBaoThuChiReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase data;
    private SharedPreferences share, sharetentaikhoan;
    private LinearLayout layout;
    private EditText editText_TenTaiKhoanDangNhap, editText_MatKhauDangNhap, maso, matkhau1, matkhau2;
    private Button thaydoi, huy;
    private ImageView imglogo, imglogo1;
    private Animation animation;
    private CheckBox checkBox_GhiNho;
    Database database;
    private String tendangnhap;
    private SimpleDateFormat simpleDateFormat;
    List<ThuChiLap> listthuchi;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = openOrCreateDatabase("data.db", MODE_PRIVATE, null);
        share = getSharedPreferences("taikhoan", MODE_PRIVATE);
        sharetentaikhoan = getSharedPreferences("tentaikhoan", MODE_PRIVATE);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        AnhXa();
        TaoBangCoSoDuLieu();
    }

    private void TaoBangCoSoDuLieu() {
        try {
            //Table Tai khoan
            data.execSQL("create table if not exists tbltaikhoan(tentaikhoan text primary key, masobimat text, matkhau text, " +
                    "hovaten text, diachi text, sodienthoai text, email text);");

            //Table Vi
            data.execSQL("create table if not exists tblvi(mavi int primary key, tenvi text, motavi text, sotienvi real, " +
                    "tentaikhoan text constraint tentaikhoan references tbltaikhoan(tentaikhoan) on delete cascade);");

            //Table Danh muc thu chi
            data.execSQL("create table if not exists tbldanhmucthuchi(madanhmuc int primary key, tendanhmuc text, loaikhoan text, tenviuutien text, " +
                    "tentaikhoan text constraint tentaikhoan references tbltaikhoan(tentaikhoan), " +
                    "mavi int constraint mavi references tblvi(mavi) on delete cascade)");

            //Table Thu chi
            data.execSQL("create table if not exists tblthuchi(mathuchi int primary key, loaithuchi text, sotienthuchi real, mota text, " +
                    "ngaythuchien date, giothuchi int, phutthuchi int, nhanthongbao int,hangngay int, mavi int constraint mavi references tblvi(mavi) on delete cascade, " +
                    "tentaikhoan text constraint tentaikhoan references tbltaikhoan(tentaikhoan) on delete cascade, " +
                    "madanhmuc int constraint madanhmuc references tbldanhmucthuchi(madanhmuc) on delete cascade)");
                        // madanhmuc int

            //Table Ke hoach tiet kiem
            data.execSQL("create table if not exists tblkehoachtietkiem(makehoachtietkiem int primary key, tenkehoachtietkiem text, ngaybatdaukehoachtietkiem date, " +
                    "ngayketthuckehoachtietkiem date, sotienkehoachtietkiem real, sotiendatietkiem real, nhanthongbao int, trangthai text, " +
                    "tentaikhoan text constraint tentaikhoan references tbltaikhoan(tentaikhoan) on delete cascade)");

            //Table Thu chi cho ke hoach tiet kiem
            data.execSQL("create table if not exists tblthuchichokehoachtietkiem(mathuchichokehoachtietkiem int primary key, " +
                    "loaithuchichokehoachtietkiem text, sotienthuchichokehoachtietkiem real, motathuchichokehoachtietkiem text, " +
                    "ngaythuchienthuchichokehoachtietkiem date, " +
                    "makehoachtietkiem int constraint makehoachtietkiem references tblkehoachtietkiem(makehoachtietkiem) on delete cascade, " +
                    "tentaikhoan text constraint tentaikhoan references tbltaikhoan(tentaikhoan) on delete cascade)");

            //Table Lich su chuyen tien
            data.execSQL("create table if not exists tbllichsuchuyentien(malichsuchuyentien int primary key, tenvichuyen text, tenvinhan text, sotienchuyen real, ngaythuchien date, " +
                    "mavi int constraint mavi references tblvi(mavi), " +
                    "tentaikhoan text constraint tentaikhoan references tbltaikhoan(tentaikhoan) on delete cascade)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void AnhXa() {
        database = new Database(this);

        imglogo = (ImageView) findViewById(R.id.imgLogo);
        imglogo1 = (ImageView) findViewById(R.id.imgLogo1);
        animation = AnimationUtils.loadAnimation(this, R.anim.animation_logo);
        imglogo.startAnimation(animation);
        imglogo1.startAnimation(animation);
        layout = (LinearLayout) findViewById(R.id.layoutDangnhap);
        layout.setVisibility(View.GONE);
        editText_TenTaiKhoanDangNhap = (EditText) findViewById(R.id.editText_TenTaiKhoanDangNhap);
        editText_MatKhauDangNhap = (EditText) findViewById(R.id.editText_MatKhauDangNhap);
        checkBox_GhiNho = (CheckBox) findViewById(R.id.checkBox_GhiNho);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                imglogo1.setVisibility(View.VISIBLE);
                imglogo.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
            }
        }, 2000);


    }

    public void DangKyTaiKhoan(View v) {
        XoaTrang();
        Intent intent = new Intent(this, DangKyActivity.class);
        startActivity(intent);
    }

    private void XoaTrang() {
        editText_TenTaiKhoanDangNhap.setText(null);
        editText_MatKhauDangNhap.setText(null);
    }

    public void DangNhap(View v) throws ParseException {
        animation = AnimationUtils.loadAnimation(this, R.anim.animation_edittext);
        boolean tk = false, mk = true;
        Cursor c = data.rawQuery("select * from tbltaikhoan", null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            if (c.getString(c.getColumnIndex("tentaikhoan")).equals(editText_TenTaiKhoanDangNhap.getText().toString())) {
                tk = true;
                if (c.getString(c.getColumnIndex("matkhau")).equals(editText_MatKhauDangNhap.getText().toString())) {
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("taikhoan", c.getString(c.getColumnIndex("tentaikhoan")));
                    startActivityForResult(intent, 2);


                    //Chuyen ten tai khoan di cac class
                    tendangnhap = c.getString(c.getColumnIndex("tentaikhoan"));
                    SharedPreferences sharedPreferences = getSharedPreferences("tendangnhap",MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString("taikhoancanchuyen",tendangnhap);
                    editor.commit();
                    //Tao san cac vi va danh muc

                    if(KiemtratrungVi()){
                        TaoSanVi();
                    }
                    if(KiemtratrungDanhmuc()){
                        TaoSanDanhMuc(); //Tao rieng tung ham danh muc
                    }

                    KiemTraThongBao();
                    KiemTraThongBaoKeHoach();
                    DanhSachThuChiLap();
                    TaoThuChiHangNgay();
                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                } else {
                    mk = false;
                }
            }
            c.moveToNext();
        }
        if (editText_TenTaiKhoanDangNhap.getText().toString().equals("")) {
            editText_TenTaiKhoanDangNhap.startAnimation(animation);
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên tài khoản", Toast.LENGTH_SHORT).show();
        } else if (editText_MatKhauDangNhap.getText().toString().equals("")) {
            editText_MatKhauDangNhap.startAnimation(animation);
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
        } else if (tk == false) {
            Toast.makeText(getApplicationContext(), "Tài khoản này không tồn tại", Toast.LENGTH_SHORT).show();
            editText_TenTaiKhoanDangNhap.startAnimation(animation);
        } else if (mk == false) {
            Toast.makeText(getApplicationContext(), "Không đúng mật khẩu", Toast.LENGTH_SHORT).show();
            editText_MatKhauDangNhap.startAnimation(animation);
        }
    }

    //Kiem tra ngay thong bao
    // Cap nhat lai thuoc tinh nhanthongbao thu chi
    public void KiemTraThongBao() throws ParseException {
        int mathuchi,gio,phut;
        String ngaythongbao,ngayhientai;
        Calendar calendar = Calendar.getInstance();

        int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        int thang = calendar.get(Calendar.MONTH) +1 ;
        int nam = calendar.get(Calendar.YEAR);

        int gio12 = calendar.get(Calendar.HOUR);
        int gio24 = calendar.get(Calendar.HOUR_OF_DAY);
        int phutht = calendar.get(Calendar.MINUTE);

        ngayhientai = ngay + "/" + thang +"/"+ nam;


        Cursor cursor = data.rawQuery("select* from tblthuchi where tentaikhoan ='" + tendangnhap + "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex("nhanthongbao")) == 1) {
                ngaythongbao = cursor.getString(cursor.getColumnIndex("ngaythuchien"));
                gio = cursor.getInt(cursor.getColumnIndex("giothuchi"));
                phut = cursor.getInt(cursor.getColumnIndex("phutthuchi"));
                mathuchi = cursor.getInt(cursor.getColumnIndex("mathuchi"));

                Date datehientai = simpleDateFormat.parse(ngayhientai);
                Date datethongbao = simpleDateFormat.parse(ngaythongbao);
                if(datethongbao.equals(datehientai)){
                    if (gio <= gio24 || gio <= gio12 +12 && phut <= phutht){ //cu >=
                        ContentValues values = new ContentValues();
                        values.put("nhanthongbao", 0);
                        data.update("tblthuchi", values, "mathuchi = " + mathuchi, null);
                        TatThongBao();
                    }
                }
            }
            cursor.moveToNext();
        }
    }

    //Tat thong bao bo trong ham CapNhatThuChi
    public void TatThongBao(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ThongBaoThuChiReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    // Cap nhat lai thuoc tinh nhanthongbao thu chi
    public void KiemTraThongBaoKeHoach() throws ParseException {
        int mathukehoach;
        String ngaythongbao,ngayhientai;
        Calendar calendar = Calendar.getInstance();

        int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        int thang = calendar.get(Calendar.MONTH) +1 ;
        int nam = calendar.get(Calendar.YEAR);

        int gio12 = calendar.get(Calendar.HOUR);
        int gio24 = calendar.get(Calendar.HOUR_OF_DAY);
        int phutht = calendar.get(Calendar.MINUTE);

        ngayhientai = ngay + "/" + thang +"/"+ nam;


        Cursor cursor = data.rawQuery("select* from tblkehoachtietkiem where tentaikhoan ='" + tendangnhap + "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex("nhanthongbao")) == 1) {
                ngaythongbao = cursor.getString(cursor.getColumnIndex("ngayketthuckehoachtietkiem"));
//                gio = cursor.getInt(cursor.getColumnIndex("giothuchi"));
//                phut = cursor.getInt(cursor.getColumnIndex("phutthuchi"));
                mathukehoach = cursor.getInt(cursor.getColumnIndex("makehoachtietkiem"));

                Date datehientai = simpleDateFormat.parse(ngayhientai);
                Date datethongbao = simpleDateFormat.parse(ngaythongbao);
                if(datethongbao.equals(datehientai)){
                    ContentValues values = new ContentValues();
                    values.put("nhanthongbao", 0);
                    data.update("tblkehoachtietkiem", values, "makehoachtietkiem = " + mathukehoach, null);
                    TatThongBaoKeHoach();

                }
            }
            cursor.moveToNext();
        }
    }

    //Tat thong bao bo trong ham CapNhatThuChi
    public void TatThongBaoKeHoach(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ThongBaoKeHoachReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    //Lay Thu Chi co check hang ngay
    public void DanhSachThuChiLap(){
        //List<ThuChiLap> listthuchi = null;
        Cursor cursor = data.rawQuery("select* from tblthuchi where tentaikhoan ='"+tendangnhap+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getInt(cursor.getColumnIndex("hangngay"))==1){
                ThuChiLap thuChiLap = new ThuChiLap();
                thuChiLap.loaithuchi = cursor.getString(cursor.getColumnIndex("loaithuchi"));
                thuChiLap.sotienthuchi = cursor.getDouble(cursor.getColumnIndex("sotienthuchi")) ;
                thuChiLap.mota = cursor.getString(cursor.getColumnIndex("mota"));
                thuChiLap.gio = cursor.getInt(cursor.getColumnIndex("giothuchi"));
                thuChiLap.phut = cursor.getInt(cursor.getColumnIndex("phutthuchi"));
                thuChiLap.mavi = cursor.getInt(cursor.getColumnIndex("mavi"));
                thuChiLap.tentaikhoan = cursor.getString(cursor.getColumnIndex("tentaikhoan"));
                thuChiLap.madanhmuc = cursor.getInt(cursor.getColumnIndex("madanhmuc"));

                listthuchi.add(thuChiLap);

            }
            cursor.moveToNext();
        }
    } // moi

    //Tao thu chi hang ngay
    public void TaoThuChiHangNgay(){
        //List<ThuChiLap> ls = DanhSachThuChiLap();
        int mathuchi = 1;
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String ngayhientai = simpleDateFormat.format(date);
        for (ThuChiLap thuchi : listthuchi) {
           Cursor cursor = data.rawQuery("select mathuchi from tblthuchi",null);
           if(cursor.moveToLast()){
               mathuchi = cursor.getInt(cursor.getColumnIndex("mathuchi")) + 1;
           }
           //Luu
            ContentValues values = new ContentValues();
           values.put("mathuchi",mathuchi);
           values.put("loaithuchi",thuchi.loaithuchi);
           values.put("sotienthuchi",thuchi.sotienthuchi);
           values.put("mota",thuchi.mota);
           values.put("ngaythuchien",ngayhientai);
           values.put("giothuchi",thuchi.gio);
           values.put("phutthuchi",thuchi.phut);
           values.put("nhanthongbao",0);
           values.put("hangngay",1);
           values.put("mavi",thuchi.mavi);
           values.put("tentaikhoan",thuchi.tentaikhoan);
           values.put("madanhmuc",thuchi.madanhmuc);
            data.insert("tblthuchi", null, values);
//            if (data.insert("tblthuchi", null, values) == -1) {
//                return false;
//            }
        }
        //return true;
    } //moi
    //Kiem tra ngay
    public void KiemTraThongBaoLap() throws ParseException {
        int mathuchi,gio,phut;
        String ngaythongbao,ngayhientai;
        Calendar calendar = Calendar.getInstance();

        int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        int thang = calendar.get(Calendar.MONTH) +1 ;
        int nam = calendar.get(Calendar.YEAR);

        int gio12 = calendar.get(Calendar.HOUR);
        int gio24 = calendar.get(Calendar.HOUR_OF_DAY);
        int phutht = calendar.get(Calendar.MINUTE);

        ngayhientai = ngay + "/" + thang +"/"+ nam;


        Cursor cursor = data.rawQuery("select* from tblthuchi where tentaikhoan ='" + tendangnhap + "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex("hangngay")) == 1) {
                ngaythongbao = cursor.getString(cursor.getColumnIndex("ngaythuchien"));
                gio = cursor.getInt(cursor.getColumnIndex("giothuchi"));
                phut = cursor.getInt(cursor.getColumnIndex("phutthuchi"));

                Date datehientai = simpleDateFormat.parse(ngayhientai);
                Date datethongbao = simpleDateFormat.parse(ngaythongbao);
                if(datethongbao.after(datehientai)){
                    if (gio <= gio24 || gio <= gio12 +12 && phut <= phutht){ //cu >=
                        TaoThuChiHangNgay();
                    }
                }
            }
            cursor.moveToNext();
        }
    } //moi

    //Kiem tra vi tao san
    public boolean KiemtratrungVi() {
        cursor = data.rawQuery("select * from tblvi where tentaikhoan = '" + tendangnhap + "'",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false) {
            if(cursor.getString(cursor.getColumnIndex("tenvi")).equals("Cá nhân")) {
               return false;
            } else if (cursor.getString(cursor.getColumnIndex("tenvi")).equals("Gia đình")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tenvi")).equals("Tiết kiệm")) {
                return false;
            }
            cursor.moveToNext();
        }
        return true;
    }
    //Kiem tra danh muc tao san
    public boolean KiemtratrungDanhmuc() {
        cursor = data.rawQuery("select* from tbldanhmucthuchi where tentaikhoan = '" + tendangnhap + "'",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Tiền lương")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Tiền thưởng")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Làm thêm")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Được cho")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Khoản thu khác")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Ăn uống")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Học tập")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Chi phí đi lại")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Quần áo")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Tiền nhà trọ")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Tiền điện nước")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Dụng cụ sinh hoạt cá nhân")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Du lịch")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Giải trí")) {
                return false;
            } else if (cursor.getString(cursor.getColumnIndex("tendanhmuc")).equals("Khoản chi khác")) {
                return false;
            }
            cursor.moveToNext();
        }
        return true;
    }

    // Tao san vi
    public void TaoSanVi() {
        //Vi Ca Nhan
        int mavi1 = 1;
        cursor = data.rawQuery("select mavi from tblvi ", null);
        if (cursor.moveToLast() == true) {
            mavi1 = cursor.getInt(cursor.getColumnIndex("mavi")) + 1;
        }
        ContentValues valuesVi1 = new ContentValues();
        valuesVi1.put("mavi", mavi1);
        valuesVi1.put("tenvi","Cá nhân");
        valuesVi1.put("motavi", "Ví dành cho việc thu chi cá nhân");
        valuesVi1.put("sotienvi", 0);
        valuesVi1.put("tentaikhoan", tendangnhap);
        data.insert("tblvi", null, valuesVi1);

        //Vi Gia Dinh
        int mavi2 = 1;
        cursor = data.rawQuery("select mavi from tblvi", null);
        if (cursor.moveToLast() == true) {
            mavi2 = cursor.getInt(cursor.getColumnIndex("mavi")) + 1;
        }
        ContentValues valuesVi2 = new ContentValues();
        valuesVi2.put("mavi", mavi2);
        valuesVi2.put("tenvi","Gia đình");
        valuesVi2.put("motavi", "Ví dành cho việc thu chi gia đình");
        valuesVi2.put("sotienvi", 0);
        valuesVi2.put("tentaikhoan", tendangnhap);
        data.insert("tblvi", null, valuesVi2);

        //Vi Tiet Kiem
        int mavi3 = 1;
        cursor = data.rawQuery("select mavi from tblvi", null);
        if (cursor.moveToLast() == true) {
            mavi3 = cursor.getInt(cursor.getColumnIndex("mavi")) + 1;
        }
        ContentValues valuesVi3 = new ContentValues();
        valuesVi3.put("mavi", mavi3);
        valuesVi3.put("tenvi","Tiết kiệm");
        valuesVi3.put("motavi", "Ví dành cho kế hoạch tiết kiệm");
        valuesVi3.put("sotienvi", 0);
        valuesVi3.put("tentaikhoan", tendangnhap);
        data.insert("tblvi", null, valuesVi3);
        cursor.close();
    }

    //Tao san cac danh muc
    public void TaoSanDanhMuc() {
        String khoanthu = "Khoản thu", khoanchi = "Khoản chi";
        String danhmucthuchovicanhan[] = {"Tiền lương","Khoản thu khác"};
        String danhmucchichovicanhan[] = {"Ăn uống","Học tập","Chi phí đi lại","Khoản chi khác"};
        String danhmucthuchovigiadinh[] = {"Được cho"}; // lam de sau nay co them thu cho vi gia dinh
        String danhmucchichovigiadinh[] = {"Tiền nhà trọ","Tiền điện nước","Dụng cụ sinh hoạt cá nhân"};
        String danhmucthuchovitietkiem[] = {"Tiền thưởng","Làm thêm"}; // lam de sau nay co them thu cho vi tiet kiem
        String danhmucchichovitietkiem[] = {"Quần áo","Giải trí","Du lịch"};

        if (khoanthu.equals("Khoản thu")) {
            for (int i = 0; i <= 1; i++) { //sau nay co them vao thi tang n danh muc thì i<= n - 1
                TaoSanDanhMucThuChiChoViCaNhan(khoanthu,danhmucthuchovicanhan[i]);
            }
            for (int i = 0; i <= 0; i++ ) { //sau nay co them vao thi tang n danh muc thì i<= n - 1
                TaoSanDanhMucThuChiChoViGiaDinh(khoanthu,danhmucthuchovigiadinh[i]);
            }
            for (int i = 0; i <= 1; i++) { //sau nay co them vao thi tang n danh muc thì i<= n - 1
                TaoSanDanhMucThuChiChoViTietKiem(khoanthu,danhmucthuchovitietkiem[i]);
            }
        }
        if (khoanchi.equals("Khoản chi")) {
            for (int i = 0 ; i <= 3; i++) { //sau nay co them vao thi tang n danh muc thì i<= n - 1
                TaoSanDanhMucThuChiChoViCaNhan(khoanchi,danhmucchichovicanhan[i]);
            }
            for (int i = 0; i <= 2; i++) { //sau nay co them vao thi tang n danh muc thì i<= n - 1
                TaoSanDanhMucThuChiChoViGiaDinh(khoanchi,danhmucchichovigiadinh[i]);
            }
            for (int i = 0; i <=2 ; i++) { //sau nay co them vao thi tang n danh muc thì i<= n - 1
                TaoSanDanhMucThuChiChoViTietKiem(khoanchi,danhmucchichovitietkiem[i]);
            }
        }
    }

    //Tao danh muc cho vi ca nhan
    public void TaoSanDanhMucThuChiChoViCaNhan(String loaikhoan,String tendanhmuc) {
        int madanhmuc = 1;
        cursor = data.rawQuery("select madanhmuc from tbldanhmucthuchi", null);
        if (cursor.moveToLast()) {
            madanhmuc = cursor.getInt(cursor.getColumnIndex("madanhmuc")) + 1;
        }
        int mavi = 1;
        cursor = data.rawQuery("select * from tblvi where tentaikhoan = '" + tendangnhap + "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("tenvi")).equals("Cá nhân")) {
                mavi = cursor.getInt(cursor.getColumnIndex("mavi"));
            }
            cursor.moveToNext();
        }

        if (loaikhoan.equals("Khoản thu")) {
            ContentValues contentValuesthu1 = new ContentValues();
            contentValuesthu1.put("madanhmuc", madanhmuc);
            contentValuesthu1.put("tendanhmuc", tendanhmuc);
            contentValuesthu1.put("loaikhoan", "Khoản thu");
            contentValuesthu1.put("mavi", mavi);
            contentValuesthu1.put("tenviuutien","Cá nhân");
            contentValuesthu1.put("tentaikhoan", tendangnhap);

            data.insert("tbldanhmucthuchi", null, contentValuesthu1);
        } else {
            ContentValues contentValuesthu1 = new ContentValues();
            contentValuesthu1.put("madanhmuc", madanhmuc);
            contentValuesthu1.put("tendanhmuc", tendanhmuc);
            contentValuesthu1.put("loaikhoan", "Khoản chi");
            contentValuesthu1.put("mavi", mavi);
            contentValuesthu1.put("tenviuutien","Cá nhân");
            contentValuesthu1.put("tentaikhoan", tendangnhap);

            data.insert("tbldanhmucthuchi", null, contentValuesthu1);
        }
    }

    //Tao danh muc cho vi gia dinh
    public void TaoSanDanhMucThuChiChoViGiaDinh(String loaikhoan,String tendanhmuc) {
        int madanhmuc = 1;
        cursor = data.rawQuery("select madanhmuc from tbldanhmucthuchi", null);
        if (cursor.moveToLast() == true) {
            madanhmuc = cursor.getInt(cursor.getColumnIndex("madanhmuc")) + 1;
        }
        int mavi = 1;
        cursor = data.rawQuery("select * from tblvi where tentaikhoan = '" + tendangnhap + "'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (cursor.getString(cursor.getColumnIndex("tenvi")).equals("Gia đình")){
                mavi = cursor.getInt(cursor.getColumnIndex("mavi"));
            }
            cursor.moveToNext();
        }

        if (loaikhoan.equals("Khoản thu")) {
            ContentValues contentValuesthu1 = new ContentValues();
            contentValuesthu1.put("madanhmuc", madanhmuc);
            contentValuesthu1.put("tendanhmuc", tendanhmuc);
            contentValuesthu1.put("loaikhoan", "Khoản thu");
            contentValuesthu1.put("mavi", mavi);
            contentValuesthu1.put("tenviuutien","Gia đình");
            contentValuesthu1.put("tentaikhoan", tendangnhap);

            data.insert("tbldanhmucthuchi", null, contentValuesthu1);
        } else {
            ContentValues contentValuesthu1 = new ContentValues();
            contentValuesthu1.put("madanhmuc", madanhmuc);
            contentValuesthu1.put("tendanhmuc", tendanhmuc);
            contentValuesthu1.put("loaikhoan", "Khoản chi");
            contentValuesthu1.put("mavi", mavi);
            contentValuesthu1.put("tenviuutien","Gia đình");
            contentValuesthu1.put("tentaikhoan", tendangnhap);

            data.insert("tbldanhmucthuchi", null, contentValuesthu1);
        }
    }

    //Tao danh muc cho vi tiet kiem
    public void TaoSanDanhMucThuChiChoViTietKiem(String loaikhoan,String tendanhmuc) {
        int madanhmuc = 1;
        cursor = data.rawQuery("select madanhmuc from tbldanhmucthuchi", null);
        if (cursor.moveToLast() == true) {
            madanhmuc = cursor.getInt(cursor.getColumnIndex("madanhmuc")) + 1;
        }
        int mavi = 1;
        cursor = data.rawQuery("select * from tblvi where tentaikhoan = '" + tendangnhap + "'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (cursor.getString(cursor.getColumnIndex("tenvi")).equals("Tiết kiệm")) {
                mavi = cursor.getInt(cursor.getColumnIndex("mavi"));
            }
            cursor.moveToNext();
        }

        if (loaikhoan.equals("Khoản thu")) {
            ContentValues contentValuesthu1 = new ContentValues();
            contentValuesthu1.put("madanhmuc", madanhmuc);
            contentValuesthu1.put("tendanhmuc", tendanhmuc);
            contentValuesthu1.put("loaikhoan", "Khoản thu");
            contentValuesthu1.put("mavi", mavi);
            contentValuesthu1.put("tenviuutien","Tiết kiệm");
            contentValuesthu1.put("tentaikhoan", tendangnhap);

            data.insert("tbldanhmucthuchi", null, contentValuesthu1);
        } else {
            ContentValues contentValuesthu1 = new ContentValues();
            contentValuesthu1.put("madanhmuc", madanhmuc);
            contentValuesthu1.put("tendanhmuc", tendanhmuc);
            contentValuesthu1.put("loaikhoan", "Khoản chi");
            contentValuesthu1.put("mavi", mavi);
            contentValuesthu1.put("tenviuutien","Tiết kiệm");
            contentValuesthu1.put("tentaikhoan", tendangnhap);

            data.insert("tbldanhmucthuchi", null, contentValuesthu1);
        }
    }


    public void QuenMatKhau(View v) {
        animation = AnimationUtils.loadAnimation(this, R.anim.animation_edittext);
        XoaTrang();
        final Dialog d = new Dialog(MainActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_quenmatkhau);
        d.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        d.show();

        maso = (EditText) d.findViewById(R.id.txtNhapmasoquen);
        matkhau1 = (EditText) d.findViewById(R.id.txtNhapmatkhauquen);
        matkhau2 = (EditText) d.findViewById(R.id.txtNhapmatkhau2quen);
        thaydoi = (Button) d.findViewById(R.id.btnThaydoiquen);
        huy = (Button) d.findViewById((R.id.btnHuyQuenMatKhau));
        thaydoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoiMatKhau()) {
                    d.dismiss();
                }
            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    public boolean DoiMatKhau() {
        String thongbao = "";
        boolean msbm = false;
        Cursor c = data.rawQuery("select * from tbltaikhoan", null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            if (c.getString(c.getColumnIndex("masobimat")).equals(maso.getText().toString())) {
                msbm = true;
            }
            c.moveToNext();
        }
        if (maso.getText().toString().equals("")) {
            thongbao = "Bạn chưa nhập mã số bí mật";
            maso.startAnimation(animation);
        } else if (matkhau1.getText().toString().equals("")) {
            thongbao = "Bạn chưa nhập mật khẩu";
            matkhau1.startAnimation(animation);
        } else if (matkhau2.getText().toString().equals("")) {
            thongbao = "Bạn chưa nhập mật khẩu mới";
            matkhau2.startAnimation(animation);
        } else if (msbm == false) {
            thongbao = "Mã số bí mật sai";
            maso.startAnimation(animation);
        } else if (matkhau1.getText().toString().equals(matkhau2.getText().toString()) == false) {
            thongbao = "Mật khẩu không khớp";
            matkhau1.startAnimation(animation);
            matkhau2.startAnimation(animation);
        } else {
            ContentValues values = new ContentValues();
            values.put("matkhau", matkhau1.getText().toString());
            data.update("tbltaikhoan", values, "masobimat=?", new String[]{maso.getText().toString()});
            thongbao = "Tạo mật khẩu thành công";
            Toast.makeText(getApplicationContext(), thongbao, Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(getApplicationContext(), thongbao, Toast.LENGTH_SHORT).show();
        return false;
    }

    public void GhiNhoTaiKhoan() {
        SharedPreferences.Editor edit = share.edit();
        if (checkBox_GhiNho.isChecked()) {
            edit.putString("user", editText_TenTaiKhoanDangNhap.getText().toString());
            edit.putString("pass", editText_MatKhauDangNhap.getText().toString());
            edit.putBoolean("ghinho", checkBox_GhiNho.isChecked());
        } else {
            edit.clear();
        }
        edit.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == 0) {
                GhiNhoTaiKhoan();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        if (share.getBoolean("ghinho", false)) {
            editText_TenTaiKhoanDangNhap.setText(share.getString("user", null));
            editText_MatKhauDangNhap.setText(share.getString("pass", null));
            //DangNhap(null);
        } else {
            editText_TenTaiKhoanDangNhap.setText(null);
            editText_MatKhauDangNhap.setText(null);
        }
        checkBox_GhiNho.setChecked(share.getBoolean("ghinho", false));
        super.onResume();
    }

    @Override
    protected void onPause() {
        GhiNhoTaiKhoan();
        super.onPause();
    }
}
