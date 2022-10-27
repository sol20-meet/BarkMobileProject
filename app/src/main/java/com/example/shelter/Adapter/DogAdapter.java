package com.example.shelter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shelter.Activities.DogProfile;
import com.example.shelter.Activities.DogUpload;
import com.example.shelter.Activities.HomePage;
import com.example.shelter.Classes.Dog;
import com.example.shelter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.UserHolder>{

    Context context;
    List<Dog>  DogList;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    public DogAdapter(Context context, List<Dog> dogList)
    {
        this.context = context;
        this.DogList = dogList;
    }
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(context).inflate(R.layout.dog_viewholder,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, int position) {
        final Dog d = DogList.get(position);
        holder.tvDogName.setText(d.getName());
        holder.tvBreed.setText(d.getBreed());
        mAuth = FirebaseAuth.getInstance();
        if(d.getImageUri() != null && !d.getImageUri().isEmpty())
            Picasso.get().load(d.getImageUri()).into(holder.DogImg);
//        else {
//                holder.DogImg.setBackgroundResource(R.drawable.ic_dog);
//                holder.DogImg.getLayoutParams().height = 300;
//                holder.DogImg.getLayoutParams().width = 300;
//                holder.DogImg.requestLayout();
//        }
        
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, DogProfile.class);
                intent1.putExtra("DogInfo" , d);
                context.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return DogList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder{
        TextView tvDogName, tvBreed;
        Button  btnMore;
        ImageView DogImg;
        // itemView points to the inflated layout file of dog_viewholder
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            tvDogName = itemView.findViewById(R.id.tvDogName);
            tvBreed = itemView.findViewById(R.id.tvBreed);
            DogImg = itemView.findViewById(R.id.dogImg);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
