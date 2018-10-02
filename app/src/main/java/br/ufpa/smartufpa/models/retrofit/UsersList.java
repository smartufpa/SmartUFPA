package br.ufpa.smartufpa.models.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.ufpa.smartufpa.models.User;

public class UsersList {


    private boolean success;
    @SerializedName("data")
    private List<User> usersList;

    public UsersList(boolean success, List<User> usersList) {
        this.success = success;
        this.usersList = usersList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }
}
