package com.heshamapps.srrs.logic;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import es.dmoral.toasty.Toasty;

public class SaveAsInProgress {

    private FirebaseFirestore db;
    int allowableCreditHours;

    public SaveAsInProgress(RelativeLayout parent, Activity activity) {
        db = FirebaseFirestore.getInstance();
        // check if GPA value is Empty
        if (((EditText) ((LinearLayout) parent.getChildAt(1)).getChildAt(2)).getText().toString().isEmpty()) {
            Toasty.error(activity, "Enter your GPA", Toast.LENGTH_SHORT).show();
        } else {
            int Hours = Integer.parseInt(((TextView) ((LinearLayout) parent.getChildAt(1)).getChildAt(1)).getText().toString());
            allowableCreditHours = new CheckGPA().CheckGPA(parent);
            if (Hours > allowableCreditHours) {
                Toasty.error(activity, "You must choose courses limit to " + allowableCreditHours + "credit Hours ", Toast.LENGTH_SHORT).show();
            } else {
                WriteBatch batch = db.batch();
                for (int i = 0; i < ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildCount(); i++) {
                    View view = ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildAt(i);

                    if (((CheckBox) view).isChecked()) {
                        Toasty.info(activity, ((CheckBox) view).getText().toString(), Toast.LENGTH_SHORT).show();

                        DocumentReference sfRef = db.collection("courses").document(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                        batch.update(sfRef, "status", "inProgress");
                    }


                }

                // Commit the batch
                batch.commit().addOnCompleteListener(task -> Toasty.info(activity, "Courses registered to current semester", Toast.LENGTH_SHORT).show());
            }
        }
    }
}

