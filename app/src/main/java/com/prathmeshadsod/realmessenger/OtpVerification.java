package com.prathmeshadsod.realmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.prathmeshadsod.realmessenger.databinding.ActivityOtpVerificationBinding;

import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {

    ActivityOtpVerificationBinding bind;

    FirebaseAuth auth;
    ProgressDialog dialog;

    String verificationId;

    /* In this project I haven't provided SHA1 key first when integrating firbase
       which cost me a lot time waste in authentication using phone number
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.otpBoxes.requestFocus();

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading re-captcha...");
        dialog.setCancelable(false);
        dialog.show();

        String phone_number = getIntent().getStringExtra("phone_number").trim();
        bind.headerText.setText("OTP is sending on +91" + phone_number);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91"+phone_number).setTimeout(120L , TimeUnit.SECONDS).setActivity(OtpVerification.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    /* Will execute if SIM is in same phone where number is typed */
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        dialog.dismiss();
                        bind.headerText.setText("OTP sent on " + phone_number);
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OtpVerification.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }

                    /* Will execute if SIM is not in same phone where number is typed */
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        dialog.dismiss();
                        bind.headerText.setText("OTP sent on " + phone_number);
                        verificationId = s;
                    }

                }).build();

                PhoneAuthProvider.verifyPhoneNumber(options);

                bind.sendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(bind.otpBoxes.getText().toString().isEmpty() || bind.otpBoxes.getText().length() != 6){
                            Toast.makeText(OtpVerification.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, bind.otpBoxes.getText().toString());
                            signInWithPhoneAuthCredential(credential);
                        }
                    }
                });

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(OtpVerification.this , ChatViews.class);
                            startActivity(intent);
                            finish();
                        } else {
                                Toast.makeText(OtpVerification.this, "Invalid Verification Code", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}