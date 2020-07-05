package com.example.doantotnghiep.model;

public class ArrayKeHoachTietKiem {
    public String tenkehoachtietkiem, ngaybatdaukehoachtietkiem, ngayketthuckehoachtietkiem;
    public double sotienkehoachtietkiem, sotiendatietkiem;
    public int makehoachtietkiem;
    public String trangthai,nhanthongbao;

    public ArrayKeHoachTietKiem(String tenkehoachtietkiem, String ngaybatdaukehoachtietkiem, String ngayketthuckehoachtietkiem, double sotienkehoachtietkiem, double sotiendatietkiem, int makehoachtietkiem, String trangthai,String nhanthongbao) {
        this.tenkehoachtietkiem = tenkehoachtietkiem;
        this.ngaybatdaukehoachtietkiem = ngaybatdaukehoachtietkiem;
        this.ngayketthuckehoachtietkiem = ngayketthuckehoachtietkiem;
        this.sotienkehoachtietkiem = sotienkehoachtietkiem;
        this.sotiendatietkiem = sotiendatietkiem;
        this.makehoachtietkiem = makehoachtietkiem;
        this.trangthai = trangthai;
        this.nhanthongbao = nhanthongbao;
    }
}
