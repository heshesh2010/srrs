package com.heshamapps.srrs.student;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.heshamapps.srrs.R;
import com.heshamapps.srrs.models.Courses;
import com.heshamapps.srrs.models.compareCourses;
import com.heshamapps.srrs.util.MyCallback;
import com.heshamapps.srrs.util.MyCallback2;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class studentFragment extends Fragment implements OnCheckedChangeListener {

    FirebaseFirestore db;

    @BindView(R.id.firstTermChipsLinerLayout)
    LinearLayout firstTermChipsLinerLayout;

    @BindView(R.id.secondTermChipsLinerLayout)
    LinearLayout secondTermChipsLinerLayout;

    @BindView(R.id.summerTermChipsLinerLayout)
    LinearLayout summerTermChipsLinerLayout;

    @BindView(R.id.firstTermTotalCreditHoursSelected)
    TextView firstTermTotalCreditHoursSelected;

    @BindView(R.id.first_next)
    Button first_next;

    @BindView(R.id.second_next)
    Button second_next;

    @BindView(R.id.second_save)
    Button second_save;

    @BindView(R.id.button3)
    Button button3;

    int termLayoutNumber = 0;

    ArrayList<String> totalPostCoursesList = new ArrayList<String>();


    public studentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           /* mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(getActivity());

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            showCourses((ArrayList<Courses>) bundle.getSerializable("EL097"), 0);
        }


        return view;
    } // end  onCreate();


    // for math condition and M251
    ArrayList<Courses> findOccurrencesInArray(ArrayList<Courses> detailedCourses) {

        ArrayList<Courses> found = new ArrayList<Courses>();

        Courses[] values = {new Courses("MT132"), new Courses("MT131"), new Courses("MT129"), new Courses("M251")};

        for (Courses value : values) {
            for (int i = 0; i < detailedCourses.size(); i++) {
                if (detailedCourses.get(i).getCourseCode().equals(value.getCourseCode())) {
                    found.add(detailedCourses.get(i));
                }
            }
        }

        return found;
    }

    // this will take list of courses that should open , then it should rank theme in a list
    void rank(ArrayList<Courses> detailedCourses, int termLayoutNumber) {

        Collections.sort(detailedCourses, new compareCourses());

        ArrayList<Courses> matchedArray = findOccurrencesInArray(detailedCourses);

        for (int i = 0; i < matchedArray.size(); i++) {

            detailedCourses.remove(matchedArray.get(i));
            detailedCourses.add(matchedArray.get(i));
        }

        Collections.reverse(detailedCourses);
        showCourses(detailedCourses, termLayoutNumber);
    }


    void showCourses(ArrayList<Courses> detailedCourses, int termLayoutNumber) {


        // Get courseHours for every course and show in next term or level
        for (int i = 0; i < detailedCourses.size(); i++) {
            CheckBox courseBTN = new CheckBox(getActivity());
            courseBTN.setOnCheckedChangeListener(studentFragment.this);
            courseBTN.setText(detailedCourses.get(i).getCourseCode() + "(" + detailedCourses.get(i).getCourseHours() + ")");
            switch (1 + termLayoutNumber) {
                case 1:
                    firstTermChipsLinerLayout.setTag("1");
                    firstTermChipsLinerLayout.addView(courseBTN);
                    break;
                case 2:
                    secondTermChipsLinerLayout.setTag("2");
                    secondTermChipsLinerLayout.addView(courseBTN);
                    break;
                case 3:
                    summerTermChipsLinerLayout.setTag("3");
                    ;
                    summerTermChipsLinerLayout.addView(courseBTN);
                    break;
                // 4  move to next activity
                default:
                    throw new IllegalArgumentException("Invalid day of the week: " + termLayoutNumber);
            }
        }
    }


    void getCoursesDetails(ArrayList<String> openedCourses, MyCallback2 myCallback2) {

        // Get full details of every opened course

        db.collection("courses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Courses> finalList = new ArrayList<Courses>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    for (String courseCode : openedCourses) {
                        if (courseCode.equals(document.toObject(Courses.class).getCourseCode())) {
                            finalList.add(document.toObject(Courses.class));
                        }
                    }
                }
                myCallback2.onCallback(finalList);
            }
        });


    }

// this method getCourses should get courses selected data that will open next semseter
    void getCourses(ArrayList<String> takenCoursesSelected, MyCallback myCallback) {

        // for each loop and wait every round to get each course post data
        for (int i = 0 ; i<takenCoursesSelected.size();i++){

            int finalI = i;
            db.collection("courses").document(takenCoursesSelected.get(i)).get().addOnSuccessListener(documentSnapshot -> {

                if (documentSnapshot.exists()) {
                    if(finalI ==takenCoursesSelected.size()-1)
                        myCallback.onCallback((ArrayList<String>) documentSnapshot.get("postCourses"),true);
                    else
                        myCallback.onCallback((ArrayList<String>) documentSnapshot.get("postCourses"), false);
                }
            }).addOnFailureListener(e -> Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show());

        }




        }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        first_next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                ArrayList<String> coursesSelected = new ArrayList<String>();
                ArrayList<String> coursesNotSelected = new ArrayList<String>();


                for (int i = 0; i < firstTermChipsLinerLayout.getChildCount(); i++) {
                    View view = firstTermChipsLinerLayout.getChildAt(i);

                    if (((CheckBox) view).isChecked()) {
                        Toasty.info(getActivity(), ((CheckBox) view).getText().toString(), Toast.LENGTH_SHORT).show();
                        coursesSelected.add(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                    }
                    else{
                        coursesNotSelected.add(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                    }

                }
                totalPostCoursesList.clear();

                // get all check box postCourses details from data base
                getCourses(coursesSelected, (singlePostCoursesList,isArrayCompleted) -> {
                    totalPostCoursesList.addAll(singlePostCoursesList);

                    if (isArrayCompleted) {
                        // send not selected course to rank again
                        totalPostCoursesList.addAll(coursesNotSelected);
                        getCoursesDetails(totalPostCoursesList, detailedCourses ->
                                rank(detailedCourses, Integer.valueOf(firstTermChipsLinerLayout.getTag().toString())));
                    }
                });


            }

        });

        second_next.setOnClickListener(v -> {

            ArrayList<String> coursesSelected = new ArrayList<>();
            ArrayList<String> coursesNotSelected = new ArrayList<>();


            for (int i = 0; i < secondTermChipsLinerLayout.getChildCount(); i++) {
                View view = secondTermChipsLinerLayout.getChildAt(i);

                if (((CheckBox) view).isChecked()) {
                    Toasty.info(getActivity(), ((CheckBox) view).getText().toString(), Toast.LENGTH_SHORT).show();
                    coursesSelected.add(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                }
                else{
                    coursesNotSelected.add(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                }


            }
            totalPostCoursesList.clear();
            // get all check box postCourses
            getCourses(coursesSelected, (settings, isArrayCompleted) -> {
                totalPostCoursesList.addAll(settings);

                if (isArrayCompleted) {

                    totalPostCoursesList.addAll(coursesNotSelected);

                    getCoursesDetails(totalPostCoursesList, detailedCourses -> rank(detailedCourses, Integer.valueOf(secondTermChipsLinerLayout.getTag().toString())));
                }
            });

// to do : save to success table
        });

        // to update in progress
        second_save.setOnClickListener(v -> {

            WriteBatch batch = db.batch();


            for (int i = 0; i < secondTermChipsLinerLayout.getChildCount(); i++) {
                View view = secondTermChipsLinerLayout.getChildAt(i);

                if (((CheckBox) view).isChecked()) {
                    Toasty.info(getActivity(), ((CheckBox) view).getText().toString(), Toast.LENGTH_SHORT).show();

                    DocumentReference sfRef = db.collection("courses").document(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                    batch.update(sfRef, "status", "inProgress");
                }


            }

            // Commit the batch
            batch.commit().addOnCompleteListener(task -> Toasty.info(getActivity(), "Courses registered to current semester", Toast.LENGTH_SHORT).show());

        });


        }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            updateTV(buttonView.getText().toString(), Integer.valueOf((((View) buttonView.getParent()).getTag()).toString()));
        } else {
            //    StringArray.remove(StringArray.indexOf(buttonView.getText().toString()));
        }
    }

    public void updateTV(String courseHours, int termLayoutNumber) {

        courseHours.replaceAll(".*\\(|\\).*", "");
        //   firstTermTotalCreditHoursSelected.setText(firstTermTotalCreditHoursSelected.getText()+ courseHours.replaceAll("\\(.*?\\)", ""));
    }


}
