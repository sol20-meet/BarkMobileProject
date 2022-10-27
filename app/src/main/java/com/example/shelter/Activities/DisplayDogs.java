package com.example.shelter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.example.shelter.Classes.Dog;
import com.example.shelter.R;
import com.example.shelter.Classes.User;
import com.example.shelter.Adapter.DogAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DisplayDogs extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView rvUsers;
    List<Dog> DogList;
    Spinner spBreeds;
    DogAdapter usAdapter;
    Button bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_dogs);
        DogList = new ArrayList<>();
        spBreeds = findViewById(R.id.spBreeds);

    }


    public void search(View v)
    {
        if(spBreeds.getSelectedItem().toString() == "Breed")
        {

        }


        db.collection("Dogs")
                .whereEqualTo("breed",spBreeds.getSelectedItem().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            String res="";
                            for(QueryDocumentSnapshot user:task.getResult())
                            {
                                DogList.add(user.toObject(Dog.class));
                                res += user.toObject(User.class).toString();
                            }
                            usAdapter = new DogAdapter(DisplayDogs.this, DogList);
                            rvUsers.setAdapter(usAdapter);
                        }
                    }
                });
    }

    public void UploadDog(View v)
    {
        Intent intent1 = new Intent(DisplayDogs.this,DogUpload.class);
        startActivity(intent1);
        finish();
    }

}