package de.samples.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomersService {

  @Inject
  CustomersSink sink;

  public Stream<Customer> getCustomers() {
    return sink.findCustomers();
  }

  public Optional<Customer> getCustomer(UUID uuid) {
    return sink.findCustomerByUuid(uuid);
  }

  public void createCustomer(Customer customer) {
    sink.createCustomer(customer);
  }

  public boolean deleteCustomer(UUID uuid) {
    return sink.deleteCustomer(uuid);
  }

  public long count() {
    return sink.count();
  }

}
