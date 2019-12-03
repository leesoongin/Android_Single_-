package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReadUserInfo extends AppCompatActivity implements View.OnClickListener{

    TextView outputRanking,outputNicname,outputMatchHistory,outputRankPoint;
    Button okButton;

    String getUserNicname;//둘다 intent전달값받기
    int getUserRanking;

    int getTotal,getWin,getDraw,getLose,getRankPoint;

    int odd;//승률

    dbHelper helper=new dbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_user_info);

        outputRanking=(TextView)findViewById(R.id.outputRanking);
        outputNicname=(TextView)findViewById(R.id.outputNicname);
        outputMatchHistory=(TextView)findViewById(R.id.outputMatchHistory);
        outputRankPoint=(TextView)findViewById(R.id.outputRankPoint);

        okButton=(Button)findViewById(R.id.okButton);

        getUserNicname=getIntent().getStringExtra("nicname");
        getUserRanking=getIntent().getIntExtra("ranking",1);

        SQLiteDatabase db=helper.getReadableDatabase();

        //랭킹이랑 닉네임은 받아왔으니 전적이랑 랭크포인트만 받자.
        Cursor cursor=db.rawQuery("select * from userInfoList where nicname='"+getUserNicname+"'",null);

        while(cursor.moveToNext()){
            getTotal=cursor.getInt(6);
            getWin=cursor.getInt(7);
            getDraw=cursor.getInt(8);
            getLose=cursor.getInt(9);
            getRankPoint=cursor.getInt(10);
        }

        if(getTotal != 0)
            odd=(int)((getWin/getTotal)*100);
        else
            odd=0;

        outputRanking.setText(getUserRanking+" 위");
        outputNicname.setText(getUserNicname);
        outputMatchHistory.setText(getTotal+" 전 "+getWin+" 승 "+getDraw+" 무 "+getLose+" 패   승률  "+odd+" %");
        outputRankPoint.setText(getRankPoint+" 점");

        okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == okButton){
            finish();
        }
    }
}
