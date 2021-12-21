package com.example.lab3.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lab3.R;

public class SplashScreen extends Fragment {
    public SplashScreen() {
        // Required empty public constructor
    }

    public static SplashScreen newInstance() {
        SplashScreen fragment = new SplashScreen();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        ImageView holder = view.findViewById(R.id.splashAnimation);
        holder.setBackgroundResource(R.drawable.splash_animation);
        AnimationDrawable anim = (AnimationDrawable) holder.getBackground();
        anim.start();
        return view;
    }
}