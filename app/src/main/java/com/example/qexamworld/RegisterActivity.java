package com.example.qexamworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText name,email, pass, repass;
    private Button regB;
    private ImageView backB;
    private FirebaseAuth mAuth;
    private String emailStr, PassStr, rePassStr, nameStr;
    private Dialog progressDialog;
    private TextView dialogText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.username);
        email = findViewById(R.id.emailID);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.repassword);
        regB = findViewById(R.id.registerB);
        backB = findViewById(R.id.backB);


        progressDialog = new Dialog(RegisterActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText =  progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering User...");

        mAuth = FirebaseAuth.getInstance();


        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        regB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    regnewuser();
                }


            }
        });


    }

    private boolean validate(){
        nameStr = name.getText().toString().trim();
        PassStr = pass.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        rePassStr = repass.getText().toString().trim();

        if(nameStr.isEmpty()){
            name.setError("Please Enter Your Name");
            return false;
        }

        if(emailStr.isEmpty()){
            email.setError("Please Enter Your Email");
            return false;
        }

        if(PassStr.isEmpty()){
            pass.setError("Please Enter Your Password");
            return false;
        }

        if(rePassStr.isEmpty()){
            repass.setError("Please Retype Password");
            return false;
        }
        
        if (PassStr.compareTo(rePassStr) != 0){
            Toast.makeText(RegisterActivity.this, "Password and confirm password should be same!", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void regnewuser(){

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, PassStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(RegisterActivity.this, "Register Successfull", Toast.LENGTH_SHORT).show();

                            DbQuery.createUserData(emailStr, nameStr, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {

                                    DbQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            RegisterActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(RegisterActivity.this, "Something went worng ! Please Try Again", Toast.LENGTH_SHORT).show();

                                            progressDialog.dismiss();
                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {

                                    Toast.makeText(RegisterActivity.this, "Something went worng ! Please Try Again", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();

                                }
                            });


                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}