package com.heshamapps.srrs.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heshamapps.srrs.R;
import com.heshamapps.srrs.util.DrawerUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AdminMainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        ButterKnife.bind(this);

        new DrawerUtil(this,mToolbar,  FirebaseAuth.getInstance());

    }


    public void addNewCourse(View view){
        Intent intent = new Intent(AdminMainActivity.this, AddNewCourseActivity.class);
        startActivity(intent);
        finish();

    }
}
