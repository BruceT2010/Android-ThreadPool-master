package com.bru.dev.threadpool.async;

import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Class Desc: Class Desc
 * <p/>
 * Creator : Bruce Ding
 * <p/>
 * Email : brucedingdev@foxmail.com
 * <p/>
 * Create Time: 2016/12/06 10:53
 */
public class ExcutorTask {

    private String TAG = getClass().getSimpleName();
    /**
     * 总共多少任务（根据CPU个数决定创建活动线程的个数,这样取的好处就是可以让手机承受得住）
     */
    private static final int count = Runtime.getRuntime().availableProcessors() - 2;
    private ExecutorService singleTaskService = null;
    private ExecutorService limitedTaskService = null;
    private ExecutorService allTaskService = null;
    private ExecutorService scheduledTaskService = null;
    private ExecutorService singleScheduledTaskService = null;

    // 线程工厂初始化方式一
    ThreadFactory tf = Executors.defaultThreadFactory();
    // 线程工厂初始化方式二
    private static class ThreadFactoryImpl implements ThreadFactory {

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("ThreadFactoryImpl");
            thread.setDaemon(true); // 将用户线程变成守护线程,默认false
            return thread;
        }
    }

    public ExcutorTask() {
        // 每次只执行一条任务的线程池
        singleTaskService = Executors.newSingleThreadExecutor();
        // 每次执行限定个数任务的线程池
        limitedTaskService = Executors.newFixedThreadPool(3);
        // 根据实际情况调整线程池中线程的数量的线程池
        allTaskService = Executors.newCachedThreadPool();
        // 指定时间里执行任务的线程池，可重复使用
        scheduledTaskService = Executors.newScheduledThreadPool(3);
        // 返回一个可以控制线程池内线程定时或周期性执行某任务的线程池。只不过和上面的区别是该线程池大小为1
        singleScheduledTaskService = Executors.newSingleThreadScheduledExecutor();
    }

    enum SERVICE_TYPE {
        SINGLE, LIMITED, ALL, SCHEDULED, SINGLE_SCHEDULED
    }

    private ExecutorService mExecutor;
    private SERVICE_TYPE type = SERVICE_TYPE.LIMITED;

    public void setServiceType(SERVICE_TYPE type) {
        this.type = type;
    }

//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }

    public ExecutorService getExecutor() {
        if (mExecutor != null)
            return mExecutor;
        ExecutorService executor = limitedTaskService;
        switch (type) {
            case SINGLE:
                executor = singleTaskService;
                break;
            case LIMITED:
                executor = limitedTaskService;
                break;
            case ALL:
                executor = allTaskService;
                break;
            case SCHEDULED:
                executor = scheduledTaskService;
                break;
            case SINGLE_SCHEDULED:
                executor = singleScheduledTaskService;
            default:
                break;
        }
        return executor;
    }

    /**
     * 会关闭线程池方式一：但不接收新的Task,关闭后，正在等待 执行的任务不受任何影响，会正常执行,无返回值!
     */
    public void shutDown() {
        getExecutor().shutdown();
        try {
            getExecutor().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 会关闭线程池方式二：也不接收新的Task，并停止正等待执行的Task（也就是说，
     * 执行到一半的任务将正常执行下去），最终还会给你返回一个正在等待执行但线程池关闭却没有被执行的Task集合！
     */
    public void shutDownNow() {
        List<Runnable> unExecRunn = getExecutor().shutdownNow();
        try {
            getExecutor().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Runnable r : unExecRunn) {
            Log.v("KKK", "未执行的任务信息：=" + unExecRunn.toString());
        }
        Log.v("KKK", "Is shutdown ? = " + String.valueOf(getExecutor().isShutdown()));
    }

    public void setExecutor(ExecutorService executor) {
        this.mExecutor = executor;
    }

}
