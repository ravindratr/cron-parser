package com.cron.utils;

import com.cron.model.CronParamType;
import com.cron.model.CronRegexType;
import com.cron.utils.CronUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CronUtilsTest {

    private CronUtils cronUtils;

    @BeforeAll
    void setUp(){
        cronUtils = new CronUtils();
    }

    @Test
    void testMinuteCronExpression() {
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MINUTE,"2"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MINUTE,"59"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MINUTE,"*/2"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MINUTE,"*"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MINUTE,"3-8"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MINUTE,"10-20"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MINUTE,"1-59"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MINUTE,"55-58"));
    }

    @Test
    void testHourCronExpression() {
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.HOUR,"2"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.HOUR,"23"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.HOUR,"*/2"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.HOUR,"*"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.HOUR,"3-8"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.HOUR,"10-20"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.HOUR,"*/20"));
    }

    @Test
    void testDayOfMonthCronExpression() {
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_MONTH,"2"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_MONTH,"23"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_MONTH,"*/2"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_MONTH,"*"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_MONTH,"3-8"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_MONTH,"1-31"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_MONTH,"2-28"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_MONTH,"10-31"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_MONTH,"*/20"));
    }

    @Test
    void testMonthCronExpression() {
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MONTH,"2"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MONTH,"12"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MONTH,"*/2"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MONTH,"*"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MONTH,"3-8"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MONTH,"1-12"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MONTH,"10-12"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.MONTH,"*/11"));
    }

    @Test
    void testDayOfWeekCronExpression() {
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_WEEK,"2"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_WEEK,"*/2"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_WEEK,"*"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_WEEK,"3-7"));
        Assertions.assertTrue(cronUtils.isValidCronString(CronParamType.DAY_OF_WEEK,"*/5"));
    }

    @Test
    void testBuildDescriptionLine() {
        Assertions.assertEquals("minute        3",cronUtils.buildDescriptionLine("minute",new String[]{"3"}));
        Assertions.assertEquals("minute        3 10 20",cronUtils.buildDescriptionLine("minute",new String[]{"3","10","20"}));
    }

    @Test
    void testGetAllValuesForCronParamType() {
        Assertions.assertTrue(Arrays.equals(new String[]{"1","2","3","4","5","6","7"},cronUtils.getAllValuesForCronParamType(CronParamType.DAY_OF_WEEK,1)));
        Assertions.assertTrue(Arrays.equals(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"},cronUtils.getAllValuesForCronParamType(CronParamType.MONTH,1)));
    }

    @Test
    void testGetCronRegexType() {
        Assertions.assertEquals(CronRegexType.ALL,cronUtils.getCronRegexType("*"));
        Assertions.assertEquals(CronRegexType.RANGE,cronUtils.getCronRegexType("1-4"));
        Assertions.assertEquals(CronRegexType.INTERVAL,cronUtils.getCronRegexType("*/3"));
        Assertions.assertEquals(CronRegexType.SPECIFIC_VALUE,cronUtils.getCronRegexType("10"));
        Assertions.assertEquals(CronRegexType.INTERVAL_WITH_RANGE,cronUtils.getCronRegexType("1-5/1"));
    }

    @Test
    void testBuildDescriptionLineValue(){
        Assertions.assertTrue(Arrays.equals(new String[]{"20"},cronUtils.buildDescriptionLineValue(CronParamType.DAY_OF_WEEK,"20")));
        Assertions.assertTrue(Arrays.equals(new String[]{"5","6","7","8"},cronUtils.buildDescriptionLineValue(CronParamType.DAY_OF_WEEK,"5-8")));
        Assertions.assertTrue(Arrays.equals(new String[]{"1","2","3","4","5","6","7"},cronUtils.buildDescriptionLineValue(CronParamType.DAY_OF_WEEK,"*")));
        Assertions.assertTrue(Arrays.equals(new String[]{"0","15","30","45"},cronUtils.buildDescriptionLineValue(CronParamType.MINUTE,"*/15")));
        Assertions.assertTrue(Arrays.equals(new String[]{"2","4","6","8","10"},cronUtils.buildDescriptionLineValue(CronParamType.HOUR,"2-10/2")));
    }

    @Test
    void buildValuesForDescendingRange(){
        Assertions.assertTrue(Arrays.equals(new String[]{"23","0","1","2"},cronUtils.buildValuesForDescendingRange(23,2,1,CronParamType.HOUR)));
        Assertions.assertTrue(Arrays.equals(new String[]{"12","1","2","3"},cronUtils.buildValuesForDescendingRange(12,3,1,CronParamType.MONTH)));
        Assertions.assertTrue(Arrays.equals(new String[]{"50","53","56","59","2","5","8"},cronUtils.buildValuesForDescendingRange(50,10,3,CronParamType.MINUTE)));
    }
}