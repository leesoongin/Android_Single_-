package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameWaitActivity extends AppCompatActivity {

    TextView FP_score,SP_score;
    ImageView fp_icon,sp_icon,gameRoundText;

    int FP_gamePoint,SP_gamePoint,gameRound;

    Thread endThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_wait);

        FP_score=(TextView)findViewById(R.id.FP_score);
        SP_score=(TextView)findViewById(R.id.SP_score);

        gameRoundText=(ImageView)findViewById(R.id.gameRoundText);
        fp_icon=(ImageView)findViewById(R.id.fp_icon);
        sp_icon=(ImageView)findViewById(R.id.sp_icon);

        FP_gamePoint=getIntent().getIntExtra("FP_score",1);
        SP_gamePoint=getIntent().getIntExtra("SP_score",1);
        gameRound=getIntent().getIntExtra("gameRound",1);


        FP_score.setText(""+FP_gamePoint);
        SP_score.setText(""+SP_gamePoint);

        endThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(5000);
                }catch (InterruptedException e){}
                finish();
            }
        });

        endThread.start();

    }


}
