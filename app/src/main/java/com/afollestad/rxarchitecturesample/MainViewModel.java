package com.afollestad.rxarchitecturesample;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.afollestad.rxarchitecturesample.util.Visibility;
import com.google.auto.value.AutoValue;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class MainViewModel implements Parcelable {

  public static Builder builder() {
    return new AutoValue_MainViewModel.Builder()
        .progressVisibility(VISIBLE)
        .errorVisibility(GONE)
        .contentVisibility(GONE)
        .retryEnabled(false);
  }

  @Visibility
  public abstract int progressVisibility();

  @Visibility
  public abstract int errorVisibility();

  @Visibility
  public abstract int contentVisibility();

  @Nullable
  public abstract String errorMessage();

  @Nullable
  public abstract String temperature();

  @Nullable
  public abstract String cityName();

  public abstract boolean retryEnabled();

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder progressVisibility(@Visibility int visibility);

    public abstract Builder errorVisibility(@Visibility int visibility);

    public abstract Builder contentVisibility(@Visibility int visibility);

    public abstract Builder errorMessage(@Nullable String errorMessage);

    public abstract Builder temperature(@Nullable String temperature);

    public abstract Builder cityName(@Nullable String cityName);

    public abstract Builder retryEnabled(boolean enabled);

    public abstract MainViewModel build();
  }
}
