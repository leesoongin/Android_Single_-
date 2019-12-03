package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TutorialEndActivity extends AppCompatActivity implements View.OnClickListener{

    TextView FP_score,SP_score;
    Button finishButton;

    int FP_gamePoint,SP_gamePoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_end);

        FP_score=(TextView)findViewById(R.id.FP_score);
        SP_score=(TextView)findViewById(R.id.SP_score);

        finishButton=(Button)findViewById(R.id.finishButton);

        FP_gamePoint=getIntent().getIntExtra("FP_score",1);
        SP_gamePoint=getIntent().getIntExtra("SP_score",1);

        FP_score.setText(""+FP_gamePoint);
        SP_score.setText(""+SP_gamePoint);

        finishButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == finishButton){
            Intent lobyIntent=new Intent(this,lobyActivity.class);
            startActivity(lobyIntent);
        }
    }
}
