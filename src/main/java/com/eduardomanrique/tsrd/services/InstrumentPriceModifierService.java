package com.eduardomanrique.tsrd.services;

import com.eduardomanrique.tsrd.entities.InstrumentPriceModifier;
import com.eduardomanrique.tsrd.repositories.InstrumentPriceModifierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by emanrique on 18/06/17.
 */
@Service
public class InstrumentPriceModifierService {

    @Autowired
    private InstrumentPriceModifierRepository repository;

    @Cacheable("instruments")
    public InstrumentPriceModifier getInstrumentByName(String name) {
        return repository.getByName(name);
    }
}
