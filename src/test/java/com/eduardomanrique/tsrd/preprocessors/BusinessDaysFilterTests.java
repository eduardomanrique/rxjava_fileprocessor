package com.eduardomanrique.tsrd.preprocessors;

import com.eduardomanrique.tsrd.algorithms.Instrument2Algorithm;
import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import com.eduardomanrique.tsrd.entities.InstrumentPriceModifier;
import com.eduardomanrique.tsrd.services.InstrumentPriceModifierService;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.concurrent.Semaphore;

import static com.eduardomanrique.tsrd.util.DateUtil.createDate;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * Created by emanrique on 19/06/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BusinessDaysFilterTests {

    @Autowired
    BusinessDaysFilter businessDaysFilter;

    @Test
    public void test() throws Exception {
        TsrdEvent[] events = {
                //friday
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2017, Calendar.JUNE, 2).getTime(), BigDecimal.valueOf(10), ""),
                //saturday
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2017, Calendar.JUNE, 3).getTime(), BigDecimal.valueOf(10), ""),
                //sunday
                new TsrdEvent("X", createDate(2017, Calendar.JUNE, 4).getTime(), BigDecimal.valueOf(10), ""),
                //monday
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2017, Calendar.JUNE, 5).getTime(), BigDecimal.valueOf(30), ""),
                //tuesday
                new TsrdEvent("Y", createDate(2017, Calendar.JUNE, 6).getTime(), BigDecimal.valueOf(40), "")
        };
        Semaphore semaphore = new Semaphore(1);
        BigDecimal[] acumulator = {null};

        semaphore.acquire();
        Observable.fromArray(events).filter(businessDaysFilter::filter)
                .scan(BigDecimal.ZERO, (sum, tsrdEvent) -> sum.add(tsrdEvent.getValue()))
                .last()
                .subscribe(sum -> {
                    acumulator[0] = sum;
                    semaphore.release();
                });

        semaphore.acquire();
        //sum filtered
        assertEquals(80, acumulator[0].intValue());
    }
}
