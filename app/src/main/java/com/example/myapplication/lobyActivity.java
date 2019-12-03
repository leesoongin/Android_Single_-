package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class lobyActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton logoutButton,adviceButton,myInfomationButton,rankingInfomationButton,tutorialButton,gameStartButton;
    String F_userInfo,S_userInfo;

    public MediaPlayer bgm,click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loby);

        logoutButton=(ImageButton)findViewById(R.id.logoutButton);
        adviceButton=(ImageButton)findViewById(R.id.adviceButton);
        myInfomationButton=(ImageButton)findViewById(R.id.myInfomationButton);
        rankingInfomationButton=(ImageButton)findViewById(R.id.rankingInfomationButton);
        tutorialButton=(ImageButton)findViewById(R.id.tutorialButton);
        gameStartButton=(ImageButton)findViewById(R.id.gameStartButton);

        bgm=MediaPlayer.create(this,R.raw.bgm);
        click=MediaPlayer.create(this,R.raw.click);

        bgm.start();
        bgm.setLooping(true); //반복 재생.

        logoutButton.setOnClickListener(this);
        adviceButton.setOnClickListener(this);
        myInfomationButton.setOnClickListener(this);
        rankingInfomationButton.setOnClickListener(this);
        tutorialButton.setOnClickListener(this);
        gameStartButton.setOnClickListener(this);

        F_userInfo=getIntent().getStringExtra("1P_id");
        S_userInfo=getIntent().getStringExtra("2P_id");

    }

    @Override
    public void onClick(View v) {
        if(v == logoutButton){
            click.start();
            bgm.stop();

            Intent logoutIntent=new Intent(this,MainActivity.class);
            Toast.makeText(this,"로그아웃",Toast.LENGTH_SHORT).show();
            startActivity(logoutIntent);
        }else if(v == adviceButton){
            click.start();

            Intent adviceIntent=new Intent(this,AdviceActivity.class);
            startActivity(adviceIntent);
        }else if(v == myInfomationButton){
            click.start();

            Intent myInfomationIntent=new Intent(this,MyInfomationActivity.class);

            myInfomationIntent.putExtra("1P_id",F_userInfo);//사용자 아이디 넘겨주기
            myInfomationIntent.putExtra("2P_id",S_userInfo);

            startActivity(myInfomationIntent);
        }else if(v == rankingInfomationButton){
            click.start();
            Intent rankingIntent=new Intent(this,RankingActivity.class);

            rankingIntent.putExtra("1P_id",F_userInfo);
            rankingIntent.putExtra("2P_id",S_userInfo);

            startActivity(rankingIntent);
            //랭킹정보화면으로 이동
        }else if(v == tutorialButton){
            click.start();
            Intent tutorialIntent=new Intent(this,TutorialStartActivity.class);

            startActivity(tutorialIntent);
        }else if(v == gameStartButton){
            click.start();
            Intent gameStartIntent=new Intent(this,GameStartActivity.class);

            gameStartIntent.putExtra("1P_id",F_userInfo);
            gameStartIntent.putExtra("2P_id",S_userInfo);

            startActivity(gameStartIntent);

        }
    }
}
