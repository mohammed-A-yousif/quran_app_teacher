package com.example.quranappteacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Control extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controler);
        CardView students_cardView = findViewById(R.id.students_cardView);
        CardView missions_cardView = findViewById(R.id.stud_missions_cardView);


        students_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Students.class);
                startActivity(i);
            }
        });

        missions_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Missions.class);
                startActivity(i);
            }
        });
    }
}
