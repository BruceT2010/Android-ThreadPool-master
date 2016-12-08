/*
 * FileName:  MyRunnable.java
 * CopyRight:  Belong to  <XiaoMaGuo Technologies > own 
 * Description:  <description>
 * Modify By :  XiaoMaGuo ^_^ 
 * Modify Date:   2013-10-21
 * Follow Order No.:  <Follow Order No.>
 * Modify Order No.:  <Modify Order No.>
 * Modify Content:  <modify content >
 */
package com.bru.dev.threadpool.sample;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.bru.dev.threadpool.async.AsyncRunnable;
import com.bru.dev.threadpool.async.RunnableQueue;

/**
 * @author XiaoMaGuo ^_^
 * @version [version-code, 2013-10-21]
 * @TODO [The Class File Description]
 * @since [Product/module]
 */
public class MyRunnable extends AsyncRunnable {

    private boolean cancleTask = false;

    private boolean cancleException = false;

    private Handler mHandler = null;
    private RunnableQueue runnableQueue;

    public MyRunnable(Handler handler, RunnableQueue runnableQueue) {
        mHandler = handler;
        this.runnableQueue = runnableQueue;
    }

    /**
     * Overriding methods
     */
    @Override
    public void run() {
        Log.i("KKK", "MyRunnable  run() is executed!!! ");
        runBefore();
        if (cancleTask == false) {
            running();
            Log.i("KKK", "调用MyRunnable run()方法");
        }

        runAfter();
    }

    @Override
    protected void runAfter() {
        Log.i("KKK", "runAfter()");
    }

    @Override
    protected void running() {
        Log.i("KKK", "running()");
        try {
            // 做点有可能会出异常的事情！！！
            int prog = 0;
            if (cancleTask == false && cancleException == false) {
                while (prog < 101) {
                    SystemClock.sleep(100);

                    if (cancleTask == false) {
                        mHandler.sendEmptyMessage(prog++);
                        Log.i("KKK", "调用 prog++ = " + (prog));
                    }
                }
            }
            runnableQueue.remove(this);
        } catch (Exception e) {
            cancleException = true;
        }
    }

    @Override
    protected void runBefore() {
        Log.i("KKK", "runBefore()");
    }

    @Override
    public void setCancelTaskUnit(boolean cancleTask) {
        this.cancleTask = cancleTask;
        Log.i("KKK", "点击了取消任务按钮 ！！！");
        // mHandler.sendEmptyMessage(0);
    }

}
