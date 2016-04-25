package com.example.whoami.news;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wilddog.client.AuthData;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ServerValue;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllUser extends Activity {
    ArrayList<User> users;
    ListView listview;
    Wilddog ref = null;
    DogTool dogTool = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users= new ArrayList<>();

        setContentView(R.layout.alluser);
        initData();
    }

    private void initData() {
        listview = (ListView) findViewById(R.id.listView);
        dogTool = new DogTool();
        ref = new Wilddog(MyConst.PATH).child(MyConst.APP_USER);

        getData(ref);

    }
    private void initList(){

        if (users.size()>0){
            listview.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return users.size();
                }

                @Override
                public Object getItem(int i) {
                    return users.get(i);
                }

                @Override
                public long getItemId(int i) {
                    return i;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {
                    RelativeLayout ra = (RelativeLayout) view.inflate(AllUser.this,R.layout.users,null);
                    TextView name = (TextView) ra.findViewById(R.id.name);
                    name.setText(users.get(i).getName());
                    return ra;
                }
            });
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ref = new Wilddog(MyConst.PATH).child(MyConst.APP_USER);
                User user = users.get(i);
                String name = user.getName();
                AuthData authData = ref.getAuth();
                if (authData == null) {
                    Toast.makeText(AllUser.this, "请登录....", Toast.LENGTH_SHORT).show();
                    return;
                }
                ref = new Wilddog(MyConst.PATH).child(MyConst.APP_USER).child(authData.getUid()).child("firends").child(name);
                ref.setValue(ServerValue.TIMESTAMP+"");
                Toast.makeText(AllUser.this, "添加成功", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getData(Wilddog ref) {
        users = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while(it.hasNext()){
                    DataSnapshot ds = it.next();
                    User u = (User) ds.getValue(User.class);
                    users.add(u);
                }
                initList();
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {

            }
        });
    }
}
