package com.heshamapps.srrs.viewModel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.heshamapps.srrs.LoginActivity;
import com.heshamapps.srrs.data.local.AppDataBase;
import com.heshamapps.srrs.data.local.entity.Users;
import com.heshamapps.srrs.util.CONFIG;

import java.util.concurrent.Executor;

import es.dmoral.toasty.Toasty;

public class LoginViewModel extends AndroidViewModel implements FirebaseAuth.AuthStateListener{
    private Users user;
    private FirebaseAuth auth;

    private String successMessage = "Login was successful";
    private String errorMessage = "Email or Password not valid";
    private AppDataBase appDataBase;

    private String toastMessage = null;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        appDataBase = AppDataBase.getDatabase(this.getApplication());
        user = new Users("","");
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(this);
    }

    public String getToastMessage() {
        return toastMessage;
    }


    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
    }


    public void setUserEmail(String email) {
        user.setEmail(email);
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    public String getUserPassword() {
        return user.getPassword();
    }

    public void setUserPassword(String password) {
        user.setPassword(password);
    }



    public void onLoginClicked() {
        if (isInputDataValid()){
            //authenticate user

            auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener( getApplication(), task -> {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toasty.error(getApplication(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            setToastMessage(errorMessage);

                        } else {

                            int type = getUserType(auth.getCurrentUser().getUid());
                            setToastMessage(successMessage);

                            switch (type) {
                                case CONFIG.ADMIN:
                                    lunchAdminScreen();
                                    break;
                                case CONFIG.STUDENT:
                                    lunchStudentScreen();
                                    break;
                                default:
                                 //   Toasty.error(getApplicationContext(), "Sorry, Unknown user type please contact app developer", Toast.LENGTH_LONG).show();
                                    break;
                            }
                            //   getSharedPreferences("myPref", MODE_PRIVATE).edit().putInt("userType", type).apply();

                        }
                    });

        }
        else
            setToastMessage(errorMessage);
    }

    private int getUserType(String uid) {

      return  appDataBase.usersDao().getUserType(uid).getRoleType();
    }

    public boolean isInputDataValid() {
        return !TextUtils.isEmpty(getUserEmail()) && Patterns.EMAIL_ADDRESS.matcher(getUserEmail()).matches() && getUserPassword().length() > 5;
    }


    public void lunchAdminScreen() {
        mAction.setValue(new Action(Action.SHOW_ADMIN));
    }

    public void lunchStudentScreen() {
        mAction.setValue(new Action(Action.SHOW_STUDENT));

    }

    //Stores actions for view.
    private MutableLiveData<Action> mAction = new MutableLiveData<Action>();

    public LiveData<Action> getAction() {
        return mAction;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
        }
    }


    public class Action {
        public static final int SHOW_ADMIN = 0;
        public static final int SHOW_STUDENT = 1;
        private final int mAction;

        public Action(int action) {
            mAction = action;
        }

        public int getValue() {
            return mAction;
        }



    }
}
