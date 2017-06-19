package com.eduardomanrique.tsrd.algorithms;

import com.eduardomanrique.tsrd.algorithms.helper.MinMaxAcumulator;
import com.eduardomanrique.tsrd.datasource.Algorithm;
import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.PrintStream;

/**
 * Created by emanrique on 18/06/17.
 */
@Component
@Slf4j
public class Instrument3Algorithm implements Algorithm {

    public static final String INSTRUMENT3 = "INSTRUMENT3";

    @Override
    public void connect(Observable<TsrdEvent> observable, PrintStream out) {
        process(observable, acumulator -> {
            out.println();
            out.println("=================================================================");
            out.println(INSTRUMENT3);
            out.println("MAX value: " + acumulator.getMax());
            out.println("MIN value: " + acumulator.getMin());
            out.println("MAX - MIN: " + acumulator.getDiff());
            out.println("=================================================================");
            out.println();
        });
    }

    protected void process(Observable<TsrdEvent> observable, Consumer<MinMaxAcumulator> onFinish) {
        observable
                .filter(tsrdEvent -> tsrdEvent.getInstrumentName().equalsIgnoreCase(INSTRUMENT3))
                .subscribeOn(Schedulers.computation())
                .scan(new MinMaxAcumulator(), (acumulator, tsrdEvent) -> acumulator.addValue(tsrdEvent.getValue()))
                .last()
                .subscribe(onFinish);
    }
}
