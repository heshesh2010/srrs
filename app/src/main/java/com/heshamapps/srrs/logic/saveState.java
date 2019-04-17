package com.heshamapps.srrs.logic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class saveState {

    public saveState(Activity mActivity, ArrayList<String> coursePassedArray) {
        SharedPreferences.Editor outState = mActivity.getSharedPreferences("state", Context.MODE_PRIVATE).edit();
    }


}
