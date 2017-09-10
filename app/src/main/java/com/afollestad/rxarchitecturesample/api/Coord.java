package com.afollestad.rxarchitecturesample.api;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class Coord implements Parcelable {

  public static TypeAdapter<Coord> typeAdapter(Gson gson) {
    return new AutoValue_Coord.GsonTypeAdapter(gson);
  }

  @SerializedName("lon")
  public abstract double longitude();

  @SerializedName("lat")
  public abstract double latitude();
}
