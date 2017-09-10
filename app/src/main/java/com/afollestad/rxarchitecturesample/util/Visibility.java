package com.afollestad.rxarchitecturesample.util;

import android.support.annotation.IntDef;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** @author Aidan Follestad (afollestad) */
@Retention(RetentionPolicy.RUNTIME)
@IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
public @interface Visibility {}
