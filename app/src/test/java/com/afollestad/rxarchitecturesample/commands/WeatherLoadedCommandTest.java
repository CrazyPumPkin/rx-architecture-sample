package com.afollestad.rxarchitecturesample.commands;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.afollestad.rxarchitecturesample.MainViewModel;
import com.afollestad.rxarchitecturesample.api.Main;
import com.afollestad.rxarchitecturesample.api.Units;
import com.afollestad.rxarchitecturesample.api.WeatherResult;
import com.afollestad.rxarchitecturesample.util.TempUtil;
import java.net.SocketTimeoutException;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

public class WeatherLoadedCommandTest {

  private WeatherResult weatherResult;

  @Before
  public void setup() {
    Main main = Main.create(73);
    weatherResult = WeatherResult.create(main, "Minneapolis");
  }

  @Test
  public void test_success() {
    Result<WeatherResult> result = Result.response(Response.success(weatherResult));

    MainViewModel vm = MainViewModel.builder().build();
    try {
      vm = WeatherLoadedCommand.create(result, Units.IMPERIAL).apply(vm);
    } catch (Exception e) {
      fail("Command throw an error!");
    }

    String expectedTemp = TempUtil.formatTemperature(73, Units.IMPERIAL);

    assertThat(vm.contentVisibility()).isEqualTo(VISIBLE);
    assertThat(vm.errorVisibility()).isEqualTo(GONE);
    assertThat(vm.progressVisibility()).isEqualTo(GONE);
    assertThat(vm.errorMessage()).isNull();
    assertThat(vm.retryEnabled()).isTrue();
    assertThat(vm.temperature()).isEqualTo(expectedTemp);
    assertThat(vm.cityName()).isEqualTo("Minneapolis");
  }

  @Test
  public void test_failure_error_timeout() {
    Result<WeatherResult> result = Result.error(new SocketTimeoutException("Oh no!"));

    MainViewModel vm = MainViewModel.builder().build();
    try {
      vm = WeatherLoadedCommand.create(result, Units.IMPERIAL).apply(vm);
    } catch (Exception e) {
      fail("Command throw an error!");
    }

    assertThat(vm.contentVisibility()).isEqualTo(GONE);
    assertThat(vm.errorVisibility()).isEqualTo(VISIBLE);
    assertThat(vm.progressVisibility()).isEqualTo(GONE);
    assertThat(vm.errorMessage()).isEqualTo("Oh no!");
    assertThat(vm.retryEnabled()).isTrue();
    assertThat(vm.temperature()).isNull();
    assertThat(vm.cityName()).isNull();
  }
}
