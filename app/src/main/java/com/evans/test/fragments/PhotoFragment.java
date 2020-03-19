package com.evans.test.fragments;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.evans.test.R;
import com.evans.test.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.evans.test.constants.Helpers.GALLERY_REQUEST_CODE;
import static com.evans.test.constants.Helpers.LONG_DATE;
import static com.evans.test.constants.Helpers.POSTS_REF;
import static com.evans.test.constants.Helpers.TIME;
import static com.evans.test.constants.Helpers.UPLOADS;

public class PhotoFragment extends Fragment {

    private static final String TAG = "PhotoFragment";
    private Button btnUpload;
    private ImageView postImg;
    private EditText postCaption;
    private Uri imageUri;
    private StorageReference mStorageReference;
    private CollectionReference postsRef;
    private String userId, postId, date, time, imageUrl, username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        //Initialize Firebase Objects
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference(UPLOADS);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        postsRef = database.collection(POSTS_REF);
        postId = postsRef.document().getId();

        userId = user != null ? user.getUid() : "";
        username = user != null ? user.getDisplayName() : "";

        date = new SimpleDateFormat(LONG_DATE, Locale.getDefault()).format(new Date());
        time = new SimpleDateFormat(TIME, Locale.getDefault()).format(new Date());

        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnUpload.setOnClickListener(v -> uploadPost());
        postImg.setOnClickListener(v -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select an Option"), GALLERY_REQUEST_CODE);
    }

    private void uploadPost() {
        String caption = postCaption.getText().toString().trim();
        Post post = new Post(postId, imageUrl, caption, "0", "0", date, time, "" , userId);
        postsRef.document(postId).set(post).addOnCompleteListener(task -> {
            // TODO: 3/19/2020 To be Continued
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Unable to add a post", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            postImg.setImageURI(imageUri);
            openConfirmDialog();
        } else {
            postImg.setImageURI(null);
        }
    }

    private void openConfirmDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_dialog);
        initDialogViews(dialog);
        dialog.show();
    }

    private void initDialogViews(Dialog dialog) {
        TextView cancel = dialog.findViewById(R.id.txtCancel);
        TextView proceed = dialog.findViewById(R.id.txtProceed);
        ProgressBar uploadProgress = dialog.findViewById(R.id.uploadProgress);

        cancel.setOnClickListener(v -> {
            imageUri = null;
            postImg.setImageURI(null);
            dialog.dismiss();
        });

        proceed.setOnClickListener(v -> uploadImage(dialog, uploadProgress));
    }

    private void uploadImage(Dialog dialog, ProgressBar uploadProgress) {
        uploadProgress.setVisibility(View.VISIBLE);
        if (imageUri != null){
            StorageReference fileRef = mStorageReference.child(postId)
                    .child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                        taskSnapshot.getTotalByteCount());
                uploadProgress.incrementProgressBy((int) progress);
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrl = uri.toString();
                        uploadProgress.setVisibility(View.GONE);
                        dialog.dismiss();
                    });
                } else {
                    uploadProgress.setVisibility(View.GONE);
                    dialog.dismiss();
                    String errMsg = task.getException().getMessage();
                    Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "uploadImage: ERROR " + errMsg);
                }
            }).addOnFailureListener(e -> {
                uploadProgress.setVisibility(View.GONE);
                dialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            });
        } else {
            uploadProgress.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Please select an image first", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver resolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(imageUri));
    }

    private void initViews(View view){
        btnUpload = view.findViewById(R.id.uploadPost);
        postImg = view.findViewById(R.id.postImage);
        postCaption = view.findViewById(R.id.postCaption);
    }
}
