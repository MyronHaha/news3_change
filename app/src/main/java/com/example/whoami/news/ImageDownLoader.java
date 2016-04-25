package com.example.whoami.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class ImageDownLoader {

    private LruCache<String, Bitmap> mMemoryCache;
    private FileUtils fileUtils;


    /**
     * @param context 上下文
     */
    public ImageDownLoader(Context context) {

        int maxMomory = (int) Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMomory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        fileUtils = new FileUtils(context);
    }


    /**
     * 添加图片到缓存里面
     *
     * @param key    图片的代表url
     * @param bitmap 图片的bitmap对象
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {

        if (getBitmapFromMemoryCache(key) == null && bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }

    }

    /**
     * @param key 通过key把图片从内存里取出来
     * @return 图片的bitmap对象
     */
    public Bitmap getBitmapFromMemoryCache(String key) {

        return mMemoryCache.get(key);

    }

    /**
     * 下载图片的主要方法
     * @param addkey 它和方法里面的suburl属性搭配，成为图片在内存里独一无二的标记
     * @param url      图片的url地址
     * @param listener 图片下载完成的监听器，如果图片下载成功就会调用监听器里面的回调方法，在回调方法里可以处理下载成功的图片bitmap对象
     * @return 如果图片有在内存里或在硬盘里面直接返回图片对象，不然返回空值
     */

    public Bitmap downloadImage(String addkey,final String url, final int width, final int heigth, final onImageLoaderListener listener) {

        final String subUrl = url.replaceAll("[^\\w]", "");//图片在内存里的标记
        final String addkey_subUrl = addkey+subUrl;

        Bitmap bitmap = showCacheBitmap(addkey_subUrl,subUrl,width,heigth);//从内存或硬盘里查找图片
        if (bitmap != null) {
            return bitmap;
        } else {
            final Handler handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    //异步的回调接口,下载图片完成就会调用他
                    listener.onImageLoader((Bitmap) msg.obj, url);

                }
            };
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    //开始下载图片，下载完成发送消息调用回调接口。
                    Bitmap bitmap = getBitmapFormUrl(url,width,heigth);
                    Message msg = handler.obtainMessage();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);

                    try {
                        fileUtils.saveBitmap(subUrl, bitmap);//把图片保存到硬盘里面
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    addBitmapToMemoryCache(addkey_subUrl, bitmap);//把图片缓存到内存里面
                }
            }.start();

        }
        return null;
    }

    /**
     *
     * @param url 图片的url地址，用来下载网络图片
     * @param width 要求图片的宽度
     * @param height 要求图片的高度
     * @return 下载成功后放回图片的bitmap对象
     */

    private Bitmap getBitmapFormUrl(String url,int width,int height) {
        Bitmap bitmap = null;
        HttpURLConnection con = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();

        try {

            URL mImageUrl = new URL(url);

            con = (HttpURLConnection) mImageUrl.openConnection();
            con.setConnectTimeout(10 * 1000);
            con.setReadTimeout(10 * 1000);

            //把inputstream转换成byte[]数组
            InputStream in = con.getInputStream();
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buf = new byte[2048];
            int pos = 0;
            while ((pos = in.read(buf)) > 0) {
                swapStream.write(buf, 0, pos);
            }
            byte[] ok = swapStream.toByteArray();


            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(ok, 0, ok.length, options);//解析出真实图片的option
            options.inSampleSize = getSampSize(options, width,height);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(ok, 0, ok.length, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return bitmap;
    }

    /**
     *
     * @param options 图片的option
     * @param reqWidth 图片的需求宽度
     * @param reqHeight 图片的需求高度
     * @return 缩放比例
     */
    public int getSampSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int width = options.outWidth;//图片的真实宽度
        final int height = options.outHeight;//图片的真实高度

        int rs = 1;
        if (height > reqHeight || width > reqWidth) {//真实的图片比需要的大
            //按照小的计算比例
            if (reqWidth > reqHeight) {
                //按宽计算
                rs = Math.round((float) height / (float) reqHeight);
            } else {
                //按高计算
                rs = Math.round((float) width / (float) reqWidth);
            }
        }

        return rs;
    }


    /**
     *
     * @param url_cache 图片在内存的标记
     * @param url_disk 图片在硬盘的标记
     * @param width 请求图片的宽度
     * @param heigth 请求图片的高度
     * @return 返回一个图片图片在内存的bitmap对象或是空
     */
    public Bitmap showCacheBitmap(String url_cache,String url_disk,int width,int heigth) {

        if (getBitmapFromMemoryCache(url_cache) != null) {
            return getBitmapFromMemoryCache(url_cache);
        } else if (fileUtils.isFileExists(url_disk) && fileUtils.getFileSize(url_disk) != 0) {
            Bitmap bitmap = fileUtils.getBitmap(url_disk,width,heigth);//从硬盘里查找
            addBitmapToMemoryCache(url_cache, bitmap);
            return bitmap;
        }
        return null;
    }

    /**
     * 图片下载成功调用的接口
     */
    public interface onImageLoaderListener {
        void onImageLoader(Bitmap bitmap, String url);
    }
}



