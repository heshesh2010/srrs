package com.heshamapps.srrs.student;


import android.os.Bundle;

import androidx.annotation.Nullable;
import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.heshamapps.srrs.R;
import com.heshamapps.srrs.logic.GetAllCourses;
import com.heshamapps.srrs.logic.Next;
import com.heshamapps.srrs.logic.NextAnyYear;
import com.heshamapps.srrs.logic.SaveAsTaken;
import com.heshamapps.srrs.models.Courses;
import com.heshamapps.srrs.util.MyCallback5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;



public class AnyYearFragment extends Fragment {

    @BindView(R.id.firstTermChipsLinerLayout)
    LinearLayout firstTermChipsLinerLayout;

    @BindView(R.id.first_next)
    Button first_next;

    @BindView(R.id.year)
    Spinner year ;

    @BindView(R.id.semster)
    Spinner semster ;

    ArrayList<Courses> coursesSelected = new ArrayList<Courses>();
    ArrayList<String> NotSelected = new ArrayList<String>();

    public AnyYearFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_any_year, container, false);
        ButterKnife.bind(this, view);
        new GetAllCourses().getAllCourses(new MyCallback5() {
            @Override
            public void callGetAllCourses(List<Courses> courses) {

                for (int i = 0; i < courses.size(); i++) {
                    CheckBox courseBTN = new CheckBox(getActivity());
                 //   courseBTN.setOnCheckedChangeListener(this);
                    courseBTN.setText(String.format(Locale.ENGLISH, "%s(%d)", courses.get(i).getCourseCode(), courses.get(i).getCourseHours()));
                            firstTermChipsLinerLayout.setTag("1");
                            firstTermChipsLinerLayout.addView(courseBTN);

                }
            }
        });

        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        first_next.setOnClickListener(v -> next((RelativeLayout) v.getParent()));

    }

    void next(RelativeLayout parent){


        for (int i = 0; i < ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildCount(); i++) {
            View view = ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildAt(i);

            if (((CheckBox) view).isChecked()) {

                coursesSelected.add(new Courses(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase()));
            }

        }

        new NextAnyYear().do_next(Integer.parseInt(semster.getSelectedItem().toString()) , Integer.parseInt(year.getSelectedItem().toString()), coursesSelected, NotSelected,getActivity(),getView());
        new SaveAsTaken().saveAnyCourses(coursesSelected);


    }

}
