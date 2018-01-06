package cn.xdf.study;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.xdf.study.CountDownTimer.CountCodeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_getcode;
    private TextView tv_pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_getcode = this.findViewById(R.id.tv_getcode);
        tv_pause = this.findViewById(R.id.tv_pause);
        tv_getcode.setOnClickListener(this);
        tv_pause.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_getcode:
                Intent countCode = new Intent(MainActivity.this, CountCodeActivity.class);
                startActivity(countCode);
                break;
            case R.id.tv_pause:
                break;
        }
    }
}
