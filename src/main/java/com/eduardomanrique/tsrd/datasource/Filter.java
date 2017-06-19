package com.eduardomanrique.tsrd.datasource;

import io.reactivex.Observable;

/**
 * Created by emanrique on 18/06/17.
 */
public interface Filter {
    boolean filter(TsrdEvent event);
}
