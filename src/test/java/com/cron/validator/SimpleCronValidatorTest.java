package com.cron.validator;

import com.cron.utils.CronUtils;
import com.cron.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleCronValidatorTest {

    private static SimpleCronValidator simpleCronValidator;

    @BeforeAll
    public void setUp(){
        simpleCronValidator = new SimpleCronValidator(new CronUtils());
    }

    @Test
    void validateArgumentsForWrongCount() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,() -> {
            simpleCronValidator.validateArguments(new String[]{"a"});});
        Assertions.assertEquals("Program requires 2 arguments. <your-program> \"<cron_expression>\" command_to_run",exception.getMessage());
    }

    @Test
    void validateArgumentsForWrongCronFormat() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class,() -> {
            simpleCronValidator.validateArguments(new String[]{"a","bcd"});});
        Assertions.assertEquals("Cron Expression should contain 5 parameters fields (minute, hour, day of month, month, and day of week)",exception.getMessage());
    }

    @Test
    void validateArgumentsSuccessfully() {
        Assertions.assertDoesNotThrow(() -> {simpleCronValidator.validateArguments(new String[]{"1 2 3 4 5","bcd"});});
    }

    @Test
    void testValidateAndBuildCronInfo(){
        Assertions.assertNotNull(simpleCronValidator.validateAndBuildCronInfo(new String[]{"* * * * *","/usr/bin"}));
    }

    // Add test case
}