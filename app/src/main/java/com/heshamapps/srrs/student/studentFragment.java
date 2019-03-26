package com.heshamapps.srrs.student;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.heshamapps.srrs.R;
import com.heshamapps.srrs.models.Courses;
import com.heshamapps.srrs.models.compareCourses;
import com.heshamapps.srrs.util.MyCallback;
import com.heshamapps.srrs.util.MyCallback2;
import com.heshamapps.srrs.util.MyCallback3;
import com.heshamapps.srrs.util.MyCallback4;
import com.heshamapps.srrs.util.MyCallback5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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


    ArrayList<String> coursePassedArray = new ArrayList<String>();
    ArrayList<String> holdCoursesArray = new ArrayList<String>();
    ArrayList<String> statusArray = new ArrayList<String>();
    ArrayList<String> coursesSelected = new ArrayList<String>();
    ArrayList<String> coursesNotSelected = new ArrayList<String>();
    ArrayList<String> singlePostCoursesListArray = new ArrayList<String>();
    Map<Courses, String> hesho = new HashMap<>();
    Map<Courses, String> meho = new HashMap<Courses, String>();
    ArrayList<Courses>AllCourses= new ArrayList<Courses>();


    int allowableCreditHours;

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
    } // end  onCreate


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


    void getPostCoursesHours(ArrayList<String> openedCourses, MyCallback myCallback2) {

        // Get full details of every openinig course

        db.collection("courses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Courses> finalList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    for (String courseCode : openedCourses) {
                        if (courseCode.equals(document.toObject(Courses.class).getCourseCode())) {
                            finalList.add(document.toObject(Courses.class));
                        }
                    }
                }
                myCallback2.callGetPostCoursesHours(finalList);
            }
        });


    }

    // this method getCourses should get courses selected data that will open next semseter
    void getPostCourses(ArrayList<String> takenCoursesSelected, MyCallback2 myCallback) {

        // for each loop and wait every round to get each course post data
        for (int i = 0; i < takenCoursesSelected.size(); i++) {

            int finalI = i;
            db.collection("courses").document(takenCoursesSelected.get(i)).get().addOnCompleteListener(task -> {

                if (task.isComplete()) {
                    DocumentSnapshot documentSnapshot= task.getResult();
                    ArrayList<String> h= (ArrayList<String>) documentSnapshot.get("postCourses");
                    while(h != null && h.contains("")) {
                        h.remove("");
                    }
                    if (finalI == takenCoursesSelected.size() - 1)
                        myCallback.CallGetPostCourses((ArrayList<String>) documentSnapshot.get("postCourses"), true);
                    else
                        myCallback.CallGetPostCourses(h, false);


                }
                else{
                    Toasty.error(getActivity(), "error", Toast.LENGTH_LONG).show();
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

        first_save.setOnClickListener(v -> saveInProgress(((RelativeLayout) v.getParent().getParent())));

        second_save.setOnClickListener(v -> saveInProgress(((RelativeLayout) v.getParent().getParent())));

        summer_save.setOnClickListener(v -> saveInProgress(((RelativeLayout) v.getParent().getParent())));
    } //end of onActivityCreated


    // to update in database status to in progress
    void saveInProgress(RelativeLayout parent) {

        int Hours = Integer.parseInt(((TextView)((LinearLayout)parent.getChildAt(1)).getChildAt(1)).getText().toString());

        if(Hours>checkGPA(parent)){
            Toasty.error(getActivity(), "You must choose courses limit to " +allowableCreditHours+ "credit Hours ", Toast.LENGTH_SHORT).show();
        }
        else {
            WriteBatch batch = db.batch();
            for (int i = 0; i < ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildCount(); i++) {
                View view = ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildAt(i);

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


    // to update in database status to takken
    void saveTakken(RelativeLayout parent) {




        WriteBatch batch = db.batch();
        for (int i = 0; i < ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildCount(); i++) {
            View view = ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildAt(i);

            if (((CheckBox) view).isChecked()) {
                Toasty.info(getActivity(), ((CheckBox) view).getText().toString(), Toast.LENGTH_SHORT).show();

                DocumentReference sfRef = db.collection("courses").document(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                batch.update(sfRef, "status", "passed");
            }


        }

        // Commit the batch
        batch.commit().addOnCompleteListener(task -> Toasty.info(getActivity(), "Courses registered to pass", Toast.LENGTH_SHORT).show());
    }


    // to move next to following semester and update courses in data base to takken
    void next(RelativeLayout parent) {
        coursePassedArray.clear();
        holdCoursesArray.clear();
        statusArray.clear();
        coursesSelected.clear();
        coursesNotSelected.clear();
        singlePostCoursesListArray.clear();

        // get selected course hours from textview
        int Hours = Integer.parseInt(((TextView)((LinearLayout)parent.getChildAt(1)).getChildAt(1)).getText().toString());

        // check if GPA value is Empty
        if(((EditText)((LinearLayout)parent.getChildAt(1)).getChildAt(2)).getText().toString().isEmpty()){
            Toasty.error(getActivity(), "Enter your GPA", Toast.LENGTH_SHORT).show();
        }

        // check GPA is not allowed show error
        else if(Hours>checkGPA(parent)){
            Toasty.error(getActivity(), "You must choose courses limit to " +allowableCreditHours+ "credit Hours ", Toast.LENGTH_SHORT).show();
        }
        else {



            for (int i = 0; i < ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildCount(); i++) {
                View view = ((LinearLayout) ((ScrollView) parent.getChildAt(0)).getChildAt(0)).getChildAt(i);

                if (((CheckBox) view).isChecked()) {

                    coursesSelected.add(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                } else {
                    coursesNotSelected.add(((CheckBox) view).getText().toString().replaceAll("\\(.*?\\)", "").toUpperCase());
                }

            }

            if (checkRules(coursesSelected)) { // if there is math or progtamming show daliog

                new AlertDialog.Builder(getActivity())
                        .setTitle("Alert")
                        .setMessage("Are you sure you want to chooses two courses math or programming together?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                            moveToNext(parent, coursesSelected , coursesNotSelected );



                        }) // end of setPositiveButton button





                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            } // end of checkRules IF

            else{ // else if there is no reuls for math or programming found

                moveToNext(parent, coursesSelected, coursesNotSelected);


            }// end of else
        }
    }


   void getAllCourses(MyCallback5 myCallBack){
        db.collection("courses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isComplete()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Courses course = document.toObject(Courses.class);
                        AllCourses.add(course);
                    }

                    myCallBack.callGetAllCourses(AllCourses);
                }
            }
        });


    }


    //Move TO NEXT
    void moveToNext(RelativeLayout parent, ArrayList<String> coursesSelected, ArrayList<String> coursesNotSelected){

        // Get all checked courses and save it in database to 'pass' status
        saveTakken(parent);



        getAllCourses(courses -> Log.d("AllCourses",String.valueOf(courses.size())));











        // Get Post courses from database based on selected courses
        getPostCourses(coursesSelected, (singlePostCoursesList, isArrayPostCoursesCompleted) -> {

            singlePostCoursesListArray.addAll(singlePostCoursesList);

            // wait until get all post courses
            if (isArrayPostCoursesCompleted) {

                /** contain 5 courses */
                Log.d("PostCoursesListArray",String.valueOf(singlePostCoursesListArray.size()));

                // add the not selected courses array to array that will hold courses to show
                singlePostCoursesListArray.addAll(coursesNotSelected);

                // Get  preRequest courses .. tak 5 courses and return each reound of pre post courses as list
                getCoursePreReq(singlePostCoursesListArray, (preReqList, isArrayPreCoursesCompleted1) -> {

                    Log.d("preReqList",String.valueOf(preReqList.size()));
                    hesho.putAll(preReqList);
                    if(isArrayPreCoursesCompleted1)
                    {



                    // Get status of preRequest course
                    // take each segment list and return ststus as signal then save it to array
                        Log.d("hesho",hesho.entrySet().toString());

                        for (Map.Entry<Courses, String> entry : hesho.entrySet()) {


                    getCoursePreReqStatus(entry.getKey().getPreReq(), (status, s, isQueryComplete) -> {


                        /**** should first get status and add to array then ater complete check if all element is passed if true then add otheriese hold ****/
                        statusArray.add(status);

                        // wait until get status of preRequest course
                        if(isQueryComplete) {
                            boolean result = true;
                            for (String preReqStatus : statusArray) {
                                if (!preReqStatus.contains("passed")) {
                                    result = false;
                                    break;
                                }
                            }
                            statusArray.clear();
                            if(result){
                                coursePassedArray.add(entry.getValue());
                                Log.d("pass",entry.getValue());
                            }
                            else{
                                holdCoursesArray.add(entry.getValue());
                                Log.d("hold",entry.getValue());

                            }
                        }
                    });


                    // if map last index
                    }
                    }

                    Log.d("pass",String.valueOf(coursePassedArray.size()));

                        getPostCoursesHours(coursePassedArray, (finalList) ->
                                {

                                    rank(finalList, Integer.valueOf(((ScrollView) parent.getChildAt(0)).getChildAt(0).getTag().toString()));
                                });


                });





            }
        });

    }



    //
    private void getCoursePreReq(ArrayList<String> totalPostCoursesList, MyCallback3 myCallback) {

        // for each loop and wait every round to get each course pre data
        for (int i = 0; i < totalPostCoursesList.size(); i++) {

            int finalI = i;
            db.collection("courses").document(totalPostCoursesList.get(i)).get().addOnCompleteListener(task -> {

                if (task.isComplete()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    Courses course = documentSnapshot.toObject(Courses.class);

                    meho.put((course),totalPostCoursesList.get(finalI));

                    if (finalI == totalPostCoursesList.size() - 1)
                        myCallback.callGetCoursePreReq(meho, true);
                    else
                        myCallback.callGetCoursePreReq(meho, false);


                }
                else{
                    Toasty.error(getActivity(), "error", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(e -> Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show());

        }

    }



    private void getCoursePreReqStatus(ArrayList<String> totalPostCoursesList, MyCallback4 myCallback) {


        // for each loop and wait every round to get each course post data
        for (int i = 0; i < totalPostCoursesList.size(); i++) {
            int finalI = i;

            if (totalPostCoursesList.get(i).isEmpty()) {

                if (finalI == totalPostCoursesList.size() - 1)
                    myCallback.callGetCoursePreReqStatus("", totalPostCoursesList.get(i), true);
                else
                    myCallback.callGetCoursePreReqStatus("", totalPostCoursesList.get(i), false);


            } else {
                db.collection("courses").document(totalPostCoursesList.get(i)).get()
                        .addOnCompleteListener(task -> {

                            if (task.isComplete()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    if (finalI == totalPostCoursesList.size() - 1)
                                        myCallback.callGetCoursePreReqStatus((String) document.get("status"), totalPostCoursesList.get(finalI), true);
                                    else
                                        myCallback.callGetCoursePreReqStatus((String) document.get("status"), totalPostCoursesList.get(finalI), false);
                                }
                            }
                            else{
                                Toasty.error(getActivity(), "error", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(e -> Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }

    }

    int checkGPA(RelativeLayout parent){

// to get GPA editText value and convert it to float
        if (Float.parseFloat(((EditText)((LinearLayout)parent.getChildAt(1)).getChildAt(2)).getText().toString())<= 2){
            allowableCreditHours=16;
        }
        else{
            allowableCreditHours=21;
        }



        return allowableCreditHours;
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
