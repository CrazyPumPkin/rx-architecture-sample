package com.afollestad.rxarchitecturesample.api;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class Wind implements Parcelable {

  public static TypeAdapter<Wind> typeAdapter(Gson gson) {
    return new AutoValue_Wind.GsonTypeAdapter(gson);
  }

  public abstract double speed();

  @SerializedName("deg")
  public abstract int degrees();
}
