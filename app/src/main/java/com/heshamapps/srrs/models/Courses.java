package com.heshamapps.srrs.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Courses  implements Serializable ,Comparable {

    private String courseCode;
    private String courseName;
    private String courseType;

    public String getStatus() {
        return status;
    }

    private String status;
    private int courseHours  ,level , coursesCount;
    private ArrayList<String> postCourses;

    public Courses( ) {
    }

    public Courses(String courseCode, String courseName, String courseType, int courseHours,ArrayList<String> postCourses,int coursesCount) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseType = courseType;
        this.courseHours = courseHours;
        this.postCourses=postCourses;
        this.coursesCount =coursesCount;
    }

    public Courses(String courseCode) {
        this.courseCode = courseCode;
    }
    public int getLevel() {
        return level;
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getCourseType() {
        return this.courseType;
    }

    public int getCourseHours() {
        return this.courseHours;
    }

    public ArrayList<String> getPostCourses() {
        return this.postCourses;
    }

    public int getCoursesCount() {
        return this.coursesCount;
    }

    @Override
    public int compareTo(Object o) {
        
        return 0;
    }
}
