package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    EditText inputId,inputPassword,inputCheckPassword,inputNicname,inputName,inputEmail;

    Button idOverlapButton,nicnameOverlapButton,passwordOverlapButton,emailOverlapButton,okButton,cancelButton;

    String id,password,checkPassword,nicname,name,email;

    dbHelper helper=new dbHelper(this);

    Boolean isIdCheck; //아이디 닉네임 중복검사 할 친구들
    Boolean isNicnameCheck;
    Boolean isPasswordCheck;
    Boolean isEmailCheck;
    Boolean[] isBlank=new Boolean[5]; //하나라도 비어있으면 등록 안되게끔 하는 친구들
    Boolean isAllCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputId=(EditText)findViewById(R.id.inputId);
        inputPassword=(EditText)findViewById(R.id.inputPassword);
        inputNicname=(EditText)findViewById(R.id.inputNicname);
        inputName=(EditText)findViewById(R.id.inputName);
        inputEmail=(EditText)findViewById(R.id.inputEmail);
        inputCheckPassword=(EditText) findViewById(R.id.inputCheckPassword);

        okButton=(Button)findViewById(R.id.okButton);
        cancelButton=(Button)findViewById(R.id.cancelButton);
        idOverlapButton=(Button)findViewById(R.id.idOverlapButton);
        nicnameOverlapButton=(Button)findViewById(R.id.nicnameOverlapButton);
        passwordOverlapButton=(Button)findViewById(R.id.passwordOverlapButton);
        emailOverlapButton=(Button)findViewById(R.id.emailOverlapButton);

        for(int i=0;i<isBlank.length;i++) //불리언들 초기화
            isBlank[i]=false;

        isIdCheck= false;
        isNicnameCheck=false;
        isPasswordCheck=false;
        isEmailCheck=false;
        isAllCheck=false;

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        idOverlapButton.setOnClickListener(this);
        nicnameOverlapButton.setOnClickListener(this);
        passwordOverlapButton.setOnClickListener(this);
        emailOverlapButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == idOverlapButton){//아이디 중복

           SQLiteDatabase db=helper.getReadableDatabase();

           id=inputId.getText().toString();

           Cursor cursor =db.rawQuery("select * from userInfoList where id='"+id+"'",null);

           if(cursor.getCount() == 0){ //아무것도 검색되지 않았다면
               Toast.makeText(this,"사용가능한 아이디 입니다.",Toast.LENGTH_SHORT).show();
               isIdCheck=true;
           }else{
               Toast.makeText(this,"중복된 아이디 입니다.",Toast.LENGTH_SHORT).show();
               isIdCheck=false;
           }

           db.close();

        }else if(v == nicnameOverlapButton){//닉네임 중복

            SQLiteDatabase db=helper.getReadableDatabase();

            nicname=inputNicname.getText().toString();

            Cursor cursor=db.rawQuery("select * from userInfoList where nicname='"+nicname+"'",null);

            if(cursor.getCount() == 0){ //아무것도 검색되지 않았다면
                Toast.makeText(this,"사용가능한 닉네임 입니다.",Toast.LENGTH_SHORT).show();
                isNicnameCheck=true;
            }else
                Toast.makeText(this,"중복된 닉네임 입니다.",Toast.LENGTH_SHORT).show();

            db.close();
        }else if(v == passwordOverlapButton){//패스워드 중복
            password=inputPassword.getText().toString();
            checkPassword=inputCheckPassword.getText().toString();

            if(password.equals(checkPassword)){//둘의 문자열이 같다면
                Toast.makeText(this,"일치하는 비밀번호 입니다.",Toast.LENGTH_SHORT).show();
                isPasswordCheck=true;
            }else
                Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();

        }else if(v == okButton){//확인
            id=inputId.getText().toString(); //중복검사 필요
            password=inputPassword.getText().toString();
            nicname=inputNicname.getText().toString();//중복검사 필요
            name= inputName.getText().toString();
            email=inputEmail.getText().toString();

            SQLiteDatabase db=helper.getWritableDatabase(); //읽고 쓰기

            isAllCheck=checkFunction(id,password,nicname,name,email); //중복 , 빈칸이 있는지 검사후 결과값

            if(isAllCheck == true){ //아이디 닉네임 중복, 빈칸이 없다면 등록
                 db.execSQL("insert into userInfoList (id,password,nicname,name,email,matchHistory,win,draw,lose,rankPoint) " +
                        "values ('"+id+"','"+password+"','"+nicname+"','"+name+"','"+email+"',0,0,0,0,0)");

                Toast.makeText(this,"회원가입에 성공하였습니다.",Toast.LENGTH_SHORT).show();

                 db.close();
                 finish();
            }else{//하나라도 만족안하면 다시해보셈
                if(isBlank[0] == false)
                    Toast.makeText(this,"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();
                else if(isIdCheck == false)
                    Toast.makeText(this,"아이디 중복확인을 해주세요.",Toast.LENGTH_SHORT).show();
                else if(isBlank[1] == false)
                    Toast.makeText(this,"패스워드를 입력해주세요.",Toast.LENGTH_SHORT).show();
                else if(isPasswordCheck == false)
                    Toast.makeText(this,"패스워드 중복확인을 해주세요.",Toast.LENGTH_SHORT).show();
                else if(isBlank[2] == false)
                    Toast.makeText(this,"닉네임을 입력해주세요.",Toast.LENGTH_SHORT).show();
                else if(isNicnameCheck == false)
                    Toast.makeText(this,"닉네임 중복확인을 해주세요.",Toast.LENGTH_SHORT).show();
                else if(isBlank[3] == false)
                    Toast.makeText(this,"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                else if(isBlank[4] == false)
                    Toast.makeText(this,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show();
                else if(isEmailCheck == false)
                    Toast.makeText(this,"이메일 중복확인을 해주세요.",Toast.LENGTH_SHORT).show();
            }
            db.close();
        }else if(v == emailOverlapButton){//이메일 중복확인버튼
            SQLiteDatabase db=helper.getReadableDatabase();

            email=inputEmail.getText().toString();

            Cursor cursor=db.rawQuery("select * from userInfoList where email='"+email+"'",null);

            if(cursor.getCount() == 0){
                Toast.makeText(this,"사용가능한 이메일 입니다.",Toast.LENGTH_SHORT).show();
                isEmailCheck =true;
            }else
                Toast.makeText(this,"중복된 이메일 입니다.",Toast.LENGTH_SHORT).show();

            db.close();

        }else if(v == cancelButton){//취소
            finish(); //이전화면으로 돌아가기.
        }

    }//onClick

    public Boolean checkFunction(String _id,String _password,String _nicname,String _name,String _email){ //공백 중복검사

       if(_id.length() != 0)
           isBlank[0]=true;
       if(_password.length() != 0)
           isBlank[1]=true;
       if(_nicname.length() != 0)
           isBlank[2]=true;
       if(_name.length() != 0)
           isBlank[3]=true;
       if(_email.length() != 0)
           isBlank[4]=true;

       if(isBlank[0] == true && isBlank[1] == true && isBlank[2] == true && isBlank[3] == true && isBlank[4] == true
               && isIdCheck == true && isNicnameCheck == true &&isPasswordCheck == true && isEmailCheck == true)
          return true;
       else
           return false;
    }//checkfunction
}//class
