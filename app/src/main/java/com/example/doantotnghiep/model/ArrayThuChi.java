package com.example.doantotnghiep.model;

public class ArrayThuChi  {
    public String thoigian, danhmucthuchi, vi, loaikhoan, mota, nhanthongbao;//moi
    public int ma;
    public double tien;

    public ArrayThuChi(String thoigian, String danhmucthuchi, String vi, double tien, int ma, String nhanthongbao, String loaikhoan, String mota) {
        this.thoigian = thoigian;
        this.danhmucthuchi = danhmucthuchi;
        this.vi = vi;
        this.tien = tien;
        this.ma = ma;
        this.nhanthongbao = nhanthongbao; //moi
        this.loaikhoan = loaikhoan;
        this.mota = mota;
    }
}
