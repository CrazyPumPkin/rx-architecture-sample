package com.afollestad.rxarchitecturesample.api;

import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class Main implements Parcelable {

  @VisibleForTesting
  public static Main create(double temp) {
    // Ideally you should never do this, use AutoValue Builders instead
    return new AutoValue_Main(temp, 0, 0, 0, 0, 0, 0);
  }

  public static TypeAdapter<Main> typeAdapter(Gson gson) {
    return new AutoValue_Main.GsonTypeAdapter(gson);
  }

  public abstract double temp();

  /** hPa */
  public abstract double pressure();

  /** Percent */
  public abstract double humidity();

  @SerializedName("temp_min")
  public abstract double tempMin();

  @SerializedName("temp_max")
  public abstract double tempMax();

  /** hPa */
  @SerializedName("sea_level")
  public abstract double seaLevel();

  /** hPa */
  @SerializedName("grnd_level")
  public abstract double groundLevel();
}
