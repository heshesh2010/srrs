package com.heshamapps.srrs.models;

import java.util.Comparator;

public class compareCourses implements Comparator<Courses> {


    @Override
    public int compare(Courses o1, Courses o2) {
        return Integer.compare(o1.getCoursesCount(), o2.getCoursesCount());
    }


    public Courses compare2(Courses o1, Courses o2) {

        if (o1.getCourseCode().equals("MT132")) {
            return o1;
        }
        else{
            return o1;

        }
    }
}
