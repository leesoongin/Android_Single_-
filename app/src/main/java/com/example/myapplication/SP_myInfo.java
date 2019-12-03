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


public class SP_myInfo extends Fragment implements View.OnClickListener{

    dbHelper helper;

    TextView db_name,db_nicname,db_email,db_matchHistory,db_rankPoint;

    Button SP_passwordChangeButton;

    String S_getId;//아이디값 받아오기

    private String getName,getNicname,getEmail;
    private int getTotal,getWin,getDraw,getLose,getRankPoint;

//이름닉네임이메일 게임전적 랭크포인트 비밀번호 변경
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_sp_my_info,container,false);

       db_name=(TextView)rootView.findViewById(R.id.db_name);
       db_nicname=(TextView)rootView.findViewById(R.id.db_nicname);
       db_email=(TextView)rootView.findViewById(R.id.db_email);
       db_matchHistory=(TextView)rootView.findViewById(R.id.db_matchHistory);
       db_rankPoint=(TextView)rootView.findViewById(R.id.db_rankPoint);

       SP_passwordChangeButton=(Button)rootView.findViewById(R.id.SP_passwordChangeButton);

       SP_passwordChangeButton.setOnClickListener(this);
       helper=new dbHelper(getContext());

       S_getId=getArguments().getString("2P_id");//아이디 넘겨받고

        SQLiteDatabase db=helper.getReadableDatabase();

        Cursor cursor=db.rawQuery("select * from userInfoList where id='"+S_getId+"'",null);

        while(cursor.moveToNext()){
            getName=cursor.getString(4);
            getNicname=cursor.getString(3);
            getEmail=cursor.getString(5);
            getTotal=cursor.getInt(6);
            getWin=cursor.getInt(7);
            getDraw=cursor.getInt(8);
            getLose=cursor.getInt(9);
            getRankPoint=cursor.getInt(10);
        }

        db.close();

        db_name.setText(getName);
        db_nicname.setText(getNicname);
        db_email.setText(getEmail);
        db_matchHistory.setText(getTotal+" 전 "+getWin+" 승 "+getDraw+" 무 "+getLose+" 패 ");
        db_rankPoint.setText(getRankPoint+" 점");

       return rootView;
    }


    @Override
    public void onClick(View v) {
        if(v == SP_passwordChangeButton){
            Intent changePasswordIntent=new Intent(getContext(),ChangePasswordActivity.class);
            changePasswordIntent.putExtra("2P_id",S_getId);

            startActivity(changePasswordIntent);
        }
    }
}
