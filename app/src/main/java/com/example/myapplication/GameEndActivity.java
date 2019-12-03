package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class GameEndActivity extends AppCompatActivity {

    int FP_gamePoint,SP_gamePoint;
    String FP_id,SP_id;

    dbHelper helper=new dbHelper(this);

    TextView FP_result,SP_result,FP_idText,SP_idText,FP_rankPoint,SP_rankPoint,finalScore;

    int FP_registRankPoint,SP_registRankPoint; //디비에 저장해줄 점수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        FP_result=(TextView)findViewById(R.id.FP_result);//승리 패배 text
        SP_result=(TextView)findViewById(R.id.SP_result);
        FP_idText=(TextView)findViewById(R.id.FP_idText);
        SP_idText=(TextView)findViewById(R.id.SP_idText);
       /* FP_rankPoint=(TextView)findViewById(R.id.FP_rankPoint); //승 패 시 증가 감소할 랭크포인트 text
        SP_rankPoint=(TextView)findViewById(R.id.SP_rankPoint);*/
        finalScore=(TextView)findViewById(R.id.finalScore);

        FP_registRankPoint=0; SP_registRankPoint=0;

        FP_gamePoint=getIntent().getIntExtra("FP_gameScore",1);
        SP_gamePoint=getIntent().getIntExtra("SP_gameScore",1);

        FP_id=getIntent().getStringExtra("1P_id");
        SP_id=getIntent().getStringExtra("2P_id");

        FP_idText.setText(FP_id); SP_idText.setText(SP_id);
        finalScore.setText(""+FP_gamePoint+"  :  "+SP_gamePoint);

        SQLiteDatabase db=helper.getWritableDatabase();


        if(FP_gamePoint > SP_gamePoint) { //1P승
            FP_result.setText("승 리");
            SP_result.setText("패 배");
            FP_registRankPoint=13; SP_registRankPoint=-13;

            //총 전적 승 무 패 랭크포인트 matchHistory
            db.execSQL("update userInfoList set matchHistory=matchHistory+1,win=win+1,rankPoint=rankPoint+13 where id='"+FP_id+"'");
            db.execSQL("update userInfoList set matchHistory=matchHistory+1,lose=lose+1,rankPoint=rankPoint-13 where id='"+SP_id+"'");
        }else if(FP_gamePoint == SP_gamePoint){ // 무승부
            FP_result.setText("무 승 부");
            SP_result.setText("무 승 부");
            FP_registRankPoint=5; SP_registRankPoint=5;

            FP_rankPoint.setText("+"+FP_registRankPoint);
            SP_rankPoint.setText("+"+SP_registRankPoint);

            //총 전적 승 무 패 랭크포인트 matchHistory
            db.execSQL("update userInfoList set matchHistory=matchHistory+1,draw=draw+1,rankPoint=rankPoint+5 where id='"+FP_id+"'");
            db.execSQL("update userInfoList set matchHistory=matchHistory+1,draw=draw+1,rankPoint=rankPoint+5 where id='"+SP_id+"'");

        }else if(FP_gamePoint < SP_gamePoint){//2P 승
            FP_result.setText("패 배");
            SP_result.setText("승 리");
            FP_registRankPoint=-13; SP_registRankPoint=13;

            //총 전적 승 무 패 랭크포인트 matchHistory
            db.execSQL("update userInfoList set matchHistory=matchHistory+1,lose=lose+1,rankPoint=rankPoint-13 where id='"+FP_id+"'");
            db.execSQL("update userInfoList set matchHistory=matchHistory+1,win=win+1,rankPoint=rankPoint+13 where id='"+SP_id+"'");
        }
        db.close();
    }
}
