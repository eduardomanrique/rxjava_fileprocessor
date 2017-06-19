package com.eduardomanrique.tsrd.algorithms;

import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import io.reactivex.Observable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.concurrent.Semaphore;

import static com.eduardomanrique.tsrd.util.DateUtil.createDate;
import static org.junit.Assert.assertEquals;

/**
 * Created by emanrique on 19/06/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OtherInstrumentsAlgorithmTests {

    @Autowired
    OtherInstrumentsAlgorithm algorithm;

    @Test
    public void testMoreThan10() throws Exception {
        TsrdEvent[] events = {
                new TsrdEvent(Instrument1Algorithm.INSTRUMENT1, createDate(2014, 2, 1).getTime(), BigDecimal.valueOf(5), ""),
                new TsrdEvent(Instrument3Algorithm.INSTRUMENT3, createDate(2014, 2, 2).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent("X", createDate(2014, 2, 3).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, 2, 4).getTime(), BigDecimal.valueOf(30), ""),
                new TsrdEvent("Y", createDate(2014, 2, 5).getTime(), BigDecimal.valueOf(20), ""),
                new TsrdEvent(Instrument1Algorithm.INSTRUMENT1, createDate(2014, 2, 6).getTime(), BigDecimal.valueOf(5), ""),
                new TsrdEvent(Instrument3Algorithm.INSTRUMENT3, createDate(2014, 2, 6).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent("X", createDate(2014, 2, 6).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, 2, 7).getTime(), BigDecimal.valueOf(30), ""),
                new TsrdEvent("Y", createDate(2014, 2, 7).getTime(), BigDecimal.valueOf(20), ""),
                new TsrdEvent(Instrument1Algorithm.INSTRUMENT1, createDate(2014, 2, 8).getTime(), BigDecimal.valueOf(5), ""),
                new TsrdEvent(Instrument3Algorithm.INSTRUMENT3, createDate(2014, 2, 8).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent("X", createDate(2014, 2, 8).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, 2, 9).getTime(), BigDecimal.valueOf(30), ""),
                new TsrdEvent("Y", createDate(2014, 2, 9).getTime(), BigDecimal.valueOf(20), ""),
                new TsrdEvent(Instrument1Algorithm.INSTRUMENT1, createDate(2014, 2, 10).getTime(), BigDecimal.valueOf(5), ""),
                new TsrdEvent(Instrument3Algorithm.INSTRUMENT3, createDate(2014, 2, 10).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent("X", createDate(2014, 2, 10).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, 2, 11).getTime(), BigDecimal.valueOf(30), ""),
                new TsrdEvent("Y", createDate(2014, 2, 11).getTime(), BigDecimal.valueOf(20), ""),
                new TsrdEvent(Instrument1Algorithm.INSTRUMENT1, createDate(2014, 2, 12).getTime(), BigDecimal.valueOf(5), ""),
                new TsrdEvent(Instrument3Algorithm.INSTRUMENT3, createDate(2014, 2, 12).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent("X", createDate(2014, 2, 13).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, 2, 14).getTime(), BigDecimal.valueOf(30), ""),
                new TsrdEvent("Y", createDate(2014, 2, 15).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent("Z", createDate(2014, 2, 15).getTime(), BigDecimal.valueOf(5), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, 2, 14).getTime(), BigDecimal.valueOf(30), ""),
                new TsrdEvent("A", createDate(2014, 2, 15).getTime(), BigDecimal.valueOf(20), ""),
        };
        Semaphore semaphore = new Semaphore(1);
        BigDecimal[] result = {null};

        semaphore.acquire();
        algorithm.process(Observable.fromArray(events), sum -> {
            result[0] = sum;
            semaphore.release();
        });

        semaphore.acquire();
        //sum of 10 last values for instruments other thant (INSTRUMENT1, INSTRUMENT2 or INSTRUMENT3)
        assertEquals(135, result[0].intValue());
    }

    @Test
    public void testLessThan10() throws Exception {
        TsrdEvent[] events = {
                new TsrdEvent(Instrument1Algorithm.INSTRUMENT1, createDate(2014, 2, 1).getTime(), BigDecimal.valueOf(5), ""),
                new TsrdEvent(Instrument3Algorithm.INSTRUMENT3, createDate(2014, 2, 2).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent("X", createDate(2014, 2, 3).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, 2, 4).getTime(), BigDecimal.valueOf(30), ""),
                new TsrdEvent("Y", createDate(2014, 2, 5).getTime(), BigDecimal.valueOf(20), ""),
                new TsrdEvent(Instrument1Algorithm.INSTRUMENT1, createDate(2014, 2, 6).getTime(), BigDecimal.valueOf(5), ""),
                new TsrdEvent(Instrument3Algorithm.INSTRUMENT3, createDate(2014, 2, 6).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent("X", createDate(2014, 2, 6).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, 2, 7).getTime(), BigDecimal.valueOf(30), ""),
        };
        Semaphore semaphore = new Semaphore(1);
        BigDecimal[] result = {null};

        semaphore.acquire();
        algorithm.process(Observable.fromArray(events), sum -> {
            result[0] = sum;
            semaphore.release();
        });

        semaphore.acquire();
        //sum of 10 last values for instruments other thant (INSTRUMENT1, INSTRUMENT2 or INSTRUMENT3)
        assertEquals(40, result[0].intValue());
    }
}
