package de.samples.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Builder
public class Customer {

  private UUID uuid;
  private String name;
  private LocalDate birthdate;
  @Builder.Default
  private CustomerState state = CustomerState.ACTIVE;

}
