package com.bu1o4ka.familyschores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.SnapshotHolder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ArrayList<String> tasks = new ArrayList<>();
    public ArrayList<String> tasks_id = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView listView;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        listView = findViewById(R.id.task_list);


        ImageButton profilebutton = findViewById(R.id.auth_button);
        profilebutton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });


        ImageButton addButton = findViewById(R.id.add_task_button);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            intent.putExtra("task", tasks_id.get(position));
            startActivity(intent);

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        tasks.clear();
        tasks_id.clear();

        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email == null) email = "";
        email = email.substring(0, email.indexOf('.'));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("task").child(mSettings.getString("familyCode", ""));
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    for (DataSnapshot snap : task.getResult().getChildren()) {
                        String task_name = (String) snap.child("name").getValue();
                        String task_description = (String) snap.child("description").getValue();
                        String task_assignee = (String) snap.child("assignee").getValue();
                        String task_reward = (String) snap.child("reward").getValue();
                        tasks.add(task_name);
                        tasks_id.add(snap.getKey());
                    }
                    findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, tasks);
                    listView.setAdapter(adapter);
                }
            }
        });


    }
}

