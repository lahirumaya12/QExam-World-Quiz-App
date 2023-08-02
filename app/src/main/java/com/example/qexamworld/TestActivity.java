package com.example.qexamworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView testView;
    private Toolbar toolbar;
    private TestAdapter adapter;
    private Dialog  progressDialog;
    private TextView dialogText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle(DbQuery.catelist.get(DbQuery.g_selected).getName());
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testView = findViewById(R.id.test_view);

        progressDialog = new Dialog(TestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText =  progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading.....");

        progressDialog.show();

        LinearLayoutManager layoutManager =  new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);

        DbQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                DbQuery.loadMyScore(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        adapter = new TestAdapter(DbQuery.testList);
                        testView.setAdapter(adapter);

                        progressDialog.dismiss();

                    }

                    @Override
                    public void onFailure() {

                        progressDialog.dismiss();
                        Toast.makeText(TestActivity.this, "Something went worng ! Please Try Again", Toast.LENGTH_SHORT).show();


                    }
                });

            }

            @Override
            public void onFailure() {

                progressDialog.dismiss();
                Toast.makeText(TestActivity.this, "Something went worng ! Please Try Again", Toast.LENGTH_SHORT).show();


            }
        });



    }

//    private  void loadTestData(){
//        testList = new ArrayList<>();
//
//        testList.add(new TestModel("1",50,20));
//        testList.add(new TestModel("2",80,20));
//        testList.add(new TestModel("3",0,25));
//        testList.add(new TestModel("4",10,40));
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            TestActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}