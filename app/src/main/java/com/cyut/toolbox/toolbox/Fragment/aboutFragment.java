package com.cyut.toolbox.toolbox.Fragment;



import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyut.toolbox.toolbox.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class aboutFragment extends Fragment {


    View view;
    public aboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_about, container, false);
        FloatingActionButton floatingActionButton=getActivity().findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.GONE);


        return view;
    }

}
