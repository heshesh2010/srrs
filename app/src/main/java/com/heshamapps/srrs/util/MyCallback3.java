package com.heshamapps.srrs.util;

import com.heshamapps.srrs.models.Courses;

import java.util.Map;

public interface MyCallback3 {

    void callGetCoursePreReq(Map<Courses, String> preReq, boolean b);
}
