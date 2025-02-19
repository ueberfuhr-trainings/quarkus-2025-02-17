package de.samples.domain;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.time.Month;

@Startup
// @Dependent
public class CustomersInitializer {

  @Inject
  CustomersService customersService;

  @PostConstruct
  public void init() {
    if (customersService.count() == 0) {
      Customer customer = new Customer();
      customer.setName("Tom Mayer");
      customer.setState(CustomerState.ACTIVE);
      customer.setBirthdate(LocalDate.of(1990, Month.JULY, 1));
      customersService.createCustomer(customer);
    }
  }

}
