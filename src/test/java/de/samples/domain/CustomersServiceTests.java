package de.samples.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class CustomersServiceTests {

  @Inject
  CustomersService customersService;

  @Test
  void when_create_customer_then_uuid_generated() {
    Customer customer = new Customer();
    customer.setName("John Doe");
    customer.setBirthdate(LocalDate.of(1990, Month.MAY, 1));

    customersService.createCustomer(customer);

    Assertions.assertNotNull(customer.getUuid()); // TODO AssertJ

  }

  @Test
  void should_initialize_at_least_one_customer() {
    assertTrue(customersService.getCustomers().count() > 0);
  }

}
