package com.heshamapps.srrs.logic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.heshamapps.srrs.R;
import com.heshamapps.srrs.models.Courses;
import com.heshamapps.srrs.models.compareCourses;
import com.heshamapps.srrs.student.FirstYearFragment;
import com.heshamapps.srrs.student.eighthYearFragment;
import com.heshamapps.srrs.student.fifthYearFragment;
import com.heshamapps.srrs.student.fourthYearFragment;
import com.heshamapps.srrs.student.secondYearFragment;
import com.heshamapps.srrs.student.seventhYearFragment;
import com.heshamapps.srrs.student.sixYearFragment;
import com.heshamapps.srrs.student.thirdYearFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RankAnyYear {

    RankAnyYear(List<Courses> detailedCourses, int termLayoutNumber,int yearNumber , Activity activity, View view) {
        // sort the list by comparing course hours

        Collections.sort(detailedCourses, new compareCourses());

        ArrayList<Courses> matchedArray = findOccurrencesInArray(detailedCourses);
        // this will make these courses found the rule to top of list
        for (int i = 0; i < matchedArray.size(); i++) {

            detailedCourses.remove(matchedArray.get(i));
            detailedCourses.add(matchedArray.get(i));
        }

        Collections.reverse(detailedCourses);

        FirstYearFragment fragment1 = new FirstYearFragment();
        secondYearFragment fragment2 = new secondYearFragment();
        thirdYearFragment fragment3 = new thirdYearFragment();
        fourthYearFragment fragment4 = new fourthYearFragment();
        fifthYearFragment fragment5 = new fifthYearFragment();
        sixYearFragment fragment6 = new sixYearFragment();
        seventhYearFragment fragment7 = new seventhYearFragment();
        eighthYearFragment fragment8 = new eighthYearFragment();
        Bundle bundle = new Bundle();

        switch (yearNumber) {


            case 1:



                bundle.putSerializable("AnyYear", (Serializable) detailedCourses);
                bundle.putInt("currentTerm",termLayoutNumber);
                fragment1.setArguments(bundle);

                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment1,"1").commit();
                break;

            case 2:

                bundle.putSerializable("AnyYear", (Serializable) detailedCourses);
                bundle.putInt("currentTerm",termLayoutNumber);
                fragment2.setArguments(bundle);

                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment2,"2").commit();
                break;

            case 3:

                bundle.putSerializable("AnyYear", (Serializable) detailedCourses);
                bundle.putInt("currentTerm",termLayoutNumber);
                fragment3.setArguments(bundle);

                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment3,"3").commit();
                break;

            case 4:
                bundle.putSerializable("AnyYear", (Serializable) detailedCourses);
                bundle.putInt("currentTerm",termLayoutNumber);
                fragment4.setArguments(bundle);

                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment4,"4").commit();
                break;

            case 5:
                bundle.putSerializable("AnyYear", (Serializable) detailedCourses);
                bundle.putInt("currentTerm",termLayoutNumber);
                fragment5.setArguments(bundle);

                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment5,"5").commit();
                break;

            case 6 :
                bundle.putSerializable("AnyYear", (Serializable) detailedCourses);
                bundle.putInt("currentTerm",termLayoutNumber);
                fragment6.setArguments(bundle);

                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment6,"6").commit();
                break;

            case 7 :
                bundle.putSerializable("AnyYear", (Serializable) detailedCourses);
                bundle.putInt("currentTerm",termLayoutNumber);
                fragment7.setArguments(bundle);

                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment7,"7").commit();
                break;

            case 8 :
                bundle.putSerializable("AnyYear", (Serializable) detailedCourses);
                bundle.putInt("currentTerm",termLayoutNumber);
                fragment8.setArguments(bundle);

                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment8,"8").commit();
                break;
        }
//        new ShowCourses(detailedCourses, termLayoutNumber, activity, view);
    }

    // for math condition and M251 , if any of these found it return found array .
    private ArrayList<Courses> findOccurrencesInArray(List<Courses> detailedCourses) {

        ArrayList<Courses> found = new ArrayList<>();

        Courses[] values = {new Courses("MT132"), new Courses("MT131"), new Courses("MT129"), new Courses("M251")};

        for (Courses value : values) {
            for (int i = 0; i < detailedCourses.size(); i++) {
                if (detailedCourses.get(i).getCourseCode().equals(value.getCourseCode())) {
                    found.add(detailedCourses.get(i));
                }
            }
        }

        return found;

    }

}
