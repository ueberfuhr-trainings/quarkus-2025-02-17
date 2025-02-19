package de.samples.persistence;

import de.samples.domain.CustomerState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Entity(name = "Customer") // JPQL: select c from Customer c
@Table(name = "CUSTOMERS") // DB Schema
public class CustomerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;
  private String name;
  @Column(name = "DATE_OF_BIRTH")
  private LocalDate birthdate;
  private CustomerState state = CustomerState.ACTIVE;

}
