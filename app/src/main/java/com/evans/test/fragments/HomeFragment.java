package com.evans.test.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        mPosts.add(new Post("001", "https://image.shutterstock.com/image-photo/singapore-8-march-2017-human-260nw-615514712.jpg", "sample caption", "1000", "50", "20 March, 2020"));

        postsRecycler.setAdapter(mPostAdapter);
        mPostAdapter.notifyDataSetChanged();
    }

    private void initViews(View view) {
        postsRecycler = view.findViewById(R.id.postsRecycler);
    }

    @Override
    public void itemClicked(Post post, View view) {
        switch (view.getId()) {
            case R.id.postItem:
                Toast.makeText(getContext(), "post item clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.likes:
                Toast.makeText(getContext(), "likes clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.comments:
                Toast.makeText(getContext(), "comments clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.postCaption:
                Toast.makeText(getContext(), "caption clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
