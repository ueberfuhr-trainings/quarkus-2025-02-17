package de.samples;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public class Customer {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private UUID uuid;
  private String name;
  private LocalDate birthdate;
  private CustomerState state = CustomerState.ACTIVE;

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public CustomerState getState() {
    return state;
  }

  public void setState(CustomerState state) {
    this.state = state;
  }
}
