package com.prathmeshadsod.realmessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prathmeshadsod.realmessenger.Adapter.FragmentAdapter;
import com.prathmeshadsod.realmessenger.databinding.ActivityChatViewsBinding;

public class ChatViews extends AppCompatActivity {

    ActivityChatViewsBinding bind;
    FirebaseAuth auth;
    Toolbar chatToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityChatViewsBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        /* For hinding and showing toolbar */
        bind.chatToolbar.animate().translationY(-bind.chatToolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        bind.chatToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();

        bind.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        bind.tabLayout.setupWithViewPager(bind.viewPager);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        bind.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatViews.this , Profile.class));
            }
        });

    }
}