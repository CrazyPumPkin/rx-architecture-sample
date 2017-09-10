package com.afollestad.rxarchitecturesample.commands;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.afollestad.rxarchitecturesample.MainViewModel;
import com.afollestad.rxarchitecturesample.R;
import com.afollestad.rxarchitecturesample.dagger.StringProvider;
import org.junit.Test;

public class LocationFailureCommandTest {

  private final StringProvider stringProvider =
      res -> {
        if (res == R.string.unable_get_permission) {
          return "We were unable to retrieve your current location";
        }
        return null;
      };

  @Test
  public void test_location_failure() {
    MainViewModel vm = MainViewModel.builder().build();
    try {
      vm = LocationFailureCommand.create(stringProvider).apply(vm);
    } catch (Exception e) {
      fail("Command throw an error!");
    }
    assertThat(vm.errorVisibility()).isEqualTo(VISIBLE);
    assertThat(vm.contentVisibility()).isEqualTo(GONE);
    assertThat(vm.progressVisibility()).isEqualTo(GONE);
    assertThat(vm.errorMessage()).isEqualTo("We were unable to retrieve your current location");
  }
}
