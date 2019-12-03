package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindInfomationActivity extends AppCompatActivity implements View.OnClickListener{

    EditText inputName,inputEmail,inputCheckName,inputCheckId;

    Button idOkButton,passwordOkButton,idCancelButton,passwordCancelButton,backButton;

    String name,email,getId,getPassword,getName; //입력받은 이름,이메일 , 검색결과 아이디, 패스워드,이름
    String checkName,checkId; //입력받은 데이터와 같은지 비교

    dbHelper helper=new dbHelper(this);

    FindId findId=new FindId(); //id프래그먼트
    FindPassword findPassword=new FindPassword();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_infomation);

        inputName=(EditText)findViewById(R.id.inputName);
        inputEmail=(EditText)findViewById(R.id.inputEmail);
        inputCheckName=(EditText)findViewById(R.id.inputCheckName);
        inputCheckId=(EditText)findViewById(R.id.inputCheckId);

        idOkButton=(Button)findViewById(R.id.idOkButton);
        passwordOkButton=(Button)findViewById(R.id.passwordOkButton);
        idCancelButton=(Button)findViewById(R.id.idCancelButton);
        passwordCancelButton=(Button)findViewById(R.id.passwordCancelButton);
        backButton=(Button)findViewById(R.id.backButton);

        idOkButton.setOnClickListener(this);
        passwordOkButton.setOnClickListener(this);
        idCancelButton.setOnClickListener(this);
        passwordCancelButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }//main

    @Override
    public void onClick(View v) {
        if(v == idOkButton){
            name=inputName.getText().toString();

            inputName.setText("");//값 넘겨주고 다시 초기화

            SQLiteDatabase db=helper.getReadableDatabase();

            Cursor cursor=db.rawQuery("select * from userInfoList where name='"+name+"'",null);

            if(cursor.getCount() == 1){ //일치하는 이름이 있다면
                while(cursor.moveToNext())
                    getId=cursor.getString(1);

                //정보전달
                Bundle args=new Bundle();
                args.putString("id",getId);
                findId.setArguments(args);

                Toast.makeText(this,"일치하는 아이디 입니다. 이름을 확인해주세요.",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.Linear_layout,findId).commitAllowingStateLoss();
            }else//없다면
                Toast.makeText(this,"일치하는 이름이 존재하지 않습니다.",Toast.LENGTH_SHORT).show();

            db.close();
        }else if(v == passwordOkButton){
            checkName=inputCheckName.getText().toString();
            checkId=inputCheckId.getText().toString();
            email=inputEmail.getText().toString();

            inputCheckName.setText("");//값 넘겨주고 초기화
            inputCheckId.setText("");
            inputEmail.setText("");

            SQLiteDatabase db=helper.getReadableDatabase();

            Cursor cursor=db.rawQuery("select * from userInfoList where email='"+email+"'",null);

            if(cursor.getCount() == 1){//일치하는 이메일이 있다면.
                while(cursor.moveToNext()){
                    getId=cursor.getString(1);
                    getPassword=cursor.getString(2);
                    getName=cursor.getString(4);
                }

                if(checkName.equals(getName) && checkId.equals(getId)){ //입력한 이름과 검색한 아이디,이름이 같다면
                    //정보전달
                    Bundle args=new Bundle();
                    args.putString("password",getPassword);
                    findPassword.setArguments(args);

                    Toast.makeText(this,"일치하는 이름,이메일 입니다. 패스워드를 확인해주세요.",Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.Linear_layout,findPassword).commitAllowingStateLoss();
                }else{
                    Toast.makeText(this,"일치하는 정보가 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this,"일치하는 정보가 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
            }

            db.close();
        }else if(v == idCancelButton)
            finish();
        else if(v == passwordCancelButton)
            finish();
        else if(v == backButton)
            finish();
    }//onClick
}
