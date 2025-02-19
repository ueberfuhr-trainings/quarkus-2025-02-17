package de.samples.persistence;

import de.samples.domain.Customer;
import de.samples.domain.CustomersSink;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@Typed(CustomersSink.class)
public class PanacheCustomersSink implements CustomersSink {

  @Inject
  CustomerEntityRepository repo;
  @Inject
  CustomerEntityMapper mapper;

  @Override
  public Stream<Customer> findCustomers() {
    return repo
      .listAll()
      .stream()
      .map(mapper::map);
  }

  @Override
  public Optional<Customer> findCustomerByUuid(UUID uuid) {
    return repo
      .findByIdOptional(uuid)
      .map(mapper::map);
  }

  @Transactional
  @Override
  public void createCustomer(Customer customer) {
    var entity = mapper.map(customer);
    repo.persist(entity);
    // customer.setUuid(entity.getUuid());
    mapper.copy(entity, customer);
  }

  @Transactional
  @Override
  public boolean deleteCustomer(UUID id) {
    return repo.deleteById(id);
  }

  @Override
  public long count() {
    return repo.count();
  }
}
