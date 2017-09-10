package com.afollestad.rxarchitecturesample.api;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class Sys implements Parcelable {

  public static TypeAdapter<Sys> typeAdapter(Gson gson) {
    return new AutoValue_Sys.GsonTypeAdapter(gson);
  }

  public abstract double message();

  public abstract String country();

  public abstract long sunrise();

  public abstract long sunset();
}
