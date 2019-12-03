package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText firstPlayerId, firstPlayerPassword;
    EditText secondPlayerId, secondPlayerPassword;

    ImageButton loginButton, signUpButton, findPasswordButton;

    dbHelper helper=new dbHelper(this);

    String getFPI,getFPP,getSPI,getSPP;  //FirstPlayerId  ....  SecondPlayerPassword
    String F_id,F_password,S_id,S_password; //디비내용 저장할 변수

    Boolean isFPP,isSPP; //로그인 담당 불리언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstPlayerId = (EditText) findViewById(R.id.firstPlayerId);
        firstPlayerPassword = (EditText) findViewById(R.id.firstPlayerPassword);

        secondPlayerId = (EditText) findViewById(R.id.secondPlayerId);
        secondPlayerPassword = (EditText) findViewById(R.id.secondPlayerPassword);

        loginButton = (ImageButton) findViewById(R.id.loginButton);
        signUpButton = (ImageButton) findViewById(R.id.signUpButton);
        findPasswordButton = (ImageButton) findViewById(R.id.findPasswordButton);

        F_id=""; F_password=""; S_id=""; S_password="";
        isFPP=false; isSPP=false;

        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        findPasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            SQLiteDatabase db=helper.getReadableDatabase();

            getFPI=firstPlayerId.getText().toString();//입력한 문자열 저장
            getFPP=firstPlayerPassword.getText().toString();
            getSPI=secondPlayerId.getText().toString();
            getSPP=secondPlayerPassword.getText().toString();

            Cursor FPcursor=db.rawQuery("select * from userInfoList where id='"+getFPI+"'",null);

            while(FPcursor.moveToNext()){//첫번째 플레이어 정보 가져와서 저장
                F_id=FPcursor.getString(1);
                F_password=FPcursor.getString(2);
            }

            Cursor SPcursor=db.rawQuery("select * from userInfoList where id='"+getSPI+"'",null);

            while(SPcursor.moveToNext()){
                S_id=SPcursor.getString(1);
                S_password=SPcursor.getString(2);
            }

            ///////////////// 입력값 저장 and 디비값 불러와 저장 마침//////////////////

            //공백검사
           if(getFPI.length() == 0){
               Toast.makeText(this,"1P 아이디를 입력하세요.",Toast.LENGTH_SHORT).show();
           }else if(getFPP.length() == 0){
               Toast.makeText(this,"1P 비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
           }else if(getSPI.length() == 0){
               Toast.makeText(this,"2P 아이디를 입력하세요.",Toast.LENGTH_SHORT).show();
           }else if(getSPP.length() == 0){
               Toast.makeText(this,"2P 비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
           }else{//공백 검사 통과했다면
               //1P 아이디 비밀번호 확인
               if(FPcursor.getCount() == 1) {//1P의 정보가져와서 확인
                   if (getFPP.equals(F_password)) {//비밀번호가 일치한다면
                       isFPP = true;
                   } else {//inner if
                       isFPP=false;
                       Toast.makeText(this, "1P 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                   }
               }else{//해당하는 아이디가 없ㄷ다면
                   Toast.makeText(this, "1P 존재하지않은 아이디 입니다.", Toast.LENGTH_SHORT).show();
               }

               //2P 아이디 비밀번호 확인
               if(SPcursor.getCount() == 1){//2P의 정보 가져와서 확인
                   if(getSPP.equals(S_password)){//비밀번호가 일치한다면
                       isSPP=true;
                   }else{
                       isSPP=false;
                       Toast.makeText(this,"2P 비밀번호가 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                   }
               }else{
                   Toast.makeText(this,"2P 존재하지않는 아이디 입니다.",Toast.LENGTH_SHORT).show();
               }

               //로그인성공 값 전달.
                   if(isFPP == true && isSPP == true ){//둘다 검사 통과and 1p 2p아이디가 다르면
                       if(getFPI.equals(getSPI)){//두 계정이 서로 같다면
                           isFPP=false; isSPP=false; //불리언 초기화
                           Toast.makeText(this,"동일한 계정을 입력하셨습니다.",Toast.LENGTH_SHORT).show();
                       }else{//서로 다르다면
                           isFPP = false; isSPP= false;
                           firstPlayerId.setText(""); firstPlayerPassword.setText(""); //EditText 값 초기화
                           secondPlayerId.setText(""); secondPlayerPassword.setText("");

                           Toast.makeText(this,"로그인 성공 ",Toast.LENGTH_SHORT).show();
                           Intent lobyIntent = new Intent(this, lobyActivity.class);
                           //putExtra로 값전달
                           lobyIntent.putExtra("1P_id",F_id);
                           lobyIntent.putExtra("2P_id",S_id);

                           startActivity(lobyIntent);
                       }
                   }//if
           }//else
        } else if (v == signUpButton) {
            Intent signUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(signUpIntent);
        } else if (v == findPasswordButton) {
            Intent findPasswordIntent = new Intent(this, FindInfomationActivity.class);
            startActivity(findPasswordIntent);
        }//find
    }//onClick
}
