package com.cron.boot;


import com.cron.utils.CronUtils;
import com.cron.validator.SimpleCronValidator;

public class CronApplication
{
    public static void main(String[] args)
    {
        CronUtils cronUtils = new CronUtils();
        SimpleCronValidator validator = new SimpleCronValidator(cronUtils);
        String result = validator.validateAndBuildCronInfo(args);
        System.out.println(result);
    }
}
