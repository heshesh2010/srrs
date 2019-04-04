package com.heshamapps.srrs.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.heshamapps.srrs.R;
import com.heshamapps.srrs.models.Courses;
import com.heshamapps.srrs.util.DrawerUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AddNewCourseActivity extends AppCompatActivity {

    @BindView(R.id.courseCodeET)
    EditText courseCode ;

    @BindView(R.id.courseHoursET)
    EditText courseHours ;

    @BindView(R.id.courseNameET)
    EditText courseName ;

    @BindView(R.id.levelSpinner)
    Spinner level ;

    @BindView(R.id.semesterSpinner)
    Spinner semester ;

    @BindView(R.id.preLL)
    LinearLayout preLL ;

    @BindView(R.id.postLL)
    LinearLayout postLL ;

    FirebaseFirestore db;
    FirebaseAuth mFirebaseAuth;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    ArrayList<String> preCourses = new ArrayList<>();
    ArrayList<String> postCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);
        ButterKnife.bind(this);
        db = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
       new DrawerUtil(this,mToolbar,mFirebaseAuth);

    }

    public void preAddViews(View view){
        LinearLayout parent = new LinearLayout(this);
        parent.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setWeightSum(4);
        EditText et=new EditText(this);
        et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, WRAP_CONTENT,3.8f));

        ImageView img = new ImageView(this);
                img.setBackgroundResource(R.drawable.ic_remove_circle_blue_400_24dp);
                img.setOnClickListener(this::removeViews);
        img.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT,    0.2f));



        parent.addView(et);
        parent.addView(img);

        preLL.addView(parent);

    }

    public void postAddViews(View view){
        LinearLayout parent = new LinearLayout(this);
        parent.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setWeightSum(4);
        EditText et=new EditText(this);
        et.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, WRAP_CONTENT,3.8f));

        ImageView img = new ImageView(this);
        img.setBackgroundResource(R.drawable.ic_remove_circle_blue_400_24dp);
        img.setOnClickListener(this::removeViews);
        img.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT,    0.2f));



        parent.addView(et);
        parent.addView(img);

        postLL.addView(parent);

    }

    public void removeViews(View view){

        ((ViewGroup)view.getParent().getParent()).removeView((ViewGroup)view.getParent());

    }



        public void save(View view){




            for(int i = 0; i <preLL.getChildCount(); i++){
                if(preLL.getChildAt(i) instanceof LinearLayout) {
                    EditText view2 = (((EditText)((LinearLayout)preLL.getChildAt(i)).getChildAt(0)));
                    preCourses.add(view2.getText().toString().toUpperCase());
                }}


            for(int i = 0; i <postLL.getChildCount(); i++){
                if(postLL.getChildAt(i) instanceof LinearLayout) {
                    EditText view2 = (((EditText)((LinearLayout)postLL.getChildAt(i)).getChildAt(0)));
                    postCourses.add(view2.getText().toString().toUpperCase());
                }}





        if(TextUtils.isEmpty(courseCode.getText())){
            Toasty.error(getApplicationContext(),"You should enter course code ",  Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(courseHours.getText())){
            Toasty.error(getApplicationContext(),"You should enter course hours ",  Toast.LENGTH_SHORT).show();
            }
        else if(TextUtils.isEmpty(courseName.getText())){
            Toasty.error(getApplicationContext(),"You should enter course name ",  Toast.LENGTH_SHORT).show();
        }
        else {
            db.collection("courses").document(courseCode.getText().toString().toUpperCase())
                    .set(new Courses(courseCode.getText().toString(), courseName.getText().toString(), semester.getSelectedItem().toString(), Integer.valueOf(courseHours.getText().toString()),postCourses,postCourses.size(),preCourses))
                    .addOnSuccessListener(aVoid -> Toasty.success(getApplicationContext(),"Course Saved!" , Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                    });

        }
    }


    }






