package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class FP_myInfo extends Fragment implements View.OnClickListener{

    String getId,getName,getNicname,getEmail; //사용자 아이디값 전달받음
    int getTotal,getWin,getDraw,getLose,getRankPoint;

    Button FP_passwordChangeButton;
    dbHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_fp_my_info,container,false);

        TextView db_name=(TextView)rootView.findViewById(R.id.db_name);
        TextView db_nicname=(TextView)rootView.findViewById(R.id.db_nicname);
        TextView db_email=(TextView)rootView.findViewById(R.id.db_email);
        TextView db_matchHistory=(TextView)rootView.findViewById(R.id.db_matchHistory);
        TextView db_rankPoint=(TextView)rootView.findViewById(R.id.db_rankPoint);

        FP_passwordChangeButton=(Button)rootView.findViewById(R.id.FP_passwordChangeButton);

        FP_passwordChangeButton.setOnClickListener(this);
        //게임전적 랭크포이트
        getId=getArguments().getString("1P_id");

        helper=new dbHelper(getContext());

        SQLiteDatabase db=helper.getReadableDatabase();

        Cursor cursor=db.rawQuery("select * from userInfoList where id='"+getId+"'",null);

        while(cursor.moveToNext()){
            getNicname=cursor.getString(3);
            getName=cursor.getString(4);
            getEmail=cursor.getString(5);
            getTotal=cursor.getInt(6);
            getWin=cursor.getInt(7);
            getDraw=cursor.getInt(8);
            getLose=cursor.getInt(9);
            getRankPoint=cursor.getInt(10);
        }

        db.close();

        db_name.setText(getId);
        db_nicname.setText(getNicname);
        db_email.setText(getEmail);
        db_matchHistory.setText(getTotal+" 전 "+getWin+" 승 "+getDraw+" 무 "+getLose+" 패 ");
        db_rankPoint.setText(getRankPoint+" 점");


        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == FP_passwordChangeButton){
            Intent changePasswordIntent=new Intent(getContext(),ChangePasswordActivity.class);
            changePasswordIntent.putExtra("1P_id",getId);

            startActivity(changePasswordIntent);
        }else
            return ;
    }
}
