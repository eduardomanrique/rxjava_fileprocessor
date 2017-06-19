package com.eduardomanrique.tsrd.preprocessors;

import com.eduardomanrique.tsrd.datasource.Filter;
import com.eduardomanrique.tsrd.datasource.TsrdEvent;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * Created by emanrique on 18/06/17.
 */
@Component
public class BusinessDaysFilter implements Filter {

    public boolean filter(TsrdEvent tsrdEvent) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(tsrdEvent.getDate());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek > 1 && dayOfWeek < 7;
    }
}
