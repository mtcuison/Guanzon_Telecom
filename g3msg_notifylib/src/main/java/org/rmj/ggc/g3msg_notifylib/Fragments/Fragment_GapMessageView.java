package org.rmj.ggc.g3msg_notifylib.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rmj.ggc.g3msg_notifylib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_GapMessageView extends Fragment {

    public Fragment_GapMessageView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gap_message, container, false);
    }
}
