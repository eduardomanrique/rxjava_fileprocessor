package com.eduardomanrique.tsrd.algorithms;

import com.eduardomanrique.tsrd.algorithms.helper.AverageAcumulator;
import com.eduardomanrique.tsrd.datasource.Algorithm;
import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import com.eduardomanrique.tsrd.util.DateUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.Calendar;

/**
 * Created by emanrique on 18/06/17.
 */
@Component
@Slf4j
public class Instrument2Algorithm implements Algorithm {

    public static final String INSTRUMENT2 = "INSTRUMENT2";

    @Override
    public void connect(Observable<TsrdEvent> observable, PrintStream out) {
        process(observable, averageAcumulator -> {
            out.println();
            out.println("=================================================================");
            out.println(INSTRUMENT2);
            out.println("Mean value after november 2014: " + averageAcumulator.getAverage());
            out.println("=================================================================");
            out.println();
        });
    }

    protected void process(Observable<TsrdEvent> observable, Consumer<AverageAcumulator> onFinish) {
        Calendar startDate = DateUtil.createDate(2014, Calendar.NOVEMBER, 1);

        observable
                .filter(tsrdEvent ->
                        tsrdEvent.getInstrumentName().equalsIgnoreCase(INSTRUMENT2) &&
                                startDate.getTime().before(tsrdEvent.getDate()))
                .subscribeOn(Schedulers.computation())
                .scan(new AverageAcumulator(), (acumulator, tsrdEvent) -> acumulator.addValue(tsrdEvent.getValue()))
                .last()
                .subscribe(onFinish);
    }
}
