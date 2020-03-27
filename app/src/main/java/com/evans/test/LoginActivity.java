package com.evans.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText mEmail, mPassword;
    private Button mLogin;
    private FirebaseAuth mAuth;
    private TextView regLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        mLogin.setOnClickListener(v -> onLogin());
        regLink.setOnClickListener(v-> toRegisterActivity());
    }

    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void onLogin() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (!email.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!password.isEmpty()) {
                    if (password.length() >= 6) {
                        proceedWithLogin(email, password);
                    } else {
                        Toast.makeText(this, "Minimum password length should be 6 characters",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mPassword.setError(getString(R.string.password_error));
                    mPassword.requestFocus();
                }
            } else {
                mEmail.setError(getString(R.string.email_invalid));
                mEmail.requestFocus();
            }
        } else {
            mEmail.setError(getString(R.string.email_valid));
            mEmail.requestFocus();
        }
    }

    private void proceedWithLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                toHomeActivity();
            } else {
                String errMsg = Objects.requireNonNull(task.getException()).getMessage();
                Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void initViews() {
        mEmail = findViewById(R.id.loginEmail);
        mPassword = findViewById(R.id.loginPassword);
        mLogin = findViewById(R.id.btnLogin);
        regLink = findViewById(R.id.regLink);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            toHomeActivity();
        } else {
            Log.d(TAG, "onStart: User is no logged in");
        }
    }

    private void toHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
