package com.example.tripaya.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.tripaya.Alert.AlertActivity;
import com.example.tripaya.MainActivity;
import com.example.tripaya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Fragment {

    EditText email, password;
    Button logBtn, goDialog;
    TextView forgotPw, signUp,welcomeMsg;


    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        goDialog = view.findViewById(R.id.goToDialog);
        initComponent();


        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.login_to_register);
            }
        });

        forgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.login_to_forgotPassword);
            }
        });

        goDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AlertActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    private void initComponent() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(getContext());
        email = view.findViewById(R.id.emailResetId);
        password = view.findViewById(R.id.passwordId);
        logBtn = view.findViewById(R.id.resetPwId);
        forgotPw = view.findViewById(R.id.forgotPassId);
        signUp = view.findViewById(R.id.tvSignUpId);
        welcomeMsg = view.findViewById(R.id.welcomeId);
    }

    private void performLogin(){

        String emailx = email.getText().toString();
        String passwordx = password.getText().toString();

        if (!emailx.matches(emailPattern)){
            email.setError("Enter Correct Email");
        }
        else if(passwordx.isEmpty() || passwordx.length()<6){
            password.setError("Password has to be 6 or more characters");
        }
        else {
            progressDialog.setMessage("Logging in...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(emailx,passwordx).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}