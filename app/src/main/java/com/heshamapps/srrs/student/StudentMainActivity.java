package com.heshamapps.srrs.student;

import android.app.Activity;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.heshamapps.srrs.R;
import com.heshamapps.srrs.aboutFragment;
import com.heshamapps.srrs.util.DrawerUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class StudentMainActivity extends AppCompatActivity implements  pastCoursesFragment.OnFragmentInteractionListener , aboutFragment.OnFragmentInteractionListener{

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


        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame,  new arEnFragment()).commit();


        mActivity = this;
        db = FirebaseFirestore.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();

        new DrawerUtil(this,mToolbar,mFirebaseAuth);



      //   AR.getSelectedItem().toString();
     //   EL.getSelectedItem().toString();




    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

