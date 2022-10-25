package com.prathmeshadsod.realmessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.prathmeshadsod.realmessenger.databinding.ActivityPhoneLoginBinding;

public class PhoneLogin extends AppCompatActivity {

    ActivityPhoneLoginBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityPhoneLoginBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bind.phoneVerify.length() < 10)
                    bind.btn1.setText("Invalid Phone Number");
                else {
                    Intent intent = new Intent(PhoneLogin.this, OtpVerification.class);
                    intent.putExtra("phone_number", bind.phoneVerify.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }
}