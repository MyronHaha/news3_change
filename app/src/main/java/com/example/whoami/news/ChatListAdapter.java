package com.example.whoami.news;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.whoaim.dao.Chat;
import com.wilddog.client.Query;


public class ChatListAdapter extends WilddogListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    //这个属性是发消息的用户名，用来区分不同的用户发的消息
    private String mUsername;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
    }


    @Override

    protected void populateView(View view, Chat chat) {
        // 界面和模型的绑定
        String author = chat.getAuthor();
        TextView authorText = (TextView) view.findViewById(R.id.author);
        authorText.setText(author);
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
            authorText.setTextColor(Color.RED);
        } else {
            authorText.setTextColor(Color.BLUE);
        }
        ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());
    }
}
