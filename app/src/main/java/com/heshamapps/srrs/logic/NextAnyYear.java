package com.heshamapps.srrs.logic;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.heshamapps.srrs.models.Courses;

import java.util.ArrayList;
import java.util.List;


public class NextAnyYear {
    private ArrayList<String> coursePassedArray = new ArrayList<>();
    private ArrayList<String> holdCoursesArray = new ArrayList<>();
    private ArrayList<Courses> AllCourses = new ArrayList<>();
    private ArrayList<Courses> found = new ArrayList<>();
    private ArrayList<Courses> finalPassedArray = new ArrayList<>();
    private ArrayList<String> postCourses = new ArrayList<>();
    private ArrayList<String> preCourses = new ArrayList<>();
    public NextAnyYear() {
        clearAllArrays();
    }

    private void clearAllArrays() {

        AllCourses.clear();
        finalPassedArray.clear();
        found.clear();
        postCourses.clear();
        preCourses.clear();
        holdCoursesArray.clear();
    }

    public void do_next(int semsterNumber, int year, ArrayList<Courses> coursesSelected, List<String> coursesNotSelected, Activity activity, View view) {
        new GetAllCourses().getAllCourses(allCourses -> {

            // get selected courses
            for (Courses value : coursesSelected) {
                for (int i = 0; i < allCourses.size(); i++) {
                    if (allCourses.get(i).getCourseCode().equals(value.getCourseCode())) {
                        found.add(allCourses.get(i));
                    }
                }
            }

            Log.d("tag", String.valueOf(found.size()));
            for (int i = 0; i < found.size(); i++) {


                for (int y = 0; y < found.get(i).getPostCourses().size(); y++) {

                    postCourses.addAll(found.get(i).getPostCourses());
                    postCourses.addAll(holdCoursesArray);


                    for (Courses courses : allCourses) {
                        if (courses.getCourseCode().equals(found.get(i).getPostCourses().get(y))) {
                            preCourses.addAll(courses.getPreReq());
                            break;
                        }
                    }


                    /**
                     * must take segment of two then two */
                    boolean result = true;

                    for (int n = 0; n < preCourses.size(); n++) {

                        for (Courses courses2 : allCourses) {
                            if (courses2.getCourseCode().equals(preCourses.get(n))) {

                                if (!courses2.getStatus().equals("passed")) {
                                    result = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (result) {
                        coursePassedArray.add(found.get(i).getPostCourses().get(y));
                        Log.d("pass", found.get(i).getPostCourses().get(y));
                    } else {
                        holdCoursesArray.add(found.get(i).getPostCourses().get(y));
                        Log.d("hold", found.get(i).getPostCourses().get(y));

                    }
                    preCourses.clear();


                }// end of pre

            } // end of post
            postCourses.clear();


            coursePassedArray.addAll(coursesNotSelected);

            for (int n = 0; n < coursePassedArray.size(); n++) {

                for (Courses courses : allCourses) {
                    if (courses.getCourseCode().equals(coursePassedArray.get(n))) {
                        finalPassedArray.add(courses);
                        break;
                    }
                }
            }

                new RankAnyYear(finalPassedArray, semsterNumber,year, activity, view);

        });
    }


}
