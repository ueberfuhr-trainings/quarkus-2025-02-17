package de.samples;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CustomerState {

  @JsonProperty("active")
  ACTIVE,
  @JsonProperty("locked")
  LOCKED,
  @JsonProperty("disabled")
  DISABLED

}
