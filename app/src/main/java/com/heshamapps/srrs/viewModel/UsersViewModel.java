package com.heshamapps.srrs.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.heshamapps.srrs.data.local.AppDataBase;
import com.heshamapps.srrs.data.local.entity.Users;

public class UsersViewModel extends AndroidViewModel {

    private AppDataBase appDataBase;
    private Users mUser;
    String mUserName , mPassword;

    public UsersViewModel(@NonNull Application application) {
        super(application);

        appDataBase = AppDataBase.getDatabase(this.getApplication());

        mUser = appDataBase.usersDao().getUser(mUserName,mPassword);
    }

    public Users getUser(String mUserName, String mPassword) {
        this.mUserName=mUserName;
        this.mPassword=mPassword;
        return mUser;
    }

    public void insertUser(Users mUser) {
        appDataBase.usersDao().insertUser(mUser);
    }


    public void signInWithEmailAndPassword(){



    }

}
