package com.heshamapps.srrs.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "users")
public class Users {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id ;

    @SerializedName("roleType")
    private
    int roleType;

    @SerializedName("userId")
    private String userId;

    @SerializedName("email")
    private String  email;

    @SerializedName("password")
    private String  password;




    public Users(String email, String password) {
        this.email = email;
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
            this.email = email;
    }
}
