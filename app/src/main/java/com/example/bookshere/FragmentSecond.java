package com.example.bookshere;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentSecond extends Fragment {



    public FragmentSecond() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent=new Intent(FragmentSecond.this.getContext(),BuySellScreen.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    }
