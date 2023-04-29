package com.mineaurion.api.server.model.embdeddable;


import com.mineaurion.api.Faker;
import com.mineaurion.api.server.model.embeddable.Version;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class VersionTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    Set<ConstraintViolation<Version>> constraintViolations;

    @Test
    public void should_fail_validation_with_bad_minecraft(){
        constraintViolations = validator.validateValue(Version.class, "minecraft", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_minecraft(){
        constraintViolations = validator.validateValue(Version.class, "minecraft", Faker.faker.lorem().word());
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void should_fail_validation_with_bad_modpack(){
        constraintViolations = validator.validateValue(Version.class, "modpack", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_modpack(){
        constraintViolations = validator.validateValue(Version.class, "modpack", Faker.faker.lorem().word());
        assertTrue(constraintViolations.isEmpty());
    }
}
