package com.heshamapps.srrs.logic;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.heshamapps.srrs.models.Courses;

public class UpdateStatus {

    private FirebaseFirestore db;

    public UpdateStatus() {
        db = FirebaseFirestore.getInstance();


       db.collection("courses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Courses course = document.toObject(Courses.class);
                        course.setStatus("notTaken");
                        String id = document.getId();
                        db.collection("courses").document(id).set(course);
                    }
                }
            }
        });



    }
}
