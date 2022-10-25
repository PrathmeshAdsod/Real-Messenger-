package com.prathmeshadsod.realmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathmeshadsod.realmessenger.Adapter.MessagesAdapter;
import com.prathmeshadsod.realmessenger.Models.Message;
import com.prathmeshadsod.realmessenger.databinding.ActivityConversationBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Conversation extends AppCompatActivity {

    ActivityConversationBinding bind;
    Toolbar chatToolbar;
    MessagesAdapter adapter;
    ArrayList<Message> messages;
    String sender_room;
    String receiver_room;

    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityConversationBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        messages = new ArrayList<>();
        adapter = new MessagesAdapter(this , messages);

        bind.conversationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bind.conversationRecyclerView.setAdapter(adapter);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        setSupportActionBar(bind.chatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bind.sendMessage.requestFocus();

        String friendName = getIntent().getStringExtra("friendName");
        String friendUid = getIntent().getStringExtra("friendUid");
        String user_uid = auth.getUid();

        sender_room = user_uid + friendUid;
        receiver_room = friendUid + user_uid;

        bind.chatToolbar.setTitle(friendName);

        database.getReference().child("conversation").child(sender_room).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Message message = snapshot1.getValue(Message.class);
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bind.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = bind.sendMessage.getText().toString();
                message = message.trim();
                if(message.length() == 0) return;

                Date date = new Date();

                Message messageClass = new Message(message,user_uid,date.getTime());
                database.getReference().child("conversation").child(sender_room).child("messages").push().setValue(messageClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("conversation").child(receiver_room).child("messages").push().setValue(messageClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
                bind.sendMessage.setText("");
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}