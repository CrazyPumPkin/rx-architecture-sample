package com.afollestad.rxarchitecturesample.dagger;

import android.support.annotation.StringRes;

/** @author Aidan Follestad (afollestad) */
public interface StringProvider {

  String getString(@StringRes int res);
}
