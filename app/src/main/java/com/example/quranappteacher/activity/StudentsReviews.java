package com.example.quranappteacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quranappteacher.InternetStatus;
import com.example.quranappteacher.R;
import com.example.quranappteacher.SharedPrefManager;
import com.example.quranappteacher.URLs;
import com.example.quranappteacher.ViewDialog;
import com.example.quranappteacher.adapter.ReviewAdapter;
import com.example.quranappteacher.model.Review;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentsReviews extends AppCompatActivity {

    String DatePicekd;
    ViewDialog viewDialog;

    private JSONArray jsonArray;
    List<Review> listItems ;
    private ReviewAdapter adapter;
    int IdTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students_reviews);

        Toolbar toolbar = findViewById(R.id.review_toolbar);
        setSupportActionBar(toolbar);

        viewDialog = new ViewDialog(this);
        Intent intent = getIntent();

        IdTeacher = SharedPrefManager.getInstance(this).getAdmin().getId();
        RecyclerView recyclerView = findViewById(R.id.review_recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();

        adapter = new ReviewAdapter(listItems, this);
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        DatePicekd = intent.getStringExtra("DatePicekd");

        if (InternetStatus.getInstance(this).isOnline()) {
            GetReview(DatePicekd);
        } else {
            Snackbar.make(findViewById(android.R.id.content), " غير متصل بالانترت حاليا ، الرجاء مراجعةالأنترنت " , Snackbar.LENGTH_LONG)
                    .setAction("محاولة مرة اخري", v ->   GetReview(DatePicekd)).show();
        }

    }

    private void GetReview(String datePicked) {
        viewDialog.showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.GetReviews + IdTeacher + "/?ReviewDate=" + datePicked , response -> {
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i ++){
                    JSONObject ReviewObject = jsonArray.getJSONObject(i);
                    int IdReview = ReviewObject.getInt("IdReview");
                    String Student = ReviewObject.getString("Student");
                    String Teacher = ReviewObject.getString("Teacher");
                    String ReviewDec = ReviewObject.getString("ReviewDec");
                    String NumberOfParts = ReviewObject.getString("NumberOfParts");
                    String CreatedAt = ReviewObject.getString("CreatedAt");
                    Review listItem = new Review(IdReview, Student,Teacher , ReviewDec, NumberOfParts, CreatedAt);
                    listItems.add(listItem);
                }

                adapter.notifyDataSetChanged();
                viewDialog.hideDialog();
                Log.d("res", jsonArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
                viewDialog.hideDialog();
                Snackbar.make(findViewById(android.R.id.content), "تعذر عرض المهات " + e , Snackbar.LENGTH_LONG)
                        .setAction("محاولة مرة اخري", v -> GetReview(DatePicekd)).show();
            }

        }, error -> {
            error.printStackTrace();
            viewDialog.hideDialog();
            Snackbar.make(findViewById(android.R.id.content), " تعذر عرض المهات " + error , Snackbar.LENGTH_LONG)
                    .setAction("محاولة مرة اخري", v -> GetReview(DatePicekd)).show();
        });

        requestQueue.add(stringRequest);

    }

}

