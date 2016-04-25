package com.example.whoami.news;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whoaim.dao.PingLun;
import com.wilddog.client.Wilddog;

public class WritePinlunToolChange extends Fragment {


    public WritePinlunToolChange() {
        // Required empty public constructor
    }
    public WritePinlunToolChange(String key) {
        this.key = key;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.writepinglunchange,container,false);

        initData();
        return v;
    }
    String key;
    private void initData() {
        message = (EditText) v.findViewById(R.id.message);
        put = (Button) v.findViewById(R.id.put);
        ref = new Wilddog(MyConst.PATH).child(MyConst.PINGLUN).child(key);
        mypinglun = (MyListView) getActivity().findViewById(R.id.pinglun);
        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().length() < 5) {
                    Toast.makeText(getActivity(), "不少于5个字", Toast.LENGTH_SHORT).show();
                } else {
                    if(ref.getAuth()!=null){
                        String Me = ref.getAuth().getUid();
                        String messageStr = message.getText().toString();
                        String to = "";
                        String time = "3333";
                        int good = 0;
                        PingLun p = new PingLun(Me,time,messageStr,to,good);
                        myput(p);
                    };

                }
            }
        });
    }

    private void myput(PingLun pingLun) {
        ref.push().setValue(pingLun);
        message.setText("");
        Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
        RelativeLayout ra = (RelativeLayout) v.inflate(getActivity(), R.layout.newpinglunitem, null);
        initPinglun(ra,pingLun);
        //mypinglun.addView(ra,0);
    }

    private void initPinglun(RelativeLayout ra,PingLun pingLun) {
        TextView name = (TextView) ra.findViewById(R.id.name);
        TextView time = (TextView) ra.findViewById(R.id.time);
        TextView message = (TextView) ra.findViewById(R.id.message);

        name.setText(pingLun.getMe());
        time.setText(pingLun.getTime());
        message.setText(pingLun.getMessage());
    }

    MyListView mypinglun = null;
    View v;
    EditText message;
    Button put ;
    Wilddog ref;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            message.requestFocus();
        }
    }
}
