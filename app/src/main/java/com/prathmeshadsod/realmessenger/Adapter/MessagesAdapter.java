package com.prathmeshadsod.realmessenger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.prathmeshadsod.realmessenger.Models.Message;
import com.prathmeshadsod.realmessenger.R;
import com.prathmeshadsod.realmessenger.databinding.ReceivedMassagesBinding;
import com.prathmeshadsod.realmessenger.databinding.SendedMassagesBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {
    int SENT = 1;
    int RECEIVE = 2;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    Context context;
    ArrayList<Message> messages;

    public MessagesAdapter(Context context , ArrayList<Message> messages) {
        this.context = context;
        this.messages=messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.sended_massages , parent , false);
            return new SentViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.received_massages , parent , false);
            return new ReceiverViewHolder(view);
        }
        //return null;
    }
    /* getItemViewType() return the view type of item at position for purpose of view recycling */
    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(auth.getUid().equals(message.getSenderId())){
            return SENT;
        }else {
            return RECEIVE;
        }
      //  return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
          if(holder.getClass() == SentViewHolder.class) {
              Message message = messages.get(position);
              SentViewHolder viewHolder = (SentViewHolder) holder;
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "HH:mm");
              viewHolder.bind1.senderMessage.setText(message.getMessage());
              viewHolder.bind1.sendedTime.setText(simpleDateFormat.format(message.getTimeStamp()));
          }else {
              Message message = messages.get(position);
              ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
              viewHolder.bind2.receiverMessage.setText(message.getMessage());
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "HH:mm");
              viewHolder.bind2.receivedTime.setText(simpleDateFormat.format(message.getTimeStamp()));
          }
    }

    @Override
    public int getItemCount() {
        return (messages == null) ? 0 : messages.size();
        // We want if no messages/conversation is there then we should display no messages
        // otherwise it will give error here
    }

    /* class for sended_messages */
    public class SentViewHolder extends RecyclerView.ViewHolder {
        SendedMassagesBinding bind1;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            bind1 = SendedMassagesBinding.bind(itemView);
        }
    }

    /* class for received_messages */
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceivedMassagesBinding bind2;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            bind2 = ReceivedMassagesBinding.bind(itemView);
        }
    }

}
