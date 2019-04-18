package com.heshamapps.srrs.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.heshamapps.srrs.R;
import com.heshamapps.srrs.admin.AdminMainActivity;
import com.heshamapps.srrs.models.Users;
import com.heshamapps.srrs.student.StudentMainActivity;
import com.heshamapps.srrs.util.CONFIG;

import androidx.annotation.NonNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends Activity {




    @BindView(R.id.usernameET)
    EditText usernameET ;

    @BindView(R.id.passwordET)
    EditText passwordET;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    String mUserName , mPassword ;


    private FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

       //   lunchAdminScreen();
     //  lunchStudentScreen();


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

        //authenticate user
        auth.signInWithEmailAndPassword(mUserName, mPassword)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (mPassword.length() < 6) {
                                passwordET.setError(getString(R.string.minimum_password));
                            } else {
                                Toasty.error(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        } else {

                            DocumentReference docRef = db.collection("users").document(auth.getCurrentUser().getUid());
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Users user = documentSnapshot.toObject(Users.class);
                                    progressBar.setVisibility(View.GONE);
                                    int type = user.getRoleId();
                                    switch (type) {
                                        case CONFIG.ADMIN:
                                            lunchAdminScreen();
                                            break;
                                        case CONFIG.STUDENT:
                                            lunchStudentScreen();
                                            break;
                                        default:
                                            Toasty.error(getApplicationContext(),"Sorry, Unknown user type please contact app developer" , Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                    getSharedPreferences("myPref", MODE_PRIVATE).edit().putInt("userType",type).apply();


                                }
                            });
                        }
                    }
                });

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