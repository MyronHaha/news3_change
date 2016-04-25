package com.example.whoami.news;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.whoami.news.slideMenu.LocalNews;
import com.example.whoami.news.slideMenu.MyNews;
import com.example.whoami.news.slideMenu.SlideMenu;
import com.wilddog.client.Wilddog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Fragment> fralist;
    Intent menuIntent=new Intent();
    SlideMenu slideMenu;
    Bundle menu_bundle=new Bundle();//菜单bundle
    private FragmentManager fragmentManager = null;
    private FragmentTransaction transaction = null;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.slidemenu);

        super.onCreate(savedInstanceState);
        slideMenu = (SlideMenu) findViewById(R.id.slide);
        menu_bundle=new Bundle();

        init();
    }
    public void init(){
        Wilddog.setAndroidContext(this);

        ImageButton b=(ImageButton)findViewById(R.id.home);


        b.setOnClickListener(this);

        ImageButton b1=(ImageButton)findViewById(R.id.chat);
        b1.setOnClickListener(this);

        ImageButton b2 = (ImageButton)findViewById(R.id.set);
        b2.setOnClickListener(this);

        fralist = new ArrayList<>();
        NewsFragment newFrag = new NewsFragment();
        RoomFragment chatFrag = new RoomFragment();
        SetFragment setFrag = new SetFragment();
        fralist.add(newFrag);
        fralist.add(chatFrag);
        fralist.add(setFrag);


        fragmentManager = this.getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.body,newFrag);
        transaction.add(R.id.body,chatFrag);
        transaction.add(R.id.body, setFrag);
        transaction.show(newFrag).hide(chatFrag).hide(setFrag);
        transaction.commit();

    }
    //菜单触控
    public void localnew_click(View v){
        Toast.makeText(MainActivity.this, "localnews", Toast.LENGTH_SHORT).show();
        menuIntent=createIntent(MainActivity.this,LocalNews.class,"这是本地新闻啊");
        startActivity(menuIntent);

    }public void mynews_click(View v){
        Toast.makeText(MainActivity.this,"mynews",Toast.LENGTH_SHORT).show();
        menuIntent=createIntent(MainActivity.this,MyNews.class,"这是我的消息");
        startActivity(menuIntent);
    }
    public Intent createIntent(Context context, Class ca, String msg){
        Intent model=new Intent(context,ca);
        menu_bundle.putString("msg",msg);
        model.putExtra("menu_bundle", menu_bundle);
        return model;
    }
    @Override
    public void onClick(View view) {
        Toast.makeText(MainActivity.this, "run", Toast.LENGTH_SHORT).show();
            switch(view.getId()){
                case R.id.home:
                    switchFragment(0);
                    break;
                case R.id.chat:
                    switchFragment(1);
                    break;
                case R.id.set:
                    switchFragment(2);
                    break;
            }
    }

    public void switchFragment(int pos){
        transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        for (int i = 0; i < fralist.size(); i++) {
            if(pos==i){
                transaction.show(fralist.get(i));
            }else{
                transaction.hide(fralist.get(i));
            }
        }

        transaction.commit();
    }
    //判断是否要退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("闻友提示");
            // 设置对话框消息
            isExit.setMessage("您确定要退出吗？");
            // 添加选择按钮并注册监听
            isExit.setButton2("确定", listener);
            isExit.setButton("取消", listener);
            // 显示对话框
            isExit.show();

        }

        return false;

    }
    /**监听要退出对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_NEGATIVE:// "确定"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_POSITIVE:// "取消"第二个按钮取消对话框
                    break;

                default:
                    break;
            }
        }
    };




    //用于创建选项菜单的事件方法，在打开界面时会被自动调用
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

      /*  menu.add(0,200,2,"添加好友");
        menu.add(0,100,1,"扫一扫");
        menu.add(0,300,3,"搜索新闻");*/

        return true;
    }
    //菜单选项的单击事件处理方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        switch (id){
            case R.id.MM:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //builder.setTitle("请选择你想要的女朋友？");
                final String[] items = {"扫一扫", "查找好友", "搜索新闻", "同城搜索"};

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    //菜单选项的子菜单单击事件处理方法
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){

        }
        return true;
    }

}

