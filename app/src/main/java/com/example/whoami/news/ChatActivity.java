package com.example.whoami.news;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whoaim.dao.Chat;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ServerValue;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

public class ChatActivity extends Activity {

    private Wilddog mWilddogRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    View view;

    private String mUsername;
    private String mFirend;
    private String roomid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        chat();
    }


    @Override
    protected void onStart() {
        super.onStart();
        final ListView listView = (ListView) this.findViewById(R.id.listview);
        mChatListAdapter = new ChatListAdapter(mWilddogRef.limit(50), this, R.layout.chat_message, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mWilddogRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(ChatActivity.this, "Connected to Wilddog", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity.this, "Disconnected from Wilddog", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {
                // No-op
            }
        });
    }

    private void chat() {
        this.setTitle("和好友: " + mFirend + " 的聊天");

        mWilddogRef = new Wilddog(MyConst.PATH).child("rooms").child(roomid);



        EditText inputText = (EditText) this.findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        this.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        EditText inputText = (EditText) this.findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, mUsername);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mWilddogRef.push().setValue(chat);
            inputText.setText("");
        }
    }

    private void initData() {
        Intent intent = getIntent();
        this.mUsername = intent.getStringExtra("mUsername");
        this.mFirend = intent.getStringExtra("mFirend");
        this.roomid = intent.getStringExtra("roomid");


    }
}
