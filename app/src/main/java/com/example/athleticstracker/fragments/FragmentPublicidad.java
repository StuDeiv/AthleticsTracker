package com.example.athleticstracker.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.athleticstracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPublicidad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPublicidad extends Fragment {

    public FragmentPublicidad() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FragmentPublicidad.
     */
    public static FragmentPublicidad newInstance(String param1, String param2) {
        FragmentPublicidad fragment = new FragmentPublicidad();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publi, container, false);
        /* Cada vez que se pulse el fragment, redigirá a la web del anunciante */
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getResources().getString(R.string.webAdidas);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        return view;
    }
}