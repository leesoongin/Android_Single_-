package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SP_matchHistory extends Fragment {

    String S_getId;//아이디 받아오기

    TextView db_matchHistory,odd;

    dbHelper helper;

    private int getTotal,getWin,getDraw,getLose,getOdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_sp_match_history,container,false);

       db_matchHistory=(TextView)rootView.findViewById(R.id.db_matchHistory);
       odd=(TextView)rootView.findViewById(R.id.odd);

       helper=new dbHelper(getContext());
       SQLiteDatabase db=helper.getReadableDatabase();

       S_getId=getArguments().getString("2P_id");

        Cursor cursor=db.rawQuery("select * from userInfoList where id='"+S_getId+"'",null);

        while(cursor.moveToNext()){
            getTotal=cursor.getInt(6);
            getWin=cursor.getInt(7);
            getDraw=cursor.getInt(8);
            getLose=cursor.getInt(9);
        }

        db_matchHistory.setText(getTotal+" 전 "+getWin+" 승 "+getDraw+" 무 "+getLose+" 패 ");

        if(getTotal != 0)
            getOdd=(int)((getWin/getTotal)*100);
        else
            getOdd=0;

        odd.setText("승률 "+getOdd+" %");

       return rootView;
    }

}
