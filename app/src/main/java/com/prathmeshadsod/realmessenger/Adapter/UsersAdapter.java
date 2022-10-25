package com.prathmeshadsod.realmessenger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.prathmeshadsod.realmessenger.Conversation;
import com.prathmeshadsod.realmessenger.R;
import com.prathmeshadsod.realmessenger.Models.User;
import com.prathmeshadsod.realmessenger.databinding.UsersConversationBinding;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder>{
    FirebaseAuth auth = FirebaseAuth.getInstance();

    Context context;
    ArrayList<User> usersList;

    public UsersAdapter(Context context , ArrayList<User> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.users_conversation , parent , false);

        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {

        User user = usersList.get(position);

        holder.bind.userNameOnChat.setText(user.getName());
        Glide.with(context).load(user.getProfileImage()).placeholder(R.drawable.user).into(holder.bind.userImageOnChat);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , Conversation.class);
                intent.putExtra("friendName" , user.getName());
                intent.putExtra("friendUid" , user.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

         UsersConversationBinding bind;

         public UsersViewHolder(@NonNull View itemView) {
             super(itemView);
             bind = UsersConversationBinding.bind(itemView);
         }
     }

}
