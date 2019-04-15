package com.heshamapps.srrs.student;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.heshamapps.srrs.R;
import com.heshamapps.srrs.util.DrawerUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StudentMainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mFirebaseAuth;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_activity_main);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        new DrawerUtil(this,mToolbar,mFirebaseAuth);


        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame,  new arEnFragment(),"first").commit();


        mActivity = this;
        db = FirebaseFirestore.getInstance();









    }

}

