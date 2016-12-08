/*
 * FileName:  MyRunnableActivity.java
 * CopyRight:  Belong to  <XiaoMaGuo Technologies > own
 * Description:  <description>
 * Modify By :  XiaoMaGuo ^_^
 * Modify Date:   2013-10-21
 * Follow Order No.:  <Follow Order No.>
 * Modify Order No.:  <Modify Order No.>
 * Modify Content:  <modify content >
 */
package com.bru.dev.threadpool.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bru.dev.threadpool.async.RunnableQueue;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author XiaoMaGuo ^_^
 * @version [version-code, 2013-10-22]
 * @TODO [线程池控制 ]
 * @since [Product/module]
 */
public class MyRunnableActivity extends Activity implements OnClickListener {

    /**
     * 任务进度
     */
    private ProgressBar pb = null;

    /**
     * 用此Handler来更新我们的UI
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb.setProgress(msg.what);
        }

    };

    private RunnableQueue<MyRunnable> runnableQueue;

    /**
     * Overriding methods
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_runnable_main);
        init();
        runnableQueue = new RunnableQueue<>();
    }

    public void init() {
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                runnableQueue.start();
                break;
            case R.id.button2:
                Toast.makeText(MyRunnableActivity.this, "任务已被取消！", Toast.LENGTH_SHORT).show();
                runnableQueue.stop();
                break;
            case R.id.button3:
                mHandler.sendEmptyMessage(0);
                runnableQueue.reload(new MyRunnable(mHandler, runnableQueue));
                break;
            case R.id.button4:
                Toast.makeText(MyRunnableActivity.this, "释放所有占用的资源！", Toast.LENGTH_SHORT).show();
                runnableQueue.release();
                /** 将ProgressBar进度置为0 */
                mHandler.sendEmptyMessage(0);
                break;
            case R.id.button5:
                mHandler.sendEmptyMessage(0);
                runnableQueue.addTask(new MyRunnable(mHandler, runnableQueue));
                Toast.makeText(MyRunnableActivity.this, "已添加一个新任务到线程池中 ！", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

}
