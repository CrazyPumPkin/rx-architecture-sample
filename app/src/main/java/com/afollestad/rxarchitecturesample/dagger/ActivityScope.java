package com.afollestad.rxarchitecturesample.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

/** @author Aidan Follestad (afollestad) */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {}
