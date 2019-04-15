package com.heshamapps.srrs.logic;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.heshamapps.srrs.R;
import com.heshamapps.srrs.models.Courses;
import com.heshamapps.srrs.student.fourthYearFragment;
import com.heshamapps.srrs.student.secondYearFragment;
import com.heshamapps.srrs.student.thirdYearFragment;
import com.heshamapps.srrs.util.MyCallback2;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowCourses implements CompoundButton.OnCheckedChangeListener {


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

    Activity finalActivity ;

    public ShowCourses(List<Courses> detailedCourses, int termLayoutNumber, Activity activity, View view) {
        Set<Courses> set = new HashSet<>(detailedCourses);
        detailedCourses.clear();
        detailedCourses.addAll(set);

        finalActivity=activity;
        ButterKnife.bind(this, view);
        // if summerCourse next move detiledCourse to new fragment
        if(termLayoutNumber==3){
            // check if year notifaction
            // if first year les than 32
            FragmentManager fragmentManager = activity.getFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_frame);
            Bundle bundle = new Bundle();
            bundle.putSerializable("postCourses", (Serializable) detailedCourses);

            new GetYearHours().readData(new MyCallback2() {
                @Override
                public void onCallback(int sourceTotalInProgress) {
                   if(Integer.parseInt(currentFragment.getTag())==1){
                       if(sourceTotalInProgress<32)
                       showDialog(32);

                       secondYearFragment fragment = new secondYearFragment();
                       fragment.setArguments(bundle);
                       activity.getFragmentManager().beginTransaction()
                               .replace(R.id.fragment_frame, fragment,"2").commit();
                   }

                   else if (Integer.parseInt(currentFragment.getTag())==2){
                       if(sourceTotalInProgress<64)
                       showDialog(64);

                       thirdYearFragment fragment = new thirdYearFragment();
                       fragment.setArguments(bundle);
                       activity.getFragmentManager().beginTransaction()
                               .replace(R.id.fragment_frame, fragment,"3").commit();
                   }
                   else if (Integer.parseInt(currentFragment.getTag())==3){
                       if(sourceTotalInProgress<89)
                       showDialog(89);

                       fourthYearFragment fragment = new fourthYearFragment();
                       fragment.setArguments(bundle);
                       activity.getFragmentManager().beginTransaction()
                               .replace(R.id.fragment_frame, fragment,"4").commit();
                   }
                }
            });





        }
        else {
            // Get courseHours for every course and show in next term or level
            for (int i = 0; i < detailedCourses.size(); i++) {
                CheckBox courseBTN = new CheckBox(activity);
                courseBTN.setOnCheckedChangeListener(this);
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
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            incrementTV(buttonView.getText().toString(), Integer.parseInt((((View) buttonView.getParent()).getTag()).toString()));
        } else {
            decrementTV(buttonView.getText().toString(), Integer.parseInt((((View) buttonView.getParent()).getTag()).toString()));

        }
    }


    private void incrementTV(String sourceCourseHours, int termLayoutNumber) {

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


    }


    private void decrementTV(String sourceCourseHours, int termLayoutNumber) {

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


    }


    private void showDialog(int hours){

        new AlertDialog.Builder(finalActivity)
                .setTitle("Alert")
                .setMessage("note you are lower than " + hours + "hours ")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {



                }).show(); // end of setPositiveButton button



    }

}
