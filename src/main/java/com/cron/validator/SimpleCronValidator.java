package com.cron.validator;

import com.cron.utils.CronUtils;
import com.cron.exception.ValidationException;
import com.cron.interfaces.CronValidator;
import com.cron.model.CronParamType;

public class SimpleCronValidator implements CronValidator {
    // (minute, hour, day of month, month, and day of week)

    private CronUtils cronUtils;

    public SimpleCronValidator(CronUtils cronUtils){
        this.cronUtils = cronUtils;
    }

    public String validateAndBuildCronInfo(String[] args) {
        validateArguments(args);
        String[] params = args[0].split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            builder.append(cronUtils.buildCronValue(params[i], CronParamType.getValue(i))).append("\n");
        }
        builder.append(cronUtils.buildDescriptionLine("command", new String[]{args[1]}));
        return builder.toString();
    }

    protected void validateArguments(String[] args){
        if(null == args || args.length !=2){
           String message = "Program requires 2 arguments. <your-program> \"<cron_expression>\" command_to_run";
           throw new ValidationException(message);
        }

        if(args[0].split(" ").length!=5){
            String message = "Cron Expression should contain 5 parameters fields (minute, hour, day of month, month, and day of week)";
            throw new ValidationException(message);
        }
    }
}
