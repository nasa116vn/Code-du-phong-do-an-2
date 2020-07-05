package com.example.doantotnghiep.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.doantotnghiep.ui.QuanLyDanhMucThuChi.QuanLyDanhMucThuChiFragment;
import com.example.doantotnghiep.ui.QuanLyTaiKhoan.QuanLyTaiKhoanFragment;
import com.example.doantotnghiep.ui.QuanLyThuChi.QuanLyThuChiFragment;
import com.example.doantotnghiep.ui.QuanLyVi.QuanLyViFragment;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private String taikhoan;

    public PagerAdapter(FragmentManager fm, String taikhoan) {
        super(fm);
        this.taikhoan = taikhoan;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }
}
