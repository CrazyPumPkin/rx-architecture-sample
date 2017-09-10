package com.afollestad.rxarchitecturesample.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import com.afollestad.rxarchitecturesample.api.OpenWeatherApi;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/** @author Aidan Follestad (afollestad) */
@ActivityScope
@Module
public class ActivityModule {

  @Provides
  @ActivityScope
  static OpenWeatherApi providesOpenWeather(Retrofit retrofit) {
    return retrofit.create(OpenWeatherApi.class);
  }

  @Provides
  @ActivityScope
  static RxPermissions providesRxPermissions(AppCompatActivity context) {
    return new RxPermissions(context);
  }

  @Provides
  @ActivityScope
  static RxSharedPreferences providesRxSharedPreferences(@AppContext Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return RxSharedPreferences.create(prefs);
  }
}
