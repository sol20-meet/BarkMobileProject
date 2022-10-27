package com.example.shelter.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shelter.Activities.DogUpload;
import com.example.shelter.Classes.Dog;
import com.example.shelter.R;
import com.example.shelter.Adapter.DogAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FragmentAdopt extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView rvDogs;
    List<Dog> DogList;
    TextView tvForm;
    Spinner spBreeds;
    DogAdapter usAdapter;
    Button btnMore;
    TextView tvResult;
    com.google.android.material.floatingactionbutton.FloatingActionButton bt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adopt, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DogList = new ArrayList<>();
        spBreeds = view.findViewById(R.id.spBreeds);
        bt = view.findViewById(R.id.btUpload);
        rvDogs = view.findViewById(R.id.rvDogs);
        btnMore = view.findViewById(R.id.btnMore);
        tvForm = view.findViewById(R.id.tvForm);
        tvResult = view.findViewById(R.id.tvResult);
        rvDogs.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDogs.setHasFixedSize(true);


        tvForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSem1N6gH27t9DpUe4yuroRDrlRPCttGLiLKYmcbixVfDp5SUQ/viewform"));
                startActivity(browserIntent);
            }
        });


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), DogUpload.class);
                startActivity(intent1);
            }
        });

        spBreeds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ClearAll();

                if(spBreeds.getSelectedItem().toString().equals("All Breeds"))
                {
                    final int[] num = {0};
                    db.collection("Dogs")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful())
                                    {
                                        String res="";
                                        for(QueryDocumentSnapshot t:task.getResult())
                                        {
                                            Dog d = t.toObject(Dog.class);
                                            d.setdId(t.getId());
                                            DogList.add(d);
                                            res += t.toObject(Dog.class).toString();
                                            num[0]++;
                                        }
                                        usAdapter = new DogAdapter(getActivity(), DogList);
                                        rvDogs.setAdapter(usAdapter);
                                        tvResult.setText(num[0] + " Results Were Found!");
                                    }
                                }
                            });
                }
                else {
                    final int[] num = {0};
                    db.collection("Dogs")
                            .whereEqualTo("breed", spBreeds.getSelectedItem().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String res = "";
                                        for (QueryDocumentSnapshot t : task.getResult()) {
                                            DogList.add(t.toObject(Dog.class));
                                            res += t.toObject(Dog.class).toString();
                                            num[0]++;
                                        }
                                        usAdapter = new DogAdapter(getActivity(), DogList);
                                        rvDogs.setAdapter(usAdapter);
                                        tvResult.setText(num[0] + " Result/s Were Found!");
                                    }
                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void ClearAll(){
        if(DogList != null){
            DogList.clear();
            if(usAdapter != null)
            {
                usAdapter.notifyDataSetChanged();
            }
        }
        DogList = new ArrayList<>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}