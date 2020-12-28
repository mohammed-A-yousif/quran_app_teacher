package com.example.quranappteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Missions extends AppCompatActivity implements MissionsAdapter.MissionsAdapterListener {
    private MissionsAdapter adapter;
    int TeacherId;
    private JSONArray jsonArray;
    List<Task> listItems ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missions_activity);
        Toolbar toolbar = findViewById(R.id.missions_toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.missions_recyclerView);
        recyclerView.setHasFixedSize(true);

        TeacherId = SharedPrefManager.getInstance(this).getAdmin().getId();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();


        adapter = new MissionsAdapter(listItems, this);
        recyclerView.setAdapter(adapter);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        getTasks();

        FloatingActionButton teachFAB = findViewById(R.id.add_mission_fab);
        teachFAB.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), AddingMission.class);
            startActivity(i);
        });
    }

    private void getTasks() {

        //        viewDialog.showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.GetTask + TeacherId , response -> {
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i ++){
                    JSONObject TaskObject = jsonArray.getJSONObject(i);
                    int Id = TaskObject.getInt("IdTask");
                    String TaskName = TaskObject.getString("TaskName");
                    String TaskDec = TaskObject.getString("TaskDec");
                    String CreatedAt = TaskObject.getString("CreatedAt");
                    Task listItem = new Task(Id,TaskName, TaskDec, CreatedAt);
                    listItems.add(listItem);
                }

                adapter.notifyDataSetChanged();
//                viewDialog.hideDialog();
                Log.d("res", jsonArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
//                viewDialog.hideDialog();
                Snackbar.make(findViewById(android.R.id.content), "Couldn't get Students " + e , Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> getTasks()).show();
            }

        }, error -> {
            error.printStackTrace();
//            viewDialog.hideDialog();
            Snackbar.make(findViewById(android.R.id.content), "Couldn't get Students " + error , Snackbar.LENGTH_LONG)
                    .setAction("Retry", v -> getTasks()).show();
        });

        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onContactSelected(Contact contact) {
        Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();
    }
}