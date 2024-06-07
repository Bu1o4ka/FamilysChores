package com.bu1o4ka.familyschores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FamilyActivity extends AppCompatActivity {

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        Button createFamilyButton = findViewById(R.id.create_family_button);
        createFamilyButton.setOnClickListener(v -> {
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (email == null) email = "";
            email = email.substring(0, email.indexOf('.'));
            DatabaseReference myRef = database.getReference("task").push();
            myRef.setValue("");
            DatabaseReference users = database.getReference("user").child(email);
            users.setValue(myRef.getKey());
            startActivity(new Intent(FamilyActivity.this, MainActivity.class));
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("familyCode", myRef.getKey());
            editor.apply();
        });

        Button joinFamilyButton = findViewById(R.id.join_family_button);
        joinFamilyButton.setOnClickListener(v -> {
            EditText codeFamily = findViewById(R.id.code_family_editText);
            String familyCode = codeFamily.getText().toString();
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (email == null) email = "";
            email = email.substring(0, email.indexOf('.'));
            DatabaseReference users = database.getReference("user").child(email);
            users.setValue(familyCode);
            startActivity(new Intent(FamilyActivity.this, MainActivity.class));
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("familyCode", familyCode);
            editor.apply(); 
        });

    }
}