package com.eduardomanrique.tsrd.datasource;

import io.reactivex.Observable;

import java.io.PrintStream;

/**
 * Created by emanrique on 18/06/17.
 */
public interface Algorithm {
    void connect(Observable<TsrdEvent> observable, PrintStream output);
}
