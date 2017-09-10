package com.afollestad.rxarchitecturesample;

import android.location.Location;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

/**
 * We cannot pass null values through streams in Rx 2, so this wraps a nullable value.
 *
 * @author Aidan Follestad (afollestad)
 */
@AutoValue
public abstract class WrappedLocation implements Parcelable {

  public static WrappedLocation create(@Nullable Location location) {
    return new AutoValue_WrappedLocation(location);
  }

  @Nullable
  public abstract Location location();

  public boolean hasLocation() {
    return location() != null;
  }
}
