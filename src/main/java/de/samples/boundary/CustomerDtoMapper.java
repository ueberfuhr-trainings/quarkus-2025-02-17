package de.samples.boundary;

import de.samples.domain.Customer;
import de.samples.domain.CustomerState;
import jakarta.ws.rs.BadRequestException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface CustomerDtoMapper {

  CustomerDto map(Customer customer);

  Customer map(CustomerDto customerDto);


  default String mapState(CustomerState state) {
    return null == state ? null : switch (state) {
      case ACTIVE -> "active";
      case DISABLED -> "disabled";
      case LOCKED -> "locked";
    };
  }

  default CustomerState mapState(String state) {
    return null == state ? null : switch (state) {
      case "active" -> CustomerState.ACTIVE;
      case "disabled" -> CustomerState.DISABLED;
      case "locked" -> CustomerState.LOCKED;
      default -> throw new BadRequestException("Unknown state: " + state);
    };
  }

}
