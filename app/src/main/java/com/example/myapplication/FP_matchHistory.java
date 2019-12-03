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


public class FP_matchHistory extends Fragment {

    TextView db_matchHistory,odd;

    String F_getId;
    int getTotal,getWin,getDraw,getLose,getOdd;


    dbHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_fp_match_history,container,false);

        db_matchHistory=(TextView)rootView.findViewById(R.id.db_matchHistory);
        odd=(TextView)rootView.findViewById(R.id.odd);

        helper=new dbHelper(getContext());

        F_getId=getArguments().getString("1P_id");

        SQLiteDatabase db=helper.getReadableDatabase();

        Cursor cursor=db.rawQuery("select * from userInfoList where id='"+F_getId+"'",null);

        while(cursor.moveToNext()){
            getTotal=cursor.getInt(6);
            getWin=cursor.getInt(7);
            getDraw=cursor.getInt(8);
            getLose=cursor.getInt(9);
        }

        db.close();
        db_matchHistory.setText(getTotal+" 전 "+getWin+" 승 "+getDraw+" 무 "+getLose+" 패 ");

        if(getTotal != 0)
            getOdd=(int)((getWin/getTotal)*100); //인트형으로 받아 저장하자
        else
            getOdd=0;

        odd.setText("승률 "+getOdd+" %");

        return rootView;
    }

}
