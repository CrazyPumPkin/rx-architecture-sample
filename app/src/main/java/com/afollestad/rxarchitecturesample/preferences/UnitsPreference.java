package com.afollestad.rxarchitecturesample.preferences;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

/** @author Aidan Follestad (afollestad) */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface UnitsPreference {}
