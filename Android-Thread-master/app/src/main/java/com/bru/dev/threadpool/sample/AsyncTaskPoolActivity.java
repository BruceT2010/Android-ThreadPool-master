package com.bru.dev.threadpool.sample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.bru.dev.threadpool.async.ExcutorTask;

import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AsyncTaskPoolActivity extends Activity {

    private int count = Runtime.getRuntime().availableProcessors() + 2;
    ListView listView;
    private int order = 0;
    boolean isCancle = false;
    ArrayList<AsyTaskItem> list;
    private ExcutorTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        task = new ExcutorTask();
        listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(new MyAdapter(getApplication(), count));
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //关闭任务
                    task.shutDownNow();
                } else {
//                    AsyTaskItem asyncTask = list.get(position);
//                    if (!isCancle) {
//                        asyncTask.cancel(false);
//                    } else {
//                        asyncTask = new AsyTaskItem(asyncTask.item, position);
//                        asyncTask.executeOnExecutor(task.getExecutor());
//                    }
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        int taskCount;

        public MyAdapter(Context context, int taskCount) {
            this.context = context;
            this.taskCount = taskCount;
            inflater = LayoutInflater.from(context);
            list = new ArrayList<AsyncTaskPoolActivity.AsyTaskItem>(taskCount);
        }

        @Override
        public int getCount() {
            return taskCount;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_view_item, null);
                MyListItem item = new MyListItem(context, convertView, 1);
                AsyTaskItem asyitem = new AsyTaskItem(item, position);
                //添加任务
                asyitem.executeOnExecutor(task.getExecutor());
                list.add(asyitem);
            }

            return convertView;
        }

    }

    class AsyTaskItem extends AsyncTask<Void, Integer, Void> {

        MyListItem item;

        String id;

        public AsyTaskItem(MyListItem item, int position) {
            this.item = item;
            id = "执行:" + String.valueOf(position + 1);
        }

        @Override
        protected void onPreExecute() {
            item.setTitle(id);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (!isCancelled() && isCancle == false) {
                int pro = 0;
                while (pro < 101) {
                    SystemClock.sleep(100);
                    publishProgress(pro);
                    pro++;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            item.setProgress(values[0]);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.shutDownNow();
    }
}
