package com.heshamapps.srrs.logic;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.heshamapps.srrs.models.Courses;
import com.heshamapps.srrs.util.MyCallback5;

import java.util.ArrayList;

public class GetAllCourses {

    private FirebaseFirestore db;
    private ArrayList<Courses> AllCourses= new ArrayList<>();

    public GetAllCourses() {
        db = FirebaseFirestore.getInstance();

    }

    public void getAllCourses(MyCallback5 myCallBack){
        db.collection("courses").get().addOnCompleteListener(task -> {
            if(task.isComplete()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Courses course = document.toObject(Courses.class);
                    AllCourses.add(course);
                }

                myCallBack.callGetAllCourses(AllCourses);
            }
        });


    }
}
