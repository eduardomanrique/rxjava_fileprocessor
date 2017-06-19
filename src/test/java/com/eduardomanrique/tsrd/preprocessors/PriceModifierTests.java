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
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by emanrique on 19/06/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PriceModifierTests {

    @Mock
    InstrumentPriceModifierService instrumentPriceModifierService;

    @Autowired
    @InjectMocks
    PriceModifier priceModifiler;

    @Before
    public void before() {
        when(instrumentPriceModifierService.getInstrumentByName(any(String.class))).thenReturn(null);
        when(instrumentPriceModifierService.getInstrumentByName("X")).thenReturn(createModifier(1, "X", 2));
        when(instrumentPriceModifierService.getInstrumentByName("Y")).thenReturn(createModifier(1, "Y", 0.5));
    }

    private InstrumentPriceModifier createModifier(long id, String name, double value) {
        InstrumentPriceModifier modifier = new InstrumentPriceModifier();
        modifier.setId(id);
        modifier.setName(name);
        modifier.setMultiplier(BigDecimal.valueOf(value));
        return modifier;
    }

    @Test
    public void test() throws Exception {
        TsrdEvent[] events = {
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, Calendar.OCTOBER, 30).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, Calendar.NOVEMBER, 2).getTime(), BigDecimal.valueOf(10), ""),
                //the events X will be modified to 20 (10*2)
                new TsrdEvent("X", createDate(2014, Calendar.NOVEMBER, 3).getTime(), BigDecimal.valueOf(10), ""),
                new TsrdEvent(Instrument2Algorithm.INSTRUMENT2, createDate(2014, Calendar.NOVEMBER, 4).getTime(), BigDecimal.valueOf(30), ""),
                //the events Y will be modified to 20 (40/2)
                new TsrdEvent("Y", createDate(2014, Calendar.NOVEMBER, 5).getTime(), BigDecimal.valueOf(40), "")
        };
        Semaphore semaphore = new Semaphore(1);
        BigDecimal[] acumulator = {null};

        semaphore.acquire();
        Observable.fromArray(events).map(priceModifiler::map)
                .scan(BigDecimal.ZERO, (sum, tsrdEvent) -> sum.add(tsrdEvent.getValue()))
                .last()
                .subscribe(sum -> {
                    acumulator[0] = sum;
                    semaphore.release();
                });

        semaphore.acquire();
        //sum with modifiers
        assertEquals(90, acumulator[0].intValue());
    }
}
