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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();



        Button signinbutton = findViewById(R.id.sign_in_button);
        signinbutton.setOnClickListener(v -> {
            EditText login = findViewById(R.id.login_sign_in);
            EditText password = findViewById(R.id.password_sign_in);
            signIn(login.getText().toString(), password.getText().toString());
        });

        TextView inup = findViewById(R.id.in_v_up);
        inup.setOnClickListener(v -> {
            startActivity( new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }




    private void signIn(String login, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithLogin:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            if (email == null) email = "";
                            email = email.substring(0, email.indexOf('.'));
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            database.getReference("user").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()){
                                        String family = (String)task.getResult().getValue();
                                        if (family != null && !family.equals("")){
                                            startActivity( new Intent(LoginActivity.this, MainActivity.class));
                                        } else {
                                            startActivity(new Intent(LoginActivity.this, FamilyActivity.class));
                                        }
                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithLogin:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }



}