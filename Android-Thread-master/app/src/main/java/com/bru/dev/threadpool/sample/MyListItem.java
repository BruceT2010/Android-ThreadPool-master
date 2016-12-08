package com.bru.dev.threadpool.sample;

/**
 * Class Desc: Class Desc
 * <p/>
 * Creator : Bruce Ding
 * <p/>
 * Email : brucedingdev@foxmail.com
 * <p/>
 * Create Time: 2016/12/06 10:44
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author XiaoMaGuo ^_^
 * @version [version-code, 2013-10-22]
 * @TODO [一个简单的自定义 ListView Item]
 * @since [Product/module]
 */
class MyListItem extends LinearLayout {

    public MyListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyListItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyListItem(Context context) {
        this(context, null);
    }

    public MyListItem(Context context, View def, int s) {
        this(context);
        this.rootView = def;
    }

    private View rootView = null;
    private TextView mTitle;
    private ProgressBar mProgress;

    public void setTitle(String title) {

        if (mTitle == null) {
            mTitle = (TextView) rootView.findViewById(R.id.task_name);
        }
        mTitle.setText(title);
    }

    public void setProgress(int pro) {
        if (mProgress == null) {
            mProgress = (ProgressBar) rootView.findViewById(R.id.task_progress);
        }
        mProgress.setProgress(pro);
    }

}