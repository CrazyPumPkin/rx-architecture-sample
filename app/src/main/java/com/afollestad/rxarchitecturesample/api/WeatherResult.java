package com.afollestad.rxarchitecturesample.api;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class WeatherResult implements Parcelable {

  @VisibleForTesting
  public static WeatherResult create(Main main, String cityName) {
    // Ideally you should never do this, use AutoValue Builders instead
    return new AutoValue_WeatherResult(
        null, null, null, main, null, null, null, null, 0, null, 0, cityName, 200);
  }

  public static TypeAdapter<WeatherResult> typeAdapter(Gson gson) {
    return new AutoValue_WeatherResult.GsonTypeAdapter(gson);
  }

  @Nullable
  public abstract Coord coord();

  @Nullable
  public abstract List<Weather> weather();

  @Nullable
  public abstract String base();

  @Nullable
  public abstract Main main();

  @Nullable
  public abstract Wind wind();

  @Nullable
  public abstract Clouds clouds();

  @Nullable
  public abstract Precipitation rain();

  @Nullable
  public abstract Precipitation snow();

  @SerializedName("dt")
  public abstract long calculationDateTime();

  @Nullable
  public abstract Sys sys();

  @SerializedName("id")
  public abstract long cityId();

  @Nullable
  @SerializedName("name")
  public abstract String cityName();

  @SerializedName("cod")
  public abstract int code();
}
