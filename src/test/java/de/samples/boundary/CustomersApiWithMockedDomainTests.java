package de.samples.boundary;

import de.samples.domain.Customer;
import de.samples.domain.CustomerState;
import de.samples.domain.CustomersService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Stubber;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static de.samples.test.CustomerMatchers.hasState;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class CustomersApiWithMockedDomainTests {

  @InjectMock
  CustomersService customersService;

  // GET /customers/{uuid} -> 404
  @Test
  void when_get_customer_by_uuid_then_not_found() {
    var uuid = UUID.randomUUID();
    when(customersService.getCustomer(uuid))
      .thenReturn(Optional.empty());

    given()
      .when()
      .get("customers/{uuid}", uuid)
      .then()
      .statusCode(is(404));

  }

  private static Stubber doSetUuid(Function<InvocationOnMock, Customer> customerFn) {
    return doAnswer(invocationOnMock -> {
      var customer = customerFn.apply(invocationOnMock);
      customer.setUuid(UUID.randomUUID());
      return null;
    });
  }

  // POST customer mit disabled -> CustomerState.DISABLED
  @Test
  void when_post_disabled_customer_then_save_disabled_customer() {
    // instruieren: UUID generieren
    doSetUuid(call -> call.getArgument(0))
      .when(customersService)
      .createCustomer(any());
    given()
      .when()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "2004-05-02",
          "state": "disabled"
        }
        """)
      .accept(ContentType.JSON)
      .post("/customers")
      .then()
      .statusCode(201);
    verify(customersService)
      .createCustomer(
        argThat(hasState(CustomerState.DISABLED))
      );
  }

}
