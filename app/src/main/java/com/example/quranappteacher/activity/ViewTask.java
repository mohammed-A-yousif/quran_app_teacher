package com.example.quranappteacher.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quranappteacher.R;
import com.google.android.material.snackbar.Snackbar;

public class ViewTask extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task);

        //      Toolbar
        Toolbar toolbar = findViewById(R.id.view_task_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        //    EditText
        EditText name_editText = findViewById(R.id.name_editText);
        EditText task_editText = findViewById(R.id.task_editText);

//      instantiate SharedPreferences
        SharedPreferences sp = getApplicationContext().getSharedPreferences("StudentTaskPrefs", Context.MODE_PRIVATE);

        String name = sp.getString("name", "");
        String task = sp.getString("task", "");

        name_editText.setHint("الاسم: "+name+" ؟!");
        task_editText.setHint("المهمة: "+task+" ؟!");

        Button editStudentButton = findViewById(R.id.edit_btn);
        editStudentButton.setOnClickListener(v -> {
            Snackbar.make(findViewById(android.R.id.content), " تم التعديل ", Snackbar.LENGTH_SHORT).show();
        });


    }

}

