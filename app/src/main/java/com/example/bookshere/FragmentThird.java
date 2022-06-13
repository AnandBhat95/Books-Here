package com.example.bookshere;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class FragmentThird extends Fragment {

    BottomNavigationView bottomNavigationView1;
    public FragmentThird() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


       // bottomNavigationView1.setSelectedItemId(R.id.home);
        Intent intent=new Intent(FragmentThird.this.getContext(),PersonInfo.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_third, container, false);

    }
}