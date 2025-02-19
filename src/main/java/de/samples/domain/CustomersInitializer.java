package de.samples.domain;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Month;

@Startup
// @Dependent
@RequiredArgsConstructor
public class CustomersInitializer {

  private final CustomersService customersService;

  @PostConstruct
  public void init() {
    if (customersService.count() == 0) {
      customersService.createCustomer(
        Customer
          .builder()
          .name("Tom Mayer")
          .birthdate(LocalDate.of(1990, Month.JULY, 1))
          .build()
      );
    }
  }

}
