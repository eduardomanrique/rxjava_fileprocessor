package com.eduardomanrique.tsrd.algorithms;

import com.eduardomanrique.tsrd.algorithms.helper.MinMaxAcumulator;
import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import io.reactivex.Observable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.concurrent.Semaphore;

import static com.eduardomanrique.tsrd.util.DateUtil.createDate;
import static org.junit.Assert.assertEquals;

/**
 * Created by emanrique on 19/06/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Instrument3AlgorithmTests {

    @Autowired
    Instrument3Algorithm algorithm;

    @Test
    public void test() throws Exception{
        TsrdEvent[] events = {
                new TsrdEvent(Instrument3Algorithm.INSTRUMENT3, createDate(2014, Calendar.OCTOBER, 30).getTime(), BigDecimal.valueOf(5), ""),
                new TsrdEvent(Instrument3Algorithm.INSTRUMENT3, createDate(2014, Calendar.NOVEMBER, 2).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent("X", createDate(2014, Calendar.NOVEMBER, 3).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument3Algorithm.INSTRUMENT3, createDate(2014, Calendar.NOVEMBER, 4).getTime(), BigDecimal.valueOf(30), ""),
                new TsrdEvent("X", createDate(2014, Calendar.NOVEMBER, 5).getTime(), BigDecimal.valueOf(20), "")
        };
        Semaphore semaphore = new Semaphore(1);
        MinMaxAcumulator[] acumulator = {null};

        semaphore.acquire();
        algorithm.process(Observable.fromArray(events), minMaxAcumulator -> {
            acumulator[0] = minMaxAcumulator;
            semaphore.release();
        });

        semaphore.acquire();
        //min and max value of INSTRUMENT3
        assertEquals(5, acumulator[0].getMin().intValue());
        assertEquals(30, acumulator[0].getMax().intValue());
    }
}
