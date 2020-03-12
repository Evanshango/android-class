package com.evans.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evans.test.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText mEmail, mPassword;
    private Button mLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        mLogin.setOnClickListener(v -> onLogin());
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
        User user = new User("TestName", "TestName", password, email, "super_user");
        Intent toHomePage = new Intent(this, HomeActivity.class);
        toHomePage.putExtra("user", user)
;        startActivity(toHomePage);
        finish();
    }

    private void initViews() {
        mEmail = findViewById(R.id.loginEmail);
        mPassword = findViewById(R.id.loginPassword);
        mLogin = findViewById(R.id.btnLogin);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
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
