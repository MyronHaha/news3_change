package com.example.whoami.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wilddog.client.AuthData;
import com.wilddog.client.ChildEventListener;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.Query;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    EditText name,email,pwd;
    Wilddog mWilddogRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("注册");
        Wilddog.setAndroidContext(this);
        init();
    }
    public void init(){
        registerButton= (Button) findViewById(R.id.registerbutton);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = check();//检查
                if (b) {
                    register(name.getText().toString(), email.getText().toString(), pwd.getText().toString());//注册
                } else
                    Toast.makeText(RegisterActivity.this, "请把参数补充完整", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean check(){
        boolean b1 = TextUtils.isEmpty(name.getText());
        boolean b2 = TextUtils.isEmpty(email.getText());
        boolean b3 = TextUtils.isEmpty(pwd.getText());

        return !(b1 ||  b2 ||  b3);
    }
    public boolean register(final String name, final String email,final String pwd){
        mWilddogRef = new Wilddog(MyConst.PATH);

        mWilddogRef.createUser(email, pwd, new Wilddog.ResultHandler() {
            @Override
            public void onSuccess() {
                Wilddog ref = new Wilddog(MyConst.PATH);
                ref.authWithPassword(email, pwd, new Wilddog.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Wilddog myref = new Wilddog(MyConst.PATH);
                        myref.child("appuser").child(authData.getUid()).child("name").setValue(name);
                        //对应关系
                        myref.child("useremail").child(name).setValue(email);

                        Toast.makeText(RegisterActivity.this, "用户注册成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationError(WilddogError wilddogError) {

                    }

                });

            }

            @Override
            public void onError(WilddogError wilddogError) {
                Toast.makeText(RegisterActivity.this, "注册失败!" + wilddogError.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }


}