package com.example.shelter.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shelter.Classes.Dog;
import com.example.shelter.R;
import com.example.shelter.Classes.User;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class DogProfile extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    TextView tvDogName , tvBreed , tvGender , tvAge , tvDogDesc ;
    Button DisplayNum;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    User u;
    de.hdodenhof.circleimageview.CircleImageView DogPic;
    ImageView DogImg;
    Dog d;
    String uri;
    String PhoneNum = "";

    com.google.android.material.floatingactionbutton.FloatingActionButton ivDeleteButton, ivEditButton;

    GoogleApiClient googleApiClient;

    //sitekey -->

    String SiteKey = "6LenNdUZAAAAAPlcCyD_b-t7DKMeuFR3wPKBnuFL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_profile);
        tvDogName = findViewById(R.id.tvDogName);
        tvBreed = findViewById(R.id.tvBreed);
        tvGender = findViewById(R.id.tvGender);
        tvAge = findViewById(R.id.tvAge);
        tvDogDesc = findViewById(R.id.tvDogDesc);
        DisplayNum = findViewById(R.id.DisplayNum);
        DogImg = findViewById(R.id.DogImg);
        DogPic = findViewById(R.id.profile_image);
        ivEditButton = findViewById(R.id.ivEditButton);
        ivDeleteButton = findViewById(R.id.ivDeleteButton);
        mAuth = FirebaseAuth.getInstance();
        d = (Dog) getIntent().getExtras().get("DogInfo");
        tvDogName.setText(d.getName());
        tvBreed.setText(d.getBreed());
        tvGender.setText(d.getGender());
        tvAge.setText(d.getAge());
        tvDogDesc.setText(d.getDescription());
        uri = d.getImageUri();
        if (uri != null && !uri.isEmpty()) {
            Picasso.get().load(uri).into(DogPic);
        }


        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u = documentSnapshot.toObject(User.class);
                PhoneNum = u.getMobile();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DogProfile.this, "Failed To retrieve the user! ", Toast.LENGTH_LONG).show();
            }
        });


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(DogProfile.this)
                .build();
        googleApiClient.connect();

        if(mAuth.getCurrentUser().getUid().equals(d.getuId())) {
            ivDeleteButton.setVisibility(View.VISIBLE);
            ivEditButton.setVisibility(View.VISIBLE);
            ivEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(DogProfile.this, EditDogProfile.class);
                    intent1.putExtra("DogInfo" , d);
                    startActivity(intent1);
                }
            });
            ivDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("Dogs").document(d.getdId()).delete();
                    Intent intent1 = new Intent(DogProfile.this,HomePage.class);
                    startActivity(intent1);
                    finish();
                }
            });
        }
        DisplayNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onClick will display the owner's number if recapacha is successful
                SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,SiteKey).setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                    @Override
                    public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                        Status status = recaptchaTokenResult.getStatus();
                        if((status != null) && status.isSuccess())
                        {
                            //Success
                            DisplayNum.setText(PhoneNum);
                            DisplayNum.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialContactPhone(PhoneNum);
                                }
                            });
                        }

                    }
                });
            }
        });
    }


    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}