package com.bu1o4ka.familyschores;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskDetailActivity extends AppCompatActivity {

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        TextView taskName = findViewById(R.id.task_nameView);
        TextView taskDescription = findViewById(R.id.task_descriptionView);
        TextView taskAssignee = findViewById(R.id.task_assigneeView);
        TextView taskReward = findViewById(R.id.task_rewardView);

        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        String key = getIntent().getStringExtra("task");

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email == null) email = "";
        email = email.substring(0, email.indexOf('.'));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("task").child(mSettings.getString("familyCode", "")).child(key);
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String task_name = (String) task.getResult().child("name").getValue();
                    String task_description = (String) task.getResult().child("description").getValue();
                    String task_assignee = (String) task.getResult().child("assignee").getValue();
                    String task_reward = (String) task.getResult().child("reward").getValue();

                    taskName.setText("Название:\n" + task_name);
                    taskDescription.setText("Описание:\n" + task_description);
                    taskAssignee.setText("Исполнитель:\n" + task_assignee);
                    taskReward.setText("Награда:\n" + task_reward);
                }
            }
        });

        Button deleteButton = findViewById(R.id.delete_task_button);
        deleteButton.setOnClickListener(v -> {
            // Логика завершения задания
            myRef.removeValue();
            finish();
        });

        Button completeButton = findViewById(R.id.sign_in_button);

            // Логика удаления задания
        completeButton.setOnClickListener(v -> {
            ClaimRewardActivity dialog = new ClaimRewardActivity(new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myRef.removeValue();
                    finish();
                }
            });
            dialog.show(getSupportFragmentManager(), "custom");
        });

    }
}


