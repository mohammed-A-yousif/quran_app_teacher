package com.example.quranappteacher;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quranappteacher.activity.TaskActivity;
import com.example.quranappteacher.model.Student;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddingTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<String> StudentArray;
    private JSONArray jsonArray;
    int TeacherId;
    int StudentId;

    List<Student> listItems ;
    Spinner addMissionStudentSpinner;
    String taskName, taskDec;

    EditText taskNameText, taskDecText;
    ViewDialog viewDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_task);

        Button addMissionButton = findViewById(R.id.add_mission_button);
        addMissionStudentSpinner = findViewById(R.id.add_mission_student_spinner);

        taskNameText = findViewById(R.id.adding_mission_name_editText);
        taskDecText = findViewById(R.id.mission_editText_dec);


        viewDialog = new ViewDialog(this);

        listItems = new ArrayList<>();
        addMissionStudentSpinner.setOnItemSelectedListener(this);

        addMissionStudentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK);
                StudentId = listItems.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Snackbar.make(findViewById(android.R.id.content), "Please Choose Teacher !!! ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TeacherId = SharedPrefManager.getInstance(this).getAdmin().getId();
        StudentArray = new ArrayList<>();

        GetStudent();

        addMissionButton.setOnClickListener(v -> {
            taskName = taskNameText.getText().toString();
            taskDec = taskDecText.getText().toString();
            addTask(taskName, taskDec);

        });
    }

    private void addTask(String taskName, String taskDec) {
        viewDialog.showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,  URLs.AddTask + "?IdTeacher=" + TeacherId + "&IdStudent=" + StudentId  + "&TaskName=" + taskName  + "&TaskDec=" + taskDec + "&TaskStatus=" + 0 + "&Enabled=" + 1, null,
                (JSONObject response) -> {
                    try {

                        int TaskId = response.getInt("IdTask");
                        Log.d("res", response.toString());
                        onInsertSuccess();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        onInsertFailed();
                    }

                    Log.d("String Response : ", ""+  response.toString());
                }, error -> Log.d("Error getting response", "" +error));

        requestQueue.add(jsonObjectRequest);
        Log.d("rs", "" + jsonObjectRequest);

    }


    public void GetStudent(){
        viewDialog.showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.GetStudents + TeacherId, response -> {
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i ++){
                    JSONObject StudentObject = jsonArray.getJSONObject(i);
                    int Id = StudentObject.getInt("IdStudent");
                    String Name = StudentObject.getString("Name");
                    String PhoneNumber = StudentObject.getString("PhoneNumber");
                    String Date = StudentObject.getString("CreatedAt");
                    Student listItem = new Student(Id ,Name, PhoneNumber, Date);
                    listItems.add(listItem);
                    StudentArray.add(listItem.getName());
                }
                addMissionStudentSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, StudentArray));
                viewDialog.hideDialog();
                Log.d("res", jsonArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
                viewDialog.hideDialog();
                Snackbar.make(findViewById(android.R.id.content), "Couldn't get Teacher " + e , Snackbar.LENGTH_LONG)
                        .setAction(" محاولة مرة اخري", v -> GetStudent()).show();
            }

        }, error -> {
            error.printStackTrace();
            viewDialog.hideDialog();
            Snackbar.make(findViewById(android.R.id.content), "Couldn't get Teacher " + error , Snackbar.LENGTH_LONG)
                    .setAction(" محاولة مرة اخري", v -> GetStudent()).show();
        });

        requestQueue.add(stringRequest);

    }

    private void onInsertFailed() {
        viewDialog.hideDialog();
        Snackbar.make(findViewById(android.R.id.content), "Sign in Failed", Snackbar.LENGTH_LONG)
                .setAction(" محاولة مرة اخري", v -> {
                    addTask(taskNameText.getText().toString() , taskDecText.getText().toString());
                }).show();
    }

    private void onInsertSuccess() {
        viewDialog.hideDialog();
        Snackbar.make(findViewById(android.R.id.content), "Sign in Successfully", Snackbar.LENGTH_LONG)
                .show();
        startActivity(new Intent(this, TaskActivity.class));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
