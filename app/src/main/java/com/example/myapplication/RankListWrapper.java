package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RankListWrapper { //리스트에 보여질 것들
//순위 닉네임 랭크포인트

    public TextView rank,nicname,rankPoint;

    public RankListWrapper(View root){
        rank=(TextView)root.findViewById(R.id.rank);
        nicname=(TextView)root.findViewById(R.id.nicname);
        rankPoint=(TextView)root.findViewById(R.id.rankPoint);
    }
}
