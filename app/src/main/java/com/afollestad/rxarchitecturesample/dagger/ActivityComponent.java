package com.afollestad.rxarchitecturesample.dagger;

import android.support.v7.app.AppCompatActivity;
import com.afollestad.rxarchitecturesample.MainActivity;
import com.afollestad.rxarchitecturesample.preferences.PreferencesModule;
import dagger.BindsInstance;
import dagger.Subcomponent;

/** @author Aidan Follestad (afollestad) */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class, PreferencesModule.class})
public interface ActivityComponent {

  void inject(MainActivity activity);

  @Subcomponent.Builder
  interface Builder {
    @BindsInstance
    Builder activity(AppCompatActivity activity);

    ActivityComponent build();
  }
}
