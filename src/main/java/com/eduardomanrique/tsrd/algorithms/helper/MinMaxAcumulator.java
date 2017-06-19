package com.eduardomanrique.tsrd.algorithms.helper;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * Created by emanrique on 18/06/17.
 */
public class MinMaxAcumulator {
    @Getter
    private BigDecimal min;
    @Getter
    private BigDecimal max;

    public MinMaxAcumulator addValue(BigDecimal value) {
        min = min == null || value.compareTo(min) < 0 ? value : min;
        max = max == null || value.compareTo(max) > 0 ? value : max;
        return this;
    }

    public BigDecimal getDiff() {
        return max != null ? max.subtract(min) : BigDecimal.ZERO;
    }
}
