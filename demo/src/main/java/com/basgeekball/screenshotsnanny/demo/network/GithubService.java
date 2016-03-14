package com.basgeekball.screenshotsnanny.demo.network;

import com.basgeekball.screenshotsnanny.demo.network.models.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class GithubService {
    public static final String API_URL = "https://api.github.com";

    private Github mGithub;

    public interface Github {
        @GET("/users/{user}")
        Call<User> users(@Path("user") String user);
    }

    public GithubService() {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        mGithub = retrofit.create(Github.class);
    }

    public User getUser(String username) {
        Call<User> call = mGithub.users(username);
        User user = null;
        try {
            user = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
}
