package com.afollestad.rxarchitecturesample.dagger;

import android.content.Context;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;

/** @author Aidan Follestad (afollestad) */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

  ActivityComponent.Builder activityComponent();

  @Component.Builder
  interface Builder {
    @BindsInstance
    AppComponent.Builder appContext(@AppContext Context context);

    AppComponent build();
  }
}
