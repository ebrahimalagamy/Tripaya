package com.example.tripaya.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripaya.R;
import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.viewmodel.LoginActivity;
import com.example.tripaya.viewmodel.SignOutViewModel;
import com.example.tripaya.viewmodel.TripViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String SHARED_PREFERENCES_NAME = "myPref";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHOTO = "photo";
    static String Tag = "gg";
    TextView userName_field, gmail_field;
    CircleImageView profile_image;
    TextView tv_trips, tv_completed, tv_cancled;
    String profileImageUrl, UserName, UserEmail;
    SharedPreferences sharedPreferences;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private FirebaseStorage storage;
    private Uri profileUri;
    private StorageReference storageReference;
    private TripViewModel tripViewModel;
    private SignOutViewModel signoutModel;

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
        tripViewModel = new ViewModelProvider(getActivity()).get(TripViewModel.class);
        signoutModel = new ViewModelProvider(getActivity()).get(SignOutViewModel.class);

        userName_field = view.findViewById(R.id.userName_field);
        gmail_field = view.findViewById(R.id.gmail_field);
        profile_image = view.findViewById(R.id.profile_image);
        tv_trips = view.findViewById(R.id.trips);
        tv_cancled = view.findViewById(R.id.canceled);
        tv_completed = view.findViewById(R.id.completed);
        setData();
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        //firebaseRef
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //imageUpload
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

    }

    private void setData() {
        tripViewModel.getAllzTrips().observe(getActivity(), tripClasses3 -> {

            tv_trips.setText(String.valueOf(tripClasses3.size()));
            Log.e("TAG", "onOptionsItemSelected: " + tripClasses3.size());

        });
        tripViewModel.getCompletedTrips().observe(getActivity(), tripClasses2 ->
        {
            tv_completed.setText(String.valueOf(tripClasses2.size()));
        });
        tripViewModel.getHistoryTrips().observe(getActivity(), tripClasses1 -> {
            tv_cancled.setText(String.valueOf(tripClasses1.size()));
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 123);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Tag, "onActivityResult: ");
        if (data.getData() != null) {
            profileUri = data.getData();
            profile_image.setImageURI(profileUri);
            Log.i(Tag, "onActivityResult: ");
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading Image...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);
        riversRef.putFile(profileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Failed To Upload", Toast.LENGTH_SHORT).show();
                }).addOnProgressListener(snapshot -> {
            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
            pd.setMessage("Progress : " + (int) progressPercent + "%");
        });
    }

    @Override
    public void onStart() {
        getActivity().setTitle("Profile");
        super.onStart();

        if (mUser == null) {
            SendUserToLoginActivity();
        } else {
            mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        UserName = snapshot.child("username").getValue().toString();
                        UserEmail = snapshot.child("email").getValue().toString();
                        profileImageUrl = snapshot.child("imageURL").getValue().toString();
                        editor.putString(KEY_NAME, UserName);
                        editor.putString(KEY_EMAIL, UserEmail);
                        //editor.putString(KEY_PHOTO,profileImageUrl);
                        editor.apply();
                        String name = sharedPreferences.getString(KEY_NAME, null);
                        String email = sharedPreferences.getString(KEY_EMAIL, null);
                        String photo = sharedPreferences.getString(KEY_PHOTO, null);
                        if (name != null || email != null || photo != null) {
                            Picasso.get().load(profileUri).into(profile_image);
                            userName_field.setText(UserName);
                            gmail_field.setText(UserEmail);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater inflater1 = getActivity().getMenuInflater();
        inflater1.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            mFirebaseAuth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            Activity activity = this.getActivity();
            Toast.makeText(activity, "Logged out", Toast.LENGTH_SHORT).show();
            tripViewModel.addIsSignedOut(true);
            tripViewModel.getAllTrips().observe(getActivity(), new Observer<List<TripClass>>() {
                @Override
                public void onChanged(List<TripClass> tripClasses) {
                    tripViewModel.getAllTrips().removeObserver(this);
                    signoutModel.deleteAllTrips();

                }
            });

            activity.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

