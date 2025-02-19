package de.samples.boundary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
public class CustomerDto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private UUID uuid;
  private String name;
  private LocalDate birthdate;
  private String state = "active";

}
