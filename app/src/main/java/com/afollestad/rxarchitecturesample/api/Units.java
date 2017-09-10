package com.afollestad.rxarchitecturesample.api;

import static com.afollestad.rxarchitecturesample.api.Units.IMPERIAL;
import static com.afollestad.rxarchitecturesample.api.Units.METRIC;
import static com.afollestad.rxarchitecturesample.api.Units.STANDARD;

import android.support.annotation.StringDef;

/** @author Aidan Follestad (afollestad) */
@StringDef({METRIC, IMPERIAL, STANDARD})
public @interface Units {
  String METRIC = "metric";
  String IMPERIAL = "imperial";
  String STANDARD = "";
}
