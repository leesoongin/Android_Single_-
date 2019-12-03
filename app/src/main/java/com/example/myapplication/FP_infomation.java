package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


public class FP_infomation extends Fragment {

    FP_myInfo fp_myInfo;
    FP_matchHistory fp_matchHistory;

    String F_getId; //유저아이디정보 받아오기

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_fp_infomation, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.fp_info_viewPager);
        TabLayout tab = (TabLayout) rootView.findViewById(R.id.fp_info_tab);

        F_getId=getArguments().getString("1P_id");


        FP_Adapter adapter = new FP_Adapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        tab.setupWithViewPager(viewPager);

        return rootView;
    }
    public class FP_Adapter extends FragmentPagerAdapter {


        private int PAGE_NUMBER = 2;

        public FP_Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                   fp_myInfo=new FP_myInfo();

                   Bundle args=new Bundle();
                   args.putString("1P_id",F_getId);

                   fp_myInfo.setArguments(args);

                    return fp_myInfo;
                case 1:
                    fp_matchHistory=new FP_matchHistory();

                    Bundle args2=new Bundle();
                    args2.putString("1P_id",F_getId);

                    fp_matchHistory.setArguments(args2);

                    return fp_matchHistory;
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "내 정보";
                case 1:
                    return "게임 전적";
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return PAGE_NUMBER;
        }
    }
}
