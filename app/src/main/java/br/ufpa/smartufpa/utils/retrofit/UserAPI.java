package br.ufpa.smartufpa.utils.retrofit;

import br.ufpa.smartufpa.models.retrofit.UsersList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserAPI {
    @GET("users/")
    Call<UsersList> usersList();

    @GET("users/{email}")
    Call<UsersList> userByEmail(@Path("email") String email);

}
