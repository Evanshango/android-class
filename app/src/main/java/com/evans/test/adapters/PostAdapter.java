package com.evans.test.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.evans.test.R;
import com.evans.test.models.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private List<Post> mPosts;
    private PostItemListener mPostItemListener;

    public PostAdapter(List<Post> posts, PostItemListener postItemListener) {
        mPosts = posts;
        mPostItemListener = postItemListener;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item, parent, false);
        return new PostHolder(view, mPostItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts != null ? mPosts.size() : 0;
    }

    class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView postImage;
        TextView likes, comments, caption;
        PostItemListener mPostItemListener;

        PostHolder(@NonNull View itemView, PostItemListener postItemListener) {
            super(itemView);
            mPostItemListener = postItemListener;
            postImage = itemView.findViewById(R.id.postImage);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            caption = itemView.findViewById(R.id.postCaption);

            itemView.setOnClickListener(this);
        }

        void bind(Post post) {
            caption.setText(post.getPostCaption());
            likes.setText(String.format("%s likes", post.getLikes()));
            comments.setText(String.format("%s comments", post.getComments()));

            Glide.with(itemView.getContext()).load(post.getPostImage()).into(postImage);
        }

        @Override
        public void onClick(View v) {
            mPostItemListener.itemClicked(mPosts.get(getAdapterPosition()), v);
        }
    }

    public interface PostItemListener {

        void itemClicked(Post post, View view);
    }
}
