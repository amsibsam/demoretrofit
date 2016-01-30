package com.example.root.retrofitandrx.network;

import android.content.Context;
import android.util.Log;

import com.example.root.retrofitandrx.pojo.Location;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit.BaseUrl;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by root on 30/01/16.
 */
public class RetrofitServices {
    private RetrofitApi retrofitApi;

    public RetrofitServices(final Context context){
        BaseUrl baseUrl = new BaseUrl() {
            @Override
            public HttpUrl url() {
                String url = "http://api.onesixoneeight.asia";
                return HttpUrl.parse(url);
            }
        };

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        OkHttpClient client = new OkHttpClient();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("RetrofitLog", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        retrofitApi = retrofit.create(RetrofitApi.class);
    }

    public Observable<Location> getLocation(){
        return Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(Subscriber<? super Location> subscriber) {
                try {
                    retrofit.Response<GenericResponse> response = retrofitApi.listLocation().execute();
                    if (response.isSuccess()){
                        GenericResponse genericResponse = response.body();

                        if (genericResponse.status.equals("success")){
                            Gson gson = new GsonBuilder()
                                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                    .create();

                            ListLocationResponse succeedResponse = gson.fromJson(genericResponse.data,
                                    ListLocationResponse.class);

                            for (ListLocationResponse.City rawCity : succeedResponse.cities){
                                subscriber.onNext(rawCity.toLocationPojo());
                            }

                            subscriber.onCompleted();
                        } else {
                            JsonObject failedResponse = genericResponse.data.getAsJsonObject();
                            if (failedResponse.has("message")){
                                subscriber.onError(new Exception(failedResponse.get("message").getAsString()));
                            } else {
                                subscriber.onError(new Exception(failedResponse.toString()));
                            }
                        }
                    } else {
                        String errorBody = response.errorBody().string();
                        Log.e("retrofitService", errorBody);
                        subscriber.onError(new Exception(errorBody));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }

            }
        });
    }


    private interface  RetrofitApi{
        @GET("/utilities/cities")
        Call<GenericResponse> listLocation();
    }

    private static class GenericResponse {
        String status;
        JsonElement data;
        String message;
    }

    private static class ListLocationResponse {
        List<City> cities;

        class City {
            String name;
            float latitude;
            float longitude;

            Location toLocationPojo() {
                return new Location(name, latitude, longitude);
            }
        }
    }
}
