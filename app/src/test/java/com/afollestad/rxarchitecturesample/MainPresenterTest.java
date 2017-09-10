package com.afollestad.rxarchitecturesample;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.location.Location;
import com.afollestad.rxarchitecturesample.api.Main;
import com.afollestad.rxarchitecturesample.api.OpenWeatherApi;
import com.afollestad.rxarchitecturesample.api.Units;
import com.afollestad.rxarchitecturesample.api.WeatherResult;
import com.afollestad.rxarchitecturesample.commands.Command;
import com.afollestad.rxarchitecturesample.commands.LocationFailureCommand;
import com.afollestad.rxarchitecturesample.dagger.StringProvider;
import com.afollestad.rxarchitecturesample.location.RxLocation;
import com.f2prateek.rx.preferences2.Preference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

public class MainPresenterTest {

  @Rule public OverrideRxSchedulersRule overrideRxSchedulersRule = new OverrideRxSchedulersRule();

  private StringProvider stringProvider = res -> null;

  @Mock OpenWeatherApi api;
  @Mock Preference<String> unitsPreference;

  @Mock FusedLocationProviderClient locationClient;
  @Mock RxPermissions rxPermissions;
  @Mock Task<Location> lastLocationTask;
  @Mock Location location;
  @Mock RxLocation rxLocation;

  private MainPresenter presenter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    // Mock api
    Main main = Main.create(73);
    WeatherResult weatherResult = WeatherResult.create(main, "Minneapolis");
    when(api.coordinates(anyDouble(), anyDouble(), anyString()))
        .thenReturn(Single.just(Result.response(Response.success(weatherResult))));

    // Mock permissions
    when(rxPermissions.request(any())).thenReturn(Observable.just(true));

    // Mock location
    when(locationClient.getLastLocation()).thenReturn(lastLocationTask);
    when(location.getLatitude()).thenReturn(2d);
    when(location.getLongitude()).thenReturn(3d);
    when(lastLocationTask.isComplete()).thenReturn(true);
    when(lastLocationTask.getResult()).thenReturn(location);
    rxLocation = new RxLocation(locationClient, rxPermissions);

    // Mock preference
    unitsPreference = mock(Preference.class);
    when(unitsPreference.get()).thenReturn(Units.IMPERIAL);

    // Create presenter with mocked classes
    presenter = new MainPresenter(api, rxLocation, stringProvider, unitsPreference);
  }

  @Test
  public void can_source_presenter() {
    PublishSubject<Boolean> loadSignal = PublishSubject.create();
    presenter.source(MainViewModel.builder().build(), loadSignal, disposable -> {});

    TestObserver<MainViewModel> obs = presenter.viewModel().distinctUntilChanged().test();
    loadSignal.onNext(true);

    // Loading state, result
    List<MainViewModel> results =
        obs.assertNotComplete().assertNoErrors().assertValueCount(2).values();
    assertThat(results.get(0).progressVisibility()).isEqualTo(VISIBLE);
    assertThat(results.get(0).temperature()).isNull();
    assertThat(results.get(0).temperature()).isNull();
    assertThat(results.get(1).progressVisibility()).isEqualTo(GONE);
    assertThat(results.get(1).temperature()).isNotNull();
    assertThat(results.get(1).cityName()).isNotNull();
  }

  /** The case with location is handled above. */
  @Test
  public void can_load_data_no_location() throws Exception {
    when(rxPermissions.request(any())).thenReturn(Observable.just(false));

    PublishSubject<Boolean> loadSignal = PublishSubject.create();
    TestObserver<Command<MainViewModel>> obs = presenter.loadData(loadSignal).test();
    loadSignal.onNext(true);

    List<Command<MainViewModel>> results =
        obs.assertNotComplete().assertNoErrors().assertValueCount(1).values();
    assertThat(results.get(0)).isInstanceOf(LocationFailureCommand.class);
  }

  @Test
  public void can_expose_content_visibility() {
    PublishSubject<MainViewModel> vms = PublishSubject.create();
    presenter.overrideVmStream(vms);

    TestObserver<Integer> obs = presenter.contentVisibility().test();

    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().contentVisibility(VISIBLE).build());
    vms.onNext(MainViewModel.builder().build());

    List<Integer> results = obs.assertValueCount(3).assertNoErrors().assertNotComplete().values();
    assertThat(results.get(0)).isEqualTo(GONE);
    assertThat(results.get(1)).isEqualTo(VISIBLE);
    assertThat(results.get(2)).isEqualTo(GONE);
  }

  @Test
  public void can_expose_progress_visibility() {
    PublishSubject<MainViewModel> vms = PublishSubject.create();
    presenter.overrideVmStream(vms);

    TestObserver<Integer> obs = presenter.progressVisibility().test();

    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().progressVisibility(GONE).build());
    vms.onNext(MainViewModel.builder().build());

    List<Integer> results = obs.assertValueCount(3).assertNoErrors().assertNotComplete().values();
    assertThat(results.get(0)).isEqualTo(VISIBLE);
    assertThat(results.get(1)).isEqualTo(GONE);
    assertThat(results.get(2)).isEqualTo(VISIBLE);
  }

  @Test
  public void can_expose_error_visibility() {
    PublishSubject<MainViewModel> vms = PublishSubject.create();
    presenter.overrideVmStream(vms);

    TestObserver<Integer> obs = presenter.errorVisibility().test();

    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().errorVisibility(VISIBLE).build());
    vms.onNext(MainViewModel.builder().build());

    List<Integer> results = obs.assertValueCount(3).assertNoErrors().assertNotComplete().values();
    assertThat(results.get(0)).isEqualTo(GONE);
    assertThat(results.get(1)).isEqualTo(VISIBLE);
    assertThat(results.get(2)).isEqualTo(GONE);
  }

  @Test
  public void can_expose_error_message() {
    PublishSubject<MainViewModel> vms = PublishSubject.create();
    presenter.overrideVmStream(vms);

    TestObserver<String> obs = presenter.errorMessage().test();

    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().errorMessage("Hello").build());
    vms.onNext(MainViewModel.builder().build());

    List<String> results = obs.assertValueCount(1).assertNoErrors().assertNotComplete().values();
    assertThat(results.get(0)).isEqualTo("Hello");
  }

  @Test
  public void can_expose_temperature() {
    PublishSubject<MainViewModel> vms = PublishSubject.create();
    presenter.overrideVmStream(vms);

    TestObserver<String> obs = presenter.temperature().test();

    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().temperature("70").build());
    vms.onNext(MainViewModel.builder().build());

    List<String> results = obs.assertValueCount(1).assertNoErrors().assertNotComplete().values();
    assertThat(results.get(0)).isEqualTo("70");
  }

  @Test
  public void can_expose_city_name() {
    PublishSubject<MainViewModel> vms = PublishSubject.create();
    presenter.overrideVmStream(vms);

    TestObserver<String> obs = presenter.cityName().test();

    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().cityName("Mpls").build());
    vms.onNext(MainViewModel.builder().build());

    List<String> results = obs.assertValueCount(1).assertNoErrors().assertNotComplete().values();
    assertThat(results.get(0)).isEqualTo("Mpls");
  }

  @Test
  public void can_expose_retry_enabled() {
    PublishSubject<MainViewModel> vms = PublishSubject.create();
    presenter.overrideVmStream(vms);

    TestObserver<Boolean> obs = presenter.retryEnabled().test();

    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().build());
    vms.onNext(MainViewModel.builder().retryEnabled(true).build());
    vms.onNext(MainViewModel.builder().build());

    List<Boolean> results = obs.assertValueCount(3).assertNoErrors().assertNotComplete().values();
    assertThat(results.get(0)).isFalse();
    assertThat(results.get(1)).isTrue();
    assertThat(results.get(2)).isFalse();
  }
}
