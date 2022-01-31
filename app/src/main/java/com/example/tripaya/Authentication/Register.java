package com.example.tripaya.Authentication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.tripaya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;


public class Register extends Fragment {
    TextView alreadyHaveAcc;
    EditText username,pass,conPass,inputEmail;
    Button register;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container,false);
        initComponent();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuth();
            }
        });

        alreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.register_to_login);
            }
        });

        return view;
    }

    private void initComponent(){
        alreadyHaveAcc = view.findViewById(R.id.alreadyHaveId);
        username = view.findViewById(R.id.usernameId);
        pass = view.findViewById(R.id.passId);
        conPass = view.findViewById(R.id.conPassId);
        inputEmail = view.findViewById(R.id.regEmailId);
        register = view.findViewById(R.id.regBtnId);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(getContext());
    }

    private void performAuth(){
        String email = inputEmail.getText().toString();
        String password = pass.getText().toString();
        String confirmPass = conPass.getText().toString();

        if (!email.matches(emailPattern)){
            inputEmail.setError("Enter Correct Email");
        }
        else if(password.isEmpty() || password.length()<6){
            pass.setError("Password has to be 6 or more characters");
        }
        else if(!password.equals(confirmPass)){
            conPass.setError("Passwords doesn't match");
        }
        else{
            progressDialog.setMessage("Registering...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        String userId = user.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("userId",userId);
                        hashMap.put("username",username.getText().toString());
                        hashMap.put("email",email);
                        hashMap.put("imageURL","default");
                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getView()).navigate(R.id.register_to_login);
                            }
                        });

                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), ""+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }





}