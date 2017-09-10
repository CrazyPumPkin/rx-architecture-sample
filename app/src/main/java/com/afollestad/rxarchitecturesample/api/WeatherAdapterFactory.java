package com.afollestad.rxarchitecturesample.api;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/** @author Aidan Follestad (afollestad) */
@GsonTypeAdapterFactory
public abstract class WeatherAdapterFactory implements TypeAdapterFactory {

  public static WeatherAdapterFactory create() {
    return new AutoValueGson_WeatherAdapterFactory();
  }
}
