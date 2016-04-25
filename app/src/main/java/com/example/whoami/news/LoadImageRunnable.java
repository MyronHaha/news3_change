package com.example.whoami.news;


        import java.io.IOException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.net.URLConnection;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Handler;
        import android.os.Message;
public class LoadImageRunnable implements Runnable
{

    private int mThreadId ;
    private Handler mHandler ;
    private String sUrl;
    public LoadImageRunnable(Handler h, int id, String str)
    {
        mHandler = h;
        mThreadId = id;
        sUrl = str;
    }
    @Override
    public void run()
    {
        Message msg = new Message();
        msg.what = mThreadId;
        msg.obj = loadImageFromNetwork();
        mHandler.sendMessage(msg);

    }

    // 从外部链接加载图片
    private Bitmap loadImageFromNetwork()
    {
        Bitmap bm = null;
        try
        {
            URL url = new URL(sUrl);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            bm = BitmapFactory.decodeStream(is);
        }
        catch (MalformedURLException e){

            e.printStackTrace();
            mHandler.sendEmptyMessage(10+mThreadId);

        }catch(IOException e)
        {
            mHandler.sendEmptyMessage(10+mThreadId);
            e.printStackTrace();

        }
        return bm;
    }

}
