package com.bru.dev.threadpool.async;

import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class Desc: Class Desc
 * <p/>
 * Creator : Bruce Ding
 * <p/>
 * Email : brucedingdev@foxmail.com
 * <p/>
 * Create Time: 2016/12/06 15:16
 */
public class RunnableQueue<T extends AsyncRunnable> {

    private String TAG = getClass().getSimpleName();
    /**
     * 任务执行队列
     */
    private ConcurrentLinkedQueue<T> taskQueue = null;

    /**
     * 正在等待执行或已经完成的任务队列
     * <p/>
     * 备注：Future类，一个用于存储异步任务执行的结果，比如：判断是否取消、是否可以取消、是否正在执行、是否已经完成等
     */
    private ConcurrentMap<Future, T> taskMap = null;

    /**
     * 创建一个不限制大小的线程池 此类主要有以下好处 1，以共享的无界队列方式来运行这些线程. 2，执行效率高。 3，在任意点，在大多数 nThreads 线程会处于处理任务的活动状态
     * 4，如果在关闭前的执行期间由于失败而导致任何线程终止，那么一个新线程将代替它执行后续的任务（如果需要）。
     */
    private ExecutorService mES = null;

    /**
     * 在此类中使用同步锁时使用如下lock对象即可，官方推荐的，不推荐直接使用MyRunnableActivity.this类型的,可以详细读一下/framework/app下面的随便一个项目
     */
    private Object lock = new Object();

    /**
     * 唤醒标志，是否唤醒线程池工作
     */
    private boolean isNotify = true;

    /**
     * 线程池是否处于运行状态(即:是否被释放!)
     */
    private boolean isRuning = true;

    public RunnableQueue() {
        taskQueue = new ConcurrentLinkedQueue<>();
        taskMap = new ConcurrentHashMap<>();
        if (mES == null) {
            mES = Executors.newSingleThreadExecutor();
        }
    }

    public void addTask(final T mr) {
        if (mES == null) {
            mES = Executors.newSingleThreadExecutor();
            notifyWork();
        }

        if (taskQueue == null) {
            taskQueue = new ConcurrentLinkedQueue<>();
        }

        if (taskMap == null) {
            taskMap = new ConcurrentHashMap<>();
        }

        mES.execute(new Runnable() {

            @Override
            public void run() {
                /**
                 * 插入一个Runnable到任务队列中 这个地方解释一下,offer跟add方法,试了下,效果都一样,没区别,官方的解释如下: 1 offer : Inserts the specified
                 * element at the tail of this queue. As the queue is unbounded, this method will never return
                 * {@code false}. 2 add: Inserts the specified element at the tail of this queue. As the queue is
                 * unbounded, this method will never throw {@link IllegalStateException} or return {@code false}.
                 *
                 *
                 * */
                taskQueue.offer(mr);
                // taskQueue.add(mr);
                //notifyWork();
                start();
            }
        });
    }

    public void release() {
        isRuning = false;

        Iterator iter = taskMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Future, Runnable> entry = (Map.Entry<Future, Runnable>) iter.next();
            Future result = entry.getKey();
            if (result == null) {
                continue;
            }
            result.cancel(true);
            taskMap.remove(result);
        }
        if (null != mES) {
            mES.shutdown();
        }

        mES = null;
        taskMap = null;
        taskQueue = null;

    }

    public void reload(final T mr) {
        if (mES == null) {
            mES = Executors.newSingleThreadExecutor();
            notifyWork();
        }

        if (taskQueue == null) {
            taskQueue = new ConcurrentLinkedQueue<>();
        }

        if (taskMap == null) {
            taskMap = new ConcurrentHashMap<>();
        }

        mES.execute(new Runnable() {

            @Override
            public void run() {
                /** 插入一个Runnable到任务队列中 */
                taskQueue.offer(mr);
                // taskQueue.add(mr);
                notifyWork();
            }
        });

        mES.execute(new Runnable() {
            @Override
            public void run() {
                if (isRuning) {
                    T myRunnable = null;
                    synchronized (lock) {
                        myRunnable = taskQueue.poll(); // 从线程队列中取出一个Runnable对象来执行，如果此队列为空，则调用poll()方法会返回null
                        if (myRunnable == null) {
                            isNotify = true;
                        }
                    }

                    if (myRunnable != null) {
                        taskMap.put(mES.submit(myRunnable), myRunnable);
                    }
                }
            }
        });
    }

    public void stop() {
        for (T runnable : taskMap.values()) {
            runnable.setCancelTaskUnit(true);
        }
    }

    public void remove(T task) {
        taskQueue.remove(task);
        start();
    }

    public void start() {
        Log.v(TAG, "Queue Size --> " + taskQueue.size());
        if (mES == null || taskQueue == null || taskMap == null) {
            Log.i("KKK", "某资源是不是已经被释放了？");
            return;
        }
        mES.execute(new Runnable() {
            @Override
            public void run() {
                if (isRuning) {
                    T myRunnable = null;
                    synchronized (lock) {
                        // 从线程队列中取出一个Runnable对象来执行，如果此队列为空，则调用poll()方法会返回null
                        myRunnable = taskQueue.poll();
                        if (myRunnable == null) {
                            isNotify = true;
                            // try
                            // {
                            // myRunnable.wait(500);
                            // }
                            // catch (InterruptedException e)
                            // {
                            // e.printStackTrace();
                            // }
                        }
                    }

                    if (myRunnable != null) {
                        taskMap.put(mES.submit(myRunnable), myRunnable);
                    }
                }

            }
        });
    }

    public void notifyWork() {
        synchronized (lock) {
            if (isNotify) {
                lock.notifyAll();
                isNotify = !isNotify;
            }
        }
    }
}
