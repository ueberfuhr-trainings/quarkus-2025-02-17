package de.samples;

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
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/customers")
public class CustomersResource {

  @Context
  UriInfo uriInfo;


  private final HashMap<UUID, Customer> customers = new HashMap<>();

  {
    Customer customer = new Customer();
    customer.setUuid(UUID.randomUUID());
    customer.setName("Tom Mayer");
    customer.setState(CustomerState.ACTIVE);
    customer.setBirthdate(LocalDate.of(1990, Month.JULY, 1));
    customers.put(customer.getUuid(), customer);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<Customer> getCustomers(
    @QueryParam("state")
    String state
  ) {
    if (null != state && !List.of("active", "locked", "disabled").contains(state)) {
      throw new BadRequestException("Invalid state: " + state);
    }
    return customers
      .values();
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
  public Customer getCustomer(
    // if we use UUID here directly,
    // JAX-RS will return 404 if the path parameter is syntactically invalid
    @PathParam("uuid")
    String uuid
  ) {
    return Optional
      .ofNullable(customers.get(fromParameter(uuid)))
      .orElseThrow(() -> new NotFoundException("Customer not found: " + uuid));
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createCustomer(
    Customer customer
  ) {

    if (null == customer.getName()) {
      throw new BadRequestException("Customer name is required");
    }

    var uuid = UUID.randomUUID();
    customer.setUuid(uuid);
    customers.put(uuid, customer);

    String location = uriInfo.getAbsolutePathBuilder()
      .path(uuid.toString())
      .build()
      .toString();

    return Response
      .created(URI.create(location))
      .entity(customer)
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
    if (null == customers.remove(fromParameter(uuid))) {
      throw new NotFoundException("Customer not found: " + uuid);
    }
  }


}
