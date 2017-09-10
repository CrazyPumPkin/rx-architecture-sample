package com.afollestad.rxarchitecturesample.location;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.VisibleForTesting;
import com.afollestad.rxarchitecturesample.WrappedLocation;
import com.afollestad.rxarchitecturesample.dagger.AppContext;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class RxLocation {

  private static final long LOOP_SLEEP = 100;
  private static final long TIMEOUT = 2000;

  private final FusedLocationProviderClient client;
  private final RxPermissions rxPermissions;

  @Inject
  RxLocation(@AppContext Context context, RxPermissions rxPermissions) {
    this.client = LocationServices.getFusedLocationProviderClient(context);
    this.rxPermissions = rxPermissions;
  }

  @VisibleForTesting
  public RxLocation(FusedLocationProviderClient client, RxPermissions rxPermissions) {
    this.client = client;
    this.rxPermissions = rxPermissions;
  }

  @SuppressLint("MissingPermission")
  public Single<WrappedLocation> lastLocation() {
    return rxPermissions
        .request(permission.ACCESS_FINE_LOCATION)
        .observeOn(Schedulers.computation())
        .singleOrError()
        .flatMap(
            granted -> {
              if (!granted) {
                // If permission was denied, pass null location
                return Single.just(WrappedLocation.create(null));
              }
              Task<Location> location = client.getLastLocation();
              long passed = 0;
              while (!location.isComplete()) {
                Thread.sleep(LOOP_SLEEP);
                if (passed >= TIMEOUT) {
                  // Didn't receive location before timeout
                  return Single.just(WrappedLocation.create(null));
                }
                passed += LOOP_SLEEP;
              }
              return Single.just(WrappedLocation.create(location.getResult()));
            });
  }
}
