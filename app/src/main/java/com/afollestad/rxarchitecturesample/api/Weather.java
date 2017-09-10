package com.afollestad.rxarchitecturesample.api;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class Weather implements Parcelable {

  public static TypeAdapter<Weather> typeAdapter(Gson gson) {
    return new AutoValue_Weather.GsonTypeAdapter(gson);
  }

  @SerializedName("id")
  public abstract long conditionId();

  /** Rain, snow, extreme, etc. */
  @SerializedName("main")
  public abstract String group();

  /** Weather condition within the group. */
  public abstract String description();

  /** Icon ID */
  public abstract String icon();
}
