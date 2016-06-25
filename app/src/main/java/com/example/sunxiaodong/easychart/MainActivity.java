package com.example.sunxiaodong.easychart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mGoLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mGoLineChart = (Button) findViewById(R.id.go_line_chart);
        mGoLineChart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_line_chart: {
                goLineChart();
            }
            break;
        }
    }

    private void goLineChart() {
        Intent intent = new Intent(this, LineCharActivity.class);
        startActivity(intent);
    }

}
