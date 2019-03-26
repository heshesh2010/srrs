package com.heshamapps.srrs.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.heshamapps.srrs.data.local.entity.Users;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM users WHERE email=:mEmail AND password=:mPassword ")
   Users getUser(String mEmail , String mPassword);

    @Insert(onConflict = REPLACE)
    void insertUser(Users muUser);

@Query("SELECT * FROM users WHERE userId=:userKey")
Users getUserType(String userKey);

}
