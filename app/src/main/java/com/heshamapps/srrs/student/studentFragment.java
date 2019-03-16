package com.heshamapps.srrs.student;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
import java.util.Locale;

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


    @BindView(R.id.firstTermTotalCreditHoursSelectedVal)
    TextView firstTermTotalCreditHoursSelectedVal;


    @BindView(R.id.secondTermTotalCreditHoursSelectedVal)
    TextView secondTermTotalCreditHoursSelectedVal;


    @BindView(R.id.summerTermTotalCreditHoursSelectedVal)
    TextView summerTermTotalCreditHoursSelectedVal;


    @BindView(R.id.first_next)
    Button first_next;

    @BindView(R.id.second_next)
    Button second_next;

    @BindView(R.id.second_save)
    Button second_save;

    @BindView(R.id.summer_next)
    Button summer_next;

    @BindView(R.id.first_save)
    Button first_save;

    @BindView(R.id.summer_save)
    Button summer_save;

    @BindView(R.id.first_gpa)
    EditText first_gpa;

    @BindView(R.id.second_gpa)
    EditText second_gpa;

    @BindView(R.id.summer_gpa)
    EditText summer_gpa;


    @BindView(R.id.button3)
    Button button3;


    ArrayList<String> totalPostCoursesList = new ArrayList<String>();

    String gpaLimit;

    public studentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        ButterKnife.bind(this, view);
        db = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(getActivity());

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            showCourses((ArrayList<Courses>) bundle.getSerializable("EL097"), 0);
        }


        return view;
    } // end  onCreate();


    // for math condition and M251 , if any of these found it return found array .
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

        // sort the list by comparing course hours
        Collections.sort(detailedCourses, new compareCourses());

        ArrayList<Courses> matchedArray = findOccurrencesInArray(detailedCourses);
// this will make these courses found the rule to top of list 
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
            courseBTN.setText(String.format(Locale.ENGLISH, "%s(%d)", detailedCourses.get(i).getCourseCode(), detailedCourses.get(i).getCourseHours()));
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
        for (int i = 0; i < takenCoursesSelected.size(); i++) {

            int finalI = i;
            db.collection("courses").document(takenCoursesSelected.get(i)).get().addOnSuccessListener(documentSnapshot -> {

                if (documentSnapshot.exists()) {
                    if (finalI == takenCoursesSelected.size() - 1)
                        myCallback.onCallback((ArrayList<String>) documentSnapshot.get("postCourses"), true);
                    else
                        myCallback.onCallback((ArrayList<String>) documentSnapshot.get("postCourses"), false);
                }
            }).addOnFailureListener(e -> Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show());

        }


    }


    boolean checkRules(ArrayList<String> sourceList) {


        ArrayList<String> mathFound = new ArrayList<>();
        ArrayList<String> programmingFound = new ArrayList<>();


        String[] mathValues = {"MT132", "MT131", "MT129"};
        String[] programmingValues = {"M251", "TM105"};

        for (String value : mathValues) {
            for (int i = 0; i < sourceList.size(); i++) {
                if (sourceList.get(i).equals(value)) {
                    mathFound.add(sourceList.get(i));
                }
            }
        }
        for (String value : programmingValues) {
            for (int i = 0; i < sourceList.size(); i++) {
                if (sourceList.get(i).equals(value)) {
                    programmingFound.add(sourceList.get(i));
                }
            }
        }


        return mathFound.size() >= 2 || programmingFound.size() >= 2;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        first_next.setOnClickListener(v -> next(((RelativeLayout) v.getParent().getParent())));

        second_next.setOnClickListener(v -> next(((RelativeLayout) v.getParent().getParent())));

        summer_next.setOnClickListener(v -> next(((RelativeLayout) v.getParent().getParent())));

        first_save.setOnClickListener(v -> save(((RelativeLayout) v.getParent().getParent())));

        second_save.setOnClickListener(v -> save(((RelativeLayout) v.getParent().getParent())));

        summer_save.setOnClickListener(v -> save(((RelativeLayout) v.getParent().getParent())));
    } //end of onActivityCreated

    void save(RelativeLayout parent) {


        if(checkGPA(parent)){
             Toasty.error(getActivity(), "You must choose courses limit to " +gpaLimit+ "credit Hours ", Toast.LENGTH_SHORT).show();
        }
        else {
            WriteBatch batch = db.batch();
            for (int i = 0; i < ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildCount(); i++) {
                View view = secondTermChipsLinerLayout.getChildAt(i);

                if (((CheckBox) view).isChecked()) {
                    Toasty.info(getActivity(), ((CheckBox) view).getText().toString(), Toast.LENGTH_SHORT).show();

                    DocumentReference sfRef = db.collection("courses").document(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                    batch.update(sfRef, "status", "inProgress");
                }


            }

            // Commit the batch
            batch.commit().addOnCompleteListener(task -> Toasty.info(getActivity(), "Courses registered to current semester", Toast.LENGTH_SHORT).show());
        }
    }


    /* TODO: I should make courses as taken status + create alertDilog for math */
    void next(RelativeLayout parent) {

        if(checkGPA(parent)){
            Toasty.error(getActivity(), "You must choose courses limit to " +gpaLimit+ "credit Hours ", Toast.LENGTH_SHORT).show();

        }
        else {
            ArrayList<String> coursesSelected = new ArrayList<String>();
            ArrayList<String> coursesNotSelected = new ArrayList<String>();


            for (int i = 0; i < ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildCount(); i++) {
                View view = ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildAt(i);

                if (((CheckBox) view).isChecked()) {
                    Toasty.info(getActivity(), ((CheckBox) view).getText().toString(), Toast.LENGTH_SHORT).show();
                    coursesSelected.add(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                } else {
                    coursesNotSelected.add(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                }

            }
            totalPostCoursesList.clear();


            if (checkRules(coursesSelected))
                Toasty.info(getActivity(), "note you are chooses two courses math or programming together ", Toast.LENGTH_SHORT).show();


            //Move TO NEXT
            // get all check box postCourses details from data base
            getCourses(coursesSelected, (singlePostCoursesList, isArrayCompleted) -> {
                // add the result array of curses to array that will hold courses to show
                totalPostCoursesList.addAll(singlePostCoursesList);

                if (isArrayCompleted) {
                    // add the not selected courses array to array that will hold courses to show
                    totalPostCoursesList.addAll(coursesNotSelected);

                    // to get hours for courses to be used in next layout
                    getCoursesDetails(totalPostCoursesList, detailedCourses ->

                            // Rank
                            rank(detailedCourses, Integer.valueOf(((ScrollView) parent.getChildAt(0)).getChildAt(0).getTag().toString())));
                }
            });
        }
    }

    boolean checkGPA(RelativeLayout parent){

        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            incrementTV(buttonView.getText().toString(), Integer.parseInt((((View) buttonView.getParent()).getTag()).toString()));
        } else {
            decrementTV(buttonView.getText().toString(), Integer.parseInt((((View) buttonView.getParent()).getTag()).toString()));

        }
    }


    public void incrementTV(String sourceCourseHours, int termLayoutNumber) {

        String courseHours = sourceCourseHours.replaceAll(".*\\(|\\).*", "");

        switch (termLayoutNumber) {
            case 1:
                firstTermTotalCreditHoursSelectedVal.setText(String.valueOf(Integer.parseInt(firstTermTotalCreditHoursSelectedVal.getText().toString()) + Integer.parseInt(courseHours)));
                break;
            case 2:
                secondTermTotalCreditHoursSelectedVal.setText(String.valueOf(Integer.parseInt(secondTermTotalCreditHoursSelectedVal.getText().toString()) + Integer.parseInt(courseHours)));
                break;
            case 3:
                summerTermTotalCreditHoursSelectedVal.setText(String.valueOf(Integer.parseInt(summerTermTotalCreditHoursSelectedVal.getText().toString()) + Integer.parseInt(courseHours)));
                break;
        }


        //  .setText(firstTermTotalCreditHoursSelected.getText()+ courseHours.replaceAll("\\(.*?\\)", ""));
    }


    public void decrementTV(String sourceCourseHours, int termLayoutNumber) {

        String courseHours = sourceCourseHours.replaceAll(".*\\(|\\).*", "");

        switch (termLayoutNumber) {
            case 1:
                firstTermTotalCreditHoursSelectedVal.setText(String.valueOf(Integer.parseInt(firstTermTotalCreditHoursSelectedVal.getText().toString()) - Integer.parseInt(courseHours)));
                break;
            case 2:
                secondTermTotalCreditHoursSelectedVal.setText(String.valueOf(Integer.parseInt(secondTermTotalCreditHoursSelectedVal.getText().toString()) - Integer.parseInt(courseHours)));
                break;
            case 3:
                summerTermTotalCreditHoursSelectedVal.setText(String.valueOf(Integer.parseInt(summerTermTotalCreditHoursSelectedVal.getText().toString()) - Integer.parseInt(courseHours)));
                break;
        }


        //  .setText(firstTermTotalCreditHoursSelected.getText()+ courseHours.replaceAll("\\(.*?\\)", ""));
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
