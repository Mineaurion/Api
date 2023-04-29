package com.mineaurion.api.server.model;

import com.mineaurion.api.Faker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ServerTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    Set<ConstraintViolation<Server>> constraintViolations;

    @Test
    public void should_fail_validation_with_bad_name(){
        constraintViolations = validator.validateValue(Server.class, "name", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());

        constraintViolations = validator.validateValue(Server.class, "name", "");
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_name(){
        constraintViolations = validator.validateValue(Server.class, "name", Faker.faker.animal().name());
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void should_fail_validation_with_bad_version(){
        constraintViolations = validator.validateValue(Server.class, "version", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_type(){
        constraintViolations = validator.validateValue(Server.class, "type", "overworld");
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void should_fail_validation_with_bad_type(){
        constraintViolations = validator.validateValue(Server.class, "type", "test");
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_fail_validation_with_bad_access(){
        constraintViolations = validator.validateValue(Server.class, "access", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_dns(){
        constraintViolations = validator.validateValue(Server.class, "dns", Faker.faker.internet().domainName());
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void should_fail_validation_with_bad_dns(){
        constraintViolations = validator.validateValue(Server.class, "dns", "*test.dns");
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_fail_validation_with_bad_administration(){
        constraintViolations = validator.validateValue(Server.class, "administration", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }
}
