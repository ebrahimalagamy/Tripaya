package com.example.tripaya.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.tripaya.R;

import com.example.tripaya.viewmodel.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.google.firebase.FirebaseApp;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements  ActivityCompat.OnRequestPermissionsResultCallback{
    TextView userName_field,gmail_field;
    CircleImageView profile_image;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    String profileImageUrl,UserName,UserEmail;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        userName_field=view.findViewById(R.id.userName_field);
        gmail_field=view.findViewById(R.id.gmail_field);
        profile_image=view.findViewById(R.id.profile_image);
        //firebaseRef
        mFirebaseAuth=FirebaseAuth.getInstance();
        mUser=mFirebaseAuth.getCurrentUser();
        mRef= FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void onStart() {
        getActivity().setTitle("Profile");
        super.onStart();

        if(mUser==null)
        {
            SendUserToLoginActivity();
        }
        else {

            mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        profileImageUrl=snapshot.child("imageURL").getValue().toString();
                        UserName=snapshot.child("username").getValue().toString();
                        UserEmail=snapshot.child("email").getValue().toString();
                        Picasso.get().load(profileImageUrl).into(profile_image);
                        userName_field.setText(UserName);
                        gmail_field.setText(UserEmail);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    private  void SendUserToLoginActivity(){
        Intent intent =new Intent(getContext(),LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater inflater1= getActivity().getMenuInflater();
        inflater1.inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout ) {
                  mFirebaseAuth.signOut();
                  Intent intent = new Intent(getContext(), LoginActivity.class);
                  startActivity(intent);
            Activity activity=this.getActivity();
            Toast.makeText(activity, "Logged out", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

