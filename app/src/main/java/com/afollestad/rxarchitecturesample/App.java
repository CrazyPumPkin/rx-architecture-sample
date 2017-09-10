package com.afollestad.rxarchitecturesample;

import android.app.Application;
import com.afollestad.rxarchitecturesample.dagger.AppComponent;
import com.afollestad.rxarchitecturesample.dagger.DaggerAppComponent;

/** @author Aidan Follestad (afollestad) */
public class App extends Application {

  private AppComponent component;

  public AppComponent component() {
    return component;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    component = DaggerAppComponent.builder().appContext(this).build();
  }
}
