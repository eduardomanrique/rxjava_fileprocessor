package com.eduardomanrique.tsrd.repositories;

import com.eduardomanrique.tsrd.entities.InstrumentPriceModifier;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * Created by emanrique on 18/06/17.
 */
public interface InstrumentPriceModifierRepository extends Repository<InstrumentPriceModifier, Long> {

    InstrumentPriceModifier getByName(String name);
}
