package com.bru.dev.threadpool.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bru.dev.threadpool.async.AsyncImageLoader;

public class AsyncImageLoaderActivity extends Activity {

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;

    String url1 = "http://img1.mm131.com/pic/2330/1.jpg";
    String url2 = "http://img1.mm131.com/pic/2330/2.jpg";
    String url3 = "http://img1.mm131.com/pic/2330/3.jpg";

    private AsyncImageLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.executor);
        imageView1 = (ImageView) findViewById(R.id.exextor_img);
        imageView2 = (ImageView) findViewById(R.id.exextor_img1);
        imageView3 = (ImageView) findViewById(R.id.exextor_img2);
        Log.i("tag", "main  " + Thread.currentThread().getName());
        loader = new AsyncImageLoader();
        loader.loadImage(url1, imageView1);
        loader.loadImage(url2, imageView2);
        loader.loadImage(url3, imageView3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loader.onDestroy();
    }
}
