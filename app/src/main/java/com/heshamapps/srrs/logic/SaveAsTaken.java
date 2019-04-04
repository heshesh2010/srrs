package com.heshamapps.srrs.logic;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.heshamapps.srrs.models.Courses;

import java.util.ArrayList;
import java.util.List;

public class SaveAsTaken {

    private FirebaseFirestore db;

    public SaveAsTaken() {
        db = FirebaseFirestore.getInstance();

    }

    // to update in database status to takken
   public void saveTaken(RelativeLayout parent) {


        WriteBatch batch = db.batch();
        for (int i = 0; i < ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildCount(); i++) {
            View view = ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildAt(i);

            if (((CheckBox) view).isChecked()) {

                DocumentReference sfRef = db.collection("courses").document(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                batch.update(sfRef, "status", "passed");
            }


        }

        // Commit the batch
        batch.commit();
    }


    // to update in database status to taken
    public void saveAnyCourses(List<Courses> course) {


        WriteBatch batch = db.batch();
        for (int i = 0; i < course.size(); i++) {


                DocumentReference sfRef = db.collection("courses").document(course.get(i).getCourseCode());
                batch.update(sfRef, "status", "passed");
            }


        // Commit the batch
        batch.commit();
    }

}
