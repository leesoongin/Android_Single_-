package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyInfomationActivity extends AppCompatActivity {

    String F_userInfo,S_userInfo;//intent 값 받아줄친구들
    BottomNavigationView bottomNavigationView;

    FP_infomation fp_infomation;
    SP_infomation sp_infomation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_infomation);

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);

        fp_infomation=new FP_infomation();
        sp_infomation=new SP_infomation();

        F_userInfo=getIntent().getStringExtra("1P_id");
        S_userInfo=getIntent().getStringExtra("2P_id");

        Bundle fp_bundle=new Bundle();
        fp_bundle.putString("1P_id",F_userInfo);
        fp_infomation.setArguments(fp_bundle); //아이디값 프래그먼트로 전달

        Bundle sp_bundle=new Bundle();
        sp_bundle.putString("2P_id",S_userInfo);
        sp_infomation.setArguments(sp_bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fp_info_fragment,fp_infomation).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.firstPlayer:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fp_info_fragment,fp_infomation).commitAllowingStateLoss();
                        return true;
                    case R.id.secondPlayer:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fp_info_fragment,sp_infomation).commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });
    }
}
