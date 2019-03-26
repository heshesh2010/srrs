package com.heshamapps.srrs.data.local;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.heshamapps.srrs.data.local.dao.UsersDao;
import com.heshamapps.srrs.data.local.entity.Users;

@Database(entities = {Users.class}, version = 1,exportSchema = true)
public abstract class AppDataBase  extends RoomDatabase {


    private static AppDataBase INSTANCE;

    public static AppDataBase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "srrs_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract UsersDao usersDao();

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
