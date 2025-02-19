package de.samples.boundary;


import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.either;

@QuarkusTest
@Tag("API")
@TestTransaction
class CustomersApiTests {

  @DisplayName("GET /customers -> 200")
  @Test
  void whenGetCustomers_thenOk() {
    given()
      .when()
      .accept(ContentType.JSON)
      .get("/customers")
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body(startsWith("["))
      .body(endsWith("]"));
  }

  @DisplayName("GET /customers (XML) -> 406")
  @Test
  void whenGetCustomersWithInvalidAcceptHeader_thenNotAcceptable() {
    given()
      .when()
      .accept(ContentType.XML)
      .get("/customers")
      .then()
      .statusCode(406);
  }

  @ParameterizedTest(name = "GET /customers (state: {0}) -> 200")
  @ValueSource(strings = {
    "active",
    "locked",
    "disabled"
  })
  void whenGetCustomersWithCorrectState_thenOk(String state) {
    given()
      .when()
      .accept(ContentType.JSON)
      .queryParam("state", state)
      .get("/customers")
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body(startsWith("["))
      .body(endsWith("]"));
  }

  @DisplayName("GET /customers (invalid state) -> 400")
  @Test
  void whenGetCustomersWithInvalidState_thenBadRequest() {
    given()
      .when()
      .accept(ContentType.JSON)
      .queryParam("state", "gelbekatze")
      .get("/customers")
      .then()
      .statusCode(400);
  }

  @DisplayName("POST /customers -> 201")
  @Test
  void whenPostCustomers_thenCreatedSuccessfully() {
    given()
      .when()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "2004-05-02",
          "state": "active"
        }
        """)
      .accept(ContentType.JSON)
      .post("/customers")
      .then()
      .statusCode(201)
      .contentType(ContentType.JSON)
      .body("name", is(equalTo("John")))
      .body("birthdate", is(equalTo("2004-05-02")))
      .body("state", is(equalTo("active")))
      .body("uuid", is(notNullValue()))
      .header("Location", is(notNullValue()));
  }

  @DisplayName("POST /customers (without state) -> active state")
  @Test
  void whenPostCustomersWithoutState_thenCreatedWithDefaultState() {
    given()
      .when()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "2004-05-02"
        }
        """)
      .accept(ContentType.JSON)
      .post("/customers")
      .then()
      .statusCode(201)
      .contentType(ContentType.JSON)
      .body("state", is(equalTo("active")));
  }

  @DisplayName("POST /customers (without name) -> 400")
  @Test
  void whenPostCustomersWithoutName_thenBadRequest() {
    given()
      .when()
      .contentType(ContentType.JSON)
      .body("""
        {
          "birthdate": "2004-05-02",
          "state" : "active"
        }
        """)
      .accept(ContentType.JSON)
      .post("/customers")
      .then()
      .statusCode(400);
  }

  @DisplayName("POST /customers (invalid birthdate) -> 400")
  @Test
  void whenPostCustomersWithInvalidBirthdate_thenBadRequest() {
    given()
      .when()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "gelbekatze",
          "state" : "active"
        }
        """)
      .accept(ContentType.JSON)
      .post("/customers")
      .then()
      .statusCode(400);
  }

  @DisplayName("POST /customers -> GET {Location-Header} -> 200")
  @Test
  void whenPostCustomers_thenReturnLocationHeaderOfExistingCustomer() {
    var location = given()
      .when()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "2004-05-02",
          "state": "active"
        }
        """)
      .accept(ContentType.JSON)
      .post("/customers")
      .thenReturn()
      .getHeader("Location");
    given()
      .when()
      .accept(ContentType.JSON)
      .get(location)
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("name", is(equalTo("John")))
      .body("birthdate", is(equalTo("2004-05-02")))
      .body("state", is(equalTo("active")));
  }

  @DisplayName("GET /customers/0 -> 400")
  @Test
  void whenGetCustomerWithInvalidUUID_thenReturnBadRequest() {
    given()
      .when()
      .accept(ContentType.JSON)
      .get("customers/0")
      .then()
      .statusCode(400);
  }

  @DisplayName("DELETE /customers/{uuid} -> GET /customers/{} -> 404")
  @Test
  void whenGetCustomerForNonExisting_thenReturnNotFound() {
    UUID uuid = UUID.randomUUID();
    given()
      .when()
      .delete("customers/{uuid}", uuid)
      .then()
      .statusCode(either(is(204)).or(is(404)));
    given()
      .when()
      .get("customers/{uuid}", uuid)
      .then()
      .statusCode(is(404));
  }

  @DisplayName("POST /customers -> DELETE {Location-Header} -> 204")
  @Test
  void whenDeleteExistingCustomer_thenReturnNoContent_andReturnNotFoundOnRetry() {
    var location = given()
      .when()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "2004-05-02",
          "state": "active"
        }
        """)
      .accept(ContentType.JSON)
      .post("/customers")
      .thenReturn()
      .getHeader("Location");
    given()
      .when()
      .delete(location)
      .then()
      .statusCode(204);
    given()
      .when()
      .delete(location)
      .then()
      .statusCode(404);
  }


}
