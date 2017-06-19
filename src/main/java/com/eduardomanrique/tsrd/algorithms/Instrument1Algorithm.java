package com.eduardomanrique.tsrd.algorithms;

import com.eduardomanrique.tsrd.algorithms.helper.AverageAcumulator;
import com.eduardomanrique.tsrd.datasource.Algorithm;
import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.math.BigDecimal;

/**
 * Created by emanrique on 18/06/17.
 */
@Component
@Slf4j
public class Instrument1Algorithm implements Algorithm {

    public static final String INSTRUMENT1 = "INSTRUMENT1";

    @Override
    public void connect(Observable<TsrdEvent> observable, PrintStream out) {
        process(observable, averageAcumulator -> {
            out.println();
            out.println("=================================================================");
            out.println(INSTRUMENT1);
            out.println("Mean value: " + averageAcumulator.getAverage());
            out.println("=================================================================");
            out.println();
        });
    }

    protected void process(Observable<TsrdEvent> observable, Consumer<AverageAcumulator> onFinish) {
        final int[] count = {0};
        final BigDecimal[] total = {BigDecimal.ZERO};

        observable
                .filter(tsrdEvent -> tsrdEvent.getInstrumentName().equalsIgnoreCase(INSTRUMENT1))
                .subscribeOn(Schedulers.computation())
                .scan(new AverageAcumulator(), (acumulator, tsrdEvent) -> acumulator.addValue(tsrdEvent.getValue()))
                .last()
                .subscribe(onFinish);
    }
}
