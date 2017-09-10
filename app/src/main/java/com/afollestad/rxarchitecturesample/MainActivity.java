package com.afollestad.rxarchitecturesample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.afollestad.rxarchitecturesample.api.Units;
import com.afollestad.rxarchitecturesample.preferences.UnitsPreference;
import com.afollestad.rxarchitecturesample.util.Rx;
import com.f2prateek.rx.preferences2.Preference;
import com.google.common.base.Preconditions;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import javax.inject.Inject;

/** @author Aidan Follestad (afollestad) */
public class MainActivity extends AppCompatActivity {

  private static final String KEY_VM = "key.vm";

  private Unbinder unbinder;
  private PublishSubject<Boolean> loadSignal;
  private CompositeDisposable subs;
  private MainViewModel savedVm;

  @Inject MainPresenter presenter;
  @Inject @UnitsPreference Preference<String> unitsPreference;

  @BindView(R.id.main_content)
  ViewGroup contentViewGroup;

  @BindView(R.id.main_error_group)
  ViewGroup errorGroup;

  @BindView(R.id.main_error)
  TextView errorView;

  @BindView(R.id.main_progress)
  ProgressBar progressView;

  @BindView(R.id.temperature)
  TextView temperatureView;

  @BindView(R.id.city)
  TextView cityView;

  @BindView(R.id.retry_button)
  Button retryButton;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    unbinder = ButterKnife.bind(this);

    ((App) getApplication()).component().activityComponent().activity(this).build().inject(this);

    subs = new CompositeDisposable();
    loadSignal = PublishSubject.create();

    if (savedInstanceState != null && savedInstanceState.containsKey(KEY_VM)) {
      savedVm = Preconditions.checkNotNull(savedInstanceState.getParcelable(KEY_VM));
    } else {
      savedVm = MainViewModel.builder().build();
    }

    // We reload from the main load signal, from retry button clicks, or from unit changes
    Observable<Boolean> refreshSignal =
        Observable.merge(
            loadSignal,
            RxView.clicks(retryButton).compose(Rx.flatMapToBoolean()),
            unitsPreference.asObservable().distinctUntilChanged().compose(Rx.flatMapToBoolean()));
    presenter.source(savedVm, refreshSignal, subs::add);

    // Subscribe to VM states
    presenter.contentVisibility().subscribe(contentViewGroup::setVisibility);

    presenter.errorVisibility().subscribe(errorGroup::setVisibility);
    presenter.errorMessage().subscribe(errorView::setText);

    presenter.progressVisibility().subscribe(progressView::setVisibility);

    presenter.retryEnabled().subscribe(retryButton::setEnabled);

    presenter.temperature().subscribe(temperatureView::setText);

    presenter
        .cityName()
        .subscribe(cityName -> cityView.setText(getString(R.string.in_x, cityName)));

    presenter.viewModel().subscribe(mainViewModel -> savedVm = mainViewModel);

    // When the units preference changes, auto invalidate the overflow menu
    subs.add(
        unitsPreference
            .asObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(s -> invalidateOptionsMenu()));
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putParcelable(KEY_VM, savedVm);
    super.onSaveInstanceState(outState);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    @Units String currentUnit = unitsPreference.get();
    menu.findItem(R.id.imperial).setChecked(Units.IMPERIAL.equals(currentUnit));
    menu.findItem(R.id.metric).setChecked(Units.METRIC.equals(currentUnit));
    menu.findItem(R.id.standard).setChecked(Units.STANDARD.equals(currentUnit));
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.imperial:
        unitsPreference.set(Units.IMPERIAL);
        break;
      case R.id.metric:
        unitsPreference.set(Units.METRIC);
        break;
      case R.id.standard:
        unitsPreference.set(Units.STANDARD);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onResume() {
    super.onResume();
    loadSignal.onNext(true);
  }

  @Override
  protected void onDestroy() {
    unbinder.unbind();
    subs.clear();
    super.onDestroy();
  }
}
