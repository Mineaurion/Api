package com.mineaurion.api.server.model.embdeddable;

import com.mineaurion.api.Faker;
import com.mineaurion.api.server.model.embeddable.Administration;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AdministrationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    Set<ConstraintViolation<Administration>> constraintViolations;

    @Test
    public void should_fail_validation_with_bad_regen(){
        constraintViolations = validator.validateValue(Administration.class, "regen", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_regen(){
        constraintViolations = validator.validateValue(Administration.class, "regen", Faker.faker.random().nextBoolean());
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void should_fail_validation_with_bad_backup(){
        constraintViolations = validator.validateValue(Administration.class, "backup", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_backup(){
        constraintViolations = validator.validateValue(Administration.class, "backup", Faker.faker.random().nextBoolean());
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void should_fail_validation_with_bad_query(){
        constraintViolations = validator.validateValue(Administration.class, "query", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_null_prometheus(){
        constraintViolations = validator.validateValue(Administration.class, "prometheus", null);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void should_fail_validation_with_bad_externalId(){
        constraintViolations = validator.validateValue(Administration.class, "externalId", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_externalId(){
        constraintViolations = validator.validateValue(Administration.class, "externalId", UUID.randomUUID());
        assertTrue(constraintViolations.isEmpty());
    }
}
