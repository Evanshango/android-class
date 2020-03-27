package com.evans.test.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evans.test.R;
import com.evans.test.models.User;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle bundle = getArguments();
        if (bundle != null){
            mUser = bundle.getParcelable("user");
        } else {
            Log.d(TAG, "onCreateView: User object not passed");
        }

        initViews(view);
        return view;
    }

    private void initViews(View view) {

    }
}
