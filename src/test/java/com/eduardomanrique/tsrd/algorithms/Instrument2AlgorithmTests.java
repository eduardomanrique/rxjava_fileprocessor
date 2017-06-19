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
import java.util.Calendar;
import java.util.concurrent.Semaphore;

import static com.eduardomanrique.tsrd.util.DateUtil.createDate;
import static org.junit.Assert.assertEquals;

/**
 * Created by emanrique on 19/06/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Instrument2AlgorithmTests {

    @Autowired
    Instrument2Algorithm algorithm;

    @Test
    public void test() throws Exception{
        TsrdEvent[] events = {
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, Calendar.OCTOBER, 30).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, Calendar.NOVEMBER, 2).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent("X", createDate(2014, Calendar.NOVEMBER, 3).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, Calendar.NOVEMBER, 4).getTime(), BigDecimal.valueOf(30), ""),
                new TsrdEvent("X", createDate(2014, Calendar.NOVEMBER, 5).getTime(), BigDecimal.valueOf(20), "")
        };
        Semaphore semaphore = new Semaphore(1);
        AverageAcumulator[] acumulator = {null};

        semaphore.acquire();
        algorithm.process(Observable.fromArray(events), averageAcumulator -> {
            acumulator[0] = averageAcumulator;
            semaphore.release();
        });

        semaphore.acquire();
        //average of all INSTRUMENT2 after november 2014
        assertEquals(20, acumulator[0].getAverage().intValue());
    }
}
