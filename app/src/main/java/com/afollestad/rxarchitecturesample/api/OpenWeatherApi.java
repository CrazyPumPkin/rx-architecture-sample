package com.afollestad.rxarchitecturesample.api;

import io.reactivex.Single;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

/** @author Aidan Follestad (afollestad) */
public interface OpenWeatherApi {

  String PATH = "data/2.5/";

  @GET(PATH + "find")
  Single<Result<WeatherResult>> id(@Query("id") int id, @Query("units") @Units String units);

  @GET(PATH + "find")
  Single<Result<WeatherResult>> query(
      @Query("q") String query, @Query("units") @Units String units);

  @GET(PATH + "weather")
  Single<Result<WeatherResult>> coordinates(
      @Query("lat") double latitude,
      @Query("lon") double longitude,
      @Query("units") @Units String units);

  /** ZIP should be in the format {zip code},{country code} */
  @GET(PATH + "weather")
  Single<Result<WeatherResult>> zip(@Query("zip") String zip, @Query("units") @Units String units);
}
