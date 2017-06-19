package com.eduardomanrique.tsrd.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by emanrique on 18/06/17.
 */
@AllArgsConstructor
public class TsrdEvent {
    @Getter
    private String instrumentName;
    @Getter
    private Date date;
    @Getter
    private BigDecimal value;
    @Getter
    private String originalLine;
}
