package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RankingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String F_getid, S_getId; //아이디 전달값 받기
    int F_getRank,S_getRank,F_getRankPoint,S_getRankPoint; //순위 저장하기

    TextView FP_myRanking, SP_myRanking;
        ListView rankList;

        ArrayList<UserIF> userData;//리스트뷰에 쓰일 데이터

        ArrayList<UserIF> myRankData; //1p 2p 랭킹확인할떄 쓸 데이터

        dbHelper helper = new dbHelper(this);
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ranking);

            FP_myRanking = (TextView) findViewById(R.id.FP_myRanking);
            SP_myRanking = (TextView) findViewById(R.id.SP_myRanking);

            rankList = (ListView) findViewById(R.id.rankList);

        F_getid = getIntent().getStringExtra("1P_id");
        S_getId = getIntent().getStringExtra("2P_id");

        SQLiteDatabase db=helper.getReadableDatabase();

        Cursor cursor =db.rawQuery("select * from userInfoList order by rankPoint desc",null);

        myRankData=new ArrayList<>();

        while(cursor.moveToNext()){//랭크포인트로 정렬된 친구들을 ArrayList에 순서대로 저장해서 position값으로 순위를 찾아내자.
            UserIF data=new UserIF();

            data.id=cursor.getString(1);
            data.rankPoint=cursor.getInt(10);

            myRankData.add(data);
        }

        for(int i=0;i<myRankData.size();i++){ //1p 2p 순위 받아오고.
            if(myRankData.get(i).id.equals(F_getid)){
                F_getRank=i+1;//인덱스값이니까 +1
                F_getRankPoint=myRankData.get(i).rankPoint;
            }else if(myRankData.get(i).id.equals(S_getId)){
                S_getRank=i+1;
                S_getRankPoint=myRankData.get(i).rankPoint;
            }
        }
        db.close();

        FP_myRanking.setText("1P 순위: "+F_getRank+"  랭크포인트:  "+F_getRankPoint);
        SP_myRanking.setText("2P 순위: "+S_getRank+"  랭크포인트:  "+S_getRankPoint);

        rankList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent readUserInfoIntent=new Intent(this,ReadUserInfo.class);
        readUserInfoIntent.putExtra("nicname",userData.get(position).nicname);
        readUserInfoIntent.putExtra("ranking",(position+1));

        startActivity(readUserInfoIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from userInfoList order by rankPoint desc", null);//내림차순

        userData = new ArrayList<>();

        while (cursor.moveToNext()) {//디비내용을 ArrayList에 저장하기
            UserIF userIF = new UserIF();

            userIF.nicname = cursor.getString(3);
            userIF.name = cursor.getString(4);
            userIF.email = cursor.getString(5);
            userIF.total = cursor.getInt(6);
            userIF.win = cursor.getInt(7);
            userIF.draw = cursor.getInt(8);
            userIF.lose = cursor.getInt(9);
            userIF.rankPoint = cursor.getInt(10);

            userData.add(userIF);
        }
        db.close();


        RankListAdapter adapter = new RankListAdapter(this, R.layout.rank_list_item, userData);

        rankList.setAdapter(adapter);

        //어댑터에 ArrayList내용 집어넣기
        //리스트뷰에 어탭터 세팅하기
    }
}
