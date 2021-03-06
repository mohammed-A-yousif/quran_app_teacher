package com.example.quranappteacher.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quranappteacher.InternetStatus;
import com.example.quranappteacher.adapter.TaskAdapter;
import com.example.quranappteacher.R;
import com.example.quranappteacher.SharedPrefManager;
import com.example.quranappteacher.URLs;
import com.example.quranappteacher.ViewDialog;
import com.example.quranappteacher.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private TaskAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int TeacherId;
    private JSONArray jsonArray;
    private ArrayList<Task> listItems;
    ViewDialog viewDialog;
    SharedPreferences sp;
    Task item;
    String nameStr, taskStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

//      @@@@@@
        sp = getSharedPreferences("StudentTaskPrefs", Context.MODE_PRIVATE);
//      @@@@@@

        //      Toolbar
        Toolbar toolbar = findViewById(R.id.task_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        //      ExampleList
        listItems = new ArrayList<>();

//        @@@@@@@@@@@@
        //      buildRecyclerView
        RecyclerView recyclerView = findViewById(R.id.task_recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        adapter = new TaskAdapter(listItems);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//              ################
                item = listItems.get(position);
                nameStr = item.getStudent();
                taskStr = item.getTaskName();


                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name", nameStr);
                editor.putString("task", taskStr);

                editor.commit();

                Intent i = new Intent(getApplicationContext(), ViewTask.class);
                startActivity(i);
                finish();
//              ################
            }
        });

        viewDialog = new ViewDialog(this);
        TeacherId = SharedPrefManager.getInstance(this).getAdmin().getId();

        if (InternetStatus.getInstance(this).isOnline()) {
            getTasks();
        } else {
            Snackbar.make(findViewById(android.R.id.content), " غير متصل بالانترت حاليا ، الرجاء مراجعةالأنترنت ", Snackbar.LENGTH_LONG)
                    .setAction("محاولة مرة اخري", v -> getTasks()).show();
        }

        FloatingActionButton teachFAB = findViewById(R.id.add_mission_fab);
        teachFAB.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), AddingTask.class);
            startActivity(i);
            finish();
        });
    }

    private void getTasks() {
        viewDialog.showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.GetTask + TeacherId, response -> {
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject TaskObject = jsonArray.getJSONObject(i);
                    int Id = TaskObject.getInt("IdTask");
                    String TaskName = TaskObject.getString("TaskName");
                    String TaskDec = TaskObject.getString("TaskDec");
                    String Teacher = TaskObject.getString("Teacher");
                    String Student = TaskObject.getString("Student");
                    int TaskStatus = TaskObject.getInt("TaskStatus");
                    String CreatedAt = TaskObject.getString("CreatedAt");
                    Task listItem = new Task(Id, TaskName, TaskDec, Teacher, Student, TaskStatus, CreatedAt);
                    listItems.add(listItem);
                }

                adapter.notifyDataSetChanged();
                viewDialog.hideDialog();
//                Log.d("res", jsonArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
                viewDialog.hideDialog();
                Snackbar.make(findViewById(android.R.id.content), "  فشل عرض المهات " + e, Snackbar.LENGTH_LONG)
                        .setAction(" محاولة مرة اخري", v -> getTasks()).show();
            }

        }, error -> {
            error.printStackTrace();
            viewDialog.hideDialog();
            Snackbar.make(findViewById(android.R.id.content), " فشل عرض المهات  " + error, Snackbar.LENGTH_LONG)
                    .setAction(" محاولة مرة اخري", v -> getTasks()).show();
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
//                adapter.getFilter().filter(newText);
                filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text) {
        ArrayList<Task> filteredList = new ArrayList<>();
        for (Task item : listItems) {
            if (item.getStudent().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

}