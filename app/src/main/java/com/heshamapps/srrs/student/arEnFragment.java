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
import com.heshamapps.srrs.logic.SaveAsTaken;
import com.heshamapps.srrs.models.Courses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    ArrayList<Courses> coursesList = new ArrayList<Courses>();
    ArrayList<Courses> coursesListTaken = new ArrayList<Courses>();


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

        // Show 97 and 98 and 99 with GRS


        Courses AR111 = new Courses("AR111");
        AR111.setCourseHours(3);

        Courses AR112 = new Courses("AR112");
        AR112.setCourseHours(3);

        Courses EL097 = new Courses("EL097");
        EL097.setCourseHours(0);

        Courses EL098 = new Courses("EL098");
        EL098.setCourseHours(0);

        Courses EL099 = new Courses("EL099");
        EL098.setCourseHours(0);

        Courses EL111 = new Courses("EL111");
        EL111.setCourseHours(3);

        Courses EL112 = new Courses("EL112");
        EL112.setCourseHours(3);

        Courses GR101 = new Courses("GR101");
        GR101.setCourseHours(3);

        Courses  GR131orGR111 = new Courses(" GR131orGR111");
        GR131orGR111.setCourseHours(3);

        Courses TU170 = new Courses("TU170");
        TU170.setCourseHours(3);

        Courses MT129 = new Courses("MT129");
        MT129.setCourseHours(4);

        Courses MT131 = new Courses("MT131");
        MT131.setCourseHours(4);

        Courses MS102 = new Courses("MS102");
        MS102.setCourseHours(3);

        Courses TM111 = new Courses("TM111");
        TM111.setCourseHours(8);

        Courses TM105 = new Courses("TM105");
        TM105.setCourseHours(4);

        Courses MT132 = new Courses("MT132");
        MT132.setCourseHours(4);

        Courses TM103 = new Courses("TM103");
        TM103.setCourseHours(4);












        coursesList.clear();
        switch (EL.getSelectedItem().toString()) {

            case "EL097":
                // register 97
                coursesListTaken.add(EL097);

                coursesList.add(EL098);
                coursesList.add(EL099);

                break;

            case "EL098":
                // register 97+98
                coursesListTaken.add(EL097);
                coursesListTaken.add(EL098);

                coursesList.add(EL099);
                coursesList.add(GR101);
                coursesList.add(GR131orGR111);


                break;
            case "EL099":
                // register 97+98 + 99
                coursesListTaken.add(EL097);
                coursesListTaken.add(EL098);
                coursesListTaken.add(EL099);

                coursesList.add(GR101);
                coursesList.add(GR131orGR111);

                coursesList.add(EL111);
                coursesList.add(TU170);
                coursesList.add(MT129);
                coursesList.add(MT131);
                coursesList.add(MS102);

                break;

            case "EL111":
                // register 97+98 + 99 + el111
                coursesListTaken.add(EL097);
                coursesListTaken.add(EL098);
                coursesListTaken.add(EL099);
                coursesListTaken.add(EL111);

                coursesList.add(GR101);
                coursesList.add(GR131orGR111);
                coursesList.add(EL112);
                coursesList.add(TU170);
                coursesList.add(MT129);
                coursesList.add(MT131);
                coursesList.add(MS102);
                coursesList.add(TM111);
                coursesList.add(TM105);
                coursesList.add(MT132);
                coursesList.add(TM103);
                // Show postCourses as normal and register El111  as pass
                break;

            case "El112":
                // register 97+98 + 99 + el11+ el112
                coursesListTaken.add(EL097);
                coursesListTaken.add(EL098);
                coursesListTaken.add(EL099);
                coursesListTaken.add(EL111);
                coursesListTaken.add(EL112);

                coursesList.add(GR101);
                coursesList.add(GR131orGR111);
                coursesList.add(TU170);
                coursesList.add(MT129);
                coursesList.add(MT131);
                coursesList.add(MS102);
                coursesList.add(TM111);
                coursesList.add(TM105);
                coursesList.add(MT132);
                coursesList.add(TM103);
                break;
        }

        switch (AR.getSelectedItem().toString()){
            case "AR111":
                coursesList.add(AR112);
                coursesListTaken.add(AR111);
                break;

                case "AR112":
                    coursesListTaken.add(AR112);
                    break;
        }


        new SaveAsTaken().saveAnyCourses(coursesListTaken);

        FirstYearFragment fragment = new FirstYearFragment();
    //    AnyYearFragment fragment = new AnyYearFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("Beginning", (Serializable) coursesList);

        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment,"1").commit();


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
