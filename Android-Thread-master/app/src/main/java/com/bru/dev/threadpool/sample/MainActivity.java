package com.bru.dev.threadpool.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Class Desc: Class Desc
 * <p/>
 * Creator : Bruce Ding
 * <p/>
 * Email : brucedingdev@foxmail.com
 * <p/>
 * Create Time: 2016/12/06 10:25
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private Button poolTest_btn, imageLoader_btn, myRunnable_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        poolTest_btn = (Button) findViewById(R.id.poolTest_btn);
        imageLoader_btn = (Button) findViewById(R.id.imageLoader_btn);
        myRunnable_btn = (Button) findViewById(R.id.myRunnable_btn);
        poolTest_btn.setOnClickListener(this);
        imageLoader_btn.setOnClickListener(this);
        myRunnable_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.poolTest_btn:
                intent.setClass(this, AsyncTaskPoolActivity.class);
                break;

            case R.id.imageLoader_btn:
                intent.setClass(this, AsyncImageLoaderActivity.class);
                break;

            case R.id.myRunnable_btn:
                intent.setClass(this, MyRunnableActivity.class);
                break;

            default:
                break;
        }
        startActivity(intent);
    }
}