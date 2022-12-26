package com.cron.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CronParamTypeTest {

    @Test
    void testCronParamValue(){
        assertEquals(CronParamType.MINUTE, CronParamType.getValue(0));
        assertEquals(CronParamType.HOUR, CronParamType.getValue(1));
        assertEquals(CronParamType.DAY_OF_MONTH, CronParamType.getValue(2));
        assertEquals(CronParamType.MONTH, CronParamType.getValue(3));
        assertEquals(CronParamType.DAY_OF_WEEK, CronParamType.getValue(4));
    }
}