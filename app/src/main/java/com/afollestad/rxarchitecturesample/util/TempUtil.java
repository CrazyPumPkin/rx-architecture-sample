package com.afollestad.rxarchitecturesample.util;

import android.support.annotation.VisibleForTesting;
import com.afollestad.rxarchitecturesample.api.Units;
import java.util.Locale;

/** @author Aidan Follestad (afollestad) */
public class TempUtil {

  @VisibleForTesting
  static String unitString(@Units String unit) {
    switch (unit) {
      case Units.IMPERIAL:
        return "°F";
      case Units.METRIC:
        return "°C";
      default:
        return "°K";
    }
  }

  public static String formatTemperature(double temp, @Units String unit) {
    int rounded = (int) Math.round(temp);
    return String.format(Locale.getDefault(), "%s%s", rounded, unitString(unit));
  }
}
