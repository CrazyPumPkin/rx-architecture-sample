package com.afollestad.rxarchitecturesample.commands;

import io.reactivex.functions.Function;

/** @author Aidan Follestad (afollestad) */
public interface Command<T> extends Function<T, T> {}
