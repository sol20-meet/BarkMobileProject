package com.example.shelter.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelter.Activities.EditProfile;
import com.example.shelter.Activities.Settings;
import com.example.shelter.Classes.Dog;
import com.example.shelter.Classes.User;
import com.example.shelter.R;
import com.example.shelter.Adapter.DogAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FragmentAccount extends Fragment {

    User u;
    TextView tvFullName , tvBio;
    TextView tvPhone;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    Button bt1;
    com.google.android.material.floatingactionbutton.FloatingActionButton btSettings;
    ImageView imgSettings;
    de.hdodenhof.circleimageview.CircleImageView ivProfilePic;
    RecyclerView rvDogs;
    List<Dog> DogList;
    Spinner spBreeds;
    DogAdapter usAdapter;
    Button bt;
    Button bt2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvBio = view.findViewById(R.id.tvBio);
        mAuth = FirebaseAuth.getInstance();
        bt1 = view.findViewById(R.id.btEdit);
        imgSettings = view.findViewById(R.id.imgSettings);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        btSettings = view.findViewById(R.id.btSettings);
        //RV
        DogList = new ArrayList<>();
        spBreeds = view.findViewById(R.id.spBreeds);
        bt = view.findViewById(R.id.btUpload);
        bt2 = view.findViewById(R.id.btnSearch);
        rvDogs = view.findViewById(R.id.rvDogs);
        rvDogs.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDogs.setHasFixedSize(true);





        db.collection("users")
                .document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u = documentSnapshot.toObject(User.class);
                tvFullName.setText(u.getfName());
                tvBio.setText(u.getBio());
                Picasso.get().load(u.getUri()).into(ivProfilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed To retrieve the user! ", Toast.LENGTH_LONG).show();
            }
        });


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), EditProfile.class);
                startActivity(intent1);
            }
        });

        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getActivity(), Settings.class);
                startActivity(intent1);
            }
        });


        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), Settings.class);
                startActivity(intent1);
            }
        });

        db.collection("Dogs")
                .whereEqualTo("uId", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            String res="";
                            for(QueryDocumentSnapshot t:task.getResult())
                            {
                                DogList.add(t.toObject(Dog.class));
                                res += t.toObject(Dog.class).toString();
                            }
                            usAdapter = new DogAdapter(getActivity(), DogList);
                            rvDogs.setAdapter(usAdapter);
                        }
                    }
                });


    }
}