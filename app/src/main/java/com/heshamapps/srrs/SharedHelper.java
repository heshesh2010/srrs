package com.heshamapps.srrs;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SharedHelper {

    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;

    private void intstantiateUser(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }


}
