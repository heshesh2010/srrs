package com.heshamapps.srrs.student;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.FirebaseApp;
import com.heshamapps.srrs.R;
import com.heshamapps.srrs.logic.CheckGPA;
import com.heshamapps.srrs.logic.Next;
import com.heshamapps.srrs.logic.SaveAsInProgress;
import com.heshamapps.srrs.logic.SaveAsTaken;
import com.heshamapps.srrs.logic.ShowCourses;
import com.heshamapps.srrs.models.Courses;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class fourthYearFragment extends Fragment {

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


    int allowableCreditHours;


    ArrayList<String> coursePassedArray = new ArrayList<String>();
    ArrayList<String> holdCoursesArray = new ArrayList<String>();
    ArrayList<String> coursesSelected = new ArrayList<String>();
    ArrayList<String> coursesNotSelected = new ArrayList<String>();
    ArrayList<String> singlePostCoursesListArray = new ArrayList<String>();


    public fourthYearFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fourth_year, container, false);
        ButterKnife.bind(this, view);
        FirebaseApp.initializeApp(getActivity());

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            if (bundle.getSerializable("Beginning") == null && bundle.getSerializable("AnyYear") != null) {
                new ShowCourses((ArrayList<Courses>) bundle.getSerializable("AnyYear"), 1, getActivity(), view);

            } else {
                new ShowCourses((ArrayList<Courses>) bundle.getSerializable("postCourses"), 0,getActivity(),view);
            }
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        first_next.setOnClickListener(v -> next(((RelativeLayout) v.getParent().getParent())));

        second_next.setOnClickListener(v -> next(((RelativeLayout) v.getParent().getParent())));

        summer_next.setOnClickListener(v -> next(((RelativeLayout) v.getParent().getParent())));

        first_save.setOnClickListener(v -> new SaveAsInProgress((((RelativeLayout) v.getParent().getParent())), getActivity()));

        second_save.setOnClickListener(v -> new SaveAsInProgress((((RelativeLayout) v.getParent().getParent())), getActivity()));

        summer_save.setOnClickListener(v -> new SaveAsInProgress((((RelativeLayout) v.getParent().getParent())), getActivity()));
    } //end of onActivityCreated


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


    // to move next to following semester and update courses in data base to taken
    void next(RelativeLayout parent) {
        coursePassedArray.clear();
        holdCoursesArray.clear();
        coursesSelected.clear();
        coursesNotSelected.clear();
        singlePostCoursesListArray.clear();

        // get selected course hours from textView
        int Hours = Integer.parseInt(((TextView) ((LinearLayout) parent.getChildAt(1)).getChildAt(1)).getText().toString());

        // check if GPA value is Empty
        if (((EditText) ((LinearLayout) parent.getChildAt(1)).getChildAt(2)).getText().toString().isEmpty()) {
            Toasty.error(getActivity(), "Enter your GPA", Toast.LENGTH_SHORT).show();
        } else {
            allowableCreditHours = new CheckGPA().CheckGPA(parent);
            if (Hours > allowableCreditHours) {
                Toasty.error(getActivity(), "You must choose courses limit to " + allowableCreditHours + "credit Hours ", Toast.LENGTH_SHORT).show();
            }


            // check GPA is not allowed show error


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

                                // Get all checked courses and save it in database to 'pass' status
                                new SaveAsTaken().saveTaken(parent);
                                new Next().do_next(parent, coursesSelected, coursesNotSelected, getActivity(), getView());


                            }) // end of setPositiveButton button


                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                } // end of checkRules IF

                else { // else if there is no reuls for math or programming found

                    // Get all checked courses and save it in database to 'pass' status

                    new SaveAsTaken().saveTaken(parent);
                    new Next().do_next(parent, coursesSelected, coursesNotSelected, getActivity(), getView());

                }// end of else
            }
        }
    }
}