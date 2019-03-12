package com.heshamapps.srrs.student;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.heshamapps.srrs.R;
import com.heshamapps.srrs.models.Courses;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class arEnFragment extends Fragment {



    @BindView(R.id.spinner)
    Spinner AR ;

    @BindView(R.id.spinner2)
    Spinner EL ;

    @BindView(R.id.next)
    Button nextBTN ;


    FirebaseFirestore db;



    public arEnFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
     /*       mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @OnClick(R.id.next)
    public void next(View view) {

        switch (EL.getSelectedItem().toString()) {
            case "El97":
                // Show 97 and 98 and 99 with GRS
                ArrayList<Courses> places = new ArrayList<Courses>(
                        Arrays.asList(new Courses("EL097"), new Courses("EL098"), new Courses("EL099")));

                studentFragment fragment = new studentFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("EL097", places);

                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment).commit();

                break;
            case "EL098":
                // Show 98 and 99 with GRS
                break;
            case "EL099":
                // Show postCourses as normal
                break;
            case "EL111":
                // Show postCourses as normal and register El111  as pass
                break;
            case "El112":
                // Show postCourses as normal and register El111 and EL112 as pass
                break;
        }




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ar_en, container, false);
        ButterKnife.bind(this, view);
        db = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(getActivity());
        return view;

    }




    @Override
    public void onDetach() {
        super.onDetach();
    }

}
