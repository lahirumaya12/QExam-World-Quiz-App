package com.example.qexamworld;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        DbQuery.g_firestore = FirebaseFirestore.getInstance();

        new Thread(){
            @Override
            public void  run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                if (mAuth.getCurrentUser() != null){

                    DbQuery.loadData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {

                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();

                        }

                        @Override
                        public void onFailure() {

                            Toast.makeText(SplashActivity.this, "Something went worng ! Please Try Again", Toast.LENGTH_SHORT).show();

                        }
                    });


                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        }.start();
    }
}