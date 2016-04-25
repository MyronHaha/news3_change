package com.example.whoami.news;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wilddog.client.AuthData;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RoomFragment extends Fragment {

    ListView listView;
    Wilddog ref;
    ArrayList<String> users;
    ArrayList<String> mkeys;
    ArrayList<String> mvalues;
    HashMap<String,String> map;
    String name;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v =  inflater.inflate(R.layout.activity_room,container,false);

        return  v;

    }

    private void initUser() {

        ref = new Wilddog(MyConst.PATH);
        AuthData authData = ref.getAuth();
        if(authData!=null){
            String uid = authData.getUid();
            ref = new Wilddog(MyConst.PATH).child(MyConst.APP_USER).child(uid).child("name");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = (String) dataSnapshot.getValue();
                    Toast.makeText(getActivity(), "name="+name, Toast.LENGTH_SHORT).show();
                    initData();
                }

                @Override
                public void onCancelled(WilddogError wilddogError) {

                }
            });
        }else{
            Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) initUser();;
    }

    private void initData() {
        map = new HashMap<>();
        mkeys = new ArrayList<>();
        mvalues = new ArrayList<>();
        ref = new Wilddog(MyConst.PATH).child(MyConst.APP_USER);

        AuthData authData = ref.getAuth();
        //Toast.makeText(getActivity(), ""+authData, Toast.LENGTH_SHORT).show();

        if(authData==null)return;
        String uid = authData.getUid();
        ref = new Wilddog(MyConst.PATH).child(MyConst.APP_USER).child(uid).child("firends");
        listView = (ListView) v.findViewById(R.id.listView);
        users = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                map = (HashMap<String, String>) dataSnapshot.getValue();
                Iterator<String> it = map.keySet().iterator();
                while(it.hasNext()){
                    String k = it.next();
                    String  v = (String) map.get(k);
                    mkeys.add(k);
                    mvalues.add(v);
                }
                if(map!=null){
                    BaseAdapter baseadapter = new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return mkeys.size();
                        }

                        @Override
                        public Object getItem(int i) {
                            return mkeys.get(i);
                        }

                        @Override
                        public long getItemId(int i) {
                            return i;
                        }

                        @Override
                        public View getView(int i, View view, ViewGroup viewGroup) {

                            LinearLayout linearlayout = new LinearLayout(getActivity());
                            TextView name = new TextView(getActivity());
                            name.setText(mkeys.get(i));
                            linearlayout.addView(name);
                            return linearlayout;


                        }
                    };
                    listView.setAdapter(baseadapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent it = new Intent(getActivity(),ChatActivity.class);
                            Toast.makeText(getActivity(), name+"=========:"+mkeys.get(i)+":"+mvalues.get(i), Toast.LENGTH_SHORT).show();
                                it.putExtra("mUsername",name);
                                it.putExtra("mFirend",mkeys.get(i));
                                it.putExtra("roomid",mvalues.get(i));
                                getActivity().startActivity(it);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {
                Toast.makeText(getActivity(), "服务器获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
