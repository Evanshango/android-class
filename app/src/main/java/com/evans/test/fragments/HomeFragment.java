package com.evans.test.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.test.CommentActivity;
import com.evans.test.R;
import com.evans.test.adapters.PostAdapter;
import com.evans.test.dialogs.PostActionDialog;
import com.evans.test.models.LikePost;
import com.evans.test.models.Post;
import com.evans.test.models.SavedPost;
import com.evans.test.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.evans.test.constants.Helpers.LIKES_REF;
import static com.evans.test.constants.Helpers.POSTS_REF;
import static com.evans.test.constants.Helpers.POST_ID;
import static com.evans.test.constants.Helpers.SAVED_POST_REF;
import static com.evans.test.constants.Helpers.USERS_REF;
import static com.evans.test.constants.Helpers.USER_ID;
import static com.evans.test.constants.Helpers.USER_NAME;

public class HomeFragment extends Fragment implements PostAdapter.PostItemListener,
        PostActionDialog.ReportListener {

    private static final String TAG = "HomeFragment";
    private RecyclerView postsRecycler;
    private PostAdapter mPostAdapter;
    private List<Post> mPosts = new ArrayList<>();
    private CollectionReference postsRef, usersRef, likesRef, savedPostsRef;
    private String userId, username, likeId, savedPostId;
    private int likesCount;
    private NavController mNavController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        //Initialize Firebase Objects
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        usersRef = database.collection(USERS_REF);
        postsRef = database.collection(POSTS_REF);
        likesRef = database.collection(LIKES_REF);
        savedPostsRef = database.collection(SAVED_POST_REF);
        likeId = likesRef.document().getId();
        savedPostId = savedPostsRef.document().getId();

        userId = user != null ? user.getUid() : "";

        initViews(view);

        loadPosts();

        if (!userId.equals(""))
            loadCurrentUser();
        else
            Log.d(TAG, "onCreateView: User not Logged in");

        mPostAdapter = new PostAdapter(mPosts, this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
    }

    private void loadCurrentUser() {
        usersRef.document(userId).get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            username = user != null ? user.getUserName() : "anonymous";
        });
    }

    private void loadPosts() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        postsRecycler.setHasFixedSize(true);
        postsRecycler.setLayoutManager(manager);

        postsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            mPosts.addAll(queryDocumentSnapshots.toObjects(Post.class));
            postsRecycler.setAdapter(mPostAdapter);
            mPostAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Log.d(TAG, "loadPosts: Error: " + e.getMessage()));
    }

    private void initViews(View view) {
        postsRecycler = view.findViewById(R.id.postsRecycler);
    }

    @Override
    public void itemClicked(Post post, View view) {
        switch (view.getId()) {
            case R.id.savePost:
                bookMarkPost(post, view);
                break;
            case R.id.likes:
                likePost(post, view);
                break;
            case R.id.comments:
                showComments(post);
                break;
            case R.id.postCaption:
                Toast.makeText(getContext(), "caption clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.moreImg:
                showMoreOptions(post);
                break;
            case R.id.authorName:
                navigateToUserProfile(post);
                break;
        }
    }

    private void navigateToUserProfile(Post post) {
        Query query = usersRef.whereEqualTo(USER_NAME, post.getAuthor()).limit(1);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                User user = snapshot.toObject(User.class);
                toProfile(user);
            }
        });
    }

    private void toProfile(User user) {
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        mNavController.navigate(R.id.action_homeFragment_to_profileFragment, args);
    }

    private void showMoreOptions(Post post) {
        PostActionDialog postActionDialog = new PostActionDialog();
        Bundle args = new Bundle();
        args.putParcelable("post", post);
        postActionDialog.setArguments(args);
        postActionDialog.show(getParentFragmentManager(), "postActionDialog");
    }

    private void bookMarkPost(Post post, View view) {
        Query query = savedPostsRef.whereEqualTo(POST_ID, post.getPostId())
                .whereEqualTo(USER_ID, userId).limit(1);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    SavedPost savedPost = snapshot.toObject(SavedPost.class);
                    unSavePost(savedPost, view);
                }
            } else {
                savePost(post, view);
            }
        });
    }

    private void unSavePost(SavedPost savedPost, View view) {
        ImageView bookmark = view.findViewById(R.id.savePost);
        savedPostsRef.document(savedPost.getSavedPostId()).delete().addOnCompleteListener(task -> {
            Log.d(TAG, "unSavePost: un-save undone");
            bookmark.setImageResource(R.drawable.ic_save_post);
            mPostAdapter.notifyDataSetChanged();
        });
    }

    private void savePost(Post post, View view) {
        ImageView bookmark = view.findViewById(R.id.savePost);
        SavedPost savedPost = new SavedPost(savedPostId, post);
        savedPostsRef.document(savedPostId).set(savedPost).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                bookmark.setImageResource(R.drawable.ic_bookmark_black);
                Toast.makeText(getContext(), "Post bookmarked", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Log.d(TAG, "bookMarkPost: Error: " + e.getMessage()));
    }

    private void showComments(Post post) {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.putExtra("post", post);
        startActivity(intent);
    }

    private void likePost(Post post, View view) {
        Query query = likesRef.whereEqualTo(POST_ID, post.getPostId())
                .whereEqualTo(USER_ID, userId).limit(1);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    LikePost likePost = snapshot.toObject(LikePost.class);
                    undoLike(likePost, view);
                }
            } else {
                doLikePost(post.getPostId(), view);
            }
        });
    }

    private void undoLike(LikePost likePost, View view) {
        ImageView favBorder = view.findViewById(R.id.favBorder);
        likesRef.document(likePost.getLikeId()).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "undoLike: like undone");
                mPostAdapter.notifyDataSetChanged();
                favBorder.setImageResource(R.drawable.ic_favorite_border);
            }
        });
    }

    private void doLikePost(String postId, View view) {
        LikePost likePost = new LikePost(likeId, postId, username, userId);
        likesRef.document(likeId).set(likePost).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateLikesCount(postId, view);
            }
        });
    }

    private void updateLikesCount(String postId, View view) {
        ImageView favBorder = view.findViewById(R.id.favBorder);
        Query query = likesRef.whereEqualTo(POST_ID, postId);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            favBorder.setImageResource(R.drawable.ic_favorite_red);
            likesCount = queryDocumentSnapshots.size();
        });

        Map<String, Object> likeMap = new HashMap<>();
        likeMap.put("likes", String.valueOf(likesCount));
        postsRef.document(postId).set(likeMap, SetOptions.merge());
    }

    @Override
    public void reportFeedback(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
