package com.mineaurion.api.server.model.embdeddable;

import com.mineaurion.api.Faker;
import com.mineaurion.api.server.model.embeddable.Address;
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
public class AddressTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    Set<ConstraintViolation<Address>> constraintViolations;

    @Test
    public void should_fail_validation_with_bad_ip(){
        constraintViolations = validator.validateValue(Address.class, "ip", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());

        constraintViolations = validator.validateValue(Address.class, "ip", "");
        assertFalse(constraintViolations.isEmpty());
        assertEquals(2, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_ip(){
        constraintViolations = validator.validateValue(Address.class, "ip", Faker.faker.internet().ipV4Address());
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void should_fail_validation_with_bad_port(){
        constraintViolations = validator.validateValue(Address.class, "port", null);
        assertFalse(constraintViolations.isEmpty());
        assertEquals(1, constraintViolations.size());
    }

    @Test
    public void should_be_valid_with_port(){
        constraintViolations = validator.validateValue(Address.class, "port", Faker.faker.internet().port());
        assertTrue(constraintViolations.isEmpty());
    }

}
