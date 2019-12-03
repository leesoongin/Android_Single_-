package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class AdviceActivity extends AppCompatActivity {

    EditText changePageNumber;
    TextView fixedPageNumber;
    ViewPager viewPager;
    TabLayout tab;

    AdvicePageOne pageOne;
    AdvicePageTwo pageTwo;
    AdvicePageThree pageThree;
    AdvicePageFour pageFour;
    AdvicePageFive pageFive;
    AdvicePageSix pageSix;
    AdvicePageSeven pageSeven;
    AdvicePageEight pageEight;
    AdvicePageNine pageNine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        tab=(TabLayout)findViewById(R.id.tab);

        AdvicePageAdapter adapter=new AdvicePageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);

    }

    public class AdvicePageAdapter extends FragmentPagerAdapter {

        private final static int TOTAL_PAGE_NUMBER=9;

        public AdvicePageAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           if(position == 0){
               pageOne=new AdvicePageOne();
               return pageOne;
           }else if(position == 1){
               pageTwo=new AdvicePageTwo();
               return pageTwo;
           }else if(position == 2){
               pageThree=new AdvicePageThree();
               return pageThree;
           }else if(position == 3){
               pageFour=new AdvicePageFour();
               return pageFour;
           }else if(position == 4){
               pageFive=new AdvicePageFive();
               return pageFive;
           }else if(position == 5){
               pageSix=new AdvicePageSix();
               return pageSix;
           }else if(position == 6){
               pageSeven=new AdvicePageSeven();
               return pageSeven;
           }else if(position == 7){
               pageEight=new AdvicePageEight();
               return pageEight;
           }
           else if(position == 8){
               pageNine=new AdvicePageNine();
               return pageNine;
           }else{
               return null;
           }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return "1P";
                case 1:
                    return "2P";
                case 2:
                    return "3P";
                case 3:
                    return "4P";
                case 4:
                    return "5P";
                case 5:
                    return "6P";
                case 6:
                    return "7P";
                case 7:
                    return "8P";
                case 8:
                    return "9P";
                default :
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TOTAL_PAGE_NUMBER;
        }
    }
}
