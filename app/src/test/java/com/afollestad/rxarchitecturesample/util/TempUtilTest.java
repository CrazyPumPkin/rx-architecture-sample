package com.afollestad.rxarchitecturesample.util;

import static com.afollestad.rxarchitecturesample.util.TempUtil.formatTemperature;
import static com.afollestad.rxarchitecturesample.util.TempUtil.unitString;
import static com.google.common.truth.Truth.assertThat;

import com.afollestad.rxarchitecturesample.api.Units;
import org.junit.Test;

public class TempUtilTest {

  @Test
  public void test_unit_string_metric() throws Exception {
    assertThat(unitString(Units.METRIC)).isEqualTo("°C");
  }

  @Test
  public void test_unit_string_imperial() throws Exception {
    assertThat(unitString(Units.IMPERIAL)).isEqualTo("°F");
  }

  @Test
  public void test_unit_string_standard() throws Exception {
    assertThat(unitString(Units.STANDARD)).isEqualTo("°K");
  }

  @Test
  public void test_format_round_up() throws Exception {
    assertThat(formatTemperature(24.87, Units.METRIC)).isEqualTo("25°C");
  }

  @Test
  public void test_format_round_down() throws Exception {
    assertThat(formatTemperature(25.01, Units.IMPERIAL)).isEqualTo("25°F");
  }

  @Test
  public void test_format_no_round() throws Exception {
    assertThat(formatTemperature(25.00, Units.STANDARD)).isEqualTo("25°K");
  }
}
