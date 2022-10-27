package com.example.shelter.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.shelter.R;
import com.example.shelter.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private static final int CAMERA = 2000;
    private static final int GALLERY = 1000;
    com.google.android.material.textfield.TextInputLayout etFName, etEmail, etPhone, etPass, etCPass;
    RadioGroup rgGender;
    Button btnSubmit;
    ImageView ivAddPic;
    String gender;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private Uri file;
    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etFName = findViewById(R.id.etFName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPass = findViewById(R.id.etPass);
        ivAddPic = findViewById(R.id.ivAddPic);
        etCPass = findViewById(R.id.etCPass);
        rgGender = findViewById(R.id.rgGender);
        mAuth = FirebaseAuth.getInstance();

        ivAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
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



    String Bio = null;
    
    public void SignUp(View v)
    {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
        if (validateInputs())
        {
            if (rgGender.getCheckedRadioButtonId()==R.id.rdFemale)
                gender="Female";
            else gender="Male";
            mAuth.createUserWithEmailAndPassword(etEmail.getEditText().getText().toString(),etPass.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mStorageRef = FirebaseStorage.getInstance().getReference().child("ProfilePics/" + mAuth.getCurrentUser().getUid());
                        if(requestCode == CAMERA) {
                            mStorageRef.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            User u = new User(mAuth.getCurrentUser().getUid(), etFName.getEditText().getText().toString(), etEmail.getEditText().getText().toString(), etPhone.getEditText().getText().toString(), gender ,Bio, uri.toString());
                                            db.collection("users").document(mAuth.getCurrentUser().getUid()).set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MainActivity.this, "Your account was successfully added!", Toast.LENGTH_SHORT).show();
                                                    mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                                                            startActivity(intent1);
                                                            finish();
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MainActivity.this, "Something went wrong, Check Your connection and try again later!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            User u = new User(mAuth.getCurrentUser().getUid(), etFName.getEditText().getText().toString(), etEmail.getEditText().getText().toString(), etPhone.getEditText().getText().toString(), gender, Bio, uri.toString() );
                                            db.collection("users").document(mAuth.getCurrentUser().getUid()).set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MainActivity.this, "Your account was successfully added!", Toast.LENGTH_SHORT).show();
                                                    mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                                                            startActivity(intent1);
                                                            finish();
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MainActivity.this, "Something went wrong, Check Your connection and try again later!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else if(requestCode == 0)
                        {
                            User u = new User(mAuth.getCurrentUser().getUid(), etFName.getEditText().getText().toString(), etEmail.getEditText().getText().toString(), etPhone.getEditText().getText().toString(),Bio, gender , "");
                            db.collection("users").document(mAuth.getCurrentUser().getUid()).set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Your account was successfully added!", Toast.LENGTH_SHORT).show();
                                    mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(intent1);
                                            finish();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Something went wrong, Check Your connection and try again later!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                    else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
        else Toast.makeText(MainActivity.this, "Data cannot be added, check your input", Toast.LENGTH_SHORT).show();
    }

    String pass, fname;


    public boolean validateInputs() //Validated the given inputs and returns true if ok.
    {
        String email = etEmail.getEditText().getText().toString();
        fname = etFName.getEditText().getText().toString();
        pass = etPass.getEditText().getText().toString();
        String cpass = etCPass.getEditText().getText().toString();
        if (fname.length()<3)
        {
            etFName.setError("This is not a valid Name");
            return false;
        }
        if (!pass.equals(cpass)||pass.length()<4)
        {
            etPass.setError("password and confirm password must be the same");
            return false;
        }
        if(!isEmailValid(email))
        {
            etEmail.setError("The Email that you entered is incorrect!");
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
        String str = etPhone.getEditText().getText().toString();
        if (str.length()!=10||str.charAt(0)!='0'||str.charAt(1)!='5')
            return false;
        for (int i=2;i<str.length();i++)
            if (str.charAt(i)<'0'||str.charAt(i)>'9') {
                etPhone.setError("mobile number is wrong");
                return false;
            }
        return true;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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