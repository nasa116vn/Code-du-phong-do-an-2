package com.example.doantotnghiep.model;

public class ArrayVi {
    public int mavi;
    public String tenvi;
    public String motavi;
    public double sotienvi;

    public ArrayVi(int mavi, String tenvi, String motavi, double sotienvi) {
        this.mavi = mavi;
        this.tenvi = tenvi;
        this.motavi = motavi;
        this.sotienvi = sotienvi;
    }

    public ArrayVi() {

    }

    public int getMavi() {
        return mavi;
    }

    public void setMavi(int mavi) {
        this.mavi = mavi;
    }

    public String getTenvi() {
        return tenvi;
    }

    public void setTenvi(String tenvi) {
        this.tenvi = tenvi;
    }

    public String getMotavi() {
        return motavi;
    }

    public void setMotavi(String motavi) {
        this.motavi = motavi;
    }

    public double getSotienvi() {
        return sotienvi;
    }

    public void setSotienvi(double sotien) {
        this.sotienvi = sotien;
    }

    @Override
    public String toString() {
        return "ArrayVi{" +
                "mavi=" + mavi +
                ", tenvi='" + tenvi + '\'' +
                ", motavi='" + motavi + '\'' +
                ", sotienvi=" + sotienvi +
                '}';
    }
}
