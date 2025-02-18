package de.samples.domain;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface CustomersSink {

  Stream<Customer> findCustomers();

  default Optional<Customer> findCustomerByUuid(UUID id) {
    return findCustomers()
      .filter(customer -> id.equals(customer.getUuid()))
      .findFirst();
  }

  void createCustomer(Customer customer);

  boolean deleteCustomer(UUID id);

  default long count() {
    return findCustomers()
      .count();
  }
}
