package com.prathmeshadsod.realmessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Action bar we have made to NoActionBar in themes.xml */

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        Thread thread = new Thread() {

            public void run() {
                try{
                    sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if(user == null){
                        startActivity(new Intent(MainActivity.this , PhoneLogin.class));
                        finish();
                    }else {
                        startActivity(new Intent(MainActivity.this , ChatViews.class));
                        finish();
                    }

                }
            }

        };
        thread.start();

    }
}