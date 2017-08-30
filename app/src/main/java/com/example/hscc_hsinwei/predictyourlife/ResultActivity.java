package com.example.hscc_hsinwei.predictyourlife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by HSCC_HsinWei on 8/30/2017.
 */

public class ResultActivity extends AppCompatActivity {
    private TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultTextView = (TextView)findViewById(R.id.result);

        //取的intent中的bundle物件
        Bundle bundle =this.getIntent().getExtras();

        String result = bundle.getString("result");
        resultTextView.setText(result);
    }
}
