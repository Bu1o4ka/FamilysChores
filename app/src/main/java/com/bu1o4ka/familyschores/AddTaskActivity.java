package com.bu1o4ka.familyschores;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTaskActivity extends AppCompatActivity {

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        EditText taskName = findViewById(R.id.task_name);
        EditText taskDescription = findViewById(R.id.task_description);
        EditText taskAssignee = findViewById(R.id.task_assignee);
        EditText taskReward = findViewById(R.id.task_reward);
        Button saveButton = findViewById(R.id.save_task_button);

        saveButton.setOnClickListener(v -> {
            String name = taskName.getText().toString();
            String description = taskDescription.getText().toString();
            String assignee = taskAssignee.getText().toString();
            String reward = taskReward.getText().toString();


            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (email == null) email = "";
            email = email.substring(0, email.indexOf('.'));

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("task").child(mSettings.getString("familyCode", "")).push();
            myRef.child("name").setValue(name);
            myRef.child("description").setValue(description);
            myRef.child("assignee").setValue(assignee);
            myRef.child("reward").setValue(reward);



            finish();
        });
    }
}
