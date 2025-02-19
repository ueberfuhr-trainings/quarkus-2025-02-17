package de.samples.boundary;

import de.samples.domain.CustomersService;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Path("/customers")
public class CustomersResource {

  @Context
  UriInfo uriInfo;
  @Inject
  CustomersService customersService;
  @Inject
  CustomerDtoMapper customerDtoMapper;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<CustomerDto> getCustomers(
    @QueryParam("state")
    String state
  ) {
    if (null != state && !List.of("active", "locked", "disabled").contains(state)) {
      throw new BadRequestException("Invalid state: " + state);
    }
    return customersService
      .getCustomers()
      .map(customerDtoMapper::map)
      .toList();
  }

  private static UUID fromParameter(String uuid) {
    try {
      return UUID.fromString(uuid);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid UUID: " + uuid);
    }
  }

  @GET
  @Path("/{uuid}")
  @Produces(MediaType.APPLICATION_JSON)
  public CustomerDto getCustomer(
    // if we use UUID here directly,
    // JAX-RS will return 404 if the path parameter is syntactically invalid
    @PathParam("uuid")
    String uuid
  ) {
    return customersService
      .getCustomer(fromParameter(uuid))
      .map(customerDtoMapper::map)
      .orElseThrow(() -> new NotFoundException("Customer not found: " + uuid));
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createCustomer(
    CustomerDto customerDto
  ) {

    if (null == customerDto.getName()) {
      throw new BadRequestException("Customer name is required");
    }
    var customer = customerDtoMapper.map(customerDto);
    customersService.createCustomer(customer);

    String location = uriInfo.getAbsolutePathBuilder()
      .path(customer.getUuid().toString())
      .build()
      .toString();

    return Response
      .created(URI.create(location))
      .entity(customerDtoMapper.map(customer))
      .build();
  }

  @DELETE
  @Path("/{uuid}")
  public void deleteCustomer(
    // if we use UUID here directly,
    // JAX-RS will return 404 if the path parameter is syntactically invalid
    @PathParam("uuid")
    String uuid
  ) {
    if (!customersService.deleteCustomer(fromParameter(uuid))) {
      throw new NotFoundException("Customer not found: " + uuid);
    }
  }


}
