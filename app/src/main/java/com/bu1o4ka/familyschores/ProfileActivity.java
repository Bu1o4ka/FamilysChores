package com.bu1o4ka.familyschores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email == null) email = "";
        email = email.substring(0, email.indexOf('.'));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("task").child(email);


        TextView emaillogin = findViewById(R.id.email_text);
        emaillogin.setText("Ваша почта:\n" + FirebaseAuth.getInstance().getCurrentUser().getEmail());

        TextView codeFamily = findViewById(R.id.family_code);
        codeFamily.setText("Код семьи:\n" + mSettings.getString("familyCode", ""));


        Button signoutbutton = findViewById(R.id.sign_out_button);
        signoutbutton.setOnClickListener(v -> {
            signOut();
        });

        Button deletebutton = findViewById(R.id.delete_button);
        deletebutton.setOnClickListener(v -> {
            delete();
            myRef.removeValue();
        });

    }

    private void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(ProfileActivity.this, SplashActivity.class));
                    }
                });
        // [END auth_fui_signout]
    }

    private void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(ProfileActivity.this, SplashActivity.class));
                    }
                });
        // ...
    }
    // [END auth_fui_delete]
}
