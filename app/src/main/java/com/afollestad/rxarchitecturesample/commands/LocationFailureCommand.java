package com.afollestad.rxarchitecturesample.commands;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import com.afollestad.rxarchitecturesample.MainViewModel;
import com.afollestad.rxarchitecturesample.R;
import com.afollestad.rxarchitecturesample.dagger.StringProvider;
import com.google.auto.value.AutoValue;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class LocationFailureCommand implements Command<MainViewModel> {

  public static Command<MainViewModel> create(StringProvider stringProvider) {
    return new AutoValue_LocationFailureCommand(stringProvider);
  }

  abstract StringProvider stringProvider();

  @Override
  public MainViewModel apply(MainViewModel previous) throws Exception {
    return previous
        .toBuilder()
        .errorVisibility(VISIBLE)
        .contentVisibility(GONE)
        .progressVisibility(GONE)
        .errorMessage(stringProvider().getString(R.string.unable_get_permission))
        .build();
  }
}
