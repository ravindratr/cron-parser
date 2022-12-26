package com.cron.model;

public enum CronParamType {
    MINUTE(0),HOUR(1),DAY_OF_MONTH(2),MONTH(3),DAY_OF_WEEK(4);

    private int index;

    CronParamType(int index){
        this.index = index;
    }

    public static CronParamType getValue(int index) {
        for(CronParamType c : CronParamType.values()){
            if(c.index == index){
                return c;
            }
        }
        return null;
    }
}
