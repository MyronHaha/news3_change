package com.example.whoami.news;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.wilddog.client.DataSnapshot;

import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class FirendsFragment extends Fragment {



    Wilddog mWilddogRef;
    View v;
    ListView listView;
    SimpleAdapter ad;
    ArrayList<String> roomid;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                    //传递对象

                    User user = (User)msg.obj;
                    initList(user);
                    break;
                case 2:
                    Toast.makeText(getActivity(), "你还没有好友", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public FirendsFragment() {
        
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        roomid=new ArrayList<>();
        return inflater.inflate(R.layout.fragment_firends, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        listView = (ListView) v.findViewById(R.id.firendlistview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(),roomid.get(i), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),roomid.get(i).toString(), Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                Bundle b= new Bundle();
                b.putString("mUsername","666");
                b.putString("mFirend","aaa");
                b.putString("roomid", roomid.get(i));
                transaction.replace(R.id.body, ChatFragment.newInstance(b));
                transaction.commit();
            }
        });
        initdog();

    }

    @Override
    public void onStart() {
        super.onStart();
    }
    public void initdog(){
        mWilddogRef = new Wilddog(MyConst.PATH);
        mWilddogRef.child("appuser/666").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               User uu = (User) dataSnapshot.getValue(User.class);

                Message msg = new Message();
                msg.what=1;
                msg.obj = uu;
                handler.sendMessage(msg);
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {

            }
        });
    }
    List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    public void initList(User user){



            if (user.getFirends()==null)return;
            Map<String,Object> item = new HashMap<String,Object>();
            Map<String, String> map = user.getFirends();
            for (Map.Entry<String, String> entry : map.entrySet()) {

                item.put("user", entry.getKey());
               // item.put("room", entry.getValue());
                roomid.add(entry.getValue());
                data.add(item);
            }



        String[] from = {"user"};
        int[] to = {R.id.firend};
        ad = new SimpleAdapter(getActivity(),data,R.layout.firendsitem,from,to);
        this.listView.setAdapter(ad);
    }
}
