package com.afollestad.rxarchitecturesample.location;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import android.location.Location;
import com.afollestad.rxarchitecturesample.OverrideRxSchedulersRule;
import com.afollestad.rxarchitecturesample.WrappedLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RxLocationTest {

  @Rule public OverrideRxSchedulersRule overrideRxSchedulersRule = new OverrideRxSchedulersRule();

  @Mock FusedLocationProviderClient client;
  @Mock RxPermissions rxPermissions;
  @Mock Task<Location> lastLocationTask;
  @Mock Location location;

  private RxLocation rxLocation;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(client.getLastLocation()).thenReturn(lastLocationTask);
    when(location.getLatitude()).thenReturn(2d);
    when(location.getLongitude()).thenReturn(3d);

    rxLocation = new RxLocation(client, rxPermissions);
  }

  @Test
  public void test_fail_permission_check() {
    when(rxPermissions.request(any())).thenReturn(Observable.just(false));
    List<WrappedLocation> resultList =
        rxLocation
            .lastLocation()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValueCount(1)
            .values();
    assertThat(resultList.get(0).location()).isNull();
  }

  @Test
  public void test_get_last_location_timeout() {
    when(rxPermissions.request(any())).thenReturn(Observable.just(true));
    List<WrappedLocation> resultList =
        rxLocation
            .lastLocation()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValueCount(1)
            .values();
    assertThat(resultList.get(0).location()).isNull();
  }

  @Test
  public void test_success() {
    when(rxPermissions.request(any())).thenReturn(Observable.just(true));
    when(lastLocationTask.isComplete()).thenReturn(true);
    when(lastLocationTask.getResult()).thenReturn(location);

    List<WrappedLocation> resultList =
        rxLocation
            .lastLocation()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValueCount(1)
            .values();
    WrappedLocation result = resultList.get(0);
    assertThat(result.hasLocation()).isTrue();
    assertThat(result.location()).isNotNull();
    assertThat(result.location().getLatitude()).isEqualTo(2d);
    assertThat(result.location().getLongitude()).isEqualTo(3d);
  }
}
