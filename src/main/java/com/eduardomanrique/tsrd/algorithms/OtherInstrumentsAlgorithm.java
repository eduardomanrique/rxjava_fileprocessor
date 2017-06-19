package com.eduardomanrique.tsrd.algorithms;

import com.eduardomanrique.tsrd.datasource.Algorithm;
import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import com.eduardomanrique.tsrd.util.BoundedTreeSet;
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
public class OtherInstrumentsAlgorithm implements Algorithm {

    @Override
    public void connect(Observable<TsrdEvent> observable, PrintStream out) {
        process(observable, sum -> {
            out.println();
            out.println("=================================================================");
            out.println("OTHER INSTRUMENTS (not INSTRUMENT1, INSTRUMENT12 OR INSTRUMENT13)");
            out.println("SUM of the last 10: " + sum);
            out.println("=================================================================");
            out.println();
        });
    }

    protected void process(Observable<TsrdEvent> observable, Consumer<BigDecimal> onFinish) {
        observable
                .filter(tsrdEvent ->
                        !tsrdEvent.getInstrumentName().equalsIgnoreCase(Instrument1Algorithm.INSTRUMENT1) &&
                                !tsrdEvent.getInstrumentName().equalsIgnoreCase(Instrument2Algorithm.INSTRUMENT2) &&
                                !tsrdEvent.getInstrumentName().equalsIgnoreCase(Instrument3Algorithm.INSTRUMENT3))
                .subscribeOn(Schedulers.computation())
                .scan(createBoundedTreeSet(10), (set, tsrdEvent) -> add(set, tsrdEvent))
                .last()
                .subscribe(set -> {
                    Observable.fromIterable(set)
                            .scan(BigDecimal.ZERO, (sum, tsrdEvent) -> sum.add(tsrdEvent.getValue()))
                            .last()
                            .subscribe(onFinish);
                });
    }

    private BoundedTreeSet<TsrdEvent> createBoundedTreeSet(int maxSize) {
        return new BoundedTreeSet<>(maxSize, (e1, e2) -> {
            int comp = e1.getDate().compareTo(e2.getDate());
            comp = comp == 0 ? e1.getInstrumentName().compareTo(e2.getInstrumentName()) : comp;
            return comp == 0 ? e1.getValue().compareTo(e2.getValue()) : comp;
        });
    }

    private BoundedTreeSet<TsrdEvent> add(BoundedTreeSet<TsrdEvent> set, TsrdEvent tsrdEvent) {
        set.add(tsrdEvent);
        return set;
    }
}
