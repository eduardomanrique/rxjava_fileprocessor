package com.eduardomanrique.tsrd.preprocessors;

import com.eduardomanrique.tsrd.datasource.Modifier;
import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import com.eduardomanrique.tsrd.entities.InstrumentPriceModifier;
import com.eduardomanrique.tsrd.services.InstrumentPriceModifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by emanrique on 18/06/17.
 */
@Component
public class PriceModifier implements Modifier {

    @Autowired
    private InstrumentPriceModifierService instrumentPriceModifierService;

    public TsrdEvent map(TsrdEvent tsrdEvent) {
        InstrumentPriceModifier modifier = instrumentPriceModifierService.getInstrumentByName(tsrdEvent.getInstrumentName());
        if (modifier != null) {
            return new TsrdEvent(
                    tsrdEvent.getInstrumentName(),
                    tsrdEvent.getDate(),
                    tsrdEvent.getValue().multiply(modifier.getMultiplier()),
                    tsrdEvent.getOriginalLine());
        }
        return tsrdEvent;
    }
}
