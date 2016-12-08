package com.bru.dev.threadpool.async;

/**
 * Class Desc: Class Desc
 * <p/>
 * Creator : Bruce Ding
 * <p/>
 * Email : brucedingdev@foxmail.com
 * <p/>
 * Create Time: 2016/12/06 15:23
 */
public abstract class AsyncRunnable implements Runnable {

    @Override
    public void run() {
    }

    protected abstract void runBefore();

    protected abstract void runAfter();

    protected abstract void running();

    protected abstract void setCancelTaskUnit(boolean canceled);

}
