package com.example.shelter.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shelter.R;
import com.example.shelter.Classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class EditProfile extends AppCompatActivity {

    EditText etName,etPhone,etBio;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    User u;
    String name, phone,bio;
    private static final int CAMERA = 2000;
    private static final int GALLERY = 1000;
    byte[] byteArray;
    ImageView ivAddPic;
    private StorageReference mStorageRef;
    private Uri file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        etName = findViewById(R.id.etFName);
        etPhone = findViewById(R.id.etPhone);
        etBio = findViewById(R.id.etBio);
        ivAddPic = findViewById(R.id.ivAddPic);
        mAuth = FirebaseAuth.getInstance();

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u = documentSnapshot.toObject(User.class);
                name = u.getfName();
                phone = u.getMobile();
                bio = u.getBio();
                etName.setText(name);
                etPhone.setText(phone);
                etBio.setText(bio);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Failed To retrieve the user! ", Toast.LENGTH_LONG).show();
            }
        });

        ivAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EditProfile.this);
                builder1.setMessage("Write your message here.");
                builder1.setTitle("Settings");
                builder1.setIcon(R.drawable.bark__1_);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                OpenCam();
                            }
                        });


                builder1.setNegativeButton(
                        "Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ChoosePic();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }


    public void UpdateInfo(View v)
    {
        final String Email = u.getEmail();
        final String Gender = u.getGender();
        final String Uri = u.getUri();
        if(validateInfo()) {
            mStorageRef = FirebaseStorage.getInstance().getReference().child("ProfilePics/" + mAuth.getCurrentUser().getUid());
            if(requestCode == CAMERA) {
                mStorageRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                User us = new User(mAuth.getCurrentUser().getUid(), etName.getText().toString(), Email, etPhone.getText().toString(), Gender , etBio.getText().toString() ,uri.toString());
                                db.collection("users").document(mAuth.getCurrentUser().getUid()).set(us).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EditProfile.this, "Your account was successfully Updated!", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(EditProfile.this,HomePage.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfile.this, "Something went wrong, Check Your connection and try again later!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (requestCode == GALLERY)
            {
                mStorageRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                User us = new User(mAuth.getCurrentUser().getUid(), etName.getText().toString(), Email, etPhone.getText().toString(), Gender ,etBio.getText().toString(), uri.toString());
                                db.collection("users").document(mAuth.getCurrentUser().getUid()).set(us).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EditProfile.this, "Your account was successfully Updated!", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(EditProfile.this,HomePage.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfile.this, "Something went wrong, Check Your connection and try again later!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if(requestCode == 0)
            {
                User us = new User(mAuth.getCurrentUser().getUid(), etName.getText().toString(), Email, etPhone.getText().toString(), Gender , etBio.getText().toString(), Uri);
                db.collection("users").document(mAuth.getCurrentUser().getUid()).set(us).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfile.this, "Your account was successfully Updated!", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(EditProfile.this,HomePage.class);
                        startActivity(intent1);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Something went wrong, Check Your connection and try again later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    public boolean validateInfo() //Validated the given inputs and returns true if ok.
    {
        String name = etName.getText().toString();
        if (name.length()<3)
        {
            etName.setError("This is not a valid Name");
            return false;
        }
        if (!isMobile()) //checks the user's phone if it's valid
        {
            etPhone.setError("Your Phone number number must be 10 digits and starts with 05");
            return  false;
        }
        return  true;
    }
    //for to check if the number is right
    public  boolean isMobile()
    {
        String str = etPhone.getText().toString();
        if (str.length()!=10||str.charAt(0)!='0'||str.charAt(1)!='5')
            return false;
        for (int i=2;i<str.length();i++)
            if (str.charAt(i)<'0'||str.charAt(i)>'9') {
                etPhone.setError("mobile number is wrong");
                return false;
            }
        return true;
    }


    public void OpenCam()
    {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture , CAMERA);
    }

    public void ChoosePic()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY);
    }

    int requestCode = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA && resultCode == RESULT_OK)
        {
            this.requestCode = CAMERA;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // a new ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // compressing the photo into a Jpeg file and sending it to the byteArrayOutputStream
            photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            // makes a byte array to store the compressed image in
            byteArray = byteArrayOutputStream.toByteArray();
            // setting up the progress dialog for the uploading task
            // sends the photo to the Firebase Storage under the User's Uid
//          Picaso.get().load(URI - HERE).into(IMAGE VIEW VAR); Show Images In application
            ivAddPic.setImageBitmap(photo);
        }
        if(requestCode == GALLERY && resultCode == RESULT_OK)
        {
            this.requestCode = GALLERY;
            file = data.getData();
            Picasso.get().load(file).into(ivAddPic);
        }

    }
}