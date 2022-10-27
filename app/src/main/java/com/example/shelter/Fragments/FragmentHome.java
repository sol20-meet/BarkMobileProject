package com.example.shelter.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelter.Activities.DogUpload;
import com.example.shelter.Activities.VideoPage;
import com.example.shelter.Classes.User;
import com.example.shelter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class FragmentHome extends Fragment {

    TextView tvWelcomeName , tvWelcome;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    User u;
    Button btWebiste;
    com.google.android.material.floatingactionbutton.FloatingActionButton btEgg;
    int Num = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvWelcomeName = view.findViewById(R.id.tvWelcomeName);
        btWebiste = view.findViewById(R.id.btWebiste);
        btEgg = view.findViewById(R.id.btEgg);
        mAuth = FirebaseAuth.getInstance();
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u = documentSnapshot.toObject(User.class);
                String name = u.getfName();
                Integer num = name.indexOf(" ");
//                String finalName = name.substring(0,num);
//                tvWelcomeName.setText(finalName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed To retrieve the user! ", Toast.LENGTH_LONG).show();
            }
        });

        btWebiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bark-meet-v2.herokuapp.com/"));
                startActivity(browserIntent);
            }
        });


        btEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Num == 5)
                {
                    Intent intent1 = new Intent(getActivity(), VideoPage.class);
                    startActivity(intent1);
                }
                else {
                    Num++;
                }
            }
        });
    }


}