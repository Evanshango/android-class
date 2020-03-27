package com.evans.test.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.evans.test.R;
import com.evans.test.models.Post;
import com.evans.test.models.ReportPost;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.evans.test.constants.Helpers.REPORT_POST;

public class PostActionDialog extends AppCompatDialogFragment {

    private static final String TAG = "PostActionDialog";
    private TextView reportPost;
    private Post mPost;
    private CollectionReference postReportsRef;
    private ReportListener mReportListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.post_more_options, null);
        initViews(view);

        Bundle bundle = getArguments();
        if (bundle != null){
            mPost = bundle.getParcelable("post");
        } else {
            Log.d(TAG, "onCreateDialog: Post object empty");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog dialog = builder.create();

        reportPost.setOnClickListener(v -> doReportPost(dialog, mPost));// report post

        return dialog;
    }

    private void doReportPost(AlertDialog dialog, Post post) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        postReportsRef = database.collection(REPORT_POST);
        String reportId = postReportsRef.document().getId();

        ReportPost reportPost = new ReportPost(reportId, post.getPostId());

        postReportsRef.document(reportId).set(reportPost).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                dialog.dismiss();
                mReportListener.reportFeedback("Post reported. We will get back to you.");
            } else {
                dialog.dismiss();
                Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->{
            dialog.dismiss();
            Toast.makeText(getActivity(), "Check you connection", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews(View view) {
        reportPost = view.findViewById(R.id.reportPost);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mReportListener = (ReportListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement reportListener");
        }
    }

    public interface ReportListener {
        void reportFeedback(String message);
    }
}
