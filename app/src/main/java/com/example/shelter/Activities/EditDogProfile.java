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

import com.example.shelter.Classes.Dog;
import com.example.shelter.Classes.User;
import com.example.shelter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class EditDogProfile extends AppCompatActivity {

    EditText etDogName, etDogAge , etDogDesc;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private Uri file;
    private static final int CAMERA = 2000;
    private static final int GALLERY = 1000;
    byte[] byteArray;
    Dog d;
    ImageView ivAddPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dog_profile);
        etDogName = findViewById(R.id.etDogName);
        etDogAge = findViewById(R.id.etDogAge);
        etDogDesc = findViewById(R.id.etDogDesc);
        mAuth = FirebaseAuth.getInstance();
        ivAddPic = findViewById(R.id.ivAddPic);
        d = (Dog) getIntent().getExtras().get("DogInfo");
        etDogName.setText(d.getName());
        etDogAge.setText(d.getAge());
        etDogDesc.setText(d.getDescription());


        ivAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EditDogProfile.this);
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
        final String Breed = d.getBreed();
        final String Gender = d.getGender();
        final String Uri = d.getImageUri();
        if(validateInfo()) {
            mStorageRef = FirebaseStorage.getInstance().getReference().child("DogPics/" + d.getdId());
            if(requestCode == CAMERA) {
                mStorageRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Dog dd = new Dog(mAuth.getCurrentUser().getUid(),etDogName.getText().toString(),etDogAge.getText().toString(),Breed, Gender, etDogDesc.getText().toString() , d.getdId() , uri.toString());
                                db.collection("Dogs").document(d.getdId()).set(dd).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EditDogProfile.this, "Your Dog was Successfully Updated", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(EditDogProfile.this, HomePage.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditDogProfile.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditDogProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Dog dd = new Dog(mAuth.getCurrentUser().getUid(), etDogName.getText().toString(), etDogAge.getText().toString(), Breed, Gender , etDogDesc.getText().toString() , d.getdId() , uri.toString());
                                db.collection("Dogs").document(d.getdId()).set(dd).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EditDogProfile.this, "Your Dog was Successfully Updated", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(EditDogProfile.this,HomePage.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditDogProfile.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditDogProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if(requestCode == 0)
            {
                Dog dd = new Dog(mAuth.getCurrentUser().getUid(), etDogName.getText().toString(), etDogAge.getText().toString(), Breed, Gender , etDogDesc.getText().toString() , d.getdId() , Uri);
                db.collection("Dogs").document(d.getdId()).set(dd).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditDogProfile.this, "Your Dog was successfully Updated!", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(EditDogProfile.this,HomePage.class);
                        startActivity(intent1);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditDogProfile.this, "Something went wrong, Check Your connection and try again later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public boolean validateInfo() //Validated the given inputs and returns true if ok.
    {
        String Name = etDogName.getText().toString();
        String DogDesc = etDogDesc.getText().toString();
        if (Name.length()<3)
        {
            etDogName.setError("This is not a valid Name");
            return false;
        }
        if(DogDesc.length() < 10)
        {
            etDogDesc.setError("This Desc is not valid");
            return false;
        }
        return  true;
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