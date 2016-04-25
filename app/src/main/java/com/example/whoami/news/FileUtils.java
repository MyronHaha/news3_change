package com.example.whoami.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by whoami on 2016/4/7.
 * FileUtils 文件操作的工具类，提供保存图片，获取图片，判断图片是否存在，删除图片的一些方法，这个类比较简单
 */
public class FileUtils {
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
    private static String mDataRootPath = null;
    private final static String FOLDER_NAME = "/AndroidImage";
    public FileUtils(Context context){
        mDataRootPath = context.getCacheDir().getPath();
    }
    public String getStorageDirectory(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
    }
    public void saveBitmap(String filename,Bitmap bitmap)throws IOException{
        if(bitmap==null)
            return;
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if(!folderFile.exists()){
            folderFile.mkdir();
        }
        File file = new File(path+File.separator+filename);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        fos.flush();
        fos.close();
    }
    public Bitmap getBitmap(String fileName,int width,int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName, options);
        options.inSampleSize = getSampSize(options, width,height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName, options);
        return bitmap;
    }


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

    public boolean isFileExists(String fileName){
        return new File(getStorageDirectory() + File.separator + fileName).exists();
    }

    public long getFileSize(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).length();
    }

    public void deleteFile(){
        File dirFile = new File(getStorageDirectory());
        if(!dirFile.exists()){
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }
            dirFile.delete();
    }
}
