package com.example.whoami.news.slideMenu;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.whoami.news.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Myron on 2016/4/25.
 */
public class TestWrite extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.test);
        super.onCreate(savedInstanceState);
    }
    public void test(View v){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
        File file=new File(path+"//test22.txt");//注意是双斜杠；

        if(!file.exists()) {
            //若目录下文件不存在则创建文件；
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
            try {
                FileOutputStream fos =new FileOutputStream(file,true);
                String [] arr=new String[]{"item1","item2","item3","item4"};
                String hh="\n";
                for(int i=0;i<arr.length;i++){
                    fos.write(arr[i].getBytes());
                    fos.write(hh.getBytes());

                }
                fos.flush();
                Toast.makeText(this,"写进成功",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
            }


    }
}
