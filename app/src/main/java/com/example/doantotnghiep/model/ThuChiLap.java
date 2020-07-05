package com.example.doantotnghiep.model;

public class ThuChiLap {
    public String loaithuchi,mota,tentaikhoan;
    public int mavi,madanhmuc,gio,phut;
    public Double sotienthuchi;

    public ThuChiLap(String loaithuchi, String mota, String tentaikhoan, int mavi, int madanhmuc, int gio, int phut, Double sotienthuchi) {
        this.loaithuchi = loaithuchi;
        this.mota = mota;
        this.tentaikhoan = tentaikhoan;
        this.mavi = mavi;
        this.madanhmuc = madanhmuc;
        this.gio = gio;
        this.phut = phut;
        this.sotienthuchi = sotienthuchi;
    }

    public ThuChiLap(){

    }
}
