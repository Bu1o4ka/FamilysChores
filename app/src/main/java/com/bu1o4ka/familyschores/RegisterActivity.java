package com.bu1o4ka.familyschores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        TextView upin = findViewById(R.id.up_v_in);
        upin.setOnClickListener(v -> {
            startActivity( new Intent(RegisterActivity.this, LoginActivity.class));
        });


        Button signupbutton = findViewById(R.id.sign_up_button);
        signupbutton.setOnClickListener(v -> {
            EditText login = findViewById(R.id.login_sign_up);
            EditText password = findViewById(R.id.password_sign_up);
            createAccount(login.getText().toString(), password.getText().toString());
        });
    }


    private void createAccount(String login, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithLogin:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity( new Intent(RegisterActivity.this, FamilyActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithLogin:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}