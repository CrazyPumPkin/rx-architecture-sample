package com.afollestad.rxarchitecturesample.util;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/** @author Aidan Follestad (afollestad) */
public final class Rx {

  public static <T> ObservableTransformer<T, T> distinctToMainThread() {
    return obs -> obs.observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged();
  }

  public static <T> ObservableTransformer<T, Boolean> flatMapToBoolean() {
    return obs -> obs.flatMap(it -> Observable.just(true));
  }
}
