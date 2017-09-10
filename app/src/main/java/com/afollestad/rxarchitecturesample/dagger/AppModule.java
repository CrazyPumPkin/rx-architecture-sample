package com.afollestad.rxarchitecturesample.dagger;

import android.content.Context;
import com.afollestad.rxarchitecturesample.BuildConfig;
import com.afollestad.rxarchitecturesample.api.WeatherAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import javax.inject.Singleton;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/** @author Aidan Follestad (afollestad) */
@Module
public class AppModule {

  @Provides
  @Singleton
  static Gson providesGson() {
    return new GsonBuilder().registerTypeAdapterFactory(WeatherAdapterFactory.create()).create();
  }

  @Provides
  @Reusable
  static HttpLoggingInterceptor providesHttpLogger() {
    HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
    logger.setLevel(Level.BODY);
    return logger;
  }

  @Provides
  @Reusable
  static OkHttpClient providesHttpClient(HttpLoggingInterceptor logger) {
    return new OkHttpClient.Builder()
        .addInterceptor(
            chain -> {
              Request request = chain.request();
              HttpUrl url =
                  request
                      .url()
                      .newBuilder()
                      .addQueryParameter("appid", BuildConfig.OW_APP_ID)
                      .build();
              request = request.newBuilder().url(url).build();
              return chain.proceed(request);
            })
        .addInterceptor(logger)
        .build();
  }

  @Provides
  @Reusable
  static Retrofit providesRetrofit(OkHttpClient client, Gson gson) {
    return new Retrofit.Builder()
        .baseUrl(BuildConfig.OW_API_HOST)
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
  }

  @Provides
  @Reusable
  static StringProvider providesStringProvider(@AppContext Context context) {
    return context::getString;
  }
}
