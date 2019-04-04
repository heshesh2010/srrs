package com.heshamapps.srrs.logic;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.heshamapps.srrs.models.Courses;
import com.heshamapps.srrs.student.pastCoursesFragment;
import com.heshamapps.srrs.util.MyCallback2;


public class GetYearHours {
    private FirebaseFirestore firestore;
    int totalInProgress =0 ;
    int totalNotTaken=0;
    int totalPassed=0;

    public GetYearHours() {
        firestore = FirebaseFirestore.getInstance();
    }


    public void readData(MyCallback2 myCallback) {

        firestore.collection("courses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Courses myObject = document.toObject(Courses.class);
                    if(myObject.getStatus().contains("inProgress")){
                        totalInProgress=totalInProgress+myObject.getCourseHours();
                    }
                    else if(myObject.getStatus().contains("notTaken")){
                        totalNotTaken=totalNotTaken+myObject.getCourseHours();
                    }
                    else if(myObject.getStatus().contains("passed")){
                        totalPassed=totalPassed+myObject.getCourseHours();
                    }

                }
                myCallback.onCallback(totalPassed);
            }
        });
    }

}
