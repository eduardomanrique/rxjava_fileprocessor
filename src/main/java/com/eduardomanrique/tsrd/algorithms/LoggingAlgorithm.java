package com.eduardomanrique.tsrd.algorithms;

import com.eduardomanrique.tsrd.datasource.Algorithm;
import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.PrintStream;

/**
 * Created by emanrique on 18/06/17.
 */
@Component
@Slf4j
public class LoggingAlgorithm implements Algorithm {

    @Override
    public void connect(Observable<TsrdEvent> observable, PrintStream out) {
        int count[] = {0};
        observable
                .subscribeOn(Schedulers.computation())
                .subscribe(tsrdEvent -> {
                    count[0]++;
                    if (count[0] % 5000 == 0) {
                        out.println("Processed " + count[0] + " lines");
                    }
                });
    }
}
