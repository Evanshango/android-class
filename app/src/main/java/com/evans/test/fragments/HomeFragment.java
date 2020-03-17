package com.evans.test.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.evans.test.R;
import com.evans.test.adapters.PostAdapter;
import com.evans.test.models.Post;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements PostAdapter.PostItemListener {

    private static final String TAG = "HomeFragment";
    private RecyclerView postsRecycler;
    private PostAdapter mPostAdapter;
    private List<Post> mPosts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);

        loadPosts();

        return view;
    }

    private void loadPosts() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        postsRecycler.setHasFixedSize(true);
        postsRecycler.setLayoutManager(manager);

        mPostAdapter = new PostAdapter(mPosts, this);

        // TODO: 3/17/2020 Add your own post items

        postsRecycler.setAdapter(mPostAdapter);
        mPostAdapter.notifyDataSetChanged();
    }

    private void initViews(View view) {
        postsRecycler = view.findViewById(R.id.postsRecycler);
    }

    @Override
    public void itemClicked(Post post, View view) {

    }
}
