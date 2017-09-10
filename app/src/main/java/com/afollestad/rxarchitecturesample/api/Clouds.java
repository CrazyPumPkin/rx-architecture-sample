package com.afollestad.rxarchitecturesample.api;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class Clouds implements Parcelable {

  public static TypeAdapter<Clouds> typeAdapter(Gson gson) {
    return new AutoValue_Clouds.GsonTypeAdapter(gson);
  }

  @SerializedName("all")
  public abstract double percent();
}
