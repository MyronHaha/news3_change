package com.example.whoami.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wilddog.client.AuthData;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

public class LoginActivity extends AppCompatActivity
{

    static Wilddog ref;
    EditText name,pwd;
    Button registerbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
 }
    public void init(){
        Wilddog.setAndroidContext(this);
        setTitle("用户登录");
        ref = new Wilddog(MyConst.PATH);

        name= (EditText) findViewById(R.id.name);
        pwd = (EditText)findViewById(R.id.pwd);
        registerbutton= (Button) findViewById(R.id.loginbutton);

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = name.getText().toString();
                String pwdStr = pwd.getText().toString();
                                                                                                                                                                      Toast.makeText(LoginActivity.this, nameStr+":"+pwdStr, Toast.LENGTH_SHORT).show();
                if (nameStr.equals("") || pwdStr.equals("")) {
                    Toast.makeText(LoginActivity.this, "请把参数补充完整", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    login(nameStr, pwdStr);
                }
            }
        });

    }
    public void login(String name, final String pwd){
            ref.child("useremail").child(name).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.exists()) {
                        Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();

                    } else {
                        String email = (String) dataSnapshot.getValue();
                        Wilddog myref = new Wilddog(MyConst.PATH);
                        myref.authWithPassword(email, pwd, new Wilddog.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAuthenticationError(WilddogError wilddogError) {
                                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(WilddogError wilddogError) {

                }

            });
    }
}
/*
 ref.child(name).authWithPassword(email,pwd, new Wilddog.AuthResultHandler(){
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAuthenticationError(WilddogError wilddogError) {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
        });
 */
