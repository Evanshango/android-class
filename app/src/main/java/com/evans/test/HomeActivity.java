package com.evans.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.evans.test.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static com.evans.test.constants.Helpers.GALLERY_REQUEST_CODE;
import static com.evans.test.constants.Helpers.UPLOADS;
import static com.evans.test.constants.Helpers.USERS_REF;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private TextView firstName, lastName, email, userName;
    private Button chooseImage, btnUpload;
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private Uri mUri;
    private StorageReference mStorageReference;
    private CollectionReference usersCollection;
    private String userId, imageUrl;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference(UPLOADS);
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        usersCollection = database.collection(USERS_REF);

        if (user != null){
            userId = user.getUid();
        } else {
            Log.d(TAG, "onCreate: User not logged in");
        }

        initViews();

        getUserDetails();

        chooseImage.setOnClickListener(v -> openGallery());

        btnUpload.setOnClickListener(v -> doUpload());
    }

    private void getUserDetails() {

        Query query = usersCollection.whereEqualTo("userId", userId);

        usersCollection.document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
//                String userId = documentSnapshot.getString("userId");
                mUser = documentSnapshot.toObject(User.class);
            } else {
                Log.d(TAG, "getUserDetails: User not found");
            }
        });

        populateViews();
    }

    private void populateViews() {
        Glide.with(this).load(mUser.getImageUrl()).into(imageView);
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            mUri = data.getData();
            imageView.setImageURI(mUri);
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtensions(Uri uri){
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString());
    }

    private void doUpload() {
        if (mUri != null){
            StorageReference fileRef = mStorageReference.child(userId)
                    .child(System.currentTimeMillis() + "." + getFileExtensions(mUri));
            fileRef.putFile(mUri).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                        taskSnapshot.getTotalByteCount());
                //mProgress.incrementProgressBy((int) progress)
            }).addOnSuccessListener(taskSnapshot -> {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();
                    updateProfile();
                }).addOnFailureListener(e -> Toast.makeText(this, "An error occurred",
                        Toast.LENGTH_SHORT).show());
            });
        } else {
            Toast.makeText(this, "Please choose an image to upload", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        Map<String, Object> map = new HashMap<>();
        map.put("imageUrl", imageUrl);
        usersCollection.document(userId).set(map, SetOptions.merge()).addOnCompleteListener(task -> {
            if ( task.isSuccessful()){
                Toast.makeText(this, "Image upload successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doSignOut() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signOut();

        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        firstName = findViewById(R.id.userFirstName);
        lastName = findViewById(R.id.userLastName);
        email = findViewById(R.id.userEmail);
        userName = findViewById(R.id.username);
        chooseImage = findViewById(R.id.btnOpenGallery);
        imageView = findViewById(R.id.imageView);
        btnUpload = findViewById(R.id.btnUpload);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            doSignOut();
        }
        return super.onOptionsItemSelected(item);
    }
}
