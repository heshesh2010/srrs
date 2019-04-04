package com.heshamapps.srrs.logic;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CheckGPA {

    int allowableCreditHours;


    public int CheckGPA(RelativeLayout parent) {
        // to get GPA editText value and convert it to float
        if (Float.parseFloat(((EditText)((LinearLayout)parent.getChildAt(1)).getChildAt(2)).getText().toString())<= 2){
            allowableCreditHours=16;
        }
        else{
            allowableCreditHours=21;
        }



        return allowableCreditHours;
    }
    }

