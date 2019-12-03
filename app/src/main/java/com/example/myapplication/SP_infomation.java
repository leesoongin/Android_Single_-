package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


public class SP_infomation extends Fragment {

    String getId;

    TabLayout sp_info_tab;
    ViewPager sp_info_viewPager;

    SP_myInfo sp_myInfo;
    SP_matchHistory sp_matchHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_sp_infomation,container,false);

        sp_info_tab=(TabLayout)rootView.findViewById(R.id.sp_info_tab);
        sp_info_viewPager=(ViewPager)rootView.findViewById(R.id.sp_info_viwePager);

        SP_Adapter adapter=new SP_Adapter(getChildFragmentManager());

        sp_info_viewPager.setAdapter(adapter);

        sp_info_tab.setupWithViewPager(sp_info_viewPager);

        getId=getArguments().getString("2P_id");

        return rootView;
    }

    public class SP_Adapter extends FragmentPagerAdapter {

        private int PAGE_NUM=2;

        public SP_Adapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           switch (position){
               case 0:
                   sp_myInfo=new SP_myInfo();

                   Bundle args=new Bundle();
                   args.putString("2P_id",getId);
                   sp_myInfo.setArguments(args);

                   return sp_myInfo;
               case 1:
                   sp_matchHistory=new SP_matchHistory();

                   Bundle args2=new Bundle();
                   args2.putString("2P_id",getId);
                   sp_matchHistory.setArguments(args2);

                   return sp_matchHistory;
               default:
                   return null;
           }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
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
            return PAGE_NUM;
        }
    }
}
