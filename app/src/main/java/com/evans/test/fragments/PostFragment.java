package com.evans.test.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evans.test.R;
import com.evans.test.adapters.MediaPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class PostFragment extends Fragment {

    private ViewPager postFragmentsViewPager;
    private TabLayout postTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        initViews(view);

        initializeUIComponents();

        return view;
    }

    private void initializeUIComponents() {
        MediaPagerAdapter pagerAdapter = new MediaPagerAdapter(getChildFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new GalleryFragment(), "Gallery");
        pagerAdapter.addFragment(new PhotoFragment(), "Photo");

        postFragmentsViewPager.setAdapter(pagerAdapter);
        postTabLayout.setupWithViewPager(postFragmentsViewPager);
    }

    private void initViews(View view) {
        postTabLayout = view.findViewById(R.id.postTabLayout);
        postFragmentsViewPager = view.findViewById(R.id.postFragmentsViewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeUIComponents();
    }
}
