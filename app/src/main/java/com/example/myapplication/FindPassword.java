package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FindPassword extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_find_password,container,false);

        TextView outputPassword=(TextView)rootView.findViewById(R.id.outputPassword);

        String password=getArguments().getString("password");

        outputPassword.setText("비밀번호는 "+password+" 입니다. ");

        return rootView;
    }

}
