package com.heshamapps.srrs.logic;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.heshamapps.srrs.models.Courses;
import com.heshamapps.srrs.student.pastCoursesFragment;
import com.heshamapps.srrs.util.MyCallback2;


class GetYearHours {
    private FirebaseFirestore fireStore;
    private int totalInProgressHours =0 ;
    private int totalNotTakenHours=0;
    private int totalPassedHours=0;

    GetYearHours() {
        fireStore = FirebaseFirestore.getInstance();
    }


    void readData(MyCallback2 myCallback) {

        fireStore.collection("courses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(task.getResult()!=null){
                    Courses myObject = document.toObject(Courses.class);
                    if(myObject.getStatus().contains("inProgress")){
                        totalInProgressHours=totalInProgressHours+myObject.getCourseHours();
                    }
                    else if(myObject.getStatus().contains("notTaken")){
                        totalNotTakenHours=totalNotTakenHours+myObject.getCourseHours();
                    }
                    else if(myObject.getStatus().contains("passed")){
                        totalPassedHours=totalPassedHours+myObject.getCourseHours();
                    }
                    }
                }
                myCallback.onCallback(totalPassedHours);
            }
        });
    }

}
