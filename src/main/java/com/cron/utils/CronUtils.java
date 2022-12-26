package com.cron.utils;

import com.cron.exception.ValidationException;
import com.cron.model.CronParamType;
import com.cron.model.CronRegexType;

import java.util.*;
import java.util.regex.Pattern;

public class CronUtils {
    // (minute, hour, day of month, month, and day of week)
    private Map<CronParamType,String> cronRegex = new HashMap<>();
    private Map<CronParamType,String> fieldNamesMap = new HashMap<>();

    public CronUtils(){
        cronRegex.put(CronParamType.MINUTE,"[0-9]|[1-5][0-9]|\\*||\\*/[0-9]||\\*/[1-5][0-9]|[0-9]-[1-5][0-9]|[0-9]-[0-9]|[1-5][0-9]-[1-5][0-9]|[1-5][0-9]-[0-9]");
        cronRegex.put(CronParamType.HOUR,"[0-9]|1[0-9]|2[0-3]|\\*|\\*/[0-9]|\\*/1[0-9]|\\*/2[0-3]|[0-9]-[0-9]|[0-9]-1[0-9]|[0-9]-2[0-3]|1[0-9]-2[0-3]");
        cronRegex.put(CronParamType.DAY_OF_MONTH,"[1-9]|[1-2][0-9]|3[0-1]|\\*|\\*/[1-9]|\\*/[1-2][0-9]|\\*/3[0-1]|[1-9]-[1-9]|[1-9]-[1-2][0-9]|[1-9]-3[0-1]|[1-2][0-9]-3[0-1]");
        cronRegex.put(CronParamType.MONTH,"[1-9]|1[0-2]|\\*/[1-9]|\\*/1[0-2]|\\*|[1-9]-[1-9]|[1-9]-1[0-2]|1[0-2]-1[0-2]");
        cronRegex.put(CronParamType.DAY_OF_WEEK,"[1-7]|\\*/[1-7]|\\*|[1-7]-[1-7]");

        fieldNamesMap.put(CronParamType.MINUTE,"minute");
        fieldNamesMap.put(CronParamType.HOUR,"hour");
        fieldNamesMap.put(CronParamType.DAY_OF_MONTH,"day of month");
        fieldNamesMap.put(CronParamType.MONTH,"month");
        fieldNamesMap.put(CronParamType.DAY_OF_WEEK,"day of week");
    }

    public String buildCronValue(String cronParamFieldValue, CronParamType cronParamFieldType) {
        if (!isValidCronString(cronParamFieldType, cronParamFieldValue)) {
            throw new ValidationException("Invalid cron expression value for field : " +cronParamFieldType);
        }
        return buildCronDescriptionString(cronParamFieldType,cronParamFieldValue);
    }

    protected boolean isValidCronString(CronParamType cronParamFieldType,String value){
        String regex = cronRegex.get(cronParamFieldType);
        if(null == regex){
            throw new ValidationException("Cron expression regex not available for field type: " + cronParamFieldType);
        }
        return Pattern.matches(regex,value);
    }

    protected CronRegexType getCronRegexType(String data) {
        if (data.contains("-") && data.contains("/")) {
            return CronRegexType.INTERVAL_WITH_RANGE;
        }

        if (data.contains("*") && data.contains("/")) {
            return CronRegexType.INTERVAL;
        }

        if (data.contains("-")) {
            return CronRegexType.RANGE;
        }

        if (data.equals("*")) {
            return CronRegexType.ALL;
        }

        return CronRegexType.SPECIFIC_VALUE;
    }

    public String buildCronDescriptionString(CronParamType cronParamFieldType,String cronParamFieldValue){
       String [] descriptionLineValue = buildDescriptionLineValue(cronParamFieldType,cronParamFieldValue);
       return buildDescriptionLine(fieldNamesMap.get(cronParamFieldType),descriptionLineValue);
    }

    protected String[] buildDescriptionLineValue(CronParamType cronParamFieldType,String cronParamFieldValue){
        CronRegexType cronRegexType = getCronRegexType(cronParamFieldValue);
        String [] range;
        String interval;
        switch (cronRegexType){
            case SPECIFIC_VALUE: // 2
                    return new String[]{cronParamFieldValue};
            case ALL:  // *
                    return getAllValuesForCronParamType(cronParamFieldType,1);
            case RANGE:
                range = cronParamFieldValue.split("-"); // 10-20
                return buildValues(Integer.parseInt(range[0]),Integer.parseInt(range[1]),1,cronParamFieldType);
            case INTERVAL:
                interval = cronParamFieldValue.split("/")[1]; // */2
                return getAllValuesForCronParamType(cronParamFieldType,Integer.parseInt(interval));
            case INTERVAL_WITH_RANGE:     // 2-30/4
                range = cronParamFieldValue.split("/");
                interval = range[1];
                range = range[0].split("-");
                return buildValues(Integer.parseInt(range[0]),Integer.parseInt(range[1]),Integer.parseInt(interval),cronParamFieldType);
        }
        throw new ValidationException("Description Line Value is not defined for "+cronRegexType);
    }


    protected String [] getAllValuesForCronParamType(CronParamType cronParamFieldType,int additionalFactor){
            switch (cronParamFieldType){
                case MINUTE:
                case HOUR:
                    return buildValues(0,getMaxValueForParam(cronParamFieldType),additionalFactor,cronParamFieldType);
                case MONTH:
                case DAY_OF_WEEK:
                case DAY_OF_MONTH:
                    return buildValues(1,getMaxValueForParam(cronParamFieldType),additionalFactor,cronParamFieldType);
            }
        throw new ValidationException("getAllValuesForCronParamType is not defined for "+cronParamFieldType);
    }

    private String [] buildValues(Integer start,Integer end,Integer additionFactor,CronParamType cronParamFieldType){
        if(start > end){
            //return buildValuesForDescendingRange(start,end,additionFactor,cronParamFieldType);
            throw new ValidationException("Start value can not be grater than end value");
        }
        List<String> values = new ArrayList<>();
        values.add(start.toString());

        while(start + additionFactor <= end){
            start = start + additionFactor;
            values.add(start.toString());
        }
        return values.toArray(new String[values.size()]);
    }

    protected String [] buildValuesForDescendingRange(Integer start,Integer end,Integer additionFactor,CronParamType cronParamFieldType){
        if(start < end){
            throw new ValidationException("Not a valid case to build descending range values");
        }
        Integer maxValue = getMaxValueForParam(cronParamFieldType);
        maxValue = (cronParamFieldType == CronParamType.HOUR || cronParamFieldType == CronParamType.MINUTE) ? maxValue+1 : maxValue;
        List<String> values = new ArrayList<>();
        values.add(start.toString());
        Integer value=(start+additionFactor)%maxValue;

        while(start < value || value <= end){
            values.add(value.toString());
            value=(value+additionFactor)%maxValue;
        }
        return values.toArray(new String[values.size()]);
    }

    private Integer getMaxValueForParam(CronParamType cronParamFieldType){
        switch (cronParamFieldType){
            case MONTH:
                return 12;
            case DAY_OF_MONTH:
                return 31;
            case MINUTE:
                return 59;
            case HOUR:
                return 23;
            case DAY_OF_WEEK:
                return 7;
        }
        throw new ValidationException("Invalid cronParamFieldType for max value calculation");
    }

    public String buildDescriptionLine(String fieldType,String [] values){
        return new Formatter().format("%-14s%s",fieldType,String.join(" ",values)).toString();
    }
}
