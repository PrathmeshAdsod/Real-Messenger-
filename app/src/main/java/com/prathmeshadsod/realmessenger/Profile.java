package com.prathmeshadsod.realmessenger;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prathmeshadsod.realmessenger.Models.User;

public class Profile extends AppCompatActivity {

    /* View binding is not coming here  that's why I used findViewById ---    */

    ImageView profileImage;
    EditText userName , userUserName , userEmail;
    Button saveBtn , Skip;
    TextView error;

    /**********   URI or Uniform Resource Identifier is a compact sequence of characters that identifies an abstract or physical resource.    ******/
    Uri imageSelectedUri;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userUserName = findViewById(R.id.userUserName);
        userEmail = findViewById(R.id.userEmail);
        saveBtn = findViewById(R.id.saveBtn);
        error = findViewById(R.id.error);
        Skip = findViewById(R.id.Skip);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait while uploading...");
        dialog.setCancelable(false);

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this , ChatViews.class));
            }
        });

        /* Accessing Photos by launching Gallary app through new way  -- https://developer.android.com/training/basics/intents/result */
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userName.getText().toString();
                String username = userUserName.getText().toString();
                String email = userEmail.getText().toString();
                String uid = auth.getUid();
                String phone = auth.getCurrentUser().getPhoneNumber();

                if(name.isEmpty() || username.isEmpty() || email.isEmpty()) {
                    error.setText("Please fill all the input feilds");
                    return;
                }
                dialog.show();
                if(imageSelectedUri != null) {
                    // Make reference to firbase storage  ------              folder will be created in storage as     profile_images / user_uid
                    StorageReference ref = storage.getReference().child("profile_images").child(auth.getUid());
                    // put files at the reference
                    ref.putFile(imageSelectedUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                         String imageUri = uri.toString();
                                         User user = new User(uid , name , phone , imageUri , username , email);
                                         /* Adding this data to Firebase Realtime Database */
                                        database.getReference().child("messenger_users").child(auth.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(Profile.this , ChatViews.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }else {
                    User user = new User(uid , name , phone , "image of user" , username , email);
                    /* Adding this data to Firebase Realtime Database */
                    database.getReference().child("messenger_users").child(auth.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.dismiss();
                            Intent intent = new Intent(Profile.this , ChatViews.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });


    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                     if(uri != null) {
                          profileImage.setImageURI(uri);
                          imageSelectedUri = uri;
                          Log.d(TAG, "Selected Image URI is : "+uri);
                     }
                }
    });

}