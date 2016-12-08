package com.bru.dev.threadpool.async;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自定义图片线程池图片下载器
 *
 * @author Administrator
 */
public class AsyncImageLoader {

    public Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (callback != null) {
                callback.onImageLoaded((Drawable) msg.obj);
            }
        }
    };

    public void loadImage(String url, ImageView imageView) {
        loadImage(url, null, imageView);
    }

    public void loadImage(String url, ImageCallback callback) {
        loadImage(url, callback, null);
    }

    public void loadImage(final String url, final ImageCallback callback, final ImageView imageView) {
        if (imageCache.containsKey(url)) {//有缓存
            SoftReference<Drawable> soft = imageCache.get(url);
            if (null != imageView)
                imageView.setImageDrawable(soft.get());
            if (callback != null) {
                callback.onImageLoaded(soft.get());
            }
        } else {
            executorService.execute(new Runnable() {

                @Override
                public void run() {
                    final Drawable drawable = loadFromURl(url);
                    imageCache.put(url, new SoftReference<Drawable>(drawable));
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (null != imageView)
                                imageView.setImageDrawable(drawable);

                            if (callback != null) {
                                callback.onImageLoaded(drawable);
                            }
                        }
                    });
                }
            });
        }

    }

    private Drawable loadFromURl(String url) {
        Drawable drawable = null;
        try {
            drawable = BitmapDrawable.createFromStream(new URL(url).openStream(), "img.gif");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public interface ImageCallback {
        public void onImageLoaded(Drawable drawable);
    }

    ImageCallback callback;

    public void setImageCallBack(ImageCallback callback) {
        this.callback = callback;
    }

    public void onDestroy() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

}
