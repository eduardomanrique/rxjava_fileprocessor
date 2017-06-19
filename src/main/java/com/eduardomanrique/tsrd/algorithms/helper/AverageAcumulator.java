package com.eduardomanrique.tsrd.algorithms.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by emanrique on 18/06/17.
 */
public class AverageAcumulator {

    private BigDecimal acumulated = BigDecimal.ZERO;
    private int count = 0;

    public AverageAcumulator addValue(BigDecimal value) {
        acumulated = acumulated.add(value);
        count++;
        return this;
    }

    public BigDecimal getAverage() {
        return count > 0 ? acumulated.divide(new BigDecimal(count), 7, RoundingMode.HALF_UP) : null;
    }
}
