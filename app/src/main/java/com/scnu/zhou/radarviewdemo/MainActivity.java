package com.scnu.zhou.radarviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.scnu.zhou.widget.RadarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadarView radarView = (RadarView) findViewById(R.id.radarView);

        RadarView.RadarData data1 = new RadarView.RadarData(80, 100, "Java");
        RadarView.RadarData data2 = new RadarView.RadarData(80, 100, "C++");
        RadarView.RadarData data3 = new RadarView.RadarData(70, 100, "JavaScript");
        RadarView.RadarData data4 = new RadarView.RadarData(67, 100, "Android");
        RadarView.RadarData data5 = new RadarView.RadarData(55, 100, "iOS");
        RadarView.RadarData data6 = new RadarView.RadarData(45, 100, "C#");
        List<RadarView.RadarData> mData = new ArrayList<>();
        mData.add(data1);
        mData.add(data2);
        mData.add(data3);
        mData.add(data4);
        mData.add(data5);
        mData.add(data6);

        radarView.setRadarData(mData);
    }

}
