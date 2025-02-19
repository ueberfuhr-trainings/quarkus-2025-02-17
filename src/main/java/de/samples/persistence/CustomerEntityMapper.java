package de.samples.persistence;

import de.samples.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface CustomerEntityMapper {

  CustomerEntity map(Customer customer);

  Customer map(CustomerEntity customerEntity);

  void copy(CustomerEntity from, @MappingTarget Customer to);

}
