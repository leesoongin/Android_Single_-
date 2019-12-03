package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RankListAdapter extends ArrayAdapter<UserIF> {

    Context context;
    ArrayList<UserIF> data;
    int layout;

    public RankListAdapter(Context context, int layout, ArrayList<UserIF> data) {
        super(context, layout);
        this.context=context;
        this.data=data;
        this.layout=layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);

            RankListWrapper wrapper=new RankListWrapper(convertView);
            convertView.setTag(wrapper);
        }//if
        RankListWrapper wrapper=(RankListWrapper)convertView.getTag();

        TextView rank=(TextView)wrapper.rank;
        TextView nicname=(TextView)wrapper.nicname;
        TextView rankPoint=(TextView)wrapper.rankPoint;

        UserIF userIF=data.get(position);

        rank.setText(""+(position+1));
        nicname.setText(userIF.nicname);
        rankPoint.setText(""+userIF.rankPoint);

        return convertView;
    }//getVIew
}
