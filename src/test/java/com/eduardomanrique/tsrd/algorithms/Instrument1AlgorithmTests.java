package com.eduardomanrique.tsrd.algorithms;

import com.eduardomanrique.tsrd.algorithms.helper.AverageAcumulator;
import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import io.reactivex.Observable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.concurrent.Semaphore;

import static org.junit.Assert.*;
import static com.eduardomanrique.tsrd.util.DateUtil.*;

/**
 * Created by emanrique on 19/06/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Instrument1AlgorithmTests {

    @Autowired
    Instrument1Algorithm algorithm;

    @Test
    public void test() throws Exception {
        TsrdEvent[] events = {
                new TsrdEvent(Instrument1Algorithm.INSTRUMENT1, createDate(2014, 2, 1).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument1Algorithm.INSTRUMENT1, createDate(2014, 2, 2).getTime(), BigDecimal.valueOf(20), ""),
                new TsrdEvent("X", createDate(2014, 2, 3).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument1Algorithm.INSTRUMENT1, createDate(2014, 2, 4).getTime(), BigDecimal.valueOf(30), ""),
                new TsrdEvent("X", createDate(2014, 2, 5).getTime(), BigDecimal.valueOf(20), "")
        };
        Semaphore semaphore = new Semaphore(1);

        AverageAcumulator[] acumulator = {null};

        semaphore.acquire();
        algorithm.process(Observable.fromArray(events), averageAcumulator -> {
            acumulator[0] = averageAcumulator;
            semaphore.release();
        });

        semaphore.acquire();
        //average of all INSTRUMENT1
        assertEquals(20, acumulator[0].getAverage().intValue());
    }
}
