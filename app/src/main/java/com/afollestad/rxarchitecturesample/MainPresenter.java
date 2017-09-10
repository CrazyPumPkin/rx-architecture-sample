package com.afollestad.rxarchitecturesample;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.location.Location;
import android.support.annotation.VisibleForTesting;
import com.afollestad.rxarchitecturesample.api.OpenWeatherApi;
import com.afollestad.rxarchitecturesample.commands.Command;
import com.afollestad.rxarchitecturesample.commands.LocationFailureCommand;
import com.afollestad.rxarchitecturesample.commands.WeatherLoadedCommand;
import com.afollestad.rxarchitecturesample.dagger.StringProvider;
import com.afollestad.rxarchitecturesample.location.RxLocation;
import com.afollestad.rxarchitecturesample.preferences.UnitsPreference;
import com.afollestad.rxarchitecturesample.util.Rx;
import com.f2prateek.rx.preferences2.Preference;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import javax.inject.Inject;

/** @author Aidan Follestad (afollestad) */
class MainPresenter {

  private final OpenWeatherApi openWeather;
  private final RxLocation rxLocation;
  private final StringProvider stringProvider;
  private final Preference<String> unitsPreference;

  private Observable<MainViewModel> vm;

  @Inject
  MainPresenter(
      OpenWeatherApi openWeather,
      RxLocation rxLocation,
      StringProvider stringProvider,
      @UnitsPreference Preference<String> unitsPreference) {
    this.openWeather = openWeather;
    this.rxLocation = rxLocation;
    this.stringProvider = stringProvider;
    this.unitsPreference = unitsPreference;
  }

  void source(
      MainViewModel initialVm, Observable<Boolean> loadSignal, Consumer<Disposable> sourceSub) {
    this.vm =
        loadData(loadSignal)
            .scan(initialVm, (previous, command) -> command.apply(previous))
            .replay(1)
            .autoConnect(1, sourceSub);
  }

  /**
   * The main data loading process. Gets the user's location, and does the other required API calls.
   */
  @VisibleForTesting
  Observable<Command<MainViewModel>> loadData(Observable<Boolean> signal) {
    return signal.flatMap(
        ignored ->
            rxLocation
                .lastLocation()
                .toObservable()
                .flatMap(
                    wrappedLocation -> {
                      if (!wrappedLocation.hasLocation()) {
                        // If we don't have location permission, show the respective error state
                        return Observable.just(LocationFailureCommand.create(stringProvider));
                      }

                      final Location location = wrappedLocation.location();
                      // Make request to OpenWeather API
                      return openWeather
                          .coordinates(
                              location.getLatitude(),
                              location.getLongitude(),
                              unitsPreference.get())
                          .toObservable()
                          .flatMap(
                              result -> {
                                // Apply result to the view model
                                return Observable.just(
                                    WeatherLoadedCommand.create(result, unitsPreference.get()));
                              })
                          .startWith(
                              previous -> {
                                // Apply the loading state
                                return previous
                                    .toBuilder()
                                    .progressVisibility(VISIBLE)
                                    .contentVisibility(GONE)
                                    .errorVisibility(GONE)
                                    .build();
                              });
                    }));
  }

  Observable<MainViewModel> viewModel() {
    return vm;
  }

  Observable<Integer> contentVisibility() {
    return vm.map(MainViewModel::contentVisibility).compose(Rx.distinctToMainThread());
  }

  Observable<Integer> progressVisibility() {
    return vm.map(MainViewModel::progressVisibility).compose(Rx.distinctToMainThread());
  }

  Observable<Integer> errorVisibility() {
    return vm.map(MainViewModel::errorVisibility).compose(Rx.distinctToMainThread());
  }

  Observable<String> errorMessage() {
    return vm.filter(it -> it.errorMessage() != null)
        .map(MainViewModel::errorMessage)
        .compose(Rx.distinctToMainThread());
  }

  Observable<String> temperature() {
    return vm.filter(it -> it.temperature() != null)
        .map(MainViewModel::temperature)
        .compose(Rx.distinctToMainThread());
  }

  Observable<String> cityName() {
    return vm.filter(it -> it.cityName() != null)
        .map(MainViewModel::cityName)
        .compose(Rx.distinctToMainThread());
  }

  Observable<Boolean> retryEnabled() {
    return vm.map(MainViewModel::retryEnabled).compose(Rx.distinctToMainThread());
  }

  @VisibleForTesting
  void overrideVmStream(Observable<MainViewModel> vm) {
    this.vm = vm;
  }
}
