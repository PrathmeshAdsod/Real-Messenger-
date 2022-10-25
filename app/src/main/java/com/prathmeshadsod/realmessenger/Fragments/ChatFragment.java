package com.prathmeshadsod.realmessenger.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prathmeshadsod.realmessenger.Adapter.UsersAdapter;
import com.prathmeshadsod.realmessenger.Models.User;
import com.prathmeshadsod.realmessenger.databinding.FragmentChatBinding;

import java.util.ArrayList;


public class ChatFragment extends Fragment {

    FragmentChatBinding bind;
    ArrayList<User> userArrayList;
    UsersAdapter usersAdapter;

    FirebaseAuth auth;
    FirebaseDatabase database;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment using view binding
        bind = FragmentChatBinding.inflate(inflater , container , false);

        userArrayList = new ArrayList<>();
        usersAdapter = new UsersAdapter(getContext() , userArrayList);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        bind.chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bind.chatRecyclerView.setAdapter(usersAdapter);

        /* We will change this reference later to users in which at least one massage conversation is done */
        /* Write now app is in development mode so this is good to go ... Change it later */
        /* To achieve this we can add a node like Friends to log_in_user node and from their fetch his friends profile in chats_fragments*/
        /* We will keep another user in currently logged in users friend list in which at least one conversation is done */

        database.getReference().child("messenger_users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {

                    User user = snapshot1.getValue(User.class);

                    if(auth.getCurrentUser().getUid().equals(user.getUid())) continue;

                    userArrayList.add(user);
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return bind.getRoot();
    }
}