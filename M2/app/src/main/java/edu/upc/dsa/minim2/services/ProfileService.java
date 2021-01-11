package edu.upc.dsa.minim2.services;

import java.util.List;

import edu.upc.dsa.minim2.models.ProfileResponse;
import edu.upc.dsa.minim2.models.RepositoriResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

//prueba nuevo repositorio
public interface ProfileService {

    @GET("service/getProfile")
    Call<List<ProfileResponse>> getProfile();

    @GET("users/{username}")
    Call<ProfileResponse> getUser(
            @Path("username") String username
    );

    @GET("users/{username}/repos")
    Call<List<RepositoriResponse>> getRepos(
            @Path("username") String username
    );

    @POST("user/register")
    Call<ResponseBody> postProfile(
            @Body ProfileResponse ProfileResponse
    );

}
