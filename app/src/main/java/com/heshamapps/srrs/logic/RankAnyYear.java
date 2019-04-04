package com.heshamapps.srrs.logic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.heshamapps.srrs.R;
import com.heshamapps.srrs.models.Courses;
import com.heshamapps.srrs.models.compareCourses;
import com.heshamapps.srrs.student.FirstYearFragment;

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

        switch (yearNumber) {
            case 1:


                FirstYearFragment fragment = new FirstYearFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("AnyYear", (Serializable) detailedCourses);

                fragment.setArguments(bundle);

                activity.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment,String.valueOf(termLayoutNumber)).commit();

                break;
            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;

            case 6 :
                break;

            case 7 :
                break;

            case 8 :
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
