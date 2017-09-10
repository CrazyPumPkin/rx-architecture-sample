package com.afollestad.rxarchitecturesample.commands;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import com.afollestad.rxarchitecturesample.MainViewModel;
import com.afollestad.rxarchitecturesample.api.Units;
import com.afollestad.rxarchitecturesample.api.WeatherResult;
import com.afollestad.rxarchitecturesample.util.TempUtil;
import com.google.auto.value.AutoValue;
import retrofit2.adapter.rxjava2.Result;

/** @author Aidan Follestad (afollestad) */
@AutoValue
public abstract class WeatherLoadedCommand implements Command<MainViewModel> {

  public static Command<MainViewModel> create(Result<WeatherResult> result, @Units String unit) {
    return new AutoValue_WeatherLoadedCommand(result, unit);
  }

  abstract Result<WeatherResult> result();

  @Units
  abstract String unit();

  @Override
  public MainViewModel apply(MainViewModel previous) throws Exception {
    if (result().isError()) {
      return previous
          .toBuilder()
          .errorVisibility(VISIBLE)
          .contentVisibility(GONE)
          .progressVisibility(GONE)
          .errorMessage(result().error().getMessage())
          .retryEnabled(true)
          .cityName(null)
          .temperature(null)
          .build();
    }

    final WeatherResult weather = result().response().body();
    final String tempString = TempUtil.formatTemperature(weather.main().temp(), unit());
    return previous
        .toBuilder()
        .errorVisibility(GONE)
        .contentVisibility(VISIBLE)
        .progressVisibility(GONE)
        .errorMessage(null)
        .retryEnabled(true)
        .temperature(tempString)
        .cityName(weather.cityName())
        .build();
  }
}
