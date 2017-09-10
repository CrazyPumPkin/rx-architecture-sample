package com.afollestad.rxarchitecturesample.api;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class Precipitation implements Parcelable {

  public static TypeAdapter<Precipitation> typeAdapter(Gson gson) {
    return new AutoValue_Precipitation.GsonTypeAdapter(gson);
  }

  /** Volume in the last 3 hours. */
  @SerializedName("3h")
  public abstract double volume();
}
