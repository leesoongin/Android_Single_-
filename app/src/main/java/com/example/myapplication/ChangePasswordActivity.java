package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText currentPassword, re_currentPassword, newPassword, re_newPassword;
    Button okButton, cancelButton;

    dbHelper helper = new dbHelper(this);

    String getCurrentPassword, getRe_currentPassword, getNewPassword, getRe_newPassword;
    String DB_Password;//db값 저장할 친구

    Boolean isCheckCurrentPassword, isCheckNewPassword;

    String F_getId,S_getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPassword = (EditText) findViewById(R.id.currentPassword);
        re_currentPassword = (EditText) findViewById(R.id.re_currentPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        re_newPassword = (EditText) findViewById(R.id.re_newPassword);

        okButton = (Button) findViewById(R.id.okButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        isCheckCurrentPassword = false;
        isCheckNewPassword = false;

        F_getId = getIntent().getStringExtra("1P_id");
        S_getId=getIntent().getStringExtra("2P_id");

        Toast.makeText(this,"fff   "+F_getId,Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"sss   "+S_getId,Toast.LENGTH_SHORT).show();

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == okButton) {
            SQLiteDatabase db = helper.getWritableDatabase();

            getCurrentPassword = currentPassword.getText().toString();
            getRe_currentPassword = re_currentPassword.getText().toString();
            getNewPassword = newPassword.getText().toString();
            getRe_newPassword = re_newPassword.getText().toString();

            if(F_getId != null){//1p에서 왔을경우
                Cursor cursor = db.rawQuery("select * from userInfoList where id='" + F_getId + "'", null);

                while (cursor.moveToNext())
                    DB_Password = cursor.getString(2);
            }else if(S_getId != null){//2p에서 왔을경우
                Cursor cursor = db.rawQuery("select * from userInfoList where id='" + S_getId + "'", null);

                while (cursor.moveToNext())
                    DB_Password = cursor.getString(2);
            }




            if (getCurrentPassword.length() == 0 || getRe_currentPassword.length() == 0) {//공백
                Toast.makeText(this, "현재 설정되어 있는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (DB_Password.equals(getCurrentPassword)) {//입력한 비밀번호가 정확한 비밀번호라면
                if (getCurrentPassword.equals(getRe_currentPassword))//비밀번호 입력이 일치한다면
                    isCheckCurrentPassword = true;
                else {
                    isCheckCurrentPassword = false;
                    Toast.makeText(this, "두 입력값이 서로 다릅니다.", Toast.LENGTH_SHORT).show();
                }
            } else if(!DB_Password.equals(getCurrentPassword)){
                Toast.makeText(this, "올바르지 않은 비밀번호 입니다.", Toast.LENGTH_SHORT).show();
            }


           if (getNewPassword.length() == 0 || getRe_newPassword.length() == 0) {//공백
                Toast.makeText(this, "변경할 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (isCheckCurrentPassword == true) { //위에가 성립한다면
                if (getNewPassword.equals(getRe_newPassword))// 입력한 비밀번호 값이 같다면
                    isCheckNewPassword = true;
                else {
                    isCheckNewPassword = false;
                    Toast.makeText(this, "입력한 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            if (isCheckCurrentPassword == true && isCheckNewPassword == true) {//모든 조건을 만족한다면
                if(!getCurrentPassword.equals(getNewPassword)){
                    if(F_getId != null)//1p라면
                        db.execSQL("update userInfoList set password='" + getNewPassword + "' where id='" + F_getId + "'");
                    else if(S_getId != null)//2p 라면
                        db.execSQL("update userInfoList set password='" + getNewPassword + "' where id='" + S_getId + "'");

                    isCheckNewPassword = false;
                    isCheckCurrentPassword = false; //초기화
                    Toast.makeText(this, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this, "이전과 동일한 비밀번호는 사용하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            //공백if//변경할 비밀번호 두 값이 같은지 체크

        } else if (v == cancelButton) {
            finish();
        }
    }
}
