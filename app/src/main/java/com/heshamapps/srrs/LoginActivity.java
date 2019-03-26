package com.heshamapps.srrs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.heshamapps.srrs.admin.AdminMainActivity;
import com.heshamapps.srrs.databinding.ActivityLoginBinding;
import com.heshamapps.srrs.student.StudentMainActivity;
import com.heshamapps.srrs.viewModel.LoginViewModel;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity  {




    @BindView(R.id.usernameET)
    EditText usernameET ;

    @BindView(R.id.passwordET)
    EditText passwordET;

    @BindView(R.id.progressBar)
     ProgressBar progressBar;

    String mUserName , mPassword ;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        lunchStudentScreen();
        ActivityLoginBinding activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        activityLoginBinding.setViewModel(new LoginViewModel(getApplication()));
        activityLoginBinding.executePendingBindings();


        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        viewModel.getAction().observe(this, action -> {
            if(action != null){
                handleAction(action);
            }
        });



    }


    private void handleAction(@NonNull final LoginViewModel.Action action) {
        switch (action.getValue()){
            case LoginViewModel.Action.SHOW_ADMIN:
                lunchAdminScreen();
                break;
            case LoginViewModel.Action.SHOW_STUDENT:
                lunchStudentScreen();
                break;
        }
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null)
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    //Login BTN
    public void login(View view){
        mUserName = usernameET.getText().toString();
        mPassword = String.valueOf(passwordET.getText().toString());

        if (TextUtils.isEmpty(mUserName)) {
            Toasty.warning(LoginActivity.this, "error" + "Enter email address!", Toast.LENGTH_SHORT).show();

            return;
        }

        if (TextUtils.isEmpty(mPassword)) {
            Toasty.warning(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);



    }

    //reset BTN
    public void reset(View view){
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    //signUp BTN
    public void signUp(View view){
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    public void lunchAdminScreen(){
    Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
    startActivity(intent);
    finish();
    }

    public void lunchStudentScreen(){
        Intent intent = new Intent(LoginActivity.this, StudentMainActivity.class);
        startActivity(intent);
        finish();
    }


}
