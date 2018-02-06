package com.hypernymbiz.logistics.api;

import com.hypernymbiz.logistics.models.JobCount;
import com.hypernymbiz.logistics.models.JobDetail;
import com.hypernymbiz.logistics.models.JobEnd;
import com.hypernymbiz.logistics.models.JobInfo;
import com.hypernymbiz.logistics.models.Profile;
import com.hypernymbiz.logistics.models.Respone_Completed_job;
import com.hypernymbiz.logistics.models.StartJob;
import com.hypernymbiz.logistics.models.User;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.AppUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Metis on 29-Dec-17.
 */

public interface ApiInterface {


  //  String HTTP = "http://192.168.2.120:8000/";
   String HTTP = "http://192.168.2.120:8000/";

    String HTTPP = "http://192.168.2.185:8000/";

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ApiInterface.HTTP)
            .addConverterFactory(GsonConverterFactory.create())
            .client(MyOkHttpClient.getHttpClient());
    ApiInterface retrofit = builder.build().create(ApiInterface.class);

    Retrofit.Builder builderr = new Retrofit.Builder()
            .baseUrl(ApiInterface.HTTPP)
            .addConverterFactory(GsonConverterFactory.create())
            .client(MyOkHttpClient.getHttpClient());
    ApiInterface retrofitt = builderr.build().create(ApiInterface.class);

    @POST("api/users/login/")
    Call<WebAPIResponse<User>> loginUser(@Body HashMap<String, Object> body);

    @GET("iof/get_app_jobs/")
    Call<WebAPIResponse<Respone_Completed_job>> getalldata(@Query("driver_id") int driver_id, @Query("status_id") int status_id);
    @GET("iof/get_notifications")
    Call<WebAPIResponse<List<JobInfo>>> getallpendingdata(@Query("driver_id") int driver_id);

    @GET("iof/get_driver_info/")
    Call<WebAPIResponse<Profile>> getprofile(@Query("driver_id") int driver_id);

    @GET("hypernet/get_user_alerts_count")
    Call<WebAPIResponse<List<JobCount>>> getcount();

    @GET("iof/get_app_jobs/")
    Call<WebAPIResponse<JobDetail>> getalldata(@Query("job_id") int job_id);

    @PUT("iof/driver_job_update")
    Call<WebAPIResponse<StartJob>> startjob(@Body HashMap<String, Object> body);
    @PUT("iof/driver_job_update")
    Call<WebAPIResponse<StartJob>> canceljob(@Body HashMap<String, Object> body);

    @PUT("iof/driver_job_update")
    Call<WebAPIResponse<JobEnd>> endjob(@Body HashMap<String, Object> body);
    class MyOkHttpClient {


        public static OkHttpClient getHttpClient() {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.readTimeout(30, TimeUnit.SECONDS);
            clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
            if (true) {
                clientBuilder.addInterceptor(logging);
            }
            clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Cache-Control", "no-cache")
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());
                    String token = "Token a1f32065fe1f5bdaf7f3075d22fccbe469a6b498";
                    if (token != null) {
                        requestBuilder.header("Authorization", token);
                    }
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }


            });


            return clientBuilder.build();
        }




    }

}