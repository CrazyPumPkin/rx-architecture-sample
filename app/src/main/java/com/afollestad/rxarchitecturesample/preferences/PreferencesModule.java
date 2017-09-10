package com.afollestad.rxarchitecturesample.preferences;

import com.afollestad.rxarchitecturesample.api.Units;
import com.afollestad.rxarchitecturesample.dagger.ActivityScope;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import dagger.Module;
import dagger.Provides;

/** @author Aidan Follestad (afollestad) */
@ActivityScope
@Module
public class PreferencesModule {

  private static final String KEY_UNITS = "key.units";

  @UnitsPreference
  @Provides
  @ActivityScope
  static Preference<String> provideUnitsPreference(RxSharedPreferences prefs) {
    return prefs.getString(KEY_UNITS, Units.IMPERIAL);
  }
}
