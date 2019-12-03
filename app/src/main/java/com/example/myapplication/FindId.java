package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FindId extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_find_id,container,false);

        TextView outputId=(TextView)rootView.findViewById(R.id.outputId);

        String id=getArguments().getString("id");

        outputId.setText("아이디는 "+id+" 입니다.");

        return rootView;
    }

}
