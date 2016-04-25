package com.example.whoami.news;

import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by whoami on 2016/4/24.
 */
public class DogTool {

    public <T> ArrayList<T>  getData(Wilddog ref, final Class model){
        final ArrayList<T> arrayList = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while(it.hasNext()){
                    DataSnapshot ds = it.next();
                    T t = (T) ds.getValue(model);
                    arrayList.add(t);
                }

            }

            @Override
            public void onCancelled(WilddogError wilddogError) {

            }
        });
        return arrayList;
    }
}
