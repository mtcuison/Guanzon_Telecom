package org.rmj.ggc.samsung_knox.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rmj.ggc.samsung_knox.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_KnoxUpload extends Fragment {

    public Fragment_KnoxUpload() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_knox_upload, container, false);
    }
}
