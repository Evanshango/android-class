package com.evans.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.evans.test.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.evans.test.constants.Helpers.USERS_REF;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText mFirstName, mLastName, mEmail, mPassword, mCPass;
    private Button mRegister;
    private TextView txtLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference colRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        colRef = mFirestore.collection(USERS_REF);

        initViews();
        txtLogin.setOnClickListener(v -> navigateToLogin());

        mRegister.setOnClickListener(view -> initiateRegistration());
    }

    private void initiateRegistration() {
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPass = mCPass.getText().toString().trim();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty()
                && !password.isEmpty() && ! confirmPass.isEmpty()){
            doRegister(firstName, lastName, email, password);
        } else {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    private void doRegister(String firstName, String lastName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                saveUserInfo(firstName, lastName, email);
            } else {
                String errorMsg = Objects.requireNonNull(task.getException()).toString();
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Log.d(TAG, "doRegister: ERROR " + e));
    }

    private void saveUserInfo(String firstName, String lastName, String email) {
        FirebaseUser fUser = mAuth.getCurrentUser();
        if (fUser != null){
            String userId = fUser.getUid();
            User user = new User(userId, firstName, lastName, email, firstName, "");
            colRef.document(userId).set(user).addOnCompleteListener(task -> {
                try {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
                        navigateToHome(user);
                    }
                } catch (Exception ex){
                    Log.d(TAG, "saveUserInfo: " + ex);
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "User not Authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToHome(User user) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user", user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initViews() {
        mFirstName = findViewById(R.id.regFirstName);
        mLastName = findViewById(R.id.regLastName);
        mEmail = findViewById(R.id.regEmail);
        mPassword = findViewById(R.id.regPassword);
        mCPass = findViewById(R.id.regConfirmPass);
        mRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLoginLink);
    }
}
