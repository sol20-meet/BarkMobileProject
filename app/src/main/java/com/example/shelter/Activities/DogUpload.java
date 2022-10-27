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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelter.Classes.Dog;
import com.example.shelter.Classes.User;
import com.example.shelter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class DogUpload extends AppCompatActivity {

    EditText etDogName,etDogDesc;
    Spinner spDogBreed, spDogAge;
    RadioGroup rgGender;
    String gender;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button bt;
    private static final int CAMERA = 2000;
    private static final int GALLERY = 1000;
    private Uri file;
    byte[] byteArray;
    ImageView ivAddPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_upload);
        etDogName = findViewById(R.id.etDogName);
        etDogDesc = findViewById(R.id.etDogDesc);
        spDogBreed = findViewById(R.id.spDogBreed);
        rgGender = findViewById(R.id.rgGender);
        ivAddPic = findViewById(R.id.ivAddPic);
        spDogAge = findViewById(R.id.spDogAge);
        mAuth = FirebaseAuth.getInstance();
        bt = findViewById(R.id.btnSubmit);

        ivAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DogUpload.this);
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

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DogUpload.this, "Uploading Dog...", Toast.LENGTH_SHORT).show();
                if(validateInputs())
                {
                    if (rgGender.getCheckedRadioButtonId()==R.id.rdFemale)
                        gender="Female";
                    else gender="Male";


                    //random creates this auto generated id which is passed in the Dog Object + used as the document's name.
                    final String DogId = db.collection("Dogs").document().getId();
                    //HERE IS THE GENERATED DOG ID


                    mStorageRef = FirebaseStorage.getInstance().getReference().child("DogPics/" + DogId);
                    if(requestCode == CAMERA)
                    {
                        mStorageRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Dog d = new Dog(mAuth.getCurrentUser().getUid(),etDogName.getText().toString(),spDogAge.getSelectedItem().toString(),spDogBreed.getSelectedItem().toString(),gender,etDogDesc.getText().toString() ,DogId, uri.toString());
                                        db.collection("Dogs").document(DogId).set(d).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(DogUpload.this, "Your Dog was Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                Intent intent1 = new Intent(DogUpload.this,HomePage.class);
                                                startActivity(intent1);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(DogUpload.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DogUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if(requestCode == GALLERY)
                    {
                        mStorageRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Dog d = new Dog(mAuth.getCurrentUser().getUid(),etDogName.getText().toString(),spDogAge.getSelectedItem().toString(),spDogBreed.getSelectedItem().toString(),gender,etDogDesc.getText().toString() ,DogId, uri.toString());
                                        db.collection("Dogs").document(DogId).set(d).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(DogUpload.this, "Your Dog was Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                Intent intent1 = new Intent(DogUpload.this,HomePage.class);
                                                startActivity(intent1);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(DogUpload.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DogUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if(requestCode == 0)
                    {
                        Dog d = new Dog(mAuth.getCurrentUser().getUid(),etDogName.getText().toString(),spDogAge.getSelectedItem().toString(),spDogBreed.getSelectedItem().toString(),gender,etDogDesc.getText().toString() ,DogId, "");
                        db.collection("Dogs").document(DogId).set(d).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DogUpload.this, "Your Dog was Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(DogUpload.this,HomePage.class);
                                startActivity(intent1);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DogUpload.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(DogUpload.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validateInputs() //Validated the given inputs and returns true if ok.
    {
        String Name = etDogName.getText().toString();
        String Desc = etDogDesc.getText().toString();
        String Age = spDogAge.getSelectedItem().toString();
        String Breed = spDogBreed.getSelectedItem().toString();
        if (Name.length() <= 0)
        {
            etDogName.setError("This is not a valid Name");
            return false;
        }
        if (etDogDesc.length() < 10)
        {
            etDogDesc.setError("Please Write a more detailed Description");
            return false;
        }
        if(Age.equals("Age"))
        {
            TextView errorTextview = (TextView) spDogAge.getSelectedView();
            errorTextview.setError("This Is an invalid Option, Try Selecting something else");
            return false;
        }
        if(Breed.equals("Breed"))
        {
            TextView errorTextview = (TextView) spDogBreed.getSelectedView();
            errorTextview.setError("This Is an invalid Option, Try Selecting something else");
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