package com.eduardomanrique.tsrd.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by emanrique on 18/06/17.
 */
@Data
@Entity()
@Table(name = "INSTRUMENT_PRICE_MODIFIER")
public class InstrumentPriceModifier {

    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private BigDecimal multiplier;

}
